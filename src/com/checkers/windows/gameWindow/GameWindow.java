package com.checkers.windows.gameWindow;

import com.checkers.engine.controllers.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameWindow extends Application {

    private static final int WIDTH = 800, HEIGHT = 600;
    private static final String FXML_PATH = "GameWindow.fxml";
    private static final String STYLE_PATH = "GameWindowStyle.css";
    private boolean isMultiplayer;
    private Stage stageToHide;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        Parent root = loader.load();
        if(!isMultiplayer){
            GameController controller = loader.getController();
            controller.evolveIntoAIGame();
        }

        Scene gameScene = new Scene(root, WIDTH, HEIGHT);
        gameScene.getStylesheets().add(getClass().getResource(STYLE_PATH).toExternalForm());

        stage.setOnCloseRequest(e->stageToHide.show());
        stage.setTitle("Checkers Game");
        stage.setScene(gameScene);
        stage.setResizable(false);
        stage.show();
    }


    public void setIsMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    public void setStageToHide(Stage stageToHide) {
        this.stageToHide = stageToHide;
    }

}
