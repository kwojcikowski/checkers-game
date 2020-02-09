package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.Move;
import com.checkers.engine.board.Tile;

import java.util.List;

public abstract class Piece {

    public Coords coords;
    final Alliance pieceAlliance;
    private Tile occupiedTile;

    public Tile getOccupied() {
        return occupiedTile;
    }

    Piece(final Alliance alliance, Tile occupiedTile){
        pieceAlliance=alliance;
        this.occupiedTile = occupiedTile;
        coords = occupiedTile.tileCoords;
    }

    final public boolean isWhite() {
        return pieceAlliance==Alliance.WHITE;
    }

    public boolean areAllies(Piece other){
        return this.pieceAlliance==other.pieceAlliance;
    }

    public boolean areEnemies(Piece other){
        return !areAllies(other);
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
