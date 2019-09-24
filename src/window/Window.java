package window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml"));
        Parent root = loader.load();
        stage.setTitle("Chess Game");
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("windowStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
