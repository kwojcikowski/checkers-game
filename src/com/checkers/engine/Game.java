package com.checkers.engine;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Tile;
import com.checkers.engine.controllers.*;
import com.checkers.engine.pieces.Piece;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private Board board;
    private Alliance turn;

    public Game(){
        board = new Board();
        turn = Alliance.WHITE;
    }

    Game(Game game){
        this.board = game.board;
        this.turn = game.turn;
    }

    public Alliance getTurn(){
        return turn;
    }

    public void nextTurn(){
        turn = (turn == Alliance.WHITE) ? Alliance.BLACK : Alliance.WHITE;
    }

    public Board getBoard(){
        return board;
    }

    public boolean isEndOfGame(){
        Tile[][] tiles = getBoard().getTiles();
        for(Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isOccupied() && tile.getOccupant().getPieceAlliance() == turn) {
                    if (!tile.getOccupant().checkForPossibleMoves(getBoard(), true).isEmpty())
                        return false;
                }
            }
        }
        return true;
    }

    public Alliance getWinner(){
        return (turn == Alliance.WHITE) ? Alliance.BLACK : Alliance.WHITE;
    }
}
