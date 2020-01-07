package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coordinates;
import com.checkers.engine.board.Move;
import com.checkers.engine.board.Tile;

import java.util.List;

public abstract class Piece {

    public Coordinates pieceCoordinates;
    final Alliance pieceAlliance;
    private Tile occupiedTile;

    public Tile getOccupied() {
        return occupiedTile;
    }

    Piece(final Alliance alliance, Tile occupiedTile){
        pieceAlliance=alliance;
        this.occupiedTile = occupiedTile;
    }

    final public int direction(){
        return pieceAlliance==Alliance.WHITE ? 1 : -1;
    }

    final public boolean isWhite() {
        return pieceAlliance==Alliance.WHITE;
    }

    final public Alliance getPieceAlliance(){
        return pieceAlliance;
    }

    public void moveTo(final Tile destination){
        occupiedTile.freeUp();
        occupiedTile = destination;
        occupiedTile.setOccupant(this);
    }

    public void takeDown(){
        occupiedTile.freeUp();
        occupiedTile = null;
    }

    public abstract List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly);

}
