package com.checkers.engine;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Tile;
import com.checkers.engine.controllers.GameController;
import com.checkers.engine.player.Player;

public class Game {

    private final Board board;
    public Player whitePlayer, blackPlayer;
    private Alliance turn;

    public Game() {
        board = new Board();
        turn = Alliance.BLACK;
    }

    Game(Game game) {
        this.board = game.board;
        this.turn = game.turn;
    }

    public Alliance getTurn() {
        return turn;
    }

    public void alternateTurn() {
        turn = turn.getOpposite();
        Player activePlayer = getActivePlayer();
        activePlayer.performNextTurn(board);
    }

    public Board getBoard() {
        return board;
    }

    public boolean isEndOfGame() {
        return
                board.alliancePieces.get(Alliance.WHITE).isEmpty()
                        || board.alliancePieces.get(Alliance.BLACK).isEmpty()
                        || board.alliancePieces.values().stream()
                        .allMatch(pieces -> pieces.stream().allMatch(piece -> piece.checkForPossibleMoves(board, true, false).isEmpty()));
    }

    public String getWinner() {
        return (turn == Alliance.WHITE) ? "White" : "Black";
    }

    public Player getActivePlayer() {
        return turn == Alliance.WHITE ? whitePlayer : blackPlayer;
    }
}
