package com.Chess3D.core.pieces;

import static com.Chess3D.core.board.generalBoardRules.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.Chess3D.core.playerColor;
import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.Move.normalMove;
import com.Chess3D.core.board.Tile;
import com.Chess3D.core.board.generalBoardRules;

public class Queen extends Piece {
    private final static int[] Possible_Moves = {-9, -8, -7, -1, 1, 7, 8, 9};
    
    public Queen(final int pieceTile, final playerColor pieceColor) {
        super( PieceType.QUEEN, pieceTile, pieceColor, true);
    }

    public Queen(final int pieceTile, final playerColor pieceColor, final boolean isFirstMove) {
        super(PieceType.QUEEN, pieceTile, pieceColor, isFirstMove);
    }
    @Override
    public List<Move> validMove(final ChessBoard board) {
        int possibleDestination;
        final List<Move> validMoves = new ArrayList<>();

        for (final int currentMove : Possible_Moves) {
            possibleDestination = this.pieceTile;
            while(isValidTileCoordinate(possibleDestination)){
                if (firstColumnPos(possibleDestination, currentMove) ||  eighthColumnPos(possibleDestination, currentMove)) {
                    break;
                }

                possibleDestination += currentMove;
                if (isValidTileCoordinate(possibleDestination)) {
                
                    final Tile possibleDestinationTile = board.getTile(possibleDestination);
                    if (!possibleDestinationTile.tileOccupation()){
                        validMoves.add(new normalMove(board, this, possibleDestination));
                    }else{
                        final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                        final playerColor pieceColorDestination = pieceAtDestination.getPieceColor();
                        if (this.pieceColor != pieceColorDestination){
                            validMoves.add(new Move.MajorAttackMove(board, this, possibleDestination, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(validMoves);
    }   

    @Override
    public String toString() {
        return pieceType.QUEEN.toString();
    }

    @Override
    public Piece movePiece(final Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getPlayingPiece().getPieceColor());
    }

    private static boolean firstColumnPos(final int currentPosition, final int Possible_Move) {
        return generalBoardRules.firstColumn[currentPosition] && ((Possible_Move == -1) ||(Possible_Move == -9) || (Possible_Move == 7));   
    }
    private static boolean eighthColumnPos(final int currentPosition, final int Possible_Move) {
        return generalBoardRules.eighthColumn[currentPosition] && ((Possible_Move == 1) ||(Possible_Move == 9) || (Possible_Move == -7));
    }
    
}
