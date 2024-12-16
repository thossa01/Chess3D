package com.Chess3D.core.player.ai;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;

public interface  MoveStrategy {
    Move execute(ChessBoard board);
}
