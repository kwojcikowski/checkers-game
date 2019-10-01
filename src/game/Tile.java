package game;

import pieces.*;

import java.util.Stack;

public class Tile {

    private Piece occupant;
    private boolean isOccupied;
    private Stack<Piece> history;

    public void updateHistory(Piece piece){
        history.push(piece);
    }

    public void setOccupant(Piece occupant) {
        this.occupant = occupant;
        isOccupied = true;
    }

    public Piece getOccupant() {
        return occupant;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
        if(!occupied){
            occupant = null;
        }
    }
}
