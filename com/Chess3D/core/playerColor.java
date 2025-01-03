package com.Chess3D.core;

import com.Chess3D.core.board.generalBoardRules;
import com.Chess3D.core.player.BlackPlayer;
import com.Chess3D.core.player.Player;
import com.Chess3D.core.player.WhitePlayer;

public enum playerColor {
    WHITE{
        @Override	
        public Player chooseActivePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
        @Override
        public String toString() {
            return "W";
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }
        @Override
        public boolean isPawnPromotionTile(int tileCoordinate) {
            return generalBoardRules.firstRow[tileCoordinate];
        }

        
    },
    BLACK{
        @Override
        public Player chooseActivePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
        @Override
        public String toString() {
            return "B";
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }
        @Override
        public boolean isPawnPromotionTile(int tileCoordinate) {
            return generalBoardRules.eighthRow[tileCoordinate];
        }
    };

    public abstract Player chooseActivePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionTile(int tileCoordinate);
}


