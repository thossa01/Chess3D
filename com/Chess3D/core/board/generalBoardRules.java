package com.Chess3D.core.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class generalBoardRules {
    

    public static final boolean[] firstColumn = columnTiles(0);
    public static final boolean[] secondColumn = columnTiles(1);
    public static final boolean[] seventhColumn = columnTiles(6);
    public static final boolean[] eighthColumn = columnTiles(7);
    public static final boolean [] firstRow = rowTiles(0);	
    public static final boolean [] secondRow = rowTiles(8);
    public static final boolean [] thirdRow = rowTiles(16);
    public static final boolean [] fourthRow = rowTiles(24);
    public static final boolean [] fifthRow = rowTiles(32);
    public static final boolean [] sixthRow = rowTiles(40);
    public static final boolean [] seventhRow = rowTiles(48);
    public static final boolean [] eighthRow = rowTiles(56);
    
    public static final String[] ALGEBREIC_NOTATION = initializeAlgebricNotation();
    public static final Map<String, Integer> POSITIONAL_NOTATION = initializePositionalNotation();

    private static boolean[] columnTiles(int columnFirstTile){

        final boolean[] currentColumn = new boolean[64];

        while (columnFirstTile < 64) {
            currentColumn[columnFirstTile] = true;
            columnFirstTile += 8;
        }
        return currentColumn;
    }

    private static boolean[] rowTiles(int rowFirstTile){
        final boolean[] currentRow = new boolean[64];
        final int rowLastTile = rowFirstTile + 8;
        while (rowFirstTile < rowLastTile) {
            currentRow[rowFirstTile] = true;
            rowFirstTile += 1;
        }
        return currentRow;
    }
    private generalBoardRules() {
        throw new RuntimeException("General Board Rules group, can not create an object.");
    }
    public static boolean isValidTileCoordinate(final int possibleDestination) {
        return possibleDestination >= 0 && possibleDestination < 64;
    }

    private static String[] initializeAlgebricNotation() {
        return new String[] {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    private static Map<String, Integer> initializePositionalNotation() {
        final Map<String, Integer> positionalNotation = new HashMap<>();
        for (int i = 0; i < 64; i++) {
            positionalNotation.put(ALGEBREIC_NOTATION[i], i);
        }
        return Collections.unmodifiableMap(positionalNotation);
    }
    
    public static int getCoordinateAtPosition(final String position) {
        return POSITIONAL_NOTATION.get(position);
    }
    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBREIC_NOTATION[coordinate];
    }
}
