package game;

public class Board {

    private Tile[][] board;

    public Board(){
        board = new Tile[8][8];
        for(Tile[] a:board)
            for(Tile t:a)
                t=new Tile();
    }

    private void setBoard(){
        for(int i=0;i<8;i++){

        }
    }

    public void startGame(){
        setBoard();

    }

}
