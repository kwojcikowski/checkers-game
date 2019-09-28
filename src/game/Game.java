package game;

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

    public LinkedList<Move> moveChecker(int x, int y, final Tile[][] tiles){
        LinkedList<Move> moves=new LinkedList<>();
        final boolean isWhite=tiles[x][y].getOccupant().isWhite();
        final int j;
        if(isWhite) j=1;
        else j=-1;
        for(int i=-1;i<3;i+=2){
            try{
                if(!tiles[x+i][y+j].isOccupied()){
                    moves.add(new Move(x+i,y+j,false));
                }
                else if(tiles[x+i][y+j].isOccupied()&&isWhite!=tiles[x+i][y+j].getOccupant().isWhite()){
                    if(!tiles[x+i*2][y+2*j].isOccupied()){
                        moves.add(new Move(x+i*2,y+2*j,true));
                        Tile[][] alternative=tiles.clone(); //hope this is not a shallow copy
                        alternative[x+i][y+j].setOccupant(null);
                        moves.addAll(moveChecker(x+i*2,y+2*j,alternative));
                    }
                }
            }catch(ArrayIndexOutOfBoundsException e){
                continue;
            }
        }

        return moves;
    }

}
