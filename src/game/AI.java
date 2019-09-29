package game;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import pieces.Piece;

import java.util.Arrays;
import java.util.LinkedList;

public class AI {

    private Controller controller;
    private Game game;
    private boolean transitionInProgress;

    public AI(Controller controller, Game game){
        this.controller = controller;
        this.game = game;
    }

    //to be improved of course
    public Move findRandomMove(LinkedList<Move> moves){
        int randomNumber = (int) (Math.random() * moves.size());
        return moves.get(randomNumber);
    }

    public void playAITurn(){
        int[] randomPiece = getRandomPiece();
        int row = randomPiece[0];
        int col = randomPiece[1];
        System.out.println("Got random piece : "+row + ","+col);
        moveAI(row, col);
        game.whiteTurn();
    }

    public void moveAI(int row, int col){
        Move targetMove = getCorrectMove(row, col);
        if(targetMove != null){
            ImageView[][] piecesImages = controller.getPiecesImages();
            VBox[][] boardLayout = controller.getBoardLayout();
            VBox boardWrap = controller.getBoardWrap();
            BorderPane container = controller.getContainer();

            //Detaching piece Object so it can move
            ImageView movingPiece = piecesImages[row][col];
            VBox startingContainer =  boardLayout[row][col];
            startingContainer.getChildren().clear();

            //Attach to container so we can see it
            container.getChildren().add(movingPiece);
            //Setting up target container
            VBox targetContainer = boardLayout[targetMove.getX()][targetMove.getY()];

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
                    controller.attachImage(row, col, targetMove.getX(), targetMove.getY());
                    controller.disableBlackPieces();
                    if (targetMove.isAttacking()) {
                        int attackedRow = row + (targetMove.getX() - row)/2;
                        int attackedCol = col + (targetMove.getY() - col)/2;
                        controller.handleAttackedPiece(attackedRow, attackedCol);
                        if(controller.checkForFurtherMoves(targetMove.getX(), targetMove.getY())) {
                            moveAI(targetMove.getX(), targetMove.getY());
                        }else{
                            game.whiteTurn();
                        }
                    }

                }
            });
            transition.play();
        }
    }

    public int[] getRandomPiece(){
        Tile[][] board = game.getBoard().getTiles();
        LinkedList<Move> moves = new LinkedList<>();
        int[][] piecesCords = new int[board.length * board[0].length][2];
        int[][] piecesCordsAttack = new int[board.length * board[0].length][2];
        int[] coordinates = new int[2];
        int counter = 0;
        int attackCounter = 0;
        for(int i = 0; i < board.length; i ++){
            for(int j = 0; j < board[i].length; j++) {
                if (board[i][j].getOccupant() != null) {
                    if (!board[i][j].getOccupant().isWhite()) {
                        moves = game.moveChecker(i, j, board, true, false);
                    }
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

    public Move getCorrectMove(int row, int col){
        Tile[][] board = game.getBoard().getTiles();
        LinkedList<Move> moves;
        LinkedList<Move> attacks;
        Move target;
        attacks = new LinkedList<>();
        System.out.println(row + "," + col);
        moves = game.moveChecker(row, col, board, true, false);
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

}
