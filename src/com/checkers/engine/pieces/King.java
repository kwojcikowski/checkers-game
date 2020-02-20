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

    public static King from(final Pawn pawn) {
        King king = new King(pawn.pieceAlliance, pawn.getOccupied());
        pawn.getOccupied().setOccupant(king);
        return king;
    }

    @Override
    public List<Move> checkForPossibleMoves(final Board board, final boolean isAvailableDirectly, final boolean recursive) {
        List<Move> moves = new LinkedList<>();
        Tile[][] tiles = board.getTiles();
        for(int horizontalShift = -1; horizontalShift < 2; horizontalShift += 2){
            for(int verticalShift = -1; verticalShift < 2; verticalShift += 2){
                int x = coords.x + horizontalShift;
                int y = coords.y + verticalShift;
                try{
                    while(tiles[x][y].isFree()){
                        if (isAvailableDirectly)
                            moves.add(Move.to(Coords.at(x, y), true));
                        x += horizontalShift;
                        y += verticalShift;
                    }
                    Tile tileToJumpOver = tiles[x][y];
                    if(tileToJumpOver.isOccupied() && areEnemies(tileToJumpOver.getOccupant())){
                        x += horizontalShift;
                        y += verticalShift;
                        while(tiles[x][y].isFree()){
                            moves.add(
                                    CapturingMove.to(Coords.at(x, y),
                                            true, tileToJumpOver.coords)
                            );
                            x += horizontalShift;
                            y += verticalShift;
                            //TODO recursive call
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException ignore){}
            }
        }
        return moves;
    }

}
