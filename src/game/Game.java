package game;

import pieces.Piece;

import java.util.LinkedList;

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

    public Board getBoard(){
        return board;
    }

    public LinkedList<Move> moveChecker(final int x, final int y, final Tile[][] tiles,
                                        final boolean availableNow, final boolean recursive){
        LinkedList<Move> moves=new LinkedList<>();
        final boolean isWhite=tiles[x][y].getOccupant().isWhite();
        final int j;
        if(isWhite) j=1;
        else j=-1;

        for(int i=-1;i<3;i+=2){
            try{
                if(!tiles[x+j][y+i].isOccupied()&&availableNow){
                    moves.add(new Move(x+j,y+i,false,true, false));
                }
            }catch(ArrayIndexOutOfBoundsException ignore){}
            for(int k=1;k>-2;k-=2){
                try{
                    if(tiles[x+j*k][y+i].isOccupied()&&isWhite!=tiles[x+j*k][y+i].getOccupant().isWhite()){
                        if(!tiles[x+j*2*k][y+2*i].isOccupied()){
                            moves.add(new Move(x+j*2*k,y+2*i,true,availableNow, !availableNow));
                            if(recursive) {
                                final Tile[][] alternative = copyBoard(tiles);
                                alternative[x + j * k][y + i].setOccupied(false);
                                alternative[x + j * 2 * k][y + i * 2].setOccupant(alternative[x][y].getOccupant());
                                alternative[x][y].setOccupied(false);
                                moves.addAll(moveChecker(x + j * 2 * k, y + 2 * i, alternative, false, true));
                            }
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException ignore){}
            }

        }

        return moves;
    }

    private Tile[][] copyBoard(Tile[][] tiles){
        Tile[][] copy = new Tile[tiles.length][tiles[0].length];
        for(int i = 0; i < copy.length; i++){
            for (int j = 0; j < copy[i].length; j++){
                copy[i][j] = new Tile();
                Piece original = tiles[i][j].getOccupant();
                if(original!=null){
                    Piece copied = new Piece(original);
                    copy[i][j].setOccupant(copied);
                }
            }
        }
        return copy;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
