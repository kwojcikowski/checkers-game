package pieces;

import game.Move;
import game.Tile;

import java.util.LinkedList;

public abstract class Piece {

    private boolean isWhite;
    private Tile occupied;

    public boolean isWhite() {
        return isWhite;
    }

    public void setOccupied(Tile occupied) {
        this.occupied = occupied;
    }

    public Tile getOccupied() {
        return occupied;
    }

    public Piece(Piece piece){
        this.isWhite = piece.isWhite();
        this.occupied = piece.occupied;
    }

    public Piece(boolean isWhite){
        this.isWhite=isWhite;
    }

    public abstract LinkedList<Move> moveChecker(final int x,final int y, final Tile[][] tiles,
                                                 final boolean availableNow);


}
