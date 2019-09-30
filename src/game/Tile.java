package game;

import pieces.Piece;

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
        //edited by kwojcikowski
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
        //edit by kwojcikowski
        if(!occupied){
            occupant = null;
        }
        //end
    }


}
