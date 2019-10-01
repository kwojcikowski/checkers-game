package pieces;

import game.Board;
import game.Move;
import game.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;

public class Pawn extends Piece {

    public Pawn(boolean isWhite){
        super(isWhite);
    }

    public Pawn(Piece piece){
        super(piece);
    }

    @Override
    public LinkedList<Move> moveChecker(final int x, final int y, final Tile[][] tiles,
                                        final boolean availableNow){
        LinkedList<Move> moves=new LinkedList<>();
        final boolean isWhite=tiles[x][y].getOccupant().isWhite();
        final int j;
        if(isWhite) j=1;
        else j=-1;

        //capturing left-right
        for(int i=-1;i<3;i+=2){
            try{
                if(!tiles[x+j][y+i].isOccupied()&&availableNow){
                    moves.add(new Move(x+j,y+i,false,true));
                }
            }catch(ArrayIndexOutOfBoundsException ignore){}

            //capturing backwards
            for(int k=1;k>-2;k-=2){
                try{
                    if(tiles[x+j*k][y+i].isOccupied()&&isWhite!=tiles[x+j*k][y+i].getOccupant().isWhite()){
                        if(!tiles[x+j*2*k][y+2*i].isOccupied()){
                            moves.add(new Move(x+j*2*k,y+2*i,true,availableNow));
                            final Tile[][] alternative = Board.copyBoard(tiles);
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
}
