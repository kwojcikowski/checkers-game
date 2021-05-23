package com.checkers.engine;

import com.checkers.engine.board.Tile;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.controllers.GameController;
import com.checkers.engine.pieces.Piece;

import java.util.*;

public class AIGame extends Game {

    private HashMap<Piece, List<Move>> movesForPiece;
    private HashMap<Piece, List<Move>> capturingMovesForPiece;
    private GameController controller;
    private boolean lockAI;
    private Random randomGenerator = new Random();

    public AIGame(Game game, GameController controller){
        super(game);
        this.controller = controller;
        movesForPiece = new HashMap<>();
        capturingMovesForPiece = new HashMap<>();
    }

    private void loadPiecesIntoMap(){
        movesForPiece.clear();
        Tile[][] tiles = getBoard().getTiles();
        for(Tile[] row : tiles)
            for(Tile tile : row)
                if(tile.isOccupied() && tile.getOccupant().getPieceAlliance() == Alliance.BLACK)
                    movesForPiece.put(tile.getOccupant(), new LinkedList<>());
    }

    @Override
    public void alternateTurn() {
        super.alternateTurn();
        if(getTurn() == Alliance.BLACK && !isEndOfGame()){
            loadPiecesIntoMap();
            computePossibleMoves();
            Piece randomlySelectedPiece;
            List<Move> consideredMoves;
            if(noAttacksAreAvailable()){
                randomlySelectedPiece = takeRandomMovablePiece();
                consideredMoves = movesForPiece.get(randomlySelectedPiece);
                performRandomMove(randomlySelectedPiece, consideredMoves);
            }else{
                randomlySelectedPiece = takeRandomAttackingPiece();
                performRandomAttackingMove(randomlySelectedPiece);
            }
        }
    }

    private void computePossibleMoves(){
        capturingMovesForPiece.clear();
        for(Piece piece : movesForPiece.keySet()) {
            List<Move> availableMoves = piece.checkForPossibleMoves(getBoard(), true, false);
            movesForPiece.replace(piece, availableMoves);
            List<Move> attackingMoves = extractAttackingMoves(availableMoves);
            if(!attackingMoves.isEmpty())
                capturingMovesForPiece.put(piece, attackingMoves);
        }
    }

    private boolean noAttacksAreAvailable(){
        return capturingMovesForPiece.isEmpty();
    }

    private Piece takeRandomAttackingPiece(){
        Object[] keys = capturingMovesForPiece.keySet().toArray();
        return (Piece) keys[randomGenerator.nextInt(keys.length)];
    }

    private Piece takeRandomMovablePiece(){
        Object[] keys = movesForPiece.keySet().toArray();
        Piece selectedPiece = (Piece) keys[randomGenerator.nextInt(keys.length)];
        while (movesForPiece.get(selectedPiece).isEmpty()){
            selectedPiece = (Piece) keys[randomGenerator.nextInt(keys.length)];
        }
        return selectedPiece;
    }

    private void performRandomMove(Piece movedPiece, List<Move> consideredMoves){
        int randomIndex = randomGenerator.nextInt(consideredMoves.size());
//        controller.movePiece(movedPiece.getCoords(), consideredMoves.get(randomIndex));
    }

    public void performRandomAttackingMove(Piece piece){
        List<Move> consideredMoves = piece.checkForPossibleMoves(getBoard(), false, false);
        if(!consideredMoves.isEmpty()) {
            int randomIndex = randomGenerator.nextInt(consideredMoves.size());
//            controller.movePiece(piece.getCoords(), consideredMoves.get(randomIndex));
        }
    }

    private List<Move> extractAttackingMoves(List<Move> moves){
        List<Move> attackingMoves = new LinkedList<>();
        for(Move move : moves)
            if(move.isCapturing())
                attackingMoves.add(move);
        return attackingMoves;
    }
}
