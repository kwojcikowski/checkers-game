package com.checkers.engine.pieces;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Move;

import java.util.List;
import com.checkers.engine.Alliance;

public class King extends Piece{

    King(final Alliance alliance){
        super(alliance);
    }

    protected static King from(final Pawn pawn) {
        return new King(pawn.pieceAlliance);
    }

    @Override
    List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly) {
        //TODO check for moves
        return null;
    }

}
