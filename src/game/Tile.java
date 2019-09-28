package game;

import pieces.Piece;

public class Tile {

    private Piece occupant;
    private boolean isOccupied;

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
