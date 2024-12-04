package com.Chess3D.core.board;

import com.Chess3D.core.board.ChessBoard.BoardBuilder;
import com.Chess3D.core.pieces.Pawn;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.core.pieces.Rook;


public abstract class Move {

    final ChessBoard board;
    final Piece playingPiece;
    final int destinationCoordinate;
    final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate) {
        this.board = board;
        this.playingPiece = playingPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = playingPiece.isFirstMove();

    }

    private Move(final ChessBoard board, final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.playingPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.playingPiece.hashCode();
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.playingPiece.getPieceTile();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() && getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getPlayingPiece().equals(otherMove.getPlayingPiece());
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public Piece getPlayingPiece() {
        return this.playingPiece;
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public int getCurrentCoordinate() {
        return this.playingPiece.getPieceTile();
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public ChessBoard executeMove() {
        final ChessBoard.BoardBuilder boardBuilder = new ChessBoard.BoardBuilder();
        for (final Piece piece : this.board.activePlayer().getcurrentPlayerPieces()) {
            if (!this.playingPiece.equals(piece)) {
                boardBuilder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.activePlayer().getOppponent().getcurrentPlayerPieces()) {
            boardBuilder.setPiece(piece);
        }
        boardBuilder.setPiece(this.playingPiece.movePiece(this));
        boardBuilder.setNextToPlay(this.board.activePlayer().getOppponent().getPlayerColor());
        return boardBuilder.build();
    }



    public static final class normalMove extends Move {
        public normalMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate) {
            super(board, playingPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof normalMove && super.equals(other));
        }

        @Override
        public String toString() {
            return playingPiece.getPieceType().toString() + generalBoardRules.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class attackingMove extends Move {
        final Piece enemyPiece;
        public attackingMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate, final Piece enemyPiece) {
            super(board, playingPiece, destinationCoordinate);
            this.enemyPiece = enemyPiece;
        }

        @Override
        public int hashCode() {
            return this.enemyPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof attackingMove)) {
                return false;
            }
            final Move otherAttackMove = (attackingMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.enemyPiece;
        }
    }

    public static final class MajorAttackMove extends attackingMove{

        public MajorAttackMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate, final Piece enemyPiece) {
            super(board, playingPiece, destinationCoordinate, enemyPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof MajorAttackMove && super.equals(other));
        }

        @Override
        public String toString() {
            return this.playingPiece.getPieceType().toString() + generalBoardRules.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate) {
            super(board, playingPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof PawnMove && super.equals(other));
        }

        @Override
        public String toString() {
            return generalBoardRules.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnAttackMove extends attackingMove {
        public PawnAttackMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate, final Piece enemyPiece) {
            super(board, playingPiece, destinationCoordinate, enemyPiece);
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PawnAttackMove)) {
                return false;
            }
            final Move otherAttackMove = (attackingMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public String toString() {
            return generalBoardRules.getPositionAtCoordinate(this.playingPiece.getPieceTile()).substring(0, 1) + "x" + generalBoardRules.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnEnPassantMove extends PawnAttackMove {

        public PawnEnPassantMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate, final Piece enemyPiece) {
            super(board, playingPiece, destinationCoordinate, enemyPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof PawnEnPassantMove && super.equals(other));
        }

        @Override
        public ChessBoard executeMove() {
            final BoardBuilder builder = new BoardBuilder();
            for (final Piece piece : this.board.activePlayer().getcurrentPlayerPieces()) {
                if (!this.playingPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.activePlayer().getOppponent().getcurrentPlayerPieces()) {
                if (!piece.equals(this.enemyPiece)) { 
                    builder.setPiece(piece);
                }
            }
            final Pawn movedPawn = (Pawn)this.playingPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setNextToPlay(this.board.activePlayer().getOppponent().getPlayerColor());
            return builder.build();
        }
    }

    public static class PawnJump extends Move {

        public PawnJump(final ChessBoard board, final Pawn playingPiece,
                        final int destinationCoordinate) {
            super(board, playingPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }

        @Override
        public ChessBoard executeMove() {
            final BoardBuilder builder = new BoardBuilder();
            for (final Piece piece : this.board.activePlayer().getcurrentPlayerPieces()) {
                if (!this.playingPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.activePlayer().getOppponent().getcurrentPlayerPieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.playingPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.EnPassantPawn(movedPawn);
            builder.setNextToPlay(this.board.activePlayer().getOppponent().getPlayerColor());
            return builder.build();
        }

        @Override
        public String toString() {
            return generalBoardRules.getPositionAtCoordinate(this.destinationCoordinate);
        }

        }

    public static final class PawnPromotion extends Move {
        final Move decoratedMove;
        final Pawn promotedPawn;
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getPlayingPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getPlayingPiece();
        }
        @Override
        public ChessBoard executeMove() {
            final ChessBoard board = this.decoratedMove.executeMove();
            final BoardBuilder builder = new BoardBuilder();
            for (final Piece piece : board.activePlayer().getcurrentPlayerPieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : board.activePlayer().getOppponent().getcurrentPlayerPieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setNextToPlay(this.board.activePlayer().getOppponent().getPlayerColor());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString() {
            return decoratedMove.toString();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31*promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnPromotion && decoratedMove.equals(other);
        }

    }

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, playingPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {  
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public ChessBoard executeMove() {
            final BoardBuilder boardBuilder = new BoardBuilder();
            for (final Piece piece : this.board.activePlayer().getcurrentPlayerPieces()) {
                if (!this.playingPiece.equals(piece)  && !this.castleRook.equals(piece)) {
                    boardBuilder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.activePlayer().getOppponent().getcurrentPlayerPieces()) {
                boardBuilder.setPiece(piece);
            }
            boardBuilder.setPiece(this.playingPiece.movePiece(this));
            boardBuilder.setPiece(new Rook(this.castleRook.getPieceTile(), this.castleRook.getPieceColor(), false));
            boardBuilder.setNextToPlay(this.board.activePlayer().getOppponent().getPlayerColor());
            return boardBuilder.build();
        }


        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            final CastleMove otherMove = (CastleMove)other;
            return this.getCastleRook().equals(otherMove.getCastleRook()) && super.equals(otherMove);
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, playingPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof KingSideCastleMove) && super.equals(other);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final ChessBoard board, final Piece playingPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, playingPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof QueenSideCastleMove) && super.equals(other);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, -1);
        }

        @Override
        public ChessBoard executeMove() {
            throw new RuntimeException("Impossible move");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final ChessBoard board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }


}



