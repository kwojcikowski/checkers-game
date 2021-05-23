package com.checkers.engine.controllers;

import com.checkers.engine.AIGame;
import com.checkers.engine.Alliance;
import com.checkers.engine.Game;
import com.checkers.engine.board.BlackTile;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.Tile;
import com.checkers.engine.board.move.CapturingMove;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.pieces.King;
import com.checkers.engine.pieces.Pawn;
import com.checkers.engine.pieces.Piece;
import com.checkers.engine.player.AIPlayer;
import com.checkers.engine.player.strategy.RandomStrategy;
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

public class GameController {

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
    @FXML
    private StackPane boardStackPane;

    private Game game;
    private Board board;
    private VBox[][] boardLayout;
    private ImageView[][] piecesImages;

    private int playerTakenCounter = 0;
    private int opponentTakenCounter = 0;

    @FXML
    void initialize() {
        boardLayout = new VBox[BOARD_SIZE][BOARD_SIZE];
        piecesImages = new ImageView[BOARD_SIZE][BOARD_SIZE];

        game = new Game(null, null);
        board = game.getBoard();
        renderBoard();

        playerTakenCounter = 0;
        opponentTakenCounter = 0;
        opponentTaken.getChildren().clear();
        playerTaken.getChildren().clear();

        resetMoveInteractions();
    }

    public void evolveIntoAIGame() {
        game = new AIGame(game, this);
        game.whitePlayer = new AIPlayer(Alliance.WHITE, new RandomStrategy(), this);
        game.blackPlayer = new AIPlayer(Alliance.BLACK, new RandomStrategy(), this);

        game.alternateTurn();
    }

    private void renderBoard() {
        Tile[][] tiles = game.getBoard().getTiles();
        boardGrid.getChildren().clear();
        for (int row = 0; row < boardGrid.getRowCount(); row++) {
            for (int column = 0; column < boardGrid.getColumnCount(); column++) {
                renderTile(row, column);
                setupPiece(tiles[row][column]);
            }
        }
    }

    private void renderTile(int row, int column) {
        VBox boardField = new VBox();
        boardField.setAlignment(Pos.CENTER);
        if (isBlackTile(row, column))
            boardField.setId("blackTile");
        else
            boardField.setId("whiteTile");
        boardGrid.add(boardField, column, row);
        boardLayout[row][column] = boardField;
    }

//    private void setupPawn(int row, int column){
//        if(isBlackTile(row, column) && row < ROWS_OF_PIECES)
//            setupBlackPawn(row, column);
//        else if (isBlackTile(row, column) && row >= BOARD_SIZE - ROWS_OF_PIECES)
//            setupWhitePawn(row, column);
//    }

    private void setupPiece(Tile tile) {
        if (tile.isOccupied()) {
            Piece occupant = tile.getOccupant();
            if(occupant instanceof Pawn) {
                if (occupant.getPieceAlliance() == Alliance.BLACK)
                    setupBlackPawn(occupant.getCoords().x, occupant.getCoords().y);
                else
                    setupWhitePawn(occupant.getCoords().x, occupant.getCoords().y);
            } else {
                if (occupant.getPieceAlliance() == Alliance.BLACK)
                    setupBlackKing(occupant.getCoords().x, occupant.getCoords().y);
                else
                    setupWhiteKing(occupant.getCoords().x, occupant.getCoords().y);
            }

        }
//            if(.getPieceAlliance() == Alliance.BLACK)
//                setupBlackPawn(tile.);
//        if(isBlackTile(row, column) && row < ROWS_OF_PIECES)
//            setupBlackPawn(row, column);
//        else if (isBlackTile(row, column) && row >= BOARD_SIZE - ROWS_OF_PIECES)
//            setupWhitePawn(row, column);
    }

    private boolean isBlackTile(int row, int col) {
        return board.getTiles()[row][col] instanceof BlackTile;
    }

    private void setupWhitePawn(int row, int col) {
        Image img = new Image("com/checkers/images/whitePiece.png");
        ImageView view = new ImageView(img);
        boardLayout[row][col].getChildren().add(view);
        piecesImages[row][col] = view;
    }

    private void setupBlackPawn(int row, int col) {
        Image img = new Image("com/checkers/images/blackPiece.png");
        ImageView view = new ImageView(img);
        boardLayout[row][col].getChildren().add(view);
        piecesImages[row][col] = view;
    }

    private void setupWhiteKing(int row, int col) {
        Image img = new Image("com/checkers/images/whiteKing.png");
        ImageView view = new ImageView(img);
        boardLayout[row][col].getChildren().add(view);
        piecesImages[row][col] = view;
    }

    private void setupBlackKing(int row, int col) {
        Image img = new Image("com/checkers/images/blackKing.png");
        ImageView view = new ImageView(img);
        boardLayout[row][col].getChildren().add(view);
        piecesImages[row][col] = view;
    }

    public void startGame() {
        game.alternateTurn();
    }

    public void movePiece(Move moveToMake) {
        disableAllInteractions();
        Coords pieceCoords = moveToMake.piece.getCoords();
        int oldRow = pieceCoords.x;
        int oldCol = pieceCoords.y;
        int targetRow = moveToMake.destinationCoords.x;
        int targetCol = moveToMake.destinationCoords.y;
        ImageView pieceImage = piecesImages[oldRow][oldCol];
        VBox startingTile = boardLayout[oldRow][oldCol];
        VBox targetTile = boardLayout[targetRow][targetCol];
        Piece movingPiece = board.getTiles()[oldRow][oldCol].getOccupant();
        final double BOARD_OFFSET_X = boardWrap.getLayoutX();
        final double BOARD_OFFSET_Y = boardWrap.getLayoutY();
        double startingPointX = BOARD_OFFSET_X + startingTile.getLayoutX() + startingTile.getWidth() / 2;
        double startingPointY = BOARD_OFFSET_Y + startingTile.getLayoutY() + startingTile.getHeight() / 2;
        double targetPointX = BOARD_OFFSET_X + targetTile.getLayoutX() + targetTile.getWidth() / 2;
        double targetPointY = BOARD_OFFSET_Y + targetTile.getLayoutY() + targetTile.getHeight() / 2;
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
            if (moveToMake.isCapturing()) {
                CapturingMove move = (CapturingMove) moveToMake;
                Coords attackedPieceCoords = move.getAttackedPieceCoords();
                Piece attackedPiece = board.getTiles()[attackedPieceCoords.x][attackedPieceCoords.y].getOccupant();
                attackedPiece.takeDown();
                takeDownPiece(attackedPieceCoords);
                board.alliancePieces.get(attackedPiece.getPieceAlliance()).remove(attackedPiece);
                removeTilesHighlighting();
            }
            checkForPromotion(movingPiece);
            //recursive call
            if (moveToMake.hasNextMove()) {
                movePiece(((CapturingMove) moveToMake).getNextMove());
            } else {
                if (game.isEndOfGame()) {
                    endGame();
                }
                game.alternateTurn();
            }
        });
        transition.play();
    }

    private void takeDownPiece(Coords pieceCoords) {
        if (game.getTurn() == Alliance.WHITE)
            opponentTaken.add(piecesImages[pieceCoords.x][pieceCoords.y], 0, opponentTakenCounter++);
        else
            playerTaken.add(piecesImages[pieceCoords.x][pieceCoords.y], 0, playerTakenCounter++);
        boardLayout[pieceCoords.x][pieceCoords.y].getChildren().clear();
    }

    private void checkForAvailableMoves(int row, int col) {
        resetMoveInteractions();
        BlackTile investigatedTile = board.getTile(row, col);
        Piece investigatedPiece = investigatedTile.getOccupant();
        List<Move> list = investigatedPiece.getAllPossibleMoves(board);
        for (Move m : list) {
            int destinationRow = m.destinationCoords.x;
            int destinationColumn = m.destinationCoords.y;
            if (m.isAvailableNow()) {
                boardLayout[destinationRow][destinationColumn].setOnMouseClicked(e ->
                        movePiece(m));
                if (m.isCapturing())
                    boardLayout[destinationRow][destinationColumn].setId("isAttacking");
                else
                    boardLayout[destinationRow][destinationColumn].setId("isAvailable");
            } else
                boardLayout[destinationRow][destinationColumn].setId("isAttacking");
        }
    }

    public void enablePieceInteraction() {

    }

    private List<Move> getFollowingMoves(Piece investigatedPiece) {
        disableAllInteractions();
        List<Move> allMoves = investigatedPiece.checkForPossibleMoves(board, true);
        List<Move> availableMoves = new LinkedList<>();
        for (Move move : allMoves)
            if (move instanceof CapturingMove)
                availableMoves.add(move);
        return availableMoves;
    }

    private void checkForPromotion(Piece candidate) {
        if (isEligibleForPromotion(candidate))
            promote(candidate);
    }

    private void showPossibleMoves(int row, int col, List<Move> moves) {
        for (Move m : moves) {
            int destinationRow = m.destinationCoords.x;
            int destinationColumn = m.destinationCoords.y;
            if (m.isAvailableNow() && m.isCapturing()) {
                boardLayout[destinationRow][destinationColumn].setOnMouseClicked(e ->
                        movePiece(m));
                boardLayout[destinationRow][destinationColumn].setId("isAttacking");
            }
        }
    }

    public void resetMoveInteractions() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Tile tile = board.getTiles()[row][col];
                if (tile instanceof BlackTile) {
                    boardLayout[row][col].setId("blackTile");
                    if (tile.isOccupied() && (game.getTurn() == tile.getOccupant().getPieceAlliance()))
                        enableFieldInteraction(row, col);
                    else
                        disableField(row, col);
                }
            }
        }
    }

    private void disableAllInteractions() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                disableField(row, col);
            }
        }
    }

    private void removeTilesHighlighting() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Tile tile = board.getTiles()[row][col];
                if (tile instanceof BlackTile)
                    boardLayout[row][col].setId("blackTile");
                else
                    boardLayout[row][col].setId("whiteTile");
            }
        }
    }

    public void enableFieldInteraction(int row, int col) {
        boardLayout[row][col].setOnMouseClicked(e -> checkForAvailableMoves(row, col));
    }

    private void disableField(int row, int col) {
        boardLayout[row][col].setOnMouseClicked(e -> {
        });
    }

    private boolean isEligibleForPromotion(Piece piece) {
        if (piece instanceof Pawn) {
            if (piece.getPieceAlliance() == Alliance.BLACK)
                return piece.getCoords().x == BOARD_SIZE - 1;
            else
                return piece.getCoords().x == 0;
        } else
            return false;
    }

    private void promote(Piece piece) {
        board.alliancePieces.get(game.getTurn()).remove(piece);
        King promoted = King.promoteFrom((Pawn) piece);
        board.alliancePieces.get(game.getTurn()).add(promoted);


        boardLayout[promoted.getCoords().x][promoted.getCoords().y].getChildren().clear();
        Image img;
        if (promoted.getPieceAlliance() == Alliance.BLACK)
            img = new Image("com/checkers/images/blackKing.png");
        else
            img = new Image("com/checkers/images/whiteKing.png");
        ImageView view = new ImageView(img);
        boardLayout[promoted.getCoords().x][promoted.getCoords().y].getChildren().add(view);
        piecesImages[promoted.getCoords().x][promoted.getCoords().y] = view;
    }

    public void endGame() {
        disableAllInteractions();

        boardStackPane.setAlignment(Pos.CENTER);
        VBox messageContainer = new VBox();
        messageContainer.setId("gameEndBox");
        messageContainer.setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setId("gameOverLabel");
        String winnerString = game.getWinner();
        Label winner = new Label(winnerString + " player wins!");
        winner.setId("winnerLabel");

        Button playAgain = new Button("Play again");
        playAgain.setId("playAgainButton");
        boolean isAIGame = game instanceof AIGame;
        playAgain.setOnMouseClicked(e -> {
            boardStackPane.setAlignment(Pos.TOP_LEFT);
            boardStackPane.getChildren().remove(messageContainer);
            initialize();
            if (isAIGame)
                evolveIntoAIGame();
        });

        messageContainer.getChildren().addAll(gameOverLabel, winner, playAgain);
        boardStackPane.getChildren().add(messageContainer);
    }
}
