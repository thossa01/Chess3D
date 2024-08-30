package com.Chess3D.core.pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.Chess3D.core.playerColor;
import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.Move.PawnAttackMove;
import com.Chess3D.core.board.Move.PawnJump;
import com.Chess3D.core.board.Move.PawnMove;
import com.Chess3D.core.board.generalBoardRules;


public final class Pawn extends Piece {

    private final static int[] Possible_Moves = {8, 16, 7, 9};
    private int directionInt;

    public Pawn(final int pieceTile, final playerColor pieceColor) {
        super(PieceType.PAWN, pieceTile, pieceColor, true);
        switch (pieceColor){
            case playerColor.WHITE:
                directionInt = -1;
                break;
            case playerColor.BLACK:
                directionInt = 1;
                break;
            default:
                throw new RuntimeException("Invalid player color");
        }
    }

     public Pawn(final int pieceTile, final playerColor pieceColor, final boolean isFirstMove) {
        super(PieceType.PAWN, pieceTile, pieceColor, isFirstMove);
    }

    /*@Override
    public int locationBonus() {
        return this.pieceColor.pawnBonus(this.pieceTile);
    }*/

    @Override
    public List<Move> validMove(final ChessBoard board) {

        final List<Move> validMoves = new ArrayList<>();
        for (final int currentMove : Possible_Moves) {
            int possibleDestination = this.pieceTile + (directionInt * currentMove);

            if (!generalBoardRules.isValidTileCoordinate(possibleDestination)) {
                continue;
            }
            if (currentMove == 8 && board.getTile(possibleDestination).getPiece() == null) {
                /*if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
                    validMoves.add(new PawnPromotion(
                            new PawnMove(board, this, possibleDestination), PieceUtils.INSTANCE.getMovedQueen(this.pieceColor, possibleDestination)));
                    validMoves.add(new PawnPromotion(
                            new PawnMove(board, this, possibleDestination), PieceUtils.INSTANCE.getMovedRook(this.pieceColor, possibleDestination)));
                    validMoves.add(new PawnPromotion(
                            new PawnMove(board, this, possibleDestination), PieceUtils.INSTANCE.getMovedBishop(this.pieceColor, possibleDestination)));
                    validMoves.add(new PawnPromotion(
                            new PawnMove(board, this, possibleDestination), PieceUtils.INSTANCE.getMovedKnight(this.pieceColor, possibleDestination)));
                }
                else {
                    validMoves.add(new PawnMove(board, this, possibleDestination));
                }*/

                validMoves.add(new PawnMove(board, this, possibleDestination));
            }
            else if (currentMove == 16 && this.isFirstMove() &&
                    ((generalBoardRules.secondRow[this.pieceTile] && this.pieceColor.isBlack()) ||
                     (generalBoardRules.seventhRow[this.pieceTile] && this.pieceColor.isWhite()))) {
                final int behindpossibleDestination =
                        this.pieceTile + (directionInt * 8);
                if (board.getTile(possibleDestination).getPiece() == null &&
                board.getTile(behindpossibleDestination).getPiece() == null) {
                    validMoves.add(new PawnJump(board, this, possibleDestination));
                }
            }
            else if (currentMove == 7 &&
                    !((generalBoardRules.eighthColumn[this.pieceTile] && this.pieceColor.isWhite()) ||
                      (generalBoardRules.firstColumn[this.pieceTile] && this.pieceColor.isBlack()))) {
                if(board.getTile(possibleDestination).getPiece() != null) {
                    final Piece pieceAtDestination = board.getTile(possibleDestination).getPiece();
                    if (this.pieceColor != pieceAtDestination.getPieceColor()) {
                        /*if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination, pieceAtDestination), PieceUtils.INSTANCE.getMovedQueen(this.pieceColor, possibleDestination)));
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination, pieceAtDestination), PieceUtils.INSTANCE.getMovedRook(this.pieceColor, possibleDestination)));
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination, pieceAtDestination), PieceUtils.INSTANCE.getMovedBishop(this.pieceColor, possibleDestination)));
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination, pieceAtDestination), PieceUtils.INSTANCE.getMovedKnight(this.pieceColor, possibleDestination)));
                        }
                        else {
                            validMoves.add(new PawnAttackMove(board, this, possibleDestination, pieceAtDestination));
                        }*/

                        validMoves.add(new PawnAttackMove(board, this, possibleDestination, pieceAtDestination));
                    }
                }/* else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getpieceTile() ==
                           (this.pieceTile + (this.pieceColor.getOppositeDirection()))) {
                    final Piece pieceAtDestination = board.getEnPassantPawn();
                    if (this.pieceColor != pieceAtDestination.getPieceColor()) {
                        validMoves.add(
                                new PawnEnPassantAttack(board, this, possibleDestination, pieceAtDestination));

                    }
                } */
            }
            else if (currentMove == 9 &&
                    !((generalBoardRules.firstColumn[this.pieceTile] && this.pieceColor.isWhite()) ||
                      (generalBoardRules.eighthColumn[this.pieceTile] && this.pieceColor.isBlack()))) {
                if(board.getTile(possibleDestination).getPiece() != null) {
                    if (this.pieceColor != board.getTile(possibleDestination).getPiece().getPieceColor()) {
                        /*if (this.pieceColor.isPawnPromotionSquare(possibleDestination)) {
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination,
                                            board.getTile(possibleDestination).getPiece()), PieceUtils.INSTANCE.getMovedQueen(this.pieceColor, possibleDestination)));
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination,
                                            board.getTile(possibleDestination).getPiece()), PieceUtils.INSTANCE.getMovedRook(this.pieceColor, possibleDestination)));
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination,
                                            board.getTile(possibleDestination).getPiece()), PieceUtils.INSTANCE.getMovedBishop(this.pieceColor, possibleDestination)));
                            validMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, possibleDestination,
                                            board.getTile(possibleDestination).getPiece()), PieceUtils.INSTANCE.getMovedKnight(this.pieceColor, possibleDestination)));
                        }
                        else {
                            validMoves.add(new PawnAttackMove(board, this, possibleDestination, board.getTile(possibleDestination).getPiece()));
                        }*/

                        validMoves.add(new PawnAttackMove(board, this, possibleDestination, board.getTile(possibleDestination).getPiece()));
                    }
                } /*else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getpieceTile() ==
                        (this.pieceTile - (this.pieceColor.getOppositeDirection()))) {
                    final Piece pieceAtDestination = board.getEnPassantPawn();
                    if (this.pieceColor != pieceAtDestination.getPieceColor()) {
                        validMoves.add(
                                new PawnEnPassantAttack(board, this, possibleDestination, pieceAtDestination));

                    }
                }*/
            }
        }
        return Collections.unmodifiableList(validMoves);
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    /*@Override
    public Pawn movePiece(final Move move) {
        return PieceUtils.INSTANCE.getMovedPawn(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }*/

    @Override
    public Piece movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getPlayingPiece().getPieceColor());
    }

}
