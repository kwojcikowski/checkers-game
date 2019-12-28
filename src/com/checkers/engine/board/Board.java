package com.checkers.engine.board;

import com.checkers.engine.Alliance;
import com.checkers.engine.pieces.*;
import com.checkers.engine.board.Tile.*;


public class Board {

    final Tile[][] tiles;
    private final int BOARD_SIZE = 8;

    protected Board(){
        tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
        setupTiles();
    }

    private void setupTiles() {
        for(int row = 0; row < tiles.length; row++){
            for(int column = 0; column < tiles[row].length; column++){
                if((row+column)%2==0)
                    tiles[row][column] = new BlackTile(new Coordinates(row, column));
                else
                    tiles[row][column] = new WhiteTile(new Coordinates(row, column));
            }
        }
    }

    protected void setupBoard(){
        setupWhitePawns();
        setupBlackPawns();
    }

    private void setupBlackPawns() {
        for(int row=7;row>4;row--){
            for(int col=0;col<8;col++){
                if(tiles[row][col] instanceof BlackTile) {
                    tiles[row][col].setOccupant(new Pawn(Alliance.BLACK));
                }
            }
        }
    }

    private void setupWhitePawns() {
        for(int row=0;row<3;row++){
            for(int col=0;col<8;col++){
                if(tiles[row][col] instanceof WhiteTile) {
                    tiles[row][col].setOccupant(new Pawn(Alliance.WHITE));
                }
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    //TODO clone
}
