package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coordinates;
import com.checkers.engine.board.Move;
import com.checkers.engine.board.Tile;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Pawn extends Piece {

    public Pawn(final Alliance alliance, Tile occupiedTile) {
        super(alliance, occupiedTile);
    }

    public static Pawn from(final Pawn pawn){
        return new Pawn(pawn.pieceAlliance, pawn.getOccupied());
    }

    King promote() {
        return King.from(this);
    }

    @Override
    public List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly) {
        //TODO check for moves
        LinkedList<Move> list = new LinkedList<>();
        list.add(new Move(new Coordinates(getOccupied().tileCoordinates.x -1, getOccupied().tileCoordinates.y -1), false, true));
        return list;
    }
}
