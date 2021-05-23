package com.checkers.engine;

public enum Alliance {
    WHITE, BLACK;

    public Alliance getOpposite() {
        return this == Alliance.WHITE ? Alliance.BLACK : Alliance.WHITE;
    }
}
