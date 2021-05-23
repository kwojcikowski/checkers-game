package com.checkers.engine.player.strategy;

import com.checkers.engine.board.move.Move;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomFirstAttackStrategy implements MovePickingStrategy {

    Random random = new Random();

    @Override
    public Move getMove(List<Move> possibleMoves) {
        List<Move> capturingMoves = possibleMoves.stream()
                .filter(Move::isCapturing)
                .collect(Collectors.toList());
        if(capturingMoves.isEmpty())
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        else
            return capturingMoves.get(random.nextInt(capturingMoves.size()));
    }
}
