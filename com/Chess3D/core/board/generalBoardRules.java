package com.Chess3D.core.board;

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
    
    //public static final String[] ALGEBREIC_NOTATION = initializeAlgebricNotation();
    //public static final Map<String, Integer> POSITIONAL_NOTATION = initializePositionalNotation();

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
    
    /*public static int getCoordinateAtPosition(final String position) {
        return POSITIONAL_NOTATION.getOrDefault(position, -1);
    }
    public static String getPositionAtCoordinate(final int coordinate) {

    }*/
}
