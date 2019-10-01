package game;

import java.util.LinkedList;

public class Move {

    private int x;
    private int y;
    private boolean isAttacking;
    private boolean isAttached;

    public LinkedList<Move> getNext() {
        return next;
    }

    public void setNext(LinkedList<Move> next) {
        this.next = next;
    }

    private LinkedList<Move> next;

    public boolean isAvailableNow() {
        return isAvailableNow;
    }

    private boolean isAvailableNow;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Move(){

    }
    public Move(int x, int y, boolean isAttacking, boolean isAvailableNow, boolean isAttached){
        this.x=x;
        this.y=y;
        this.isAttacking=isAttacking;
        this.isAvailableNow=isAvailableNow;
        this.isAttached = isAttached;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean attached) {
        isAttached = attached;
    }
}
