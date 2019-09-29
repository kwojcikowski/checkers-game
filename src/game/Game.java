package game;

import pieces.Piece;

import java.util.LinkedList;

public class Game {

    private Controller controller;
    private Board board;
    private boolean whiteTurn;

    public void startGame(Controller controller){
        this.controller = controller;
        board = new Board();
        board.setBoard();
    }

    void whiteTurn(){
        whiteTurn = true;
        controller.disableBlackPieces();
        controller.enableWhitePieces();
    }

    void blackTurn(){
        whiteTurn = false;
        controller.disableWhitePieces();
        /*
        AI black=new AI();
        //black.findBestMove(moveChecker(,,board.getTiles(),true));*/
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
            try {
                for(int k=-1;k<3;k+=2){
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
                }

            }catch(ArrayIndexOutOfBoundsException ignore){}
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
}
