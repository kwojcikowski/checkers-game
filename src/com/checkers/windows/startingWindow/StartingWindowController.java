package com.checkers.windows.startingWindow;

import com.checkers.engine.GameType;
import com.checkers.windows.gameWindow.GameWindow;
import javafx.stage.Stage;

public class StartingWindowController {

    private Stage currentStage;

    public void runMultiplayerGame() {
        launchGameScene(GameType.PLAYER_TO_PLAYER);
    }

    public void runAIGame(){
        launchGameScene(GameType.PLAYER_TO_AI);
    }

    public void runDoubleAIGame(){
        launchGameScene(GameType.AI_TO_AI);
    }

    private void launchGameScene(GameType gameType){
        GameWindow gameWindow = new GameWindow();
        gameWindow.setStageToHide(currentStage);
        gameWindow.setGameType(gameType);
        try {
            gameWindow.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentStage.hide();
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}

