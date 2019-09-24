package game;

public class Tile {

    private Piece occupant;
    private boolean isOccupied;

    public void setOccupant(Piece occupant) {
        this.occupant = occupant;
    }

    public Piece getOccupant() {
        return occupant;
    }


}
