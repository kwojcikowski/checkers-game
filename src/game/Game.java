package game;

import pieces.*;

public class Game {

    private Board board;

    protected void startGame(){
       board = new Board();
       board.setBoard();
    }

    Board getBoard(){
        return board;
    }





}
