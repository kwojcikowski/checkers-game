package com.checkers.engine.board;

public class Move {

    public final Coordinates destinationCoordinates;
    boolean isAttacking;
    private boolean availableNow;
    //TODO List<Move> nextPossibleMoves

    public Move(final Coordinates destinationCoordinates, final boolean isAttacking, final boolean isAvailableNow){
        this.destinationCoordinates = destinationCoordinates;
        this.isAttacking = isAttacking;
        this.availableNow = isAvailableNow;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isAvailableNow() {
        return availableNow;
    }
}
