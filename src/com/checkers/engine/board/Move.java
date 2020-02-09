package com.checkers.engine.board;

public class Move {

    public final Coords destinationCoords;
    boolean isAttacking;
    private boolean availableNow;
    //TODO List<Move> nextPossibleMoves

    private Move(final Coords destinationCoords, final boolean isAttacking, final boolean isAvailableNow){
        this.destinationCoords = destinationCoords;
        this.isAttacking = isAttacking;
        this.availableNow = isAvailableNow;
    }

    public static Move to(final Coords destination, final boolean isAttacking, final boolean isAvailableNow){
        return new Move(destination, isAttacking, isAvailableNow);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isAvailableNow() {
        return availableNow;
    }
}
