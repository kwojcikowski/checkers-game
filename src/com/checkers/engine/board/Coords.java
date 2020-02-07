package com.checkers.engine.board;

public class Coords {

    public final int x;
    public final int y;

    private Coords(int x, int y){
        this.x=x;
        this.y=y;
    }

    public static Coords at(int x, int y){
        return new Coords(x, y);
    }

    public static Coords plus(int x, int y, Coords coords){
        return new Coords(coords.x+x, coords.y+y);
    }

}