package com.checkers.engine.board;

import com.checkers.engine.pieces.*;

public abstract class Tile {

    public final Coords coords;

    Tile(final Coords coords){
        this.coords = coords;
    }

    public abstract void setOccupant(Piece piece);
    public abstract boolean isOccupied();
    public boolean isFree() { return !isOccupied(); }
    public abstract Piece getOccupant();
    public abstract void freeUp();
    protected abstract Tile clone() throws CloneNotSupportedException;

    public static class BlackTile extends Tile {

        private Piece occupant;
        private boolean isOccupied;

        BlackTile(Coords tileCoords) {
            super(tileCoords);
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

        @Override
        protected BlackTile clone() throws CloneNotSupportedException {
            BlackTile alternativeTile = new BlackTile(this.coords);
            if(this.isOccupied){
                Piece alternativePiece;
                if(occupant instanceof Pawn)
                    alternativePiece = new Pawn(occupant.getPieceAlliance(), alternativeTile);
                else
                    alternativePiece = new King(occupant.getPieceAlliance(), alternativeTile);
                alternativeTile.setOccupant(alternativePiece);
            }
            return alternativeTile;
        }
    }

    public static class WhiteTile extends Tile {

        WhiteTile(Coords tileCoords) {
            super(tileCoords);
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

        @Override
        protected WhiteTile clone() throws CloneNotSupportedException {
            return new WhiteTile(this.coords);
        }
    }
}
