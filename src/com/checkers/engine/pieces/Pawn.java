package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coordinates;
import com.checkers.engine.board.Move;
import com.checkers.engine.board.Tile;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(final Alliance alliance) {
        super(alliance);
    }

    public static Pawn from(final Pawn pawn){
        return new Pawn(pawn.pieceAlliance);
    }

    King promote() {
        return King.from(this);
    }

    @Override
    protected List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly) {
        //TODO check for moves
        return null;
    }
}
