package com.checkers.engine.board;

import com.checkers.engine.pieces.Piece;

public class BlackTile extends Tile {

    private Piece occupant;
    private final Coords coords;

    BlackTile(Coords coords) {
        this.coords = coords;
    }

    public Coords getCoords() {
        return coords;
    }

    @Override
    public void setOccupant(Piece occupant) {
        this.occupant = occupant;
    }

    @Override
    public Piece getOccupant() {
        return occupant;
    }

    @Override
    public boolean isOccupied() {
        return this.occupant!=null;
    }

    @Override
    public void freeUp() {
        occupant = null;
    }

    @Override
    protected BlackTile clone() throws CloneNotSupportedException {
        BlackTile alternativeTile = new BlackTile(Coords.at(this.coords.x, this.coords.y));
        if(this.isOccupied()){
            var alternativePiece = occupant.clone();
            alternativeTile.setOccupant(alternativePiece);
            alternativePiece.setOccupiedTile(alternativeTile);
        }
        return alternativeTile;
    }
}
