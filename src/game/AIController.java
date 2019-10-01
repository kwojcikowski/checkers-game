package game;

import javafx.stage.Stage;

public class AIController {

    public void setHiddenScene(Stage stage, Stage hiddenStage){
        stage.setOnCloseRequest(windowEvent -> hiddenStage.show());
    }
}
