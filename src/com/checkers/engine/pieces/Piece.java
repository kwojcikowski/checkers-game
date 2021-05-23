package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.BlackTile;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
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

    public abstract List<Move> getAllPossibleMoves(Board board) throws CloneNotSupportedException;

    public abstract List<Move> checkForPossibleMoves(Board board,
                                                     boolean isAvailableDirectly,
                                                     boolean recursive);

    public List<Move> checkForPossibleMoves(Board board,
                                            boolean isAvailableDirectly) {
        return checkForPossibleMoves(board, isAvailableDirectly, true);
    }

    @Override
    public abstract Piece clone() throws CloneNotSupportedException;

}
