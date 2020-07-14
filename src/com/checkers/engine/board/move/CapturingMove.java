package com.checkers.engine.board.move;

import com.checkers.engine.board.Coords;

public class CapturingMove extends Move{

    private Coords attackedPieceCoords;

    private CapturingMove(Coords destinationCoords, boolean isAvailableNow, Coords attackedPieceCoords) {
        super(destinationCoords, isAvailableNow);
        this.attackedPieceCoords = attackedPieceCoords;
    }

    public static Move to(final Coords destination, final boolean isAvailableNow, final Coords attackedPieceCoords){
        return new CapturingMove(destination, isAvailableNow, attackedPieceCoords);
    }

    public Coords getAttackedPieceCoords() {
        return attackedPieceCoords;
    }

    @Override
    public boolean isAttacking() {
        return true;
    }
}
