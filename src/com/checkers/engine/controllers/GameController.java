package com.checkers.engine.controllers;

import com.checkers.engine.AIGame;
import com.checkers.engine.Alliance;
import com.checkers.engine.Game;
import com.checkers.engine.board.BlackTile;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.move.CapturingMove;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.board.Tile;
import com.checkers.engine.pieces.King;
import com.checkers.engine.pieces.Pawn;
import com.checkers.engine.pieces.Piece;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;

import static com.checkers.engine.board.Board.BOARD_SIZE;
import static com.checkers.engine.board.Board.ROWS_OF_PIECES;

public class GameController {

    @FXML private BorderPane container;
    @FXML private GridPane boardGrid;
    @FXML private GridPane opponentTaken;
    @FXML private GridPane playerTaken;
    @FXML private VBox boardWrap;
    @FXML private StackPane boardStackPane;

    private Game game;
    private Board board;
    private VBox[][] boardLayout;
    private ImageView[][] piecesImages;

    private int playerTakenCounter = 0;
    private int opponentTakenCounter = 0;

    @FXML
    void initialize(){
        boardLayout = new VBox[BOARD_SIZE][BOARD_SIZE];
        piecesImages = new ImageView[BOARD_SIZE][BOARD_SIZE];

        game = new Game();
        board = game.getBoard();
        renderBoard();

        playerTakenCounter = 0;
        opponentTakenCounter = 0;
        opponentTaken.getChildren().clear();
        playerTaken.getChildren().clear();

        resetMoveInteractions();
    }

    public void evolveIntoAIGame(){
        game = new AIGame(game, this);
    }

    private void renderBoard(){
        boardGrid.getChildren().clear();
        for (int row = 0; row < boardGrid.getRowCount(); row++) {
            for (int column = 0; column < boardGrid.getColumnCount(); column++) {
                renderTile(row, column);
                setupPawn(row, column);
            }
        }
    }

    private void renderTile(int row, int column){
        VBox boardField = new VBox();
        boardField.setAlignment(Pos.CENTER);
        if (isBlackTile(row, column))
            boardField.setId("blackTile");
        else
            boardField.setId("whiteTile");
        boardGrid.add(boardField, column, row);
        boardLayout[row][column] = boardField;
    }

    private void setupPawn(int row, int column){
        if(isBlackTile(row, column) && row < ROWS_OF_PIECES)
            setupBlackPawn(row, column);
        else if (isBlackTile(row, column) && row >= BOARD_SIZE - ROWS_OF_PIECES)
            setupWhitePawn(row, column);
    }

    private boolean isBlackTile(int row, int col){
        return board.getTiles()[row][col] instanceof BlackTile;
    }

    private void setupWhitePawn(int row, int col){
        Image img = new Image("com/checkers/images/whitePiece.png");
        ImageView view = new ImageView(img);
        boardLayout[row][col].getChildren().add(view);
        piecesImages[row][col] = view;
    }

    private void setupBlackPawn(int row, int col){
        Image img = new Image("com/checkers/images/blackPiece.png");
        ImageView view = new ImageView(img);
        boardLayout[row][col].getChildren().add(view);
        piecesImages[row][col] = view;
    }


    public void movePiece(Coords pieceCoords, Move moveToMake){

        disableAllInteractions();

        int oldRow = pieceCoords.x;
        int oldCol = pieceCoords.y;
        int targetRow = moveToMake.destinationCoords.x;
        int targetCol = moveToMake.destinationCoords.y;
        ImageView pieceImage = piecesImages[oldRow][oldCol];
        VBox startingTile = boardLayout[oldRow][oldCol];
        VBox targetTile = boardLayout[targetRow][targetCol];
        Piece movingPiece = board.getTiles()[oldRow][oldCol].getOccupant();

        final double boardOffsetX = boardWrap.getLayoutX();
        final double boardOffsetY = boardWrap.getLayoutY();

        double startingPointX = boardOffsetX + startingTile.getLayoutX() + startingTile.getWidth()/2;
        double startingPointY = boardOffsetY + startingTile.getLayoutY() + startingTile.getHeight()/2;
        double targetPointX = boardOffsetX + targetTile.getLayoutX() + targetTile.getWidth()/2;
        double targetPointY = boardOffsetY + targetTile.getLayoutY() + targetTile.getHeight()/2;

        VBox movingBox = new VBox();
        movingBox.setId("transparentTile");
        boardStackPane.getChildren().add(movingBox);
        StackPane.setMargin(movingBox,
                new Insets(startingTile.getLayoutY(), 0, 0, startingTile.getLayoutX()));
        movingBox.getChildren().add(pieceImage);
        movingBox.setAlignment(Pos.CENTER);

        startingTile.getChildren().clear();

        TranslateTransition transition = new TranslateTransition();
        transition.setByX(targetPointX - startingPointX);
        transition.setByY(targetPointY - startingPointY);
        transition.setDuration(Duration.seconds(0.5));
        transition.setNode(movingBox);

        transition.setOnFinished(e -> {
            movingPiece.moveTo(board.getTile(targetRow, targetCol));

            targetTile.getChildren().clear();
            targetTile.getChildren().add(pieceImage);
            boardStackPane.getChildren().remove(movingBox);

            piecesImages[targetRow][targetCol] = pieceImage;
            piecesImages[oldRow][oldCol] = null;

            if(moveToMake instanceof CapturingMove){
                CapturingMove move = (CapturingMove)moveToMake;
                Coords coords = move.getAttackedPieceCoords();
                int attackedPieceRow = coords.x;
                int attackedPieceCol = coords.y;
                Piece attackedPiece = board.getTiles()[attackedPieceRow][attackedPieceCol].getOccupant();
                attackedPiece.takeDown();
                takeDownPiece(attackedPieceRow, attackedPieceCol);
                List<Move> furtherMoves = getFurtherMoves(movingPiece);
                removeHighlighting();
                if(furtherMoves.isEmpty()){
                    if(canBePromoted(movingPiece))
                        promote(movingPiece);
                    game.nextTurn();
                    resetMoveInteractions();
                }else {
                    if(game instanceof AIGame && game.getTurn() == Alliance.BLACK) {
                        ((AIGame) game).performRandomAttackingMove(movingPiece);
                    }else {
                        displayMoves(targetRow, targetCol, furtherMoves);
                    }
                }
            }else {
                if(canBePromoted(movingPiece))
                    promote(movingPiece);
                game.nextTurn();
                resetMoveInteractions();
            }
            if(game.isEndOfGame())
                endGame();
        });

        transition.play();
    }

    private void takeDownPiece(int row, int col){
        if(game.getTurn() == Alliance.WHITE)
            opponentTaken.add(piecesImages[row][col], 0, opponentTakenCounter++);
        else
            playerTaken.add(piecesImages[row][col], 0, playerTakenCounter++);
        boardLayout[row][col].getChildren().clear();
    }

    private void checkForAvailableMoves(int row, int col){
        resetMoveInteractions();
        BlackTile investigatedTile = board.getTile(row, col);
        Piece investigatedPiece = investigatedTile.getOccupant();
        List<Move> list = investigatedPiece.checkForPossibleMoves(board, true);
        for(Move m : list){
            int destinationRow = m.destinationCoords.x;
            int destinationColumn = m.destinationCoords.y;
            if(m.isAvailableNow()) {
                boardLayout[destinationRow][destinationColumn].setOnMouseClicked(e ->
                        movePiece(investigatedTile.getCoords(), m));
                if (m.isAttacking())
                    boardLayout[destinationRow][destinationColumn].setId("isAttacking");
                else
                    boardLayout[destinationRow][destinationColumn].setId("isAvailable");
            }else
                boardLayout[destinationRow][destinationColumn].setId("isAttacking");
        }
    }

    private List<Move> getFurtherMoves(Piece investigatedPiece){
        disableAllInteractions();
        List<Move> allMoves = investigatedPiece.checkForPossibleMoves(board, true);
        List<Move> availableMoves = new LinkedList<>();
        for(Move move : allMoves)
            if(move instanceof CapturingMove)
                availableMoves.add(move);
        return availableMoves;
    }
    
    private void displayMoves(int row, int col, List<Move> moves){
        for(Move m : moves){
            int destinationRow = m.destinationCoords.x;
            int destinationColumn = m.destinationCoords.y;
            if (m.isAvailableNow() && m.isAttacking()) {
                boardLayout[destinationRow][destinationColumn].setOnMouseClicked(e ->
                        movePiece(board.getTiles()[row][col].getOccupant().getCoords(), m));
                boardLayout[destinationRow][destinationColumn].setId("isAttacking");
            }
        }
    }

    private void resetMoveInteractions(){
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                Tile tile  = board.getTiles()[row][col];
                if(tile instanceof BlackTile) {
                    boardLayout[row][col].setId("blackTile");
                    if(tile.isOccupied() && (game.getTurn() == tile.getOccupant().getPieceAlliance()))
                        makeFieldClickable(row, col);
                    else
                        disableField(row, col);
                }
            }
        }
    }

    private void disableAllInteractions(){
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                disableField(row, col);
            }
        }
    }

    private void removeHighlighting(){
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                Tile tile  = board.getTiles()[row][col];
                if(tile instanceof BlackTile)
                    boardLayout[row][col].setId("blackTile");
                else
                    boardLayout[row][col].setId("whiteTile");
            }
        }
    }

    private void makeFieldClickable(int row, int col){
        boardLayout[row][col].setOnMouseClicked(e -> checkForAvailableMoves(row, col));
    }

    private void disableField(int row, int col){
        boardLayout[row][col].setOnMouseClicked(e -> {});
    }

    private boolean canBePromoted(Piece piece){
        if(piece instanceof Pawn){
            if(piece.getPieceAlliance() == Alliance.BLACK)
                return piece.getCoords().x == BOARD_SIZE -1;
            else
                return piece.getCoords().x == 0;
        }else
            return false;
    }

    private void promote(Piece piece){
        piece = King.promoteFrom((Pawn) piece);

        boardLayout[piece.getCoords().x][piece.getCoords().y].getChildren().clear();
        Image img;
        if(piece.getPieceAlliance() == Alliance.BLACK)
            img = new Image("com/checkers/images/blackKing.png");
        else
            img = new Image("com/checkers/images/whiteKing.png");
        ImageView view = new ImageView(img);
        boardLayout[piece.getCoords().x][piece.getCoords().y].getChildren().add(view);
        piecesImages[piece.getCoords().x][piece.getCoords().y] = view;
    }

    public void endGame(){
        disableAllInteractions();
        boardStackPane.setAlignment(Pos.CENTER);
        VBox messageContainer = new VBox();
        messageContainer.setId("gameEndBox");
        messageContainer.setAlignment(Pos.CENTER);
        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setId("gameOverLabel");
        String winnerString = game.getWinner().toString().charAt(0) + game.getWinner().toString().substring(1).toLowerCase();
        Label winner = new Label(winnerString + " player wins!");
        winner.setId("winnerLabel");
        Button playAgain = new Button("Play again");
        playAgain.setId("playAgainButton");
        boolean isAIGame = game instanceof AIGame;
        playAgain.setOnMouseClicked(e -> {
            boardStackPane.setAlignment(Pos.TOP_LEFT);
            boardStackPane.getChildren().remove(messageContainer);
            initialize();
            if(isAIGame)
                evolveIntoAIGame();
        });

        messageContainer.getChildren().addAll(gameOverLabel, winner, playAgain);
        boardStackPane.getChildren().add(messageContainer);
    }
}
