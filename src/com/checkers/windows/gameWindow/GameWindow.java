package com.checkers.windows.gameWindow;

import com.checkers.engine.GameType;
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
    private GameType gameType;
    private Stage stageToHide;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        Parent root = loader.load();

        Scene gameScene = new Scene(root, WIDTH, HEIGHT);
        gameScene.getStylesheets().add(getClass().getResource(STYLE_PATH).toExternalForm());

        stage.setOnCloseRequest(e->stageToHide.show());
        stage.setTitle("Checkers Game");
        stage.setScene(gameScene);
        stage.setResizable(false);
        stage.show();

        GameController controller = loader.getController();
        switch (gameType) {
            case PLAYER_TO_PLAYER:
                controller.startTwoPlayersGame();
                break;
            case PLAYER_TO_AI:
                controller.startPlayerAIGame();
                break;
            case AI_TO_AI:
                controller.startDoubleAIGame();
                break;
        }
    }


    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setStageToHide(Stage stageToHide) {
        this.stageToHide = stageToHide;
    }

}
