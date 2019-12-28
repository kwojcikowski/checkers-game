package com.checkers.engine.controllers;

import com.checkers.engine.Game;
import com.checkers.engine.board.Move;
import com.checkers.engine.board.Tile;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.checkers.engine.pieces.*;

import java.util.LinkedList;

public class AIController {
    @FXML
    private BorderPane container;
    @FXML
    private GridPane boardGrid;
    @FXML
    private GridPane opponentTaken;
    @FXML
    private GridPane playerTaken;
    @FXML
    private VBox boardWrap;
    private Tile[][] board;
    private ImageView[][] piecesImages = new ImageView[8][8];
    private Image whitePieceImg;
    private Image blackPieceImg;
    private Image whiteKingImg;
    private Image blackKingImg;
    private Game game;
    private AI ai;

    private int numberOfWhiteTaken = 0;
    private int numberOfBlackTaken = 0;

    //General layout
    private VBox[][] boardLayout = new VBox[8][8];


    //Method runs when com.chess.windows.window initialized
    @FXML
    public void initialize() {
        //setting white and black piece com.chess.images.img
        whitePieceImg = new Image(getClass().getResource("/com/checkers/images/whitePiece.png").toExternalForm());
        blackPieceImg = new Image(getClass().getResource("/com/checkers/images/blackPiece.png").toExternalForm());
        whiteKingImg = new Image(getClass().getResource("/com/checkers/images/whiteKing.png").toExternalForm());
        blackKingImg = new Image(getClass().getResource("/com/checkers/images/blackKing.png").toExternalForm());

        //Generate backend
        game = new Game();
        game.startGame(this);
        board = game.getBoard().getTiles();

        //Generate AI
        ai = new AI(this, game, piecesImages, boardLayout, container, boardWrap);

        //Generating board and assigning ids
        for (int i = 0; i < boardGrid.getRowCount(); i++) {
            for (int j = 0; j < boardGrid.getColumnCount(); j++) {
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER);
                if ((i + j) % 2 == 0) {
                    box.setId("whiteTile");
                } else {
                    box.setId("blackTile");
                }

                //Reversing board so 0,0 is in left bottom corner
                boardLayout[boardGrid.getColumnCount() - 1 - j][i] = box;
                boardGrid.add(box, i, j);
            }
        }

        //Placing com.chess.engine.pieces
        for (int i = 0; i < board.length; i++) {
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
                        piecesImages[i][j] = view;
                        boardLayout[i][j].getChildren().add(view);
                    }
                    occupant.setOccupied(t);
                }
            }
        }
        game.whiteTurn();
    }

    public void clearBoard(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                deleteChecker(i, j);
            }
        }
    }

    public void setUpPawn(int row, int col, boolean isWhite){
        ImageView view;
        if(isWhite)
            view = new ImageView(whitePieceImg);
        else
            view = new ImageView(blackPieceImg);
        piecesImages[row][col] = view;
        boardLayout[row][col].getChildren().clear();
        boardLayout[row][col].getChildren().add(view);
        board[row][col].setOccupant(new Pawn(isWhite));
    }

    public void setUpKing(int row, int col, boolean isWhite){
        ImageView view;
        if(isWhite)
            view = new ImageView(whiteKingImg);
        else
            view = new ImageView(blackKingImg);
        piecesImages[row][col] = view;
        boardLayout[row][col].getChildren().clear();
        boardLayout[row][col].getChildren().add(view);
        board[row][col].setOccupant(new King(new Pawn(isWhite)));
    }

    public void deleteChecker(int row, int col){
        boardLayout[row][col].getChildren().clear();
        piecesImages[row][col] = null;
        board[row][col].setOccupied(false);
    }

    public void handlePieceClick(int row, int col) {
        Piece piece = board[row][col].getOccupant();
        LinkedList<Move> fields = piece.checkPossibleMoves(row, col, board, true, true);
        setupFields(fields, row, col);
    }

    public void movePiece(int currentRow, int currentCol, int targetRow, int targetCol, boolean isAttacking) {
        //Disable all com.chess.engine.pieces so others cant be moved
        disableAllPieces();
        boardLayout[targetRow][targetCol].setOnMouseClicked(mouseEvent -> {});

        //Detaching piece Object so it can move
        ImageView movingPiece = piecesImages[currentRow][currentCol];
        VBox startingContainer = boardLayout[currentRow][currentCol];
        startingContainer.getChildren().clear();

        //Attach to container so we can see it
        container.getChildren().add(movingPiece);
        //Setting up target container
        VBox targetContainer = boardLayout[targetRow][targetCol];

        //Calculating the path object has to move
        //Starting xPos of line
        double x = boardWrap.getLayoutX() + boardWrap.getBorder().getInsets().getLeft()
                + startingContainer.getLayoutX() + startingContainer.getWidth() / 2;
        //Starting yPos of line
        double y = boardWrap.getLayoutY() + boardWrap.getBorder().getInsets().getTop()
                + startingContainer.getLayoutY() + startingContainer.getHeight() / 2;
        //End point of X
        double endX = boardWrap.getLayoutX() + boardWrap.getBorder().getInsets().getLeft()
                + targetContainer.getLayoutX() + targetContainer.getWidth() / 2;
        //End point of Y
        double endY = boardWrap.getLayoutY() + boardWrap.getBorder().getInsets().getTop()
                + targetContainer.getLayoutY() + targetContainer.getHeight() / 2;

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
                boolean maintainTurn;
                if (isAttacking) {
                    handleAttackedPiece(currentRow, currentCol, targetRow, targetCol);
                    //Handling com.chess.engine.game end
                    if (checkGameEnd()) {
                        event.consume();
                    }
                    maintainTurn = checkForFurtherMoves(targetRow, targetCol);
                    //Change of turn
                    if (!maintainTurn) {
                        checkPromotion(targetRow, targetCol);
                        game.blackTurn();
                    }
                } else {
                    checkPromotion(targetRow, targetCol);
                    game.blackTurn();
                }
            }
        });
        transition.play();
    }

    public void attachImage(int oldRow, int oldCol, int row, int col) {
        Image img;
        boolean isKing = false;
        if (piecesImages[oldRow][oldCol].getImage().getUrl().contains("King")) {
            isKing = true;
        }
        if (piecesImages[oldRow][oldCol].getImage().getUrl().contains("white")) {
            if (isKing) {
                img = whiteKingImg;
            } else {
                img = whitePieceImg;
            }
        } else {
            if (isKing) {
                img = blackKingImg;
            } else {
                img = blackPieceImg;
            }
        }
        //Clear old spot
        container.getChildren().remove(piecesImages[oldRow][oldCol]);
        piecesImages[oldRow][oldCol] = null;
        //Move in Image array
        piecesImages[row][col] = new ImageView(img);
        //Add to board
        boardLayout[row][col].getChildren().clear();
        boardLayout[row][col].getChildren().add(piecesImages[row][col]);
        //Perform move on backend board
        board[row][col].setOccupant(board[oldRow][oldCol].getOccupant());
        board[oldRow][oldCol].setOccupied(false);
        board[row][col].setOccupied(true);

        updateOnMouse();
    }

    public void setupFields(LinkedList<Move> fields, int currentRow, int currentCol) {
        updateOnMouse();
        //Change background of available field and set onClickEvent
        for (Move m : fields) {
            int row = m.getX();
            int col = m.getY();
            boolean isAttacking = m.isAttacking();
            if(m.isAvailableNow()){
                boardLayout[row][col].setId("available");
                boardLayout[row][col].setOnMouseClicked(mouseEvent -> {
                    movePiece(currentRow, currentCol, row, col, isAttacking);
                });
            }
            if(isAttacking) boardLayout[row][col].setId("isAttacking");

        }
    }

    //This method resets board fields after showing available
    //also it resets onClickEvent
    public void updateOnMouse() {
        for (int i = 0; i < boardLayout.length; i++) {
            for (int j = 0; j < boardLayout[i].length; j++) {
                VBox box = boardLayout[i][j];
                if (box.getId().equalsIgnoreCase("available") ||
                        box.getId().equalsIgnoreCase("notDirectlyAvailable") ||
                        box.getId().equalsIgnoreCase("isAttacking")) {
                    box.setId("blackTile");
                    box.setOnMouseClicked(mouseEvent -> {
                    });
                }
            }
        }
    }

    public void handleAttackedPiece(int currentRow, int currentCol, int targetRow, int targetCol) {
        int row = -1;
        int col = -1;

        //Indicator if attacked in row below or above
        int rowComponent;
        if (currentRow - targetRow > 0)
            rowComponent = -1;
        else
            rowComponent = 1;
        //Indicator if attacked in col right or left
        int colComponent;
        if (currentCol - targetCol > 0)
            colComponent = -1;
        else
            colComponent = 1;

        //Find a piece that has been attacked
        for (int i = currentRow, j = currentCol; i != targetRow && j != targetCol; i += rowComponent, j += colComponent) {
            if (board[i][j].isOccupied()) {
                if (board[i][j].getOccupant().isWhite() != board[targetRow][targetCol].getOccupant().isWhite()) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        ImageView takenPieceImg = piecesImages[row][col];
        VBox takenSpot = boardLayout[row][col];
        Piece takenPiece = board[row][col].getOccupant();
        if (takenPiece.isWhite()) {
            playerTaken.add(takenPieceImg, 0, numberOfWhiteTaken);
            numberOfWhiteTaken++;
        } else {
            opponentTaken.add(takenPieceImg, 0, numberOfBlackTaken);
            numberOfBlackTaken++;
        }
        board[row][col].setOccupied(false);
        takenSpot.getChildren().clear();
        piecesImages[row][col] = null;
    }

    public boolean checkForFurtherMoves(int row, int col) {
        Piece piece = board[row][col].getOccupant();
        LinkedList<Move> moves = piece.checkPossibleMoves(row, col, board, true, true);
        LinkedList<Move> availableMoves = new LinkedList<>();
        if (moves.isEmpty())
            return false;
        for (Move m : moves) {
            if (m.isAttacking()) {
                availableMoves.add(m);
            }
        }
        setupFields(availableMoves, row, col);
        return !availableMoves.isEmpty();
    }

    public void disableWhitePieces() {
        for (ImageView[] row : piecesImages) {
            for (ImageView v : row) {
                if (v != null) {
                    if (v.getImage().getUrl().contains("white")) {
                        v.setOnMouseClicked(mouseEvent -> {
                        });
                    }
                }
            }
        }
    }

    public void disableBlackPieces() {
        for (ImageView[] row : piecesImages) {
            for (ImageView v : row) {
                if (v != null) {
                    if (v.getImage().getUrl().contains("black")) {
                        v.setOnMouseClicked(mouseEvent -> {
                        });
                    }
                }
            }
        }
    }

    public void enableWhitePieces() {
        for (int i = 0; i < piecesImages.length; i++) {
            for (int j = 0; j < piecesImages[i].length; j++) {
                ImageView v = piecesImages[i][j];
                if (v != null) {
                    int row = i;
                    int col = j;
                    if (v.getImage().getUrl().contains("white")) {
                        v.setOnMouseClicked(mouseEvent -> handlePieceClick(row, col));
                    }
                }
            }
        }
    }

    public void enableBlackPieces() {
        for (int i = 0; i < piecesImages.length; i++) {
            for (int j = 0; j < piecesImages[i].length; j++) {
                ImageView v = piecesImages[i][j];
                if (v != null) {
                    int row = i;
                    int col = j;
                    if (v.getImage().getUrl().contains("black")) {
                        v.setOnMouseClicked(mouseEvent -> handlePieceClick(row, col));
                    }
                }
            }
        }
    }

    public void disableAllPieces() {
        disableBlackPieces();
        disableWhitePieces();
    }

    public boolean checkGameEnd(){
        if(playerTaken.getChildren().size() == 12){
            updateOnMouse();
            game.endGame("Player");
            disableAllPieces();
            return true;
        }else if(opponentTaken.getChildren().size() == 12){
            updateOnMouse();
            game.endGame("Opponent");
            disableAllPieces();
            return true;
        }
        boolean availableWhiteMoves = false;
        boolean availableBlackMoves = false;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                Tile t = board[i][j];
                if(t.getOccupant() != null){
                    if(t.getOccupant().isWhite()){
                        if(!t.getOccupant().checkPossibleMoves(i, j, board, true, false).isEmpty()) {
                            availableWhiteMoves = true;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                Tile t = board[i][j];
                if(t.getOccupant() != null){
                    if(!t.getOccupant().isWhite()){
                        if(!t.getOccupant().checkPossibleMoves(i, j, board, true, false).isEmpty()) {
                            availableBlackMoves = true;
                        }
                    }
                }
            }
        }
        if(!availableWhiteMoves){
            updateOnMouse();
            game.endGame("Opponent");
            disableAllPieces();
            return true;
        }
        if(!availableBlackMoves){
            updateOnMouse();
            game.endGame("Player");
            disableAllPieces();
            return true;
        }
        return false;
    }

    public void setHiddenScene(Stage stage, Stage hiddenStage) {
        stage.setOnCloseRequest(windowEvent -> {
            hiddenStage.show();
            if(!game.isFinished()) {
                game.endGame("No winner");
            }
        });
    }

    protected void checkPromotion (int row, int col){
        ImageView newView = null;
        boolean promotion = false;
        //white promotion
        if (board[row][col].getOccupant().isWhite()) {
            if (row == board.length - 1) {
                for (int i = 1; i < board[row].length; i += 2) {
                    if (col == i) {
                        newView = new ImageView(whiteKingImg);
                        promotion = true;
                    }
                }
            }
        } else {
            //black promotion
            if (row == 0) {
                for (int i = 0; i < board[row].length; i += 2) {
                    if (col == i) {
                        newView = new ImageView(blackKingImg);
                        promotion = true;
                    }
                }
            }
        }
        if (promotion) {
            boardLayout[row][col].getChildren().clear();
            boardLayout[row][col].getChildren().add(newView);
            piecesImages[row][col] = newView;
            King king = new King(board[row][col].getOccupant());
            board[row][col].setOccupant(king);
            newView.setOnMouseClicked(mouseEvent -> {
                handlePieceClick(2, 0);
            });
        }
    }
}
