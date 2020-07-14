package com.checkers.engine.board;

import com.checkers.engine.pieces.*;

public abstract class Tile {
    public abstract void setOccupant(Piece piece);
    public abstract boolean isOccupied();
    public boolean isFree() { return !isOccupied(); }
    public abstract Piece getOccupant();
    public abstract void freeUp();
    protected abstract Tile clone() throws CloneNotSupportedException;
}
