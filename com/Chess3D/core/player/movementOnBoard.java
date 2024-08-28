package com.Chess3D.core.player;
import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.MoveStatus;

public class movementOnBoard {

    private final ChessBoard board;
    private final Move move;
    private final MoveStatus moveStatus;

    public movementOnBoard(ChessBoard board, Move move, MoveStatus moveStatus) {
        this.board = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public ChessBoard getNewBoard() {
        return this.board;
    }

}
