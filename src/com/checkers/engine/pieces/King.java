package com.checkers.engine.pieces;

import com.checkers.engine.board.BlackTile;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.board.move.CapturingMove;

import java.util.LinkedList;
import java.util.List;
import com.checkers.engine.Alliance;
import com.checkers.engine.board.Tile;

public class King extends Piece{

    private King(final Alliance alliance, BlackTile occupiedTile){
        super(alliance, occupiedTile);
    }

    @Override
    public List<Move> getAllPossibleMoves(Board board) throws CloneNotSupportedException {
        return null;
    }

    public static King promoteFrom(Pawn pawn) {
        var king = new King(pawn.getPieceAlliance(), pawn.getOccupiedTile());
        pawn.getOccupiedTile().setOccupant(king);
        return king;
    }

    @Override
    public List<Move> checkForPossibleMoves(Board board,
                                            boolean isAvailableDirectly,
                                            boolean recursive) {
        List<Move> moves = new LinkedList<>();
        Tile[][] tiles = board.getTiles();
        for(int horizontalShift = -1; horizontalShift < 2; horizontalShift += 2){
            for(int verticalShift = -1; verticalShift < 2; verticalShift += 2){
                var coords = getCoords();
                int x = coords.x + horizontalShift;
                int y = coords.y + verticalShift;
                try{
                    while(tiles[x][y].isFree()){
//                        if (isAvailableDirectly)
//                            moves.add(Move.to(Coords.at(x, y), true));
                        x += horizontalShift;
                        y += verticalShift;
                    }
                    BlackTile tileToJumpOver = (BlackTile) tiles[x][y];
                    if(tileToJumpOver.isOccupied() && areEnemies(tileToJumpOver.getOccupant())){
                        x += horizontalShift;
                        y += verticalShift;
                        while(tiles[x][y].isFree()){
//                            moves.add(
//                                    CapturingMove.to(Coords.at(x, y),
//                                            true, tileToJumpOver.getCoords()));
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

    @Override
    public King clone() throws CloneNotSupportedException {
        return new King(pieceAlliance, occupiedTile);
    }

}
