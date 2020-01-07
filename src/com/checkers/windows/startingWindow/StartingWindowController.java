package com.checkers.windows.startingWindow;

import com.checkers.windows.gameWindow.GameWindow;
import javafx.stage.Stage;

public class StartingWindowController {

    private Stage currentStage;

    public void runMultiplayerGame() {
        launchGameScene(true);
    }

    public void runAIGame(){
        launchGameScene(false);
    }

    private void launchGameScene(boolean isMultiplayer){
        GameWindow gameWindow = new GameWindow();
        gameWindow.setStageToHide(currentStage);
        gameWindow.setIsMultiplayer(isMultiplayer);
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

