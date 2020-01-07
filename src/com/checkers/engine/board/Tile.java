package com.checkers.engine.board;

import com.checkers.engine.pieces.*;

public abstract class Tile {

    public final Coordinates tileCoordinates;

    Tile(final Coordinates tileCoordinates){
        this.tileCoordinates=tileCoordinates;
    }

    public abstract void setOccupant(Piece piece);
    public abstract boolean isOccupied();
    public abstract Piece getOccupant();
    public abstract void freeUp();

    public static class BlackTile extends Tile {

        private Piece occupant;
        private boolean isOccupied;

        BlackTile(Coordinates tileCoordinates) {
            super(tileCoordinates);
        }

        @Override
        public void setOccupant(final Piece occupant) {
            this.occupant = occupant;
            this.isOccupied = true;
        }

        @Override
        public Piece getOccupant() {
            return occupant;
        }

        @Override
        public boolean isOccupied() {
            return isOccupied;
        }

        @Override
        public void freeUp() {
            occupant = null;
            isOccupied = false;
        }

    }

    public static class WhiteTile extends Tile {

        WhiteTile(Coordinates tileCoordinates) {
            super(tileCoordinates);
        }

        @Override
        public void setOccupant(Piece piece) {
            throw new RuntimeException("Cannot set occupant on a white tile");
        }

        @Override
        public boolean isOccupied() {
            return false;
        }

        @Override
        public Piece getOccupant() {
            return null;
        }

        @Override
        public void freeUp() {
            throw new RuntimeException("Cannot free a white tile");
        }

    }
}
