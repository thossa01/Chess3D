package com.Chess3D.core.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.Chess3D.core.playerColor;
import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.Tile;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.core.pieces.Rook;


public class WhitePlayer extends Player{
    public WhitePlayer(final ChessBoard board, final Collection<Move> whiteLegalMoves, final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getcurrentPlayerPieces() {
        return board.getWhitePieces();
    }


    @Override
    public playerColor getPlayerColor() {
        return playerColor.WHITE;
    }


    @Override
    public Player getOppponent() {
        return board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingSideCastleMoves(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isKingCheck) {
            if (!this.board.getTile(61).tileOccupation() && !this.board.getTile(62).tileOccupation()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.tileOccupation() && rookTile.getPiece().isFirstMove()) {
                    if(Player.getCurrentTileAttack(61, opponentLegalMoves).isEmpty() && Player.getCurrentTileAttack(62, opponentLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                    }
                }
            }


            if (!this.board.getTile(59).tileOccupation() && !this.board.getTile(58).tileOccupation() && !this.board.getTile(57).tileOccupation()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.tileOccupation() && rookTile.getPiece().isFirstMove()) {
                    if(Player.getCurrentTileAttack(58, opponentLegalMoves).isEmpty() && Player.getCurrentTileAttack(59, opponentLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                    }
                }
            }
        }

        
        
        return Collections.unmodifiableList(kingCastles);
    }
}