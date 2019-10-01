package window;

import game.AIController;
import game.MultiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {

    private String fxmlFile;
    private Stage hiddenStage;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        stage.setTitle("Chess Game");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("WindowStyle.css").toExternalForm());
        if(fxmlFile.equalsIgnoreCase("WindowAI.fxml")){
            AIController aiController = loader.getController();
            aiController.setHiddenScene(stage, hiddenStage);
        }else{
            MultiController multiController = loader.getController();
            multiController.setHiddenStage(stage, hiddenStage);
        }
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void setFxmlFile(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public void setHiddenStage(Stage hiddenStage) {
        this.hiddenStage = hiddenStage;
    }
}
