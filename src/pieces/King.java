package pieces;

import game.Move;
import game.Tile;

import java.util.LinkedList;

public class King extends Piece{

    public King(Piece piece){
        super(piece);
    }

    public King(boolean isWhite){
        super(isWhite);
    }

    @Override
    public LinkedList<Move> moveChecker(int x, int y, Tile[][] tiles, boolean availableNow) {
        LinkedList<Move> moves=new LinkedList<>();
        final boolean isWhite=tiles[x][y].getOccupant().isWhite();
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
                try{
                    int k=0;
                    while(!tiles[x+j*k][y+i*k].isOccupied()){
                        moves.add(new Move(x+j*k,y+j*k,false,true));
                        k++;
                    }
                    if(isWhite!=tiles[x+j*k][y+i*k].getOccupant().isWhite()){
                        while(!tiles[x+j*k+j][y+i*k+i].isOccupied()){
                            moves.add(new Move(x+j*k+j,+i*k+i,true,true));
                            k++;
                        }
                    }

                }catch(ArrayIndexOutOfBoundsException ignore){}
            }
        }
        return moves;
    }

}
