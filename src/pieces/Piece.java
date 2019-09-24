package pieces;

import game.Tile;

public class Piece{

    //Player owner;
    private Tile occupied;


    public void setOccupied(Tile occupied) {
        this.occupied = occupied;
    }

    public Tile getOccupied() {
        return occupied;
    }

}