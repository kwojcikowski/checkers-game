package game;

import java.util.LinkedList;

public class Move {

    private int x;
    private int y;
    private boolean isAttacking;

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
    public Move(int x, int y, boolean isAttacking, boolean isAvailableNow){
        this.x=x;
        this.y=y;
        this.isAttacking=isAttacking;
        this.isAvailableNow=isAvailableNow;
    }

    public boolean isAttacking() {
        return isAttacking;
    }
}
