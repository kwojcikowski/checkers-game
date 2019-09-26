package game;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import pieces.Piece;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Controller {

    @FXML private BorderPane container;
    @FXML private GridPane boardGrid;
    @FXML private GridPane opponentTaken;
    @FXML private GridPane playerTaken;
    @FXML private VBox boardWrap;
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
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Tile t = board[i][j];
                if (t.isOccupied()) {
                    Piece occupant = t.getOccupant();
                    if (occupant.isWhite()) {
                        boardLayout[i][j].getChildren().clear();
                        ImageView view = new ImageView(whitePieceImg);
                        boardLayout[i][j].getChildren().add(view);
                        VBox box = boardLayout[i][j];
                        int finalI = i;
                        int finalJ = j;
                        view.setOnMouseClicked(mouseEvent -> setupFields(finalI, finalJ));
                    } else {
                        boardLayout[i][j].getChildren().clear();
                        ImageView view = new ImageView(blackPieceImg);
                        view.toFront();
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

    public void movePiece(VBox view, int i, int j){
        int currentI = boardGrid.getRowCount() - GridPane.getRowIndex(view) -1;
        int currentJ = GridPane.getColumnIndex(view);

        boardGrid.getChildren().remove(view);
        VBox newBox = new VBox();
        boardLayout[currentI][currentJ] = newBox;
        newBox.setAlignment(Pos.CENTER);
        newBox.setId("blackTile");
        boardGrid.add(newBox, GridPane.getColumnIndex(view), GridPane.getRowIndex(view));

        view.setId("TravellingPiece");
        container.getChildren().add(view);
        double x = newBox.getLayoutX() + boardWrap.getLayoutX() + (whitePieceImg.getWidth() * Math.sqrt(2)/2)
                + boardWrap.getPadding().getLeft()/2;
        double y = newBox.getLayoutY() + boardWrap.getLayoutY() + (whitePieceImg.getHeight() * Math.sqrt(2)/2)
                + boardWrap.getPadding().getTop()/2;
        double endX;
        if(currentJ < j) {
            endX = x + (view.getWidth() * Math.sqrt(2) / 3 * 2) + boardWrap.getBorder().getInsets().getLeft();
        }else{
            endX = x - (view.getWidth() * Math.sqrt(2) / 3 * 2) - boardWrap.getBorder().getInsets().getLeft();
        }
        double endY = y - (view.getHeight() * Math.sqrt(2)/3 * 2) + boardWrap.getBorder().getInsets().getTop();

        //Transition effect
        Line line = new Line(x, y, endX, endY);

        PathTransition transition = new PathTransition();
        transition.setNode(view);
        transition.setDuration(Duration.seconds(0.3));
        transition.setPath(line);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exchangeBoxes(view, i, j);
            }
        });
        transition.play();
    }

    public void exchangeBoxes(VBox view, int i, int j){
        updateOnMouse();
        VBox temp = boardLayout[i][j];
        int valI = GridPane.getColumnIndex(temp);
        int valJ = GridPane.getRowIndex(temp);
        temp.setId("blackTile");
        temp.getChildren().addAll(view.getChildren());
        temp.getChildren().get(0).setOnMouseClicked(mouseEvent -> setupFields(i, j));
        System.out.println(temp.getChildren().get(0));
        System.out.println(temp);
        System.out.println(boardLayout[i][j]);
        boardGrid.getChildren().remove(temp);
        boardGrid.getChildren().add(temp);
        GridPane.setConstraints(temp, valI, valJ);
        container.getChildren().remove(view);
    }

    public void setupFields(int i, int j){
        updateOnMouse();
        if(i+1 < 8 && j+1 <8){
            boardLayout[i+1][j+1].setOnMouseClicked(mouseEvent -> {
                movePiece(boardLayout[i][j], i+1, j+1);
            });
            boardLayout[i+1][j+1].setId("available");
        }
        if(i+1 < 8 && j-1 > -1){
            boardLayout[i+1][j-1].setOnMouseClicked(mouseEvent -> {
                movePiece(boardLayout[i][j], i+1, j-1);
            });
            boardLayout[i+1][j-1].setId("available");
        }
    }

    public void updateOnMouse(){
        for(int i = 0; i < boardLayout.length; i++) {
            for (int j = 0; j < boardLayout[i].length; j++) {
                VBox box = boardLayout[i][j];
                if(box.getId().equalsIgnoreCase("available")){
                    box.setId("blackTile");
                    int finalI = i;
                    int finalJ = j;
                    box.setOnMouseClicked(mouseEvent -> setupFields(finalI, finalJ));
                }
            }
        }
    }
}
