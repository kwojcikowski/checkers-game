package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.move.Move;
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
        coords = occupiedTile.coords;
    }

    final public boolean areEnemies(Piece other){
        return this.pieceAlliance!=other.pieceAlliance;
    }

    public Alliance getPieceAlliance(){
        return pieceAlliance;
    }

    public void moveTo(final Tile destination){
        occupiedTile.freeUp();
        occupiedTile = destination;
        occupiedTile.setOccupant(this);
        coords = occupiedTile.coords;
    }

    public void takeDown(){
        occupiedTile.freeUp();
        occupiedTile = null;
    }

    public abstract List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly);

}
