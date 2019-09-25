package game;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import pieces.Piece;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller {

    @FXML private BorderPane container;
    @FXML private GridPane boardGrid;
    @FXML private GridPane opponentTaken;
    @FXML private GridPane playerTaken;
    private Tile[][] board;
    private Image whitePieceImg;
    private Image blackPieceImg;

    //General layout
    private VBox[][] boardLayout = new VBox[8][8];


    //Method runs when window initialized
    @FXML
    public void initialize(){
        //setting white and black piece img
        whitePieceImg = new Image(getClass().getResource("/img/whitePiece.png").toExternalForm());
        blackPieceImg = new Image(getClass().getResource("/img/blackPiece.png").toExternalForm());

        //Generate backend
        Game game = new Game();
        game.startGame();
        board = game.getBoard().getBoard();

        //Generating board and assigning ids
        for(int i = 0; i < boardGrid.getRowCount(); i++){
            for (int j = 0; j < boardGrid.getColumnCount(); j++){
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER);
                if((i+j) % 2 == 0) {
                    box.setId("whiteTile");
                }else{
                    box.setId("blackTile");
                }

                //Reversing board so 0,0 is in left bottom corner
                boardLayout[boardGrid.getColumnCount()-1-j][i] = box;
                boardGrid.add(box, i, j);
            }
        }

        //Placing pieces
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++) {
                Tile t = board[i][j];
                if(t.isOccupied()){
                    Piece occupant = t.getOccupant();
                    if(occupant.isWhite()){
                        boardLayout[i][j].getChildren().clear();
                        ImageView view = new ImageView(whitePieceImg);
                        boardLayout[i][j].getChildren().add(view);
                    }else{
                        boardLayout[i][j].getChildren().clear();
                        ImageView view = new ImageView(blackPieceImg);
                        boardLayout[i][j].getChildren().add(view);
                    }
                }
            }
        }

        /*
        //Testing taken slots
        for(int i = 0; i < playerTaken.getRowCount(); i++){
            ImageView view = new ImageView();
            Image img = new Image(getClass().getResource("/img/blackPiece.png").toExternalForm());
            view.setImage(img);
            playerTaken.add(view, 0, i);
            view = new ImageView();
            img = new Image(getClass().getResource("/img/whitePiece.png").toExternalForm());
            view.setImage(img);
            opponentTaken.add(view, 0, i);
        }*/
    }
}
