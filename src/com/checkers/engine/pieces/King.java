package com.checkers.engine.pieces;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.board.move.CapturingMove;

import java.util.LinkedList;
import java.util.List;
import com.checkers.engine.Alliance;
import com.checkers.engine.board.Tile;

public class King extends Piece{

    public King(final Alliance alliance, Tile occupiedTile){
        super(alliance, occupiedTile);
    }

    protected static King from(final Pawn pawn) {
        return new King(pawn.pieceAlliance, pawn.getOccupied());
    }

    @Override
    public List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly) {
        List<Move> moves = new LinkedList<>();
        Tile[][] tiles = board.getTiles();
        for(int horizontalShift = -1; horizontalShift < 2; horizontalShift += 2){
            for(int verticalShift = -1; verticalShift < 2; verticalShift += 2){
                int distance = 1;
                int x = coords.x + horizontalShift + distance;
                int y = coords.y + verticalShift + distance;
                try{
                    while(tiles[x][y].isFree() && isAvailableDirectly){
                        moves.add(
                                Move.to(Coords.at(x, y), true)
                        );
                        x += distance;
                        y += distance;
                    }
                }catch(ArrayIndexOutOfBoundsException ignore){}
                try{
                    Tile tileToJumpOver = tiles[x][y];
                    if(tileToJumpOver.isOccupied() && areEnemies(tileToJumpOver.getOccupant())){
                        while(tiles[x+horizontalShift][y+verticalShift].isFree()){
                            moves.add(
                                    CapturingMove.to(Coords.at(x+horizontalShift, y+verticalShift),
                                            true, tileToJumpOver.coords)
                            );
                            //TODO recursive call
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException ignore){}
            }
        }
        return moves;
    }

}
