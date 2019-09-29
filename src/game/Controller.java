package game;

import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private AI ai;
    private PathTransition pieceMove;

    private int numberOfWhiteTaken = 0;
    private int numberOfBlackTaken = 0;

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
        game.startGame(this);
        board = game.getBoard().getTiles();

        //Generate AI
        ai = new AI(this, game);

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
        enableWhitePieces();
        game.whiteTurn();
    }

    public void handlePieceClick(int row, int col){
        LinkedList<Move> fields = game.moveChecker(row, col, board, true, true);
        setupFields(fields, row, col);
    }

    public void movePiece(int currentRow, int currentCol, int targetRow, int targetCol, boolean isAttacking){
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
        pieceMove = transition;
        transition.setNode(movingPiece);
        transition.setDuration(Duration.seconds(0.3));
        transition.setPath(line);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                attachImage(currentRow, currentCol, targetRow, targetCol);
                boolean maintainTurn = false;
                if (isAttacking) {
                    int attackedRow = currentRow + (targetRow - currentRow)/2;
                    int attackedCol = currentCol + (targetCol - currentCol)/2;
                    handleAttackedPiece(attackedRow, attackedCol);
                    maintainTurn = checkForFurtherMoves(targetRow, targetCol);
                }
                transition.stop();
                System.out.println(transition.getStatus());
                //Change of turn
                if(!maintainTurn){
                    if(game.isWhiteTurn()){
                        game.blackTurn();
                        ai.playAITurn();
                    }
                }
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
        //Update backend board
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
            boolean isAttacking = m.isAttacking();
            if(m.isAvailableNow()){
                boardLayout[row][col].setOnMouseClicked(mouseEvent -> {
                    movePiece(currentRow, currentCol, row, col, isAttacking);
                });
                boardLayout[row][col].setId("available");
            }
            else boardLayout[row][col].setId("notDirectlyAvailable");

            if(isAttacking) boardLayout[row][col].setId("isAttacking");

        }
    }

    //This method resets board fields after showing available
    //also it resets onClickEvent
    public void updateOnMouse(){
        for(int i = 0; i < boardLayout.length; i++) {
            for (int j = 0; j < boardLayout[i].length; j++) {
                VBox box = boardLayout[i][j];
                if(box.getId().equalsIgnoreCase("available")||
                        box.getId().equalsIgnoreCase("notDirectlyAvailable")||
                        box.getId().equalsIgnoreCase("isAttacking")){
                    box.setId("blackTile");
                    box.setOnMouseClicked(mouseEvent -> {});
                }
            }
        }
    }

    public void handleAttackedPiece(int row, int col){
        ImageView takenPieceImg = piecesImages[row][col];
        VBox takenSpot = boardLayout[row][col];
        Piece takenPiece = board[row][col].getOccupant();
        if(takenPiece.isWhite()){
            playerTaken.add(takenPieceImg, 0,numberOfWhiteTaken);
            numberOfWhiteTaken++;
        }else{
            opponentTaken.add(takenPieceImg, 0, numberOfBlackTaken);
            numberOfBlackTaken++;
        }
        board[row][col].setOccupied(false);
        takenSpot.getChildren().clear();
        piecesImages[row][col] = null;
    }

    public boolean checkForFurtherMoves(int row, int col){
        LinkedList<Move> moves = game.moveChecker(row, col, board, true, false);
        LinkedList<Move> availableMoves = new LinkedList<>();
        if(moves.size() == 0)
            return false;
        for(Move m : moves){
            if(m.isAttacking()){
                availableMoves.add(m);
            }
        }
        if(availableMoves.size() == 0)
            return false;
        setupFields(availableMoves, row, col);
        disableAllPieces();
        return true;
    }

    public void disableWhitePieces(){
        for(ImageView[] row : piecesImages){
            for(ImageView v : row){
                if(v != null) {
                    if(v.getImage().getUrl().contains("white")) {
                        v.setOnMouseClicked(mouseEvent -> {
                        });
                    }
                }
            }
        }
    }

    public void disableBlackPieces(){
        for(ImageView[] row : piecesImages){
            for(ImageView v : row){
                if(v != null) {
                    if(v.getImage().getUrl().contains("black")) {
                        v.setOnMouseClicked(mouseEvent -> {
                        });
                    }
                }
            }
        }
    }

    public void enableWhitePieces(){
        for(int i = 0; i < piecesImages.length; i++){
            for(int j = 0; j < piecesImages[i].length; j++){
                ImageView v = piecesImages[i][j];
                if(v != null) {
                    int row = i;
                    int col = j;
                    if(v.getImage().getUrl().contains("white")) {
                        v.setOnMouseClicked(mouseEvent -> handlePieceClick(row, col));
                    }
                }
            }
        }
    }

    public void enableBlackPieces(){
        for(int i = 0; i < piecesImages.length; i++){
            for(int j = 0; j < piecesImages[i].length; j++){
                ImageView v = piecesImages[i][j];
                if(v != null) {
                    int row = i;
                    int col = j;
                    if(v.getImage().getUrl().contains("black")) {
                        v.setOnMouseClicked(mouseEvent -> handlePieceClick(row, col));
                    }
                }
            }
        }
    }

    public void disableAllPieces(){
        disableBlackPieces();
        disableWhitePieces();
    }

    public VBox getBoardWrap() {
        return boardWrap;
    }

    public ImageView[][] getPiecesImages() {
        return piecesImages;
    }

    public VBox[][] getBoardLayout() {
        return boardLayout;
    }

    public BorderPane getContainer() {
        return container;
    }
}
