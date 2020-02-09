package com.checkers.engine;

import com.checkers.engine.controllers.GameController;

public class AIGame extends Game {

    GameController controller;

    public AIGame(Game game, GameController controller){
        super(game);
        this.controller = controller;
    }

    @Override
    public void nextTurn() {
        super.nextTurn();
        if(getTurn() == Alliance.BLACK){
            System.out.println("Hello, AI here");
            super.nextTurn();
            //TODO AI moves
        }
    }
}
