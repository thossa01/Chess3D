package com.Chess3D;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.gui.Table;

public class ChessGame {

    public static void main(String[] args) {
        ChessBoard board = ChessBoard.createStandardBoard();
        System.out.println(board);
        Table table = new Table();
    }
}
