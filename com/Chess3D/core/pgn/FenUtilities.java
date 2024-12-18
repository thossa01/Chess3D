package com.Chess3D.core.pgn;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.ChessBoard.BoardBuilder;
import com.Chess3D.core.board.generalBoardRules;
import com.Chess3D.core.pieces.Bishop;
import com.Chess3D.core.pieces.King;
import com.Chess3D.core.pieces.Knight;
import com.Chess3D.core.pieces.Pawn;
import com.Chess3D.core.pieces.Queen;
import com.Chess3D.core.pieces.Rook;
import com.Chess3D.core.playerColor;

public class FenUtilities {

    private FenUtilities() {
        throw new RuntimeException("Not Instantiable!");
    }

private static ChessBoard fenToBoard(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");
        final BoardBuilder builder = new BoardBuilder();
        final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);
        final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);
        final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);
        final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);
        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {
                case 'r' -> {
                    builder.setPiece(new Rook(i, playerColor.BLACK));
                    i++;
                }
                case 'n' -> {
                    builder.setPiece(new Knight(i, playerColor.BLACK));
                    i++;
                }
                case 'b' -> {
                    builder.setPiece(new Bishop(i, playerColor.BLACK));
                    i++;
                }
                case 'q' -> {
                    builder.setPiece(new Queen(i, playerColor.BLACK));
                    i++;
                }
                case 'k' -> {
                    final boolean isCastled = !blackKingSideCastle && !blackQueenSideCastle;
                    builder.setPiece(new King(i, playerColor.BLACK, blackKingSideCastle, blackQueenSideCastle));
                    i++;
                }
                case 'p' -> {
                    builder.setPiece(new Pawn(i, playerColor.BLACK));
                    i++;
                }
                case 'R' -> {
                    builder.setPiece(new Rook(i, playerColor.WHITE));
                    i++;
                }
                case 'N' -> {
                    builder.setPiece(new Knight(i, playerColor.WHITE));
                    i++;
                }
                case 'B' -> {
                    builder.setPiece(new Bishop(i, playerColor.WHITE));
                    i++;
                }
                case 'Q' -> {
                    builder.setPiece(new Queen(i, playerColor.WHITE));
                    i++;
                }
                case 'K' -> {
                    builder.setPiece(new King(i, playerColor.WHITE, whiteKingSideCastle, whiteQueenSideCastle));
                    i++;
                }
                case 'P' -> {
                    builder.setPiece(new Pawn(i, playerColor.WHITE));
                    i++;
                }
                case '-' -> i++;
                default -> throw new RuntimeException("Invalid FEN String " +gameConfiguration);
            }
        }
        builder.setNextToPlay(moveMaker(fenPartitions[1]));
        return builder.build();
    }
    private static playerColor moveMaker(final String moveMakerString) {
        if(moveMakerString.equals("w")) {
            return playerColor.WHITE;
        } else if(moveMakerString.equals("b")) {
            return playerColor.BLACK;
        }
        throw new RuntimeException("Invalid FEN String " +moveMakerString);
    }

    private static boolean whiteKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("K");
    }

    private static boolean whiteQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("Q");
    }

    private static boolean blackKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("k");
    }

    private static boolean blackQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("q");
    }

    public static String boardToFen(final ChessBoard board) {
        return calculateBoardText(board) + " " + calculateCurrentPlayerText(board) + " " + calculateCastlingText(board) + " " + calculateEnPassantText(board) + " " + "0 1";
    }

    private static String calculateCurrentPlayerText(final ChessBoard board) {
        return board.activePlayer().toString().substring(0, 1).toLowerCase();
    }
    private static String calculateCastlingText(ChessBoard board) {
        final StringBuilder sb = new StringBuilder();
        if (board.getWhitePlayer().isKingSideCastleCapable()) {
            sb.append("K");
        }
        if (board.getWhitePlayer().isQueenSideCastleCapable()) {
            sb.append("Q");
        }
        if (board.getBlackPlayer().isKingSideCastleCapable()) {
            sb.append("k");
        }
        if (board.getBlackPlayer().isQueenSideCastleCapable()) {
            sb.append("q");
        }
        return sb.toString().isEmpty() ? "-" : sb.toString();
    }
    private static String calculateEnPassantText(ChessBoard board) {
        final Pawn enPassant = board.getEnPassantPawn();
        if (enPassant == null) {
            return "-";
        }
        return generalBoardRules.getPositionAtCoordinate(enPassant.getPieceTile() + 8 * enPassant.getDirectionInt());
    }

    private static String calculateBoardText(ChessBoard board) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            final String tileString = board.getTile(i).toString();
            sb.append(tileString);
        }

        sb.insert(8, "/");
        sb.insert(17, "/");
        sb.insert(26, "/");
        sb.insert(35, "/");
        sb.insert(44, "/");
        sb.insert(53, "/");
        sb.insert(62, "/");
        return sb.toString().replaceAll("--------", "8").replaceAll("-------", "7").replaceAll("------", "6").replaceAll("-----", "5").replaceAll("----", "4").replaceAll("---", "3").replaceAll("--", "2").replaceAll("-", "1");
    }

}
