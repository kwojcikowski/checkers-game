package com.checkers.engine.player;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.controllers.GameController;
import com.checkers.engine.pieces.Piece;
import com.checkers.engine.player.strategy.MovePickingStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AIPlayer extends Player{

    MovePickingStrategy movePickingStrategy;

    public AIPlayer(Alliance alliance, MovePickingStrategy movePickingStrategy, GameController gameController) {
        this.alliance = alliance;
        this.movePickingStrategy = movePickingStrategy;
        this.gameController = gameController;
    }


    @Override
    public void performNextTurn(Board board) {
        List<Move> possibleMoves = board.alliancePieces.get(alliance)
                .stream()
                .map(piece -> piece.getAllPossibleMoves(board))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if(possibleMoves.isEmpty())
            return;
        Move chosenMove = movePickingStrategy.getMove(possibleMoves);
        gameController.movePiece(chosenMove, false);
    }
}
