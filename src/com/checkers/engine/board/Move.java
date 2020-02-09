package com.checkers.engine.board;

public class Move {

    public final Coords destinationCoords;
    public final Coords attackedPieceCords;
    boolean isAttacking;
    private boolean availableNow;
    //TODO List<Move> nextPossibleMoves

    private Move(final Coords destinationCoords, final boolean isAttacking, final boolean isAvailableNow, final Coords attackedPieceCords){
        this.destinationCoords = destinationCoords;
        this.isAttacking = isAttacking;
        this.availableNow = isAvailableNow;
        this.attackedPieceCords = attackedPieceCords;
    }

    public static Move to(final Coords destination, final boolean isAttacking, final boolean isAvailableNow, final Coords attackedPieceCords){
        return new Move(destination, isAttacking, isAvailableNow, attackedPieceCords);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isAvailableNow() {
        return availableNow;
    }
}
