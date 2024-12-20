package com.Chess3D.core.player;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.MoveStatus;
import com.Chess3D.core.pieces.King;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.core.playerColor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    protected final ChessBoard board;
    protected final King playerKing;
    protected final Collection<Move> playerLegalMoves;
    protected final Collection<Move> opponentLegalMoves;
    protected final boolean isKingCheck;

    protected Player(ChessBoard board, Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves) {
        this.board = board;
        this.playerKing = createKing();
        this.playerLegalMoves = Collections.unmodifiableCollection(getTotalLegalMoves(playerLegalMoves, calculateKingSideCastleMoves(playerLegalMoves, opponentLegalMoves)));
        this.opponentLegalMoves = Collections.unmodifiableCollection(getTotalLegalMoves(opponentLegalMoves, calculateKingSideCastleMoves(opponentLegalMoves, playerLegalMoves)));
        this.isKingCheck = !Player.getCurrentTileAttack(this.playerKing.getPieceTile(), opponentLegalMoves).isEmpty();
    }

    
    private Collection<Move> getTotalLegalMoves(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves) {
        Collection<Move> TotalLegalMoves = new ArrayList<>(playerLegalMoves);
        TotalLegalMoves.addAll(opponentLegalMoves);
        return TotalLegalMoves;
    }

    private King createKing() {
        for (final Piece piece: getcurrentPlayerPieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("King not found");
    }

    public Collection<Move> getPlayerLegalMoves(){
        return this.playerLegalMoves;
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public boolean isLegalMove(final Move move) {
        return playerLegalMoves.contains(move);
    }

    public boolean CheckMove(){
        return this.isKingCheck;
    }

    public boolean CheckMate(){
        return this.isKingCheck && !haslegalEscapeMoves();
    }

    public boolean haslegalEscapeMoves(){
        for (final Move move : playerLegalMoves) {
            final movementOnBoard trialMove = movePiece(move);
            if (trialMove.getMoveStatus().isOk()) {
                return true;
            }
        }

        return false;
    }

    public boolean StaleMate(){
        return this.isKingCheck && !haslegalEscapeMoves();
    }

    public boolean isCastled() {
        return this.playerKing.isCastled();
    }

    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }

    public movementOnBoard movePiece(Move move){
        if (!isLegalMove(move)) {
            return new movementOnBoard(board, move, MoveStatus.ILLEGAL);
        }
        else{
            final ChessBoard movementBoard = move.executeMove();

            final Collection<Move> attacksOnKing = Player.getCurrentTileAttack(movementBoard.activePlayer().getOppponent().getPlayerKing().getPieceTile(), movementBoard.activePlayer().getPlayerLegalMoves());
            if (!attacksOnKing.isEmpty()) {
                return new movementOnBoard(movementBoard, move, MoveStatus.PLAYER_IN_CHECK);
            }
            return new movementOnBoard(movementBoard, move, MoveStatus.OK);
        }
    }

    protected static Collection<Move> getCurrentTileAttack( int tile, Collection<Move> legalMoves) {
        final List<Move> currentAttacks = new ArrayList<>();
        for (final Move move : legalMoves) {
            if (tile == move.getDestinationCoordinate()) {
                currentAttacks.add(move);
            }
        }
        return Collections.unmodifiableList(currentAttacks);
    }

    public abstract Collection<Piece> getcurrentPlayerPieces();
    public abstract playerColor getPlayerColor();
    public abstract Player getOppponent();
    protected abstract Collection<Move> calculateKingSideCastleMoves(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves);
}
