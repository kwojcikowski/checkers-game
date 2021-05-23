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
                        if (isAvailableDirectly) {
                            Board alternativeBoard = Board.copyOf(board);
                            Tile[][] alternativeTiles = alternativeBoard.getTiles();
                            Piece movedPiece = alternativeTiles[occupiedTile.getCoords().x][occupiedTile.getCoords().y].getOccupant();
                            movedPiece.moveTo((BlackTile) alternativeTiles[x][y]);
                            moves.add(
                                    Move.to(this.deepClone(), Coords.at(x, y), true, alternativeBoard)
                            );
                        }

                        x += horizontalShift;
                        y += verticalShift;
                    }
                    BlackTile tileToJumpOver = (BlackTile) tiles[x][y];
                    if(tileToJumpOver.isOccupied() && areEnemies(tileToJumpOver.getOccupant())){
                        x += horizontalShift;
                        y += verticalShift;
                        while(tiles[x][y].isFree()){
                            BlackTile candidateTile = (BlackTile) tiles[x][y];
                            Board boardAfterMove = Board.copyOf(board);
                            Tile[][] tilesAfterMove = boardAfterMove.getTiles();
                            Piece alternativePiece = tilesAfterMove[coords.x][coords.y].getOccupant();
                            alternativePiece.moveTo((BlackTile) tilesAfterMove[candidateTile.getCoords().x][candidateTile.getCoords().y]);
                            tilesAfterMove[tileToJumpOver.getCoords().x][tileToJumpOver.getCoords().y].freeUp();

                            moves.add(
                                    CapturingMove.to(this.deepClone(), Coords.at(x, y), false, tileToJumpOver.getCoords(), null, boardAfterMove)
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

    @Override
    public King deepClone() {
        return new King(pieceAlliance, occupiedTile);
    }

}
