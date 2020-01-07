package com.checkers.engine.pieces;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Move;

import java.util.List;
import com.checkers.engine.Alliance;
import com.checkers.engine.board.Tile;

public class King extends Piece{

    King(final Alliance alliance, Tile occupiedTile){
        super(alliance, occupiedTile);
    }

    protected static King from(final Pawn pawn) {
        return new King(pawn.pieceAlliance, pawn.getOccupied());
    }

    @Override
    public List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly) {
        //TODO check for moves
        return null;
    }

}
