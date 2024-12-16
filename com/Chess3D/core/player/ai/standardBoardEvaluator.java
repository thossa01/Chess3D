

package com.Chess3D.core.player.ai;
import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.core.player.Player;

public final class standardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    @Override
    public int evaluate(final ChessBoard board, final int depth) {
        return scorePlayer(board, board.getWhitePlayer(), depth) - scorePlayer(board, board.getBlackPlayer(), depth);
    }

    private int scorePlayer(final ChessBoard board, final Player player, final int depth) {
        return pieceValue(player) + mobility(player) + check(player) + checkmate(player, depth) + castled(player);
    }

    private int pieceValue(Player player) {
        int value = 0;
        for (final Piece piece : player.getcurrentPlayerPieces()) {
            value += piece.getPieceValue();
        }
        return value;
    }

    private int mobility(Player player) {
        return player.getPlayerLegalMoves().size();
    }

    private int check(Player player) {
        return player.CheckMove() ? CHECK_BONUS : 0;
    }

    private int checkmate(Player player, final int depth) {
        return player.CheckMate()? CHECKMATE_BONUS * depthBonus(depth) : 0;
    }

    private int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

}
