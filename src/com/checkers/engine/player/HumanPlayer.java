package com.checkers.engine.player;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.controllers.GameController;
import com.checkers.engine.pieces.Piece;
import com.checkers.engine.player.strategy.MovePickingStrategy;

import java.util.Set;

public class HumanPlayer extends Player {

    public HumanPlayer(Alliance alliance, GameController gameController) {
        this.alliance = alliance;
        this.gameController = gameController;
    }

    @Override
    public void performNextTurn(Board board) {
        gameController.resetMoveInteractions(alliance);

    }
}
