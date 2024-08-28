package com.Chess3D.gui;
import com.Chess3D.core.board.*;
import com.Chess3D.core.pieces.*;
import com.Chess3D.core.player.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.SwingUtilities.*;

public class Table {

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
    public Table() {
        this.gameFrame = new JFrame("Chess");

        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar menuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(menuBar);
        this.gameFrame.setSize(OUTER_DIMENSION);
        this.direction = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.chessBoard = ChessBoard.createStandardBoard();
        this.tablePanel = new TablePanel();
        this.gameFrame.add(this.tablePanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar populateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPreferencesMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Open PGN");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open PGN");
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
                            }

                            currentTile = null;
                            destinationTile = null;
                            movedPiece = null;


                        }

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                tablePanel.drawNewBoard(chessBoard);
                            }
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
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch(Exception e){
                            e.printStackTrace();
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