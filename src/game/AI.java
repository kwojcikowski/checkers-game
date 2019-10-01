package game;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import pieces.Piece;

import java.util.Arrays;
import java.util.LinkedList;

public class AI implements Runnable{

    private AIController controller;
    private Game game;
    private Tile[][] board;
    private ImageView[][] piecesImages;
    private VBox[][] boardLayout;
    private BorderPane container;
    private VBox boardWrap;
    private Thread aiThread;
    private int recursionCounter;

    public AI(AIController controller, Game game, ImageView[][] piecesImages,
              VBox[][] boardLayout, BorderPane container, VBox boardWrap){
        this.controller = controller;
        this.game = game;
        this.piecesImages = piecesImages;
        this.boardLayout = boardLayout;
        this.container = container;
        this.boardWrap = boardWrap;
        recursionCounter = 1;
        aiThread = new Thread(this);
        aiThread.start();
    }

    //draw random move
    public Move findRandomMove(LinkedList<Move> moves){
        int randomNumber = (int) (Math.random() * moves.size());
        return moves.get(randomNumber);
    }

    //general movement method
    public void playAITurn(){
        int[] randomPiece = getRandomPiece();
        if(randomPiece == null) {
            game.endGame("Player");
            return;
        }
        int row = randomPiece[0];
        int col = randomPiece[1];
        moveAI(row, col);
    }

    //can be recalled to handle attacks
    public void moveAI(int row, int col){
        Move targetMove = getCorrectMove(row, col);
        if(targetMove != null) {
            int targetRow = targetMove.getX();
            int targetCol = targetMove.getY();
            boolean isAttacking = targetMove.isAttacking();
            movePiece(row, col, targetRow, targetCol, isAttacking);
        }
    }

    //Individual method for black pieces as the actions occur after thread loop
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
        transition.setNode(movingPiece);
        transition.setDuration(Duration.seconds(0.3));
        transition.setPath(line);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.attachImage(currentRow, currentCol, targetRow, targetCol);
                boolean maintainTurn;
                if (isAttacking) {
                    controller.handleAttackedPiece(currentRow, currentCol, targetRow, targetCol);
                    //Check game end
                    if(controller.checkGameEnd()){
                        event.consume();
                    }
                    //Check if further moves
                    maintainTurn = controller.checkForFurtherMoves(targetRow, targetCol);
                    if (maintainTurn) {
                        moveAI(targetRow, targetCol);
                    }else{
                        controller.checkPromotion(targetRow, targetCol);
                    }
                }
                //Counting moves so we know how much we need to delay enabling white pieces
                recursionCounter++;
            }
        });
        transition.play();
    }

    //For picking random piece coordinates
    public int[] getRandomPiece(){
        Tile[][] board = game.getBoard().getTiles();
        LinkedList<Move> moves;
        int[][] piecesCords = new int[board.length * board[0].length][2];
        int[][] piecesCordsAttack = new int[board.length * board[0].length][2];
        int[] coordinates = new int[2];
        int counter = 0;
        int attackCounter = 0;
        for(int i = 0; i < board.length; i ++){
            for(int j = 0; j < board[i].length; j++) {
                if (board[i][j].getOccupant() != null) {
                    if (!board[i][j].getOccupant().isWhite()) {
                        Piece temp = board[i][j].getOccupant();
                        moves = temp.moveChecker(i, j, board, true);
                        if (moves.size() != 0) {
                            boolean attack = false;
                            for(Move m : moves){
                                if(m.isAttacking()) {
                                    attack = true;
                                    break;
                                }
                            }
                            if (attack) {
                                piecesCordsAttack[attackCounter][0] = i;
                                piecesCordsAttack[attackCounter][1] = j;
                                attackCounter++;
                            }
                            piecesCords[counter][0] = i;
                            piecesCords[counter][1] = j;
                            counter++;
                        }
                    }
                }
            }
        }
        if(counter == 0)
            return null;
        //selecting random
        if(attackCounter > 0){
            int random = (int) (Math.random() * attackCounter);
            coordinates[0] = piecesCordsAttack[random][0];
            coordinates[1] = piecesCordsAttack[random][1];
        }else{
            int random = (int) (Math.random() * counter);
            coordinates[0] = piecesCords[random][0];
            coordinates[1] = piecesCords[random][1];
        }
        return coordinates;
    }

    //Getting random move for given piece
    public Move getCorrectMove(int row, int col){
        Tile[][] board = game.getBoard().getTiles();
        LinkedList<Move> moves;
        LinkedList<Move> attacks;
        Move target;
        attacks = new LinkedList<>();
        Piece temp = board[row][col].getOccupant();
        moves = temp.moveChecker(row, col, board, true);
        for(Move m : moves){
            if(m.isAttacking()){
                attacks.add(m);
            }
        }
        if(moves.isEmpty())
            return null;
        if(!attacks.isEmpty()){
            target = findRandomMove(attacks);
        }else{
            target = findRandomMove(moves);
        }
        return target;
    }

    @Override
    public void run() {
        //waiting for white player to move
        game.whiteTurn();
        while (!game.isFinished()) {
            while (game.isWhiteTurn()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(game.isFinished()){
                    return;
                }
            }
            if(game.isFinished()){
                return;
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    recursionCounter = 1;
                    playAITurn();
                }
            });
            //Sleep to prevent from enabling whites during attacks
            //Because black moves are handled after loop
            try {
                Thread.sleep(recursionCounter * 300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Change of turn
            game.whiteTurn();
        }
    }
}
