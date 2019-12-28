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

    Piece(final Alliance alliance){
        pieceAlliance=alliance;
    }

    final public int direction(){
        return pieceAlliance==Alliance.WHITE ? 1 : -1;
    }

    final public boolean isWhite() {
        return pieceAlliance==Alliance.WHITE;
    }

    void moveTo(final Tile destination){
        occupiedTile.freeUp();
        occupiedTile = destination;
    }

    abstract List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly);

}
