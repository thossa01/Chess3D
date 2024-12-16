package com.Chess3D.core.player.ai;

import com.Chess3D.core.board.ChessBoard;

public interface BoardEvaluator {
    int evaluate(ChessBoard board, int depth);
}
