package com.checkers.engine.board.move;

import com.checkers.engine.board.Coords;

public class CapturingMove extends Move{

    private final Coords attackedPieceCoords;

    private CapturingMove(Coords destinationCoords,
                          boolean isAvailableNow,
                          Coords attackedPieceCoords) {
        super(destinationCoords, isAvailableNow);
        this.attackedPieceCoords = attackedPieceCoords;
    }

    public static Move to(Coords destination,
                          boolean isAvailableNow,
                          Coords attackedPieceCoords){
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
