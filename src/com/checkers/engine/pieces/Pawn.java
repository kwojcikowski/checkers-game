package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.board.move.CapturingMove;
import com.checkers.engine.board.Tile;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {

    private final int direction;

    public Pawn(final Alliance alliance, Tile occupiedTile) {
        super(alliance, occupiedTile);
        direction = pieceAlliance==Alliance.WHITE? -1: 1;
    }

    public static Pawn from(final Pawn pawn){
        return new Pawn(pawn.pieceAlliance, pawn.getOccupied());
    }

    King promote() {
        return King.from(this);
    }

    @Override
    public List<Move> checkForPossibleMoves(final Board board, final boolean isDirect) {
        List<Move> moves = new LinkedList<>();
        Tile[][] tiles = board.getTiles();
        for(int horizontalShift = -1; horizontalShift < 3; horizontalShift += 2){
            try{
                int x = coords.x+direction;
                int y = coords.y+horizontalShift;
                if(tiles[x][y].isFree() && isDirect){
                    moves.add(
                            Move.to(Coords.at(x, y), true)
                    );
                }
            }
            catch (ArrayIndexOutOfBoundsException ignore){}
            for(int verticalShift = 1; verticalShift > -2; verticalShift -= 2){
                try{
                    Tile tileToJumpOver = tiles[coords.x+direction*verticalShift][coords.y+horizontalShift];
                    Tile candidateTile = tiles[coords.x+2*direction*verticalShift][coords.y+2*horizontalShift];
                    if(tileToJumpOver.isOccupied() && areEnemies(tileToJumpOver.getOccupant())
                            && candidateTile.isFree()){
                        moves.add(CapturingMove.to(candidateTile.coords, isDirect, tileToJumpOver.coords));
                        //TODO consider if block - recursive?
                        Board alternativeBoard = Board.copyOf(board);
                        Tile[][] alternativeTiles = alternativeBoard.getTiles();
                        alternativeTiles[tileToJumpOver.coords.x][tileToJumpOver.coords.y].freeUp();
                        alternativeTiles[coords.x][coords.y].getOccupant()
                                .moveTo(alternativeTiles[candidateTile.coords.x][candidateTile.coords.y]);
                        moves.addAll(alternativeTiles[candidateTile.coords.x][candidateTile.coords.y].getOccupant()
                                .checkForPossibleMoves(alternativeBoard, false));
                    }
                }catch (ArrayIndexOutOfBoundsException ignore){}
            }
        }
        return moves;
    }
}
