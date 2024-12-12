package com.Chess3D.gui;
import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.Tile;
import com.Chess3D.core.board.generalBoardRules;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.core.player.ai.MiniMax;
import com.Chess3D.core.player.ai.MoveStrategy;
import com.Chess3D.core.player.movementOnBoard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.SwingUtilities.*;
import javax.swing.SwingWorker;

public class Table{

    public final JFrame gameFrame;
    private ChessBoard chessBoard;
    private final TablePanel tablePanel;
    private Tile currentTile;
    private Tile destinationTile;
    private Piece movedPiece;
    private BoardDirection direction;
    public boolean highlightLegalMoves;
    private final static Dimension OUTER_DIMENSION = new Dimension(600, 600);
    private final static Dimension TABLE_PANEL_DIMENSION = new Dimension(400, 400);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(50, 50);
    private final static String pieceIconPath = "art/pieces/common/";
    private final static String miscIconPath = "art/misc/";
    private final GameHistoryPanel gameHistoryPanel;
    private final CapturedPiecesPanel capturedPiecesPanel;
    private final MoveRecord moveLog;
    private static final Table INSTANCE = new Table();
    private final GameSetup gameSetup;
    private Move ComputerMove;
    private final PropertyChangeSupport support;
    
    private Table() {
        this.gameFrame = new JFrame("Chess");

        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar menuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(menuBar);
        this.gameFrame.setSize(OUTER_DIMENSION);
        this.direction = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.chessBoard = ChessBoard.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.capturedPiecesPanel = new CapturedPiecesPanel();
        this.tablePanel = new TablePanel();
        this.moveLog = new MoveRecord();
        
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(this.capturedPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.tablePanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.support = new PropertyChangeSupport(this);
        support.addPropertyChangeListener(new AIWatch());
    }

    public static Table get(){
        return INSTANCE;
    }

    private JFrame getGameFrame() {
        return this.gameFrame;
    }

    public void show() {
        Table.get().getMoveLog().clearLog();
        Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
        Table.get().getCapturedPiecesPanel().redoMove(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawNewBoard(Table.get().getGameBoard());
    }

    private GameSetup getGameSetup(){
        return this.gameSetup;
    }

    private ChessBoard getGameBoard(){
        return this.chessBoard;
    }
    private void setupUpdate(final GameSetup gameSetup) {
        final GameSetup oldSetup = this.gameSetup;
        support.firePropertyChange("gameSetup", oldSetup, gameSetup);
    }


    private static class AIWatch implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().activePlayer()) 
            && !Table.get().getGameBoard().activePlayer().CheckMate() 
            && !Table.get().getGameBoard().activePlayer().StaleMate()) {
                
            final AIThoughtProcess thoughtProcess = new AIThoughtProcess();
            thoughtProcess.execute();
        }

        if (Table.get().getGameBoard().activePlayer().CheckMate()) {
            System.out.println("Game Over: " + Table.get().getGameBoard().activePlayer().getPlayerColor() + " is in checkmate");
        }

        if (Table.get().getGameBoard().activePlayer().StaleMate()) {
            System.out.println("Game Over: " + Table.get().getGameBoard().activePlayer().getPlayerColor() + " is in stalemate");
        }
    }
}

    private static class AIThoughtProcess extends SwingWorker <Move, String>{
        private AIThoughtProcess() {
            
        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MiniMax(4);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        protected void done() {
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().activePlayer().movePiece(bestMove).getNewBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getCapturedPiecesPanel().redoMove(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawNewBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.AI);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    private void updateComputerMove(final Move move){
        this.ComputerMove = move;
    }

    private void updateGameBoard(final ChessBoard board){
        this.chessBoard = board;
    }

    private void moveMadeUpdate(final PlayerType playerType){
        support.firePropertyChange("moveMade", null, playerType);
    }

    private MoveRecord getMoveLog(){
        return this.moveLog;
    }

    private TablePanel getBoardPanel(){
        return this.tablePanel;
    }

    private GameHistoryPanel getGameHistoryPanel(){
        return this.gameHistoryPanel;
    }

    private CapturedPiecesPanel getCapturedPiecesPanel(){
        return this.capturedPiecesPanel;
    }



    private JMenuBar populateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPreferencesMenu());
        menuBar.add(createOptionsMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Open PGN");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int option = chooser.showOpenDialog(Table.get().getGameFrame());
                if (option == JFileChooser.APPROVE_OPTION) {
                    //loadPGNFile(chooser.getSelectedFile());
                }
            }
        });

        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exit);
        fileMenu.add(openPGN);
        return fileMenu;
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");	
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                direction = direction.flip();
                tablePanel.drawNewBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);

        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHighlighter = new JCheckBoxMenuItem("Legal Move Highlighter", false);
        legalMoveHighlighter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighter.isSelected();
                tablePanel.repaint();
            }
        });
        preferencesMenu.add(legalMoveHighlighter);
        return preferencesMenu;
    }

    private JMenu createOptionsMenu(){
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener (new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });

        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }


    private class TablePanel extends JPanel {
        final List<TilePanel> boardTiles;

        public TablePanel() {    
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for (int i = 0; i < 64; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(TABLE_PANEL_DIMENSION);
            validate();
        }

        public void drawNewBoard(ChessBoard chessBoard) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawNewTile(chessBoard);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveRecord {
        private final List<Move> moves;
        public MoveRecord() {
            this.moves = new ArrayList<>();
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int moveCount(){
            return this.moves.size();
        }

        public void clearLog(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public Move removeMove(final Move move) {
            this.moves.remove(move);
            return move;
        }

        public List<Move> getMoveList(){
            return moves;
        }
    }

    public enum PlayerType{
        HUMAN,
        AI
    }

    private class TilePanel extends JPanel {

        private final int tileID;
        public TilePanel(final TablePanel tablePanel, final int tileID) {
            super(new GridBagLayout());

            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                   if (isRightMouseButton(e)) {
                       currentTile = null;
                       destinationTile = null;
                       movedPiece = null;
                   }else if (isLeftMouseButton(e)) {
                        if (currentTile == null) {
                            currentTile = chessBoard.getTile(tileID);
                            movedPiece = chessBoard.getTile(tileID).getPiece();
                            if (movedPiece == null) {
                                currentTile = null;
                            }
                        }else {
                            destinationTile = chessBoard.getTile(tileID);
                            final Move move = Move.MoveFactory.createMove(chessBoard, currentTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final movementOnBoard movement = chessBoard.activePlayer().movePiece(move);
                            if (movement.getMoveStatus().isOk()) {   
                                chessBoard = movement.getNewBoard();
                                moveLog.addMove(move);
                            }

                            currentTile = null;
                            destinationTile = null;
                            movedPiece = null;


                        }

                        SwingUtilities.invokeLater(() -> {
                            gameHistoryPanel.redo(chessBoard, moveLog);
                            capturedPiecesPanel.redoMove(moveLog);

                            if (gameSetup.isAIPlayer(chessBoard.activePlayer())) {
                                Table.get().moveMadeUpdate(PlayerType.HUMAN);
                            }
                            tablePanel.drawNewBoard(chessBoard);
                        });

                   }
                }        
                @Override    
                public void mousePressed(final MouseEvent e) {}
                @Override
                public void mouseReleased(final MouseEvent e) {}
                @Override
                public void mouseEntered(final MouseEvent e) {}
                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();
        }

        public void drawNewTile(final ChessBoard board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMove(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final ChessBoard board) {
            this.removeAll();

            if (board.getTile(this.tileID).tileOccupation()) {
                
                try {
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath + 
                    board.getTile(this.tileID).getPiece().getPieceColor().toString().substring(0, 1) +
                    board.getTile(this.tileID).getPiece().toString() + ".gif"));

                    add(new JLabel(new ImageIcon(image)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (generalBoardRules.firstRow[this.tileID] || generalBoardRules.thirdRow[this.tileID] 
                || generalBoardRules.fifthRow[this.tileID] || generalBoardRules.seventhRow[this.tileID]) {
                    setBackground(this.tileID % 2 == 0 ? Color.decode("#F0D9B5") : Color.decode("#B58863"));
            } else if (generalBoardRules.secondRow[this.tileID] || generalBoardRules.fourthRow[this.tileID] 
                || generalBoardRules.sixthRow[this.tileID] || generalBoardRules.eighthRow[this.tileID]) {
                    setBackground(this.tileID % 2 == 0 ? Color.decode("#B58863") : Color.decode("#F0D9B5"));
            } else {
                setBackground(this.tileID % 2 == 0 ? Color.decode("#F0D9B5") : Color.decode("#B58863"));
            }
        }

        private void highlightLegalMove(final ChessBoard chessBoard){
            if(highlightLegalMoves){
                for (final Move move: pieceLegalMoves(chessBoard)){
                    if (move.getDestinationCoordinate() == this.tileID){
                        if(move.isAttack()){
                            try{
                                Image image = ImageIO.read(new File(miscIconPath + "red_dot.png"));
                                Image scaledImage = image.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
                                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                                add(new JLabel(scaledIcon));
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            try{
                                add(new JLabel(new ImageIcon(ImageIO.read(new File(miscIconPath + "green_dot.png")))));
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final ChessBoard chessBoard){
            if(movedPiece != null && movedPiece.getPieceColor() == chessBoard.activePlayer().getPlayerColor()){
                return movedPiece.validMove(chessBoard);
            }
            return Collections.emptyList();
        }

    }

    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }
            @Override
            BoardDirection flip() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                Collections.reverse(boardTiles);
                return boardTiles;
            }
            @Override
            BoardDirection flip() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract  BoardDirection flip();
    }

}
