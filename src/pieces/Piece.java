package pieces;
import game.Tile;
public class Piece{
    //Player owner;
    private boolean isWhite;
    private Tile occupied;

    public Piece(boolean isWhite){
        this.isWhite=isWhite;
    }

    public void setOccupied(Tile occupied) {
        this.occupied = occupied;
    }

    public Tile getOccupied() {
        return occupied;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
