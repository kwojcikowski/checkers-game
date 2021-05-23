package com.checkers.engine.player;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.pieces.Piece;

import java.util.Set;

public class HumanPlayer extends Player {

    @Override
    public void performNextTurn(Board board) {

        Move pickedMove = gameController.getHumanMove();
    }
}
