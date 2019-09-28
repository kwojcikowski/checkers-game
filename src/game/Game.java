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
        boolean isWhite = false;
        //edited
        try {
            isWhite = tiles[x][y].getOccupant().isWhite();
        }catch (NullPointerException ignored){}
        //end of edit
        if(isWhite){
            if(y+1<8){
                for(int i=-1;i<3;i+=2){
                    //kwojcikowski split 1 try into 3 (so one error does not impact others)
                    try {
                        //edited by kwojcikowski
                        if (!tiles[x + i][y - 1].isOccupied()) {
                            moves.add(new Move(x + i, y - 1, false));
                        }
                    }catch (ArrayIndexOutOfBoundsException ignored){}
                        //end of edit
                    try {
                        if (!tiles[x + i][y + 1].isOccupied()) {
                            moves.add(new Move(x + i, y + 1, false));
                        }
                    }catch (ArrayIndexOutOfBoundsException ignored){}
                        /*
                        else if(tiles[x+i][y+1].isOccupied()&&tiles[x+i][y+1].getOccupant().isWhite()!=tiles[x+i][y+1].getOccupant()
                                .isWhite()){
                            if(/*y+2<8&&x+2*i<8&&*//*!tiles[x+i*2][y+2].isOccupied()){
                                moves.add(new Move(x+i*2,y+2,true));
                                moves.addAll(moveChecker(x+i*2,y+2));
                            }
                        }
                         */
                    try{
                        if(tiles[x+i][y+1].isOccupied()&&!tiles[x+i][y+1].getOccupant()
                                .isWhite()){
                            System.out.println("here");
                            if(/*y+2<8&&x+2*i<8&&*/!tiles[x+i+1][y+2].isOccupied()){
                                moves.add(new Move(x+i+1,y+2,true));
                                moves.addAll(moveChecker(x+i+1,y+2));
                            }
                        }
                    }catch(ArrayIndexOutOfBoundsException ignored){}
                }
            }
        }
        return moves;
    }

}
