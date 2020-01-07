package com.checkers.windows.startingWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartingWindow extends Application {

    private static final int WIDTH = 500, HEIGHT = 250;
    private static final String FXML_PATH = "StartingWindow.fxml";
    private static final String STYLE_PATH = "StartingWindowStyle.css";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        Parent root = loader.load();

        Scene startingScene = new Scene(root, WIDTH, HEIGHT);
        startingScene.getStylesheets().add(getClass().getResource(STYLE_PATH).toExternalForm());

        StartingWindowController controller = loader.getController();
        controller.setCurrentStage(stage);

        stage.setTitle("Checkers game");
        stage.setScene(startingScene);
        stage.setResizable(false);
        stage.show();
    }
}
