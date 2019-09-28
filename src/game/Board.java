package game;

import pieces.Piece;

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
                    tiles[i][j].setOccupant(new Piece(true)); //for white
                    tiles[i][j].setOccupied(true);
                }

        System.out.println();
        for(int i=7;i>4;i--)
            for(int j=0;j<8;j++)
                if((j+i)%2==0) {
                    tiles[i][j].setOccupant(new Piece(false)); //for black
                    tiles[i][j].setOccupied(true);
                }
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
