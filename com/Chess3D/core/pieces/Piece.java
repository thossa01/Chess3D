package com.Chess3D.core.pieces;

import com.Chess3D.core.board.ChessBoard;
import com.Chess3D.core.board.Move;
import com.Chess3D.core.playerColor;
import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int pieceTile;
    protected final playerColor pieceColor;
    private final boolean isFirstMove;
    
    public Piece(final PieceType pieceType, final int pieceTile, final playerColor pieceColor, final boolean isFirstMove){
        this.pieceType = pieceType;
        this.pieceTile = pieceTile;
        this.pieceColor = pieceColor;
        this.isFirstMove = isFirstMove;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return pieceTile == otherPiece.getPieceTile() && pieceColor == otherPiece.getPieceColor() && pieceType == otherPiece.getPieceType();
    }

    @Override
    public int hashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceTile;
        result = 31 * result + pieceColor.hashCode();
        return result;
    }

    public PieceType getPieceType(){
        return pieceType;
    }

    public int getPieceTile(){
        return pieceTile;
    }

    public playerColor getPieceColor(){
        return pieceColor;
    }
    
    public abstract Collection<Move> validMove(final ChessBoard board);
    public abstract Piece movePiece(Move move);

    public enum PieceType{
        PAWN("P"){

            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook(){
                return false;
            }
        },
        KNIGHT("N"){

            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook(){
                return false;
            }
        },
        BISHOP("B"){

            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook(){
                return false;
            }
        },
        ROOK("R"){

            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook(){
                return true;
            }
        },
        QUEEN("Q"){

            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook(){
                return false;
            }
        },
        KING("K"){

            @Override
            public boolean isKing(){
                return true;
            }

            @Override
            public boolean isRook(){
                return false;
            }
        };

        private final String name;
        PieceType(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return this.name;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
        
    } 
}

