package com.checkers.engine.board;

public class Move {

    final Coordinates destinationCoordinates;
    boolean isAttacking;
    private boolean availableNow;
    //TODO List<Move> nextPossibleMoves

    public Move(final Coordinates destinationCoordinates, final boolean isAttacking, final boolean isAvailableNow){
        this.destinationCoordinates = destinationCoordinates;
        this.isAttacking = isAttacking;
        this.availableNow = isAvailableNow;
    }

    boolean isAttacking() {
        return isAttacking;
    }

    boolean isAvailableNow() {
        return availableNow;
    }
}
