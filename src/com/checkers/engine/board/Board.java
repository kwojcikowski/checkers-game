package com.checkers.engine.board;

import com.checkers.engine.Alliance;
import com.checkers.engine.pieces.*;
import com.checkers.engine.board.Tile.*;

import java.util.Arrays;


public class Board {

    public static final int BOARD_SIZE = 8;
    public static final int ROWS_OF_PIECES = 3;

    final Tile[][] tiles;

    public Board(){
        tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
        setupTiles();
        setupBoard();
    }

    private Board(Board other){
        tiles = other.tiles.clone();
    }

    public static Board copyOf(Board other){
        return new Board(other);
    }

    private void setupTiles() {
        for(int row = 0; row < tiles.length; row++){
            for(int column = 0; column < tiles[row].length; column++){
                if(isBlackTile(row, column))
                    tiles[row][column] = new BlackTile(Coords.at(row, column));
                else
                    tiles[row][column] = new WhiteTile(Coords.at(row, column));
            }
        }
    }

    private boolean isBlackTile(int row, int column){
        return (row + column) % 2 == 0;
    }

    protected void setupBoard(){
        setupBlackPawns();
        setupWhitePawns();
    }

    private void setupWhitePawns() {
        for(int row=BOARD_SIZE-1;row >= BOARD_SIZE-ROWS_OF_PIECES;row--){
            for(int col=0;col<BOARD_SIZE;col++){
                if(tiles[row][col] instanceof BlackTile) {
                    tiles[row][col].setOccupant(new Pawn(Alliance.WHITE, tiles[row][col]));
                }
            }
        }
    }

    private void setupBlackPawns() {
        for(int row=0;row<ROWS_OF_PIECES;row++){
            for(int col=0;col<BOARD_SIZE;col++){
                if(tiles[row][col] instanceof BlackTile) {
                    tiles[row][col].setOccupant(new Pawn(Alliance.BLACK, tiles[row][col]));
                }
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    //TODO clone? copy constructor?
}
