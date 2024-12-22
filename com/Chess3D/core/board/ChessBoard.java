package com.Chess3D.core.board;

import com.Chess3D.core.pieces.Bishop;
import com.Chess3D.core.pieces.King;
import com.Chess3D.core.pieces.Knight;
import com.Chess3D.core.pieces.Pawn;
import com.Chess3D.core.pieces.Piece;
import com.Chess3D.core.pieces.Queen;
import com.Chess3D.core.pieces.Rook;
import com.Chess3D.core.player.BlackPlayer;
import com.Chess3D.core.player.Player;
import com.Chess3D.core.player.WhitePlayer;
import com.Chess3D.core.playerColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoard {

    public final List<Tile> currentGameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player activePlayer;
    private final Collection<Move> whiteLegalMoves;
    private final Collection<Move> blackLegalMoves;
    private final Pawn enPassantPawn;

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final playerColor pieceColor) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Tile tile : gameBoard) {
            if (tile.getPiece() != null && tile.getPiece().getPieceColor() == pieceColor) {
                activePieces.add(tile.getPiece());
            }
        }
        return Collections.unmodifiableList(activePieces);
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.validMove(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }

    private ChessBoard(final BoardBuilder boardBuilder){
        this.currentGameBoard = createGameBoard(boardBuilder);
        this.whitePieces = calculateActivePieces(this.currentGameBoard, playerColor.WHITE);
        this.blackPieces = calculateActivePieces(this.currentGameBoard, playerColor.BLACK);

        this.enPassantPawn = boardBuilder.EnPassantPawn;

        this.whiteLegalMoves = calculateLegalMoves(this.whitePieces);
        this.blackLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);
        this.activePlayer = boardBuilder.nextToPlay.chooseActivePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Player activePlayer(){
        return this.activePlayer;
    }

    public WhitePlayer getWhitePlayer() {
        return this.whitePlayer;
    }

    public BlackPlayer getBlackPlayer() {
        return this.blackPlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++){
            final String tile = this.currentGameBoard.get(i).toString();
            sb.append(String.format("%3s", tile));
            if ((i + 1) % 8 == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static List<Tile> createGameBoard(final BoardBuilder boardBuilder) {
        final Tile[] tiles = new Tile[64];
        for (int i = 0; i < 64; i++) {
            tiles[i] = Tile.createTile(i, boardBuilder.boardIDs.get(i));
        }
        return Collections.unmodifiableList(Arrays.asList(tiles));
    }

    public Tile getTile(final int tileCoordinate) {
        return currentGameBoard.get(tileCoordinate);
    }

    public static ChessBoard createStandardBoard(){
        final BoardBuilder boardBuilder = new BoardBuilder();
        boardBuilder.setPiece(new Rook(0, playerColor.BLACK));
        boardBuilder.setPiece(new Knight(1, playerColor.BLACK));
        boardBuilder.setPiece(new Bishop(2, playerColor.BLACK));
        boardBuilder.setPiece(new Queen(3, playerColor.BLACK));
        boardBuilder.setPiece(new King(4, playerColor.BLACK, true, true));
        boardBuilder.setPiece(new Bishop(5, playerColor.BLACK));
        boardBuilder.setPiece(new Knight(6, playerColor.BLACK));
        boardBuilder.setPiece(new Rook(7, playerColor.BLACK));
        boardBuilder.setPiece(new Pawn(8, playerColor.BLACK));
        boardBuilder.setPiece(new Pawn(9, playerColor.BLACK));
        boardBuilder.setPiece(new Pawn(10, playerColor.BLACK));
        boardBuilder.setPiece(new Pawn(11, playerColor.BLACK)); 
        boardBuilder.setPiece(new Pawn(12, playerColor.BLACK));
        boardBuilder.setPiece(new Pawn(13, playerColor.BLACK));
        boardBuilder.setPiece(new Pawn(14, playerColor.BLACK));
        boardBuilder.setPiece(new Pawn(15, playerColor.BLACK));

        boardBuilder.setPiece(new Pawn(48, playerColor.WHITE));
        boardBuilder.setPiece(new Pawn(49, playerColor.WHITE));
        boardBuilder.setPiece(new Pawn(50, playerColor.WHITE));
        boardBuilder.setPiece(new Pawn(51, playerColor.WHITE));
        boardBuilder.setPiece(new Pawn(52, playerColor.WHITE));
        boardBuilder.setPiece(new Pawn(53, playerColor.WHITE));
        boardBuilder.setPiece(new Pawn(54, playerColor.WHITE));
        boardBuilder.setPiece(new Pawn(55, playerColor.WHITE));
        boardBuilder.setPiece(new Rook(56, playerColor.WHITE));
        boardBuilder.setPiece(new Knight(57, playerColor.WHITE));
        boardBuilder.setPiece(new Bishop(58, playerColor.WHITE));
        boardBuilder.setPiece(new Queen(59, playerColor.WHITE));
        boardBuilder.setPiece(new King(60, playerColor.WHITE, true, true));
        boardBuilder.setPiece(new Bishop(61, playerColor.WHITE));
        boardBuilder.setPiece(new Knight(62, playerColor.WHITE));
        boardBuilder.setPiece(new Rook(63, playerColor.WHITE));

        boardBuilder.setNextToPlay(playerColor.WHITE);
        return boardBuilder.build();
    }

    public Collection<Move> getAllLegalMoves(){
        Collection<Move> AllLegalMoves = new ArrayList<>(this.whiteLegalMoves);
        AllLegalMoves.addAll(this.blackLegalMoves);
        return AllLegalMoves;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    


    public static class BoardBuilder{

        Map <Integer, Piece> boardIDs;
        playerColor nextToPlay;
        Pawn EnPassantPawn;

        public BoardBuilder(){
            this.boardIDs = new HashMap<>();
        }

        public BoardBuilder setPiece(final Piece piece){
            this.boardIDs.put(piece.getPieceTile(), piece);
            return this;
        }

        public BoardBuilder setNextToPlay(final playerColor nextToPlay){
            this.nextToPlay = nextToPlay;
            return this;
        }

        public ChessBoard build(){
            return new ChessBoard(this);
        }

        public BoardBuilder EnPassantPawn(final Pawn movedPawn){
            this.EnPassantPawn = movedPawn;
            return this;
        }



    }
}

}

