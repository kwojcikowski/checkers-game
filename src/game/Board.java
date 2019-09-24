package game;

import pieces.Piece;

public class Board {

    private Tile[][] board;

    public Board(){
        board = new Tile[8][8];
        for(Tile[] a:board)
            for(Tile t:a)
                t=new Tile();
    }

    private void setBoard(){

        for(int i=0;i<4;i++)
            for(int j=0;j<8;j++)
                if((j+i)%2==0) board[i][j].setOccupant(new Piece()); //for white

        for(int i=8;i>5;i--)
            for(int j=0;j<8;j++)
                if((j+i)%2==0) board[i][j].setOccupant(new Piece()); //for black
    }

    public void startGame(){
        setBoard();

    }

}
