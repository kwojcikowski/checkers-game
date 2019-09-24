package window;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML private GridPane board;
    @FXML private GridPane opponentTaken;
    @FXML private GridPane playerTaken;

    //General layout
    private VBox[][] boardLayout = new VBox[8][8];


    //Method runs when window initialized
    @FXML
    public void initialize(){
        //Generating labels for taken pieces
        for(int i = 0; i < opponentTaken.getRowCount(); i++){
            Label lab = new Label("oppTaken #"+i);
            opponentTaken.add(lab, 0, i);
            lab = new Label("playerTaken #"+i);
            playerTaken.add(lab, 0, i);
        }
        //Actual board config
        board.setMinSize(640, 640);
        board.setMaxSize(640, 640);
        for(int i = 0; i < board.getRowCount(); i++){
            for (int j = 0; j < board.getColumnCount(); j++){
                VBox box = new VBox();
                if((i+j) % 2 == 0) {
                    box.setId("whiteTile");
                }else{
                    box.setId("blackTile");
                }
                //Reversing board so 0,0 is in left bottom corner
                boardLayout[board.getColumnCount()-1-j][i] = box;
                board.add(box, i, j);
            }
        }
    }
}
