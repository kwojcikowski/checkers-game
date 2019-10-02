package pieces;

import game.Board;
import game.Move;
import game.Tile;

import java.util.LinkedList;

public class King extends Piece{

    public King(Piece piece){
        super(piece);
    }

    @Override
    public LinkedList<Move> moveChecker(int x, int y, Tile[][] tiles, final boolean availableNow, final boolean recursive) {
        LinkedList<Move> moves=new LinkedList<>();
        final boolean isWhite=tiles[x][y].getOccupant().isWhite();
        for(int i=-1; i<2; i+=2){
            for(int j=-1; j<2;j+=2){
                int k=1;
                try{
                    //Moving on diagonals with no attack
                    while(!tiles[x+i*k][y+j*k].isOccupied()&&availableNow){
                        moves.add(new Move(x+i*k,y+j*k,false,true));
                        k++;
                    }
                }catch(ArrayIndexOutOfBoundsException ignore){}
                try{
                    //Attacks
                    if(tiles[x+i*k][y+j*k].isOccupied()) {
                        if (isWhite != tiles[x + i * k][y + j * k].getOccupant().isWhite()) {
                            while (!tiles[x + i * k + i][y + j * k + j].isOccupied()) {
                                moves.add(new Move(x + i * k + i, y + j * k + j, true, true));
                                if(recursive) {
                                    Tile[][] alternative = Board.copyBoard(tiles);
                                    alternative[x + i * k][y + j * k].setOccupied(false);
                                    alternative[x + i * k + i][y + j * k + j].setOccupant(alternative[x][y].getOccupant());
                                    alternative[x][y].setOccupied(false);
                                    moves.addAll(moveChecker(x + i * k + i, y + j * k + j, alternative, false, true));
                                }
                                k++;
                            }
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException ignore){}
            }
        }
        return moves;
    }
}
