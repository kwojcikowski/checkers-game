package com.checkers.engine.controllers;

import com.checkers.engine.AIGame;
import com.checkers.engine.Alliance;
import com.checkers.engine.Game;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.Move;
import com.checkers.engine.board.Tile;
import com.checkers.engine.pieces.Piece;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.List;

import static com.checkers.engine.board.Board.BOARD_SIZE;
import static com.checkers.engine.board.Board.ROWS_OF_PIECES;

public class GameController {

    @FXML private BorderPane container;
    @FXML private GridPane boardGrid;
    @FXML private GridPane opponentTaken;
    @FXML private GridPane playerTaken;
    @FXML private VBox boardWrap;

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

        resetMoveInteractions();
    }

    public void evolveIntoAIGame(){
        System.out.println("Evolving");
        game = new AIGame(game, this);
    }

    private void renderBoard(){
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
        return board.getTiles()[row][col] instanceof Tile.BlackTile;
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

        final double boardOffsetX = boardWrap.getLayoutX();
        final double boardOffsetY = boardWrap.getLayoutY();

        double startingPointX = boardOffsetX + startingTile.getLayoutX() + startingTile.getWidth()/2;
        double startingPointY = boardOffsetY + startingTile.getLayoutY() + startingTile.getHeight()/2;
        double targetPointX = boardOffsetX + targetTile.getLayoutX() + targetTile.getWidth()/2;
        double targetPointY = boardOffsetY + targetTile.getLayoutY() + targetTile.getHeight()/2;

        startingTile.getChildren().clear();
        container.getChildren().add(pieceImage);

        Line line = new Line(startingPointX, startingPointY, targetPointX, targetPointY);
        PathTransition transition = new PathTransition();
        transition.setNode(pieceImage);
        transition.setDuration(Duration.seconds(0.3));
        transition.setPath(line);

        transition.setOnFinished(e -> {
            board.getTiles()[oldRow][oldCol].getOccupant().moveTo(board.getTiles()[targetRow][targetCol]);

            container.getChildren().remove(piecesImages[oldRow][oldCol]);
            boardLayout[targetRow][targetCol].getChildren().clear();
            if(game.getTurn() == Alliance.WHITE)
                setupWhitePawn(targetRow, targetCol);
            else
                setupBlackPawn(targetRow, targetCol);
            piecesImages[oldRow][oldCol] = null;

            if(moveToMake.isAttacking()){
                int attackedPieceRow = Math.abs(targetRow - oldRow);
                int attackedPieceCol = Math.abs(targetCol - oldCol);

                Piece piece = board.getTiles()[attackedPieceRow][attackedPieceCol].getOccupant();
                piece.takeDown();
                takeDownPiece(attackedPieceRow, attackedPieceCol);
                if(pieceHasNoFurtherMoves(targetRow, targetCol)){
                    game.nextTurn();
                }
            }else
                game.nextTurn();
            resetMoveInteractions();
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
        Tile investigatedTile = board.getTiles()[row][col];
        Piece investigatedPiece = investigatedTile.getOccupant();
        List<Move> list = investigatedPiece.checkForPossibleMoves(board, true);
        for(Move m : list){
            int destinationRow = m.destinationCoords.x;
            int destinationColumn = m.destinationCoords.y;
            boardLayout[destinationRow][destinationColumn].setOnMouseClicked(e ->
                    movePiece(investigatedTile.tileCoords, m));

            if(m.isAvailableNow())
                boardLayout[destinationRow][destinationColumn].setId("isAvailable");
            else
                boardLayout[destinationRow][destinationColumn].setId("isAttacking");
        }
    }

    private boolean pieceHasNoFurtherMoves(int row, int col){
        resetMoveInteractions();
        Tile investigatedTile = board.getTiles()[row][col];
        Piece investigatedPiece = investigatedTile.getOccupant();
        List<Move> list = investigatedPiece.checkForPossibleMoves(board, true);
        for(Move m : list){
            int destinationRow = m.destinationCoords.x;
            int destinationColumn = m.destinationCoords.y;
            if(m.isAvailableNow()) {
                boardLayout[destinationRow][destinationColumn].setOnMouseClicked(e -> movePiece(investigatedTile.tileCoords, m));
                boardLayout[destinationRow][destinationColumn].setId("isAttacking");
            }
        }
        return list.isEmpty();
    }

    private void resetMoveInteractions(){
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                Tile tile  = board.getTiles()[row][col];
                if(tile instanceof Tile.BlackTile) {
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

    private void makeFieldClickable(int row, int col){
        boardLayout[row][col].setOnMouseClicked(e -> checkForAvailableMoves(row, col));
    }

    private void disableField(int row, int col){
        boardLayout[row][col].setOnMouseClicked(e -> {});
    }
}
