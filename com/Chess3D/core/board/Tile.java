package com.Chess3D.core.board;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.Chess3D.core.playerColor;
import com.Chess3D.core.pieces.Piece;

public abstract class Tile {

    protected final int tileCoordinates;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_MAP = createAllEmptyTiles();
    
    private static Map<Integer, EmptyTile> createAllEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTilesMap = new HashMap<>();
        for (int i = 0; i < 64; i++) {
            emptyTilesMap.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTilesMap);
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_MAP.get(tileCoordinate);
    }

    private Tile(final int tileCoordinates) {
        this.tileCoordinates = tileCoordinates;
    }

    public int getTileCoordinate() {
        return this.tileCoordinates;
    }

    public abstract boolean tileOccupation();
    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile {

        private EmptyTile(final int tileCoordinates) {
            super(tileCoordinates);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean tileOccupation() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {

        private final Piece occupyingPiece;

        private OccupiedTile(int tileCoordinates, final Piece occupyingPiece) {
            super(tileCoordinates);
            this.occupyingPiece = occupyingPiece;
        }

        @Override
        public String toString() {
            return (getPiece().getPieceColor() == playerColor.BLACK)? getPiece().toString().toLowerCase() : getPiece().toString();
        }

        @Override
        public boolean tileOccupation() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return occupyingPiece;
        }
    }

}
