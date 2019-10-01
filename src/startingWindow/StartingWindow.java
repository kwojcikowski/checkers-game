package startingWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartingWindow extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartingWindow.fxml"));
        Parent root = loader.load();
        stage.setTitle("Checker Game");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("StartingWindowStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        StartingWindowController startingWindowController = loader.getController();
        startingWindowController.setCurrentStage(stage);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
