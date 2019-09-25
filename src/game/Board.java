package game;

import pieces.Piece;

public class Board {

    private Tile[][] board;

    public Board(){
        board = new Tile[8][8];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = new Tile();
            }
        }
    }

    protected void setBoard(){

        for(int i=0;i<3;i++)
            for(int j=0;j<8;j++)
                if((j+i)%2==0) {
                    System.out.println(i + ","+j);
                    board[i][j].setOccupant(new Piece(true)); //for white
                    board[i][j].setOccupied(true);
                }

        System.out.println();
        for(int i=7;i>4;i--)
            for(int j=0;j<8;j++)
                if((j+i)%2==0) {
                    System.out.println(i + ","+j);
                    board[i][j].setOccupant(new Piece(false)); //for black
                    board[i][j].setOccupied(true);
                }
    }

    public Tile[][] getBoard() {
        return board;
    }
}
