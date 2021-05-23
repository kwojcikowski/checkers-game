package com.checkers.engine.player;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.controllers.GameController;
import com.checkers.engine.pieces.Piece;

import java.util.Set;

public abstract class Player {

    public Alliance alliance;
    public GameController gameController;

    public abstract void performNextTurn(Board board);

}
