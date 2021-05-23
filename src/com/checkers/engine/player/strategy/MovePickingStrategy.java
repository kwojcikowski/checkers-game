package com.checkers.engine.player.strategy;

import com.checkers.engine.board.move.Move;

import java.util.List;

public interface MovePickingStrategy {

    public Move getMove(List<Move> possibleMoves);

}
