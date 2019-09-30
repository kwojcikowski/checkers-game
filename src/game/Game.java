package game;

import pieces.Piece;

import java.util.Arrays;
import java.util.LinkedList;

public class Game {

    private Board board;

    public void startGame(){
       board = new Board();
       board.setBoard();
    }

    public Board getBoard(){
        return board;
    }

    public LinkedList<Move> moveChecker(final int x, final int y, final Tile[][] tiles,
                                        final boolean availableNow){
        LinkedList<Move> moves=new LinkedList<>();
        final boolean isWhite=tiles[x][y].getOccupant().isWhite();
        final int j;
        if(isWhite) j=1;
        else j=-1;

        for(int i=-1;i<3;i+=2){
            try{
                if(!tiles[x+j][y+i].isOccupied()&&availableNow){
                    moves.add(new Move(x+j,y+i,false,true));
                }
            }catch(ArrayIndexOutOfBoundsException ignore){}

            for(int k=1;k>-2;k-=2){
                try{
                    if(tiles[x+j*k][y+i].isOccupied()&&isWhite!=tiles[x+j*k][y+i].getOccupant().isWhite()){
                        if(!tiles[x+j*2*k][y+2*i].isOccupied()){
                            moves.add(new Move(x+j*2*k,y+2*i,true,availableNow));
                            final Tile[][] alternative = copyBoard(tiles);
                            alternative[x+j*k][y+i].setOccupied(false);
                            alternative[x+j*2*k][y+i*2].setOccupant(alternative[x][y].getOccupant());
                            alternative[x][y].setOccupied(false);
                            moves.addAll(moveChecker(x+j*2*k,y+2*i,alternative,false));
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

}
