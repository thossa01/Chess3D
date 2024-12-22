package com.Chess3D.core.board;

public enum MoveStatus {
    OK{
        @Override
        public boolean isOk() {
            return true;
        }
        
    },
    ILLEGAL{
        @Override
        public boolean isOk() {
            return false;
        }
    },

    PLAYER_IN_CHECK{
        @Override
        public boolean isOk() {
            return false;
        }
    };
    public abstract boolean isOk();
    
}
