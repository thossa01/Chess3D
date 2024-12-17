package com.Chess3D.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.Chess3D.core.playerColor;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.gui.Table.MoveRecord;



public class CapturedPiecesPanel extends JPanel {

    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_BACKGROUND_COLOR = Color.decode("0xFDF5F6");
    private static final Dimension TakenPieceDimension = new Dimension(50, 50);
    private final static String pieceIconPath = "art/pieces/common/";


    private final JPanel BlackPiecesPanel;
    private final JPanel WhitePiecesPanel;
    public CapturedPiecesPanel() {
        super(new BorderLayout());
        setBackground(PANEL_BACKGROUND_COLOR);
        setBorder(PANEL_BORDER);
        this.BlackPiecesPanel = new JPanel(new GridLayout(8, 2));
        this.WhitePiecesPanel = new JPanel(new GridLayout(8, 2));

        this.BlackPiecesPanel.setBackground(PANEL_BACKGROUND_COLOR);
        this.WhitePiecesPanel.setBackground(PANEL_BACKGROUND_COLOR);
        this.add(this.BlackPiecesPanel, BorderLayout.WEST);
        this.add(this.WhitePiecesPanel, BorderLayout.EAST);
        setPreferredSize(TakenPieceDimension);


    }

    public void redoMove(final MoveRecord moveRecord) {
        BlackPiecesPanel.removeAll();
        WhitePiecesPanel.removeAll();
        
        final List<Piece> blackTakenList = new ArrayList<>();
        final List<Piece> whiteTakenList = new ArrayList<>();

        for (final Move move : moveRecord.getMoveList()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceColor() == playerColor.WHITE) {
                    whiteTakenList.add(takenPiece);
                } else if (takenPiece.getPieceColor() == playerColor.BLACK) {
                    blackTakenList.add(takenPiece);
                }else{
                    throw new RuntimeException("Invalid Piece Color!");
                }
            }
        }

        Collections.sort(blackTakenList, new Comparator<Piece>(){
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        Collections.sort(whiteTakenList, new Comparator<Piece>(){
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for (final Piece takenPiece : blackTakenList) {
            try {
                final BufferedImage image = ImageIO.read(new File(pieceIconPath + takenPiece.getPieceColor().toString().substring(0, 1) + takenPiece.toString() + ".gif"));
                final ImageIcon imageIcon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(imageIcon);
                this.BlackPiecesPanel.add(imageLabel);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        for (final Piece takenPiece : whiteTakenList) {
            try {
                final BufferedImage image = ImageIO.read(new File(pieceIconPath + takenPiece.getPieceColor().toString().substring(0, 1) + takenPiece.toString() + ".gif"));
                final ImageIcon imageIcon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(imageIcon);
                this.BlackPiecesPanel.add(imageLabel);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        validate();

    }
}
