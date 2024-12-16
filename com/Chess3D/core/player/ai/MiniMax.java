package com.Chess3D.core.player.ai;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.MoveStatus;
import com.Chess3D.core.player.movementOnBoard;
import com.Chess3D.core.playerColor;

public class MiniMax implements MoveStrategy {

    public final BoardEvaluator boardEvaluator;
    private final int depth;

    public MiniMax(final int depth) {
        this.depth = depth;
        this.boardEvaluator = new standardBoardEvaluator();
    }
    @Override
    public String toString() {
        return "MiniMax";
    }
    @Override
    public Move execute(ChessBoard board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;
        int current;

        System.out.println(board.activePlayer().getPlayerColor() + " is thinking with depth: " + this.depth + "...");

        int numMoves = board.activePlayer().getPlayerLegalMoves().size();
        for (final Move move: board.activePlayer().getPlayerLegalMoves()){
            final movementOnBoard movementOnBoard = board.activePlayer().movePiece(move);
            if (movementOnBoard.getMoveStatus() == MoveStatus.OK){
                current = board.activePlayer().getPlayerColor() == playerColor.WHITE ? max(movementOnBoard.getNewBoard(), this.depth - 1) : min(movementOnBoard.getNewBoard(), this.depth - 1);
                if (board.activePlayer().getPlayerColor() == playerColor.WHITE && current >= highest){
                    highest = current;
                    bestMove = move;
                }else if (board.activePlayer().getPlayerColor() == playerColor.BLACK && current <= lowest){
                    lowest = current;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;
        return bestMove;
    }

    public int min(final ChessBoard board, final int depth){
        if (depth == 0 || isGameOver(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }

        int lowest = Integer.MAX_VALUE;
        for (final Move move: board.activePlayer().getPlayerLegalMoves()){
            final movementOnBoard movementOnBoard = board.activePlayer().movePiece(move);
            if (movementOnBoard.getMoveStatus() == MoveStatus.OK){
                final int currentValue = max(movementOnBoard.getNewBoard(), depth - 1);
                if (currentValue <= lowest){
                    lowest = currentValue;
                }
            }
        }

        return lowest;
            
    }

    public int max(final ChessBoard board, final int depth){
        if (depth == 0 || isGameOver(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highest = Integer.MIN_VALUE;
        for (final Move move: board.activePlayer().getPlayerLegalMoves()){
            final movementOnBoard movementOnBoard = board.activePlayer().movePiece(move);
            if (movementOnBoard.getMoveStatus() == MoveStatus.OK){
                final int currentValue = min(movementOnBoard.getNewBoard(), depth - 1);
                if (currentValue <= highest){
                    highest = currentValue;
                }
            }
        }

        return highest;
    }

    private boolean isGameOver(ChessBoard board) {
        return board.activePlayer().StaleMate() || board.activePlayer().CheckMate();
    }

}
