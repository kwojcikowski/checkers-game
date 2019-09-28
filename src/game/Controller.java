package game;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import pieces.Piece;

import java.util.LinkedList;

public class Controller {

    @FXML private BorderPane container;
    @FXML private GridPane boardGrid;
    @FXML private GridPane opponentTaken;
    @FXML private GridPane playerTaken;
    @FXML private VBox boardWrap;
    private Tile[][] board;
    private ImageView[][] piecesImages = new ImageView[8][8];
    private Image whitePieceImg;
    private Image blackPieceImg;
    private Game game;

    //General layout
    private VBox[][] boardLayout = new VBox[8][8];


    //Method runs when window initialized
    @FXML
    public void initialize(){
        //setting white and black piece img
        whitePieceImg = new Image(getClass().getResource("/img/whitePiece.png").toExternalForm());
        blackPieceImg = new Image(getClass().getResource("/img/blackPiece.png").toExternalForm());

        //Generate backend
        game = new Game();
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
                        piecesImages[i][j] = view;
                        boardLayout[i][j].getChildren().add(view);
                        int finalI = i;
                        int finalJ = j;
                        view.setOnMouseClicked(mouseEvent -> handlePieceClick(finalI, finalJ));
                    } else {
                        boardLayout[i][j].getChildren().clear();
                        ImageView view = new ImageView(blackPieceImg);
                        view.toFront();
                        boardLayout[i][j].getChildren().add(view);
                        piecesImages[i][j] = view;
                    }
                }
            }
        }

        //Testing moves
        VBox testBox = boardLayout[3][5];
        ImageView v = new ImageView(blackPieceImg);
        testBox.getChildren().add(v);
        board[3][5].setOccupant(new Piece(false));
        System.out.println(board[3][5].isOccupied());
    }

    public void handlePieceClick(int row, int col){
        LinkedList<Move> fields = game.moveChecker(row, col);
        System.out.println("linked list size : "+ fields.size());
        for(Move m:fields){
            System.out.println(m.getX() +","+m.getY());
        }
        setupFields(fields, row, col);
    }
    public void movePiece(int currentRow, int currentCol, int targetRow, int targetCol){

        //Detaching piece Object so it can move
        ImageView movingPiece = piecesImages[currentRow][currentCol];
        VBox startingContainer =  boardLayout[currentRow][currentCol];
        startingContainer.getChildren().clear();

        //Attach to container so we can see it
        container.getChildren().add(movingPiece);
        //Setting up target container
        VBox targetContainer = boardLayout[targetRow][targetCol];

        //Calculating the path object has to move
        //Starting xPos of line
        double x = boardWrap.getLayoutX() + boardWrap.getBorder().getInsets().getLeft()
                + startingContainer.getLayoutX() + startingContainer.getWidth()/2;
        //Starting yPos of line
        double y = boardWrap.getLayoutY() + boardWrap.getBorder().getInsets().getTop()
                + startingContainer.getLayoutY() + startingContainer.getHeight()/2;
        //End point of X
        double endX = boardWrap.getLayoutX() + boardWrap.getBorder().getInsets().getLeft()
                + targetContainer.getLayoutX() + targetContainer.getWidth()/2;
        //End point of Y
        double endY = boardWrap.getLayoutY() + boardWrap.getBorder().getInsets().getTop()
                + targetContainer.getLayoutY() + targetContainer.getHeight()/2;

        //Transition effect
        Line line = new Line(x, y, endX, endY);
        PathTransition transition = new PathTransition();
        transition.setNode(movingPiece);
        transition.setDuration(Duration.seconds(0.3));
        transition.setPath(line);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                attachImage(currentRow, currentCol, targetRow, targetCol);
            }
        });
        transition.play();
    }

    public void attachImage(int oldRow, int oldCol, int row, int col){
        Image img;
        if(piecesImages[oldRow][oldCol].getImage().getUrl().contains("white")){
            img = whitePieceImg;
        }else{
            img = blackPieceImg;
        }
        //Clear old spot
        container.getChildren().remove(piecesImages[oldRow][oldCol]);
        piecesImages[oldRow][oldCol] = null;
        //Move in Image array
        piecesImages[row][col] = new ImageView(img);
        piecesImages[row][col].setOnMouseClicked(mouseEvent -> handlePieceClick(row, col));
        //Add to board
        boardLayout[row][col].getChildren().clear();
        boardLayout[row][col].getChildren().add(piecesImages[row][col]);
        //Update backend borad
        board[row][col].setOccupant(board[oldRow][oldCol].getOccupant());
        board[oldRow][oldCol].setOccupied(false);
        board[row][col].setOccupied(true);

        updateOnMouse();
    }

    public void setupFields(LinkedList<Move> fields, int currentRow, int currentCol){
        updateOnMouse();
        //Change background of available field and set onClickEvent
        for(Move m : fields){
            int row = m.getX();
            int col = m.getY();
            boardLayout[row][col].setOnMouseClicked(mouseEvent -> {
                movePiece(currentRow, currentCol, row, col);
            });
            boardLayout[row][col].setId("available");
        }
    }

    //This method resets board fields after showing available
    //also it resets onClickEvent
    public void updateOnMouse(){
        for(int i = 0; i < boardLayout.length; i++) {
            for (int j = 0; j < boardLayout[i].length; j++) {
                VBox box = boardLayout[i][j];
                if(box.getId().equalsIgnoreCase("available")){
                    box.setId("blackTile");
                    int finalI = i;
                    int finalJ = j;
                    box.setOnMouseClicked(mouseEvent -> handlePieceClick(finalI, finalJ));
                }
            }
        }
    }
}
