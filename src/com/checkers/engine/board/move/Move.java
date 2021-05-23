package com.checkers.engine.board.move;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.pieces.Piece;

public class Move {

    public final Piece piece;
    public final Coords destinationCoords;
    private final boolean availableNow;
    private final Board boardState;

    protected Move(final Piece piece, final Coords destinationCoords, final boolean isAvailableNow, final Board boardState){
        this.piece = piece;
        this.destinationCoords = destinationCoords;
        this.availableNow = isAvailableNow;
        this.boardState = boardState;
    }

    public static Move to(final Piece piece, final Coords destination, final boolean isAvailableNow, final Board boardState){
        return new Move(piece, destination, isAvailableNow, boardState);
    }

    public boolean isCapturing(){
        return false;
    }

    public boolean isAvailableNow() {
        return availableNow;
    }

    public boolean hasNextMove() {
        return false;
    }

    public Board getBoardState() {
        return boardState;
    }
}
