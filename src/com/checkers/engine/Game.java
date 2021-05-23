package com.checkers.engine;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Tile;
import com.checkers.engine.controllers.GameController;
import com.checkers.engine.player.Player;

public class Game {

    private final Board board;
    public Player whitePlayer, blackPlayer;
    private Alliance turn;

    public Game(Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        board = new Board();
        turn = Alliance.WHITE;
    }

    Game(Game game) {
        this.board = game.board;
        this.turn = game.turn;
    }

    public Alliance getTurn() {
        return turn;
    }

    public void alternateTurn() {
        Player activePlayer = getActivePlayer();
        activePlayer.performNextTurn(board);
        turn = turn.getOpposite();
    }

    public Board getBoard() {
        return board;
    }

    public boolean isEndOfGame() {
        Tile[][] tiles = getBoard().getTiles();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isOccupied() && tile.getOccupant().getPieceAlliance() == turn) {
                    if (!tile.getOccupant().checkForPossibleMoves(getBoard(), true).isEmpty())
                        return false;
                }
            }
        }
        return true;
    }

    public String getWinner() {
        return (turn == Alliance.WHITE) ? "Black" : "White";
    }

    public Player getActivePlayer() {
        return turn == Alliance.WHITE ? whitePlayer : blackPlayer;
    }
}
