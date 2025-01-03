package com.Chess3D.core.pieces;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.Move.normalMove;
import com.Chess3D.core.board.Tile;
import com.Chess3D.core.board.generalBoardRules;
import static com.Chess3D.core.board.generalBoardRules.*;
import com.Chess3D.core.playerColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class King extends Piece{

    private final static int[] Possible_Moves = {-9, -8, -7, -1, 1, 7, 8, 9};
    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(final int pieceTile, final playerColor pieceColor, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(PieceType.KING, pieceTile, pieceColor, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final int pieceTile, final playerColor pieceColor, final boolean isFirstMove, final boolean isCastled, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(PieceType.KING, pieceTile, pieceColor, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }

    @Override
    public List<Move> validMove(final ChessBoard board) {

        int possibleDestination;
        final List<Move> validMoves = new ArrayList<>();

        for (final int currentMove : Possible_Moves) {
            possibleDestination = this.pieceTile + currentMove;
            if (isValidTileCoordinate(possibleDestination)) {

                if (firstColumnPos(possibleDestination, currentMove) || eighthColumnPos(possibleDestination, currentMove)) {
                    continue;
                }

                
                final Tile possibleDestinationTile = board.getTile(possibleDestination);
                if (!possibleDestinationTile.tileOccupation()){
                    validMoves.add(new normalMove(board, this, possibleDestination));
                }else{
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                    final playerColor pieceColorDestination = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceColorDestination){
                        validMoves.add(new Move.MajorAttackMove(board, this, possibleDestination, pieceAtDestination));
                    }
                }
            }
        }
        
        return Collections.unmodifiableList(validMoves);
    }

    @Override
    public String toString() {
        return pieceType.KING.toString();
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    @Override
    public Piece movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(), this.pieceColor, false, move.isCastlingMove(), false, false);
    }

    private static boolean firstColumnPos(final int currentPosition, final int Possible_Move) {
        return generalBoardRules.firstColumn[currentPosition] && ((Possible_Move == -9) || (Possible_Move == -1) || (Possible_Move == 7));   
    }

    private static boolean eighthColumnPos(final int currentPosition, final int Possible_Move) {
        return generalBoardRules.eighthColumn[currentPosition] && ((Possible_Move == 1) ||(Possible_Move == 9) || (Possible_Move == -7));
    }

    
}
