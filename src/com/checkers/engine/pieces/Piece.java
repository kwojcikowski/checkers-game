package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.BlackTile;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.move.CapturingMove;
import com.checkers.engine.board.move.Move;

import java.util.List;

public abstract class Piece {

    protected final Alliance pieceAlliance;
    protected BlackTile occupiedTile;

    Piece(Alliance pieceAlliance, BlackTile occupiedTile){
        this.pieceAlliance=pieceAlliance;
        this.occupiedTile = occupiedTile;
    }

    final public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    final public BlackTile getOccupiedTile() {
        return occupiedTile;
    }

    final public void setOccupiedTile(BlackTile occupiedTile) {
        this.occupiedTile = occupiedTile;
    }

    final public Coords getCoords(){
        return this.occupiedTile.getCoords();
    }

    final public boolean areEnemies(Piece other){
        return this.pieceAlliance!=other.pieceAlliance;
    }

    public void moveTo(BlackTile destination){
        occupiedTile.freeUp();
        occupiedTile = destination;
        occupiedTile.setOccupant(this);
    }

    public void takeDown(){
        occupiedTile.freeUp();
        occupiedTile = null;
        //TODO cemetery?
    }

    public List<Move> getAllPossibleMoves(Board board) {
        List<Move> possibleMoves = checkForPossibleMoves(board, true, false);

        for (int i = 0; i < possibleMoves.size(); i++) {
            Move m = possibleMoves.get(i);
            if(m.isCapturing()){
                CapturingMove lastMove = ((CapturingMove) m).getLastCapturingMove();
                Piece movingPiece = lastMove.getResultingPiece();

                if(board.isEligibleForPromotion(movingPiece))
                    movingPiece = ((Pawn) movingPiece).promote();
                List<Move> furtherMoves = movingPiece.checkForPossibleMoves(lastMove.getBoardState(), false, false);

                while(!furtherMoves.isEmpty()) {
                    for(int j = 1; j < furtherMoves.size(); j++) {
                        CapturingMove copiedMoveSequence = ((CapturingMove) m).copy();
                        copiedMoveSequence.addLastMove((CapturingMove) furtherMoves.get(j));
                        possibleMoves.add(copiedMoveSequence);
                    }
                    ((CapturingMove) m).addLastMove((CapturingMove) furtherMoves.get(0));

                    lastMove = (CapturingMove) furtherMoves.get(0);
                    movingPiece = lastMove.getResultingPiece();
                    furtherMoves = movingPiece.checkForPossibleMoves(lastMove.getBoardState(), false, false);
                }
            }
        }

        return possibleMoves;
    }

    public abstract List<Move> checkForPossibleMoves(Board board,
                                                     boolean isAvailableDirectly,
                                                     boolean recursive);

    public List<Move> checkForPossibleMoves(Board board,
                                            boolean isAvailableDirectly) {
        return checkForPossibleMoves(board, isAvailableDirectly, true);
    }

    public abstract Piece deepClone();

}
