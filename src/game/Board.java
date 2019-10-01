package game;

import pieces.Pawn;
import pieces.*;

public class Board {

    private Tile[][] tiles;

    public Board(){
        tiles = new Tile[8][8];
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                tiles[i][j] = new Tile();
            }
        }
    }

    protected void setBoard(){
        for(int i=0;i<3;i++)
            for(int j=0;j<8;j++)
                if((j+i)%2==0) {
                    tiles[i][j].setOccupant(new Pawn(true)); //for white
                    tiles[i][j].setOccupied(true);
                }

        System.out.println();
        for(int i=7;i>4;i--)
            for(int j=0;j<8;j++)
                if((j+i)%2==0) {
                    tiles[i][j].setOccupant(new Pawn(false)); //for black
                    tiles[i][j].setOccupied(true);
                }
    }

    public static Tile[][] copyBoard(Tile[][] tiles){
        Tile[][] copy = new Tile[tiles.length][tiles[0].length];
        for(int i = 0; i < copy.length; i++){
            for (int j = 0; j < copy[i].length; j++){
                copy[i][j] = new Tile();
                Piece original = tiles[i][j].getOccupant();
                if(original!=null){
                    Piece copied;
                    if(original instanceof Pawn){
                        copied=new Pawn(original);
                    }
                    else{
                        copied=new King(original);
                    }
                    copy[i][j].setOccupant(copied);
                }
            }
        }
        return copy;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
