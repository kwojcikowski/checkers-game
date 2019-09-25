package window;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import pieces.Piece;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller {

    @FXML private BorderPane container;
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

        for(int i = 0; i < board.getRowCount(); i++){
            for (int j = 0; j < board.getColumnCount(); j++){
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER);
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

        //Placing pieces
        for(int i = 0; i < boardLayout.length; i++){
            for(int j = 0; j < boardLayout[i].length; j++){
                VBox box = boardLayout[i][j];
                if(box.getId().equalsIgnoreCase("blackTile")){
                    Image piece;
                    ImageView pieceView = new ImageView();
                    if (i < 3) {
                        piece = new Image(getClass().getResource("/img/whitePiece.png").toExternalForm());
                        pieceView.setImage(piece);
                    } else if (i > 4) {
                        piece = new Image(getClass().getResource("/img/blackPiece.png").toExternalForm());
                        pieceView.setImage(piece);
                    } else {
                        continue;
                    }
                    pieceView.setSmooth(true);
                    box.getChildren().add(pieceView);
                }
            }
        }
    }
}
