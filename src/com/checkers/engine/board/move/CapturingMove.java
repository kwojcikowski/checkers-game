package com.checkers.engine.board.move;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class CapturingMove extends Move {

    private final Coords attackedPieceCoords;
    private CapturingMove nextMove;

    private CapturingMove(Piece piece, Coords destinationCoords,
                          boolean isAvailableNow, Coords attackedPieceCoords, Board boardState) {
        super(piece, destinationCoords, isAvailableNow, boardState);
        this.attackedPieceCoords = attackedPieceCoords;
    }

    private CapturingMove(Piece piece, Coords destinationCoords,
                         boolean isAvailableNow, Coords attackedPieceCoords,
                         CapturingMove nextMove, Board boardState) {
        this(piece, destinationCoords, isAvailableNow, attackedPieceCoords, boardState);
        this.nextMove = nextMove;
    }

    public static CapturingMove to(Piece piece, Coords destination,
                                   boolean isAvailableNow, Coords attackedPieceCoords,
                                   CapturingMove nextMove, Board boardState) {
        return new CapturingMove(piece, destination, isAvailableNow, attackedPieceCoords, nextMove, boardState);
    }

    public Coords getAttackedPieceCoords() {
        return attackedPieceCoords;
    }

    public CapturingMove getLastCapturingMove() {
        return nextMove == null ? this : nextMove.getLastCapturingMove();
    }

    public void addLastMove(CapturingMove move) {
        if(nextMove == null)
            nextMove = move;
        else
            nextMove.addLastMove(move);
    }

    @Override
    public boolean isCapturing() {
        return true;
    }

    @Override
    public boolean hasNextMove() {
        return this.nextMove != null;
    }

    public CapturingMove getNextMove() {
        return nextMove;
    }

    public CapturingMove copy() throws CloneNotSupportedException {
        CapturingMove rootCopy = unlinkedCopy();
        List<CapturingMove> links = new ArrayList<>();
        CapturingMove next = nextMove;
        while(next != null) {
            links.add(next.unlinkedCopy());
            next = next.nextMove;
        }
        next = rootCopy;
        for(int i = 0; i < links.size(); i++) {
            next.nextMove = links.get(i);
            next = next.nextMove;
        }
        return rootCopy;
    }

    private CapturingMove unlinkedCopy() throws CloneNotSupportedException {
        Piece clonePieced = piece.clone();
        Coords copiedDestCoords = Coords.at(destinationCoords.x, destinationCoords.y);
        Board copiedBoardState = Board.copyOf(getBoardState());
        Coords attackedPieceCoords = Coords.at(getAttackedPieceCoords().x, getAttackedPieceCoords().y);
        return new CapturingMove(clonePieced, copiedDestCoords, isAvailableNow(), attackedPieceCoords, null, copiedBoardState);
    }

    @Override
    public String toString() {
        String s = nextMove == null ? "" : nextMove.toString();
        return "(" + destinationCoords.x + ", " + destinationCoords.y + ") -> " + s;
    }
}
