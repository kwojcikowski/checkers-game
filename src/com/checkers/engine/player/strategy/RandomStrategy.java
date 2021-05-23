package com.checkers.engine.player.strategy;

import com.checkers.engine.board.move.Move;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements MovePickingStrategy {

    Random random = new Random();

    @Override
    public Move getMove(List<Move> possibleMoves) {
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

}
