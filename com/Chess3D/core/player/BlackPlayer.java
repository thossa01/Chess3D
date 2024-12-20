package com.Chess3D.core.player;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.board.Move.KingSideCastleMove;
import com.Chess3D.core.board.Move.QueenSideCastleMove;
import com.Chess3D.core.board.Tile;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.core.pieces.Rook;
import com.Chess3D.core.playerColor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlackPlayer extends Player{

    public BlackPlayer(final ChessBoard board, final Collection<Move> blackLegalMoves, final Collection<Move> whiteLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getcurrentPlayerPieces() {
        return board.getBlackPieces();
    }


    @Override
    public playerColor getPlayerColor() {
        return playerColor.BLACK;
    }


    @Override
    public Player getOppponent() {
        return board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingSideCastleMoves(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isKingCheck) {
            if (!this.board.getTile(5).tileOccupation() && !this.board.getTile(6).tileOccupation()) {
                final Tile rookTile = this.board.getTile(7);
                if (rookTile.tileOccupation() && rookTile.getPiece().isFirstMove()) {
                    if(Player.getCurrentTileAttack(5, opponentLegalMoves).isEmpty() && Player.getCurrentTileAttack(6, opponentLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }


            if (!this.board.getTile(1).tileOccupation() && !this.board.getTile(2).tileOccupation() && !this.board.getTile(3).tileOccupation()) {
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.tileOccupation() && rookTile.getPiece().isFirstMove()) {
                    if(Player.getCurrentTileAttack(2, opponentLegalMoves).isEmpty() && Player.getCurrentTileAttack(3, opponentLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 2, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                    }
                }
            }
        }

        
        
        return Collections.unmodifiableList(kingCastles);
    }

}
