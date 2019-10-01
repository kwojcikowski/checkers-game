package game;

public class Game {

    private MultiController controller;
    private AIController aiController;
    private Board board;
    private boolean whiteTurn;
    private boolean isFinished;
    private boolean isMulti;

    public void startGame(MultiController controller){
        this.controller = controller;
        board = new Board();
        board.setBoard();
        this.isFinished = false;
        isMulti = true;
    }

    public void startGame(AIController controller){
        this.aiController = controller;
        board = new Board();
        board.setBoard();
        this.isFinished = false;
        isMulti = false;
    }

    public void endGame(String winner){
        System.out.println("Ending game");
        if(isMulti) {
            controller.updateOnMouse();
            controller.disableAllPieces();
            setFinished(true);
        }else{
            aiController.updateOnMouse();
            aiController.disableAllPieces();
            setFinished(true);
        }
        System.out.println("Game Over!\nThe Winner is : "+winner);
    }

    void whiteTurn(){
        this.whiteTurn = true;
        if(isMulti) {
            controller.disableBlackPieces();
            controller.enableWhitePieces();
        }else{
            aiController.disableBlackPieces();
            aiController.enableWhitePieces();
        }
    }

    void blackTurn(){
        this.whiteTurn = false;
        if(isMulti) {
            controller.disableWhitePieces();
            controller.enableBlackPieces();
        }else{
            aiController.disableWhitePieces();
        }
    }

    public boolean isWhiteTurn(){
        return whiteTurn;
    }

    Board getBoard(){
        return board;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
