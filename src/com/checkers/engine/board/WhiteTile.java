package com.checkers.engine.board;

import com.checkers.engine.pieces.Piece;

public class WhiteTile extends Tile {

    private static final WhiteTile instance = new WhiteTile();

    public static WhiteTile get(){
        return instance;
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
    protected Tile clone() throws CloneNotSupportedException {
        return get();
    }

}
