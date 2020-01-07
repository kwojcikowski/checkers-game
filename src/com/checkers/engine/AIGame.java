package com.checkers.engine;

import com.checkers.engine.controllers.GameController;

public class AIGame extends Game {

    GameController controller;

    public AIGame(GameController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public void nextTurn() {
        super.nextTurn();
        if(getTurn() == Alliance.BLACK){
            //TODO AI moves
        }
    }
}
