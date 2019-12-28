package com.checkers.windows.startingWindow;

import javafx.stage.Stage;
import com.checkers.windows.window.Window;

public class StartingWindowController {

    private Stage currentStage;
    public void multiplayerClicked(){
        Window game = new Window();
        game.setFxmlFile("WindowMultiplayer.fxml");
        game.setHiddenStage(currentStage);
        try {
            game.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentStage.hide();
    }

    public void aiClicked(){
        Window game = new Window();
        game.setFxmlFile("WindowAI.fxml");
        game.setHiddenStage(currentStage);
        try {
            game.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentStage.hide();
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}
