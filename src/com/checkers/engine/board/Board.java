package com.checkers.engine.board;

import com.checkers.engine.Alliance;
import com.checkers.engine.pieces.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {

    public static final int BOARD_SIZE = 8;
    public static final int ROWS_OF_PIECES = 3;

    final Tile[][] tiles;

    public Map<Alliance, Set<Piece>> alliancePieces = new HashMap<>();
//    public Set<Piece> blackPieces, whitePieces;

    public Board(){
        tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
        alliancePieces.put(Alliance.WHITE, new HashSet<>());
        alliancePieces.put(Alliance.BLACK, new HashSet<>());
        setupTiles();
//        setupBoard();


        setupPawn(Alliance.WHITE, 6, 4);
        setupKing(Alliance.BLACK, 5, 3);
//        setupPawn(Alliance.BLACK, 3, 1);
//        setupPawn(Alliance.BLACK, 3, 3);
//        setupPawn(Alliance.BLACK, 5, 3);
//        setupPawn(Alliance.BLACK, 1, 5);
//        setupPawn(Alliance.BLACK, 1, 3);
//        setupPawn(Alliance.BLACK, 3, 5);
//        setupPawn(Alliance.BLACK, 5, 5);
//
//        setupPawn(Alliance.WHITE, 6, 4);
    }

    private void setupPawn(Alliance alliance, int row, int col) {
        Pawn pawn = new Pawn(alliance, (BlackTile) tiles[row][col]);
        tiles[row][col].setOccupant(pawn);
        alliancePieces.get(alliance).add(pawn);
    }

    private void setupKing(Alliance alliance, int row, int col) {
        Pawn pawn = new Pawn(alliance, (BlackTile) tiles[row][col]);
        King promoted = pawn.promote();
        tiles[row][col].setOccupant(promoted);
        alliancePieces.get(alliance).add(promoted);
    }

    private Board(Board other){
        tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
        for(int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                try {
                    tiles[row][col] = other.getTiles()[row][col].clone();
                } catch (CloneNotSupportedException e) {
                    System.out.println("Could not clone object");
                }
            }
        }
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
                    tiles[row][column] = WhiteTile.get();
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
                    setupPawn(Alliance.WHITE, row, col);
                }
            }
        }
    }

    private void setupBlackPawns() {
        for(int row=0;row<ROWS_OF_PIECES;row++){
            for(int col=0;col<BOARD_SIZE;col++){
                if(tiles[row][col] instanceof BlackTile) {
                    setupPawn(Alliance.BLACK, row, col);
                }
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public BlackTile getTile(int row, int col) {
        Tile tile = tiles[row][col];
        if(tile instanceof WhiteTile) {
            throw new UnsupportedOperationException();
        }
        else return (BlackTile) tile;
    }

    //TODO clone? copy constructor?
}
