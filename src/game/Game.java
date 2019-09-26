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

    public LinkedList<Move> moveChecker(int x, int y){
        final Tile[][] tiles=getBoard().getBoard();
        LinkedList<Move> moves=new LinkedList<>();
        final boolean isWhite=tiles[x][y].getOccupant().isWhite();
        if(isWhite){
            if(y+1<8){
                for(int i=-1;i<3;i+=2){
                    try{
                        if(!tiles[x+i][y+1].isOccupied()){
                            moves.add(new Move(x+i,y+1,false));
                        }
                        else if(tiles[x+i][y+1].isOccupied()&&isWhite!=tiles[x+i][y+1].getOccupant()
                                .isWhite()){
                            if(/*y+2<8&&x+2*i<8&&*/!tiles[x+i*2][y+2].isOccupied()){
                                moves.add(new Move(x+i*2,y+2,true));
                                moves.addAll(moveChecker(x+i*2,y+2));
                            }
                        }
                    }catch(ArrayIndexOutOfBoundsException e){}
                }
            }
        }
        return moves;
    }

}
