package com.Chess3D.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.Chess3D.core.playerColor;
import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;

public class GameHistoryPanel extends JPanel{

    private final DataModel dataModel;
    private final JScrollPane scrollPane;
    private static final Dimension HISTORY_PANEL = new Dimension(100, 400);
    public GameHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.dataModel = new DataModel();
        final JTable table = new JTable(this.dataModel);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void redo(final ChessBoard board, final Table.MoveRecord MoveRecord) {
        int currentRow = 0;
        this.dataModel.clear();
        for (final Move move : MoveRecord.getMoveList()) {
            final String moveText = move.toString();
            if (move.getPlayingPiece().getPieceColor() == playerColor.WHITE) {
                this.dataModel.setValueAt(moveText, currentRow, 0);
            }else if (move.getPlayingPiece().getPieceColor() == playerColor.BLACK) {
                this.dataModel.setValueAt(moveText, currentRow, 1);
            }
            currentRow++;
        }

        if (!MoveRecord.getMoveList().isEmpty()) {
            final Move lastMove = MoveRecord.getMoveList().get(MoveRecord.moveCount() - 1);
            final String moveText = lastMove.toString();
            if (lastMove.getPlayingPiece().getPieceColor() == playerColor.WHITE) {
                this.dataModel.setValueAt(moveText + calculateCheckOrCheckMateHash(board), currentRow, 0);
            } else if (lastMove.getPlayingPiece().getPieceColor() == playerColor.BLACK) {
                this.dataModel.setValueAt(moveText + calculateCheckOrCheckMateHash(board), currentRow - 1, 1);
            }
        }

        final JScrollBar scrollBar = new JScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
        this.scrollPane.setVerticalScrollBar(scrollBar);
    }

    private String calculateCheckOrCheckMateHash(ChessBoard board) {
        if (board.activePlayer().CheckMate()) {
            return "#";
        } else if (board.activePlayer().CheckMove()) {
            return "+";
        }
        return "";
    }

    private static class DataModel extends DefaultTableModel {
        private final List<Row> values;
        private static final String[] COLUMN_NAMES = {"White", "Black"};

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            final Row row = this.values.get(rowIndex);
            if (row == null) {
                return "";
            }
            return switch (columnIndex) {
                case 0 -> row.getWhiteMove();
                case 1 -> row.getBlackMove();
                default -> "";
            };
        }

        @Override
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            final Row row;
            if (this.values.size() <= rowIndex) {
                row = new Row();
                this.values.add(row);
            } else {
                row = this.values.get(rowIndex);
            }
            if (row == null) {
                return;
            }
            switch (columnIndex) {
                case 0 -> { 
                    row.setWhiteMove((String) aValue);
                    fireTableRowsInserted(rowIndex, rowIndex);
                }
                case 1 -> { 
                    row.setBlackMove((String) aValue);
                    fireTableRowsUpdated(rowIndex, columnIndex);
                }
                default -> {
                    return;
                }
            }
        }

        @Override
        public Class<?> getColumnClass(final int columnIndex) {
            return Move.class;
        }

        @Override
        public String getColumnName(final int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }

    }

    private static class Row {
        private String whiteMove;
        private String blackMove;

        Row(){
        }

        public String getWhiteMove() {
            return this.whiteMove;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setWhiteMove(final String whiteMove) {
            this.whiteMove = whiteMove;
        }

        public void setBlackMove(final String blackMove){
            this.blackMove = blackMove;
        }

    }
}
