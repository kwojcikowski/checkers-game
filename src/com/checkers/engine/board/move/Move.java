package com.checkers.engine.board.move;

import com.checkers.engine.board.Coords;

public class Move {

    public final Coords destinationCoords;
    private boolean availableNow;

    protected Move(final Coords destinationCoords, final boolean isAvailableNow){
        this.destinationCoords = destinationCoords;
        this.availableNow = isAvailableNow;
    }

    public static Move to(final Coords destination, final boolean isAvailableNow){
        return new Move(destination, isAvailableNow);
    }

    public boolean isAttacking(){
        return false;
    }

    public boolean isAvailableNow() {
        return availableNow;
    }

}
