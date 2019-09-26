package game;

public class Move {

    private int x;
    private int y;
    private boolean isAttacking;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Move(int x, int y, boolean isAttacking){
        this.x=x;
        this.y=y;
        this.isAttacking=isAttacking;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

}
