package com.checkers.engine.pieces;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.BlackTile;
import com.checkers.engine.board.Board;
import com.checkers.engine.board.Coords;
import com.checkers.engine.board.move.Move;
import com.checkers.engine.board.move.CapturingMove;
import com.checkers.engine.board.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {

    private final int direction;

    public Pawn(final Alliance alliance, BlackTile occupiedTile) {
        super(alliance, occupiedTile);
        direction = pieceAlliance==Alliance.WHITE? -1: 1;
    }

    King promote() {
        return King.promoteFrom(this);
    }

    public List<Move> getAllPossibleMoves(Board board) throws CloneNotSupportedException {
        List<Move> possibleMoves = checkForPossibleMoves(board, true, false);

        for (int i = 0; i < possibleMoves.size(); i++) {
            Move m = possibleMoves.get(i);
            if(m.isCapturing()){
                CapturingMove lastMove = ((CapturingMove) m).getLastCapturingMove();
                List<Move> furtherMoves = lastMove.piece.checkForPossibleMoves(lastMove.getBoardState(), false, false);

                while(!furtherMoves.isEmpty()) {
                    for(int j = 1; j < furtherMoves.size(); j++) {
                        CapturingMove copiedMoveSequence = ((CapturingMove) m).copy();
                        copiedMoveSequence.addLastMove((CapturingMove) furtherMoves.get(j));
                        possibleMoves.add(copiedMoveSequence);
                    }
                    ((CapturingMove) m).addLastMove((CapturingMove) furtherMoves.get(0));

                    lastMove = (CapturingMove) furtherMoves.get(0);
                    furtherMoves = lastMove.piece.checkForPossibleMoves(lastMove.getBoardState(), false, false);
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public List<Move> checkForPossibleMoves(final Board board, final boolean isDirect, final boolean recursive) {
        List<Move> moves = new LinkedList<>();
        Tile[][] tiles = board.getTiles();
        Coords coords = getCoords();
        for(int horizontalShift = -1; horizontalShift < 3; horizontalShift += 2){
            try{
                int x = coords.x+direction;
                int y = coords.y+horizontalShift;
                if(tiles[x][y].isFree() && isDirect){
                    Board alternativeBoard = Board.copyOf(board);
                    Tile[][] alternativeTiles = alternativeBoard.getTiles();
                    Piece movedPiece = alternativeTiles[occupiedTile.getCoords().x][occupiedTile.getCoords().y].getOccupant();
                    movedPiece.moveTo((BlackTile) alternativeTiles[x][y]);
                    moves.add(
                            Move.to(movedPiece, Coords.at(x, y), true, alternativeBoard)
                    );
                }
            }
            catch (ArrayIndexOutOfBoundsException ignore){}
            for(int verticalShift = 1; verticalShift > -2; verticalShift -= 2){
                try{
                    BlackTile tileToJumpOver = (BlackTile) tiles[coords.x+direction*verticalShift][coords.y+horizontalShift];
                    BlackTile candidateTile = (BlackTile) tiles[coords.x+2*direction*verticalShift][coords.y+2*horizontalShift];
                    if(tileToJumpOver.isOccupied() && areEnemies(tileToJumpOver.getOccupant())
                            && candidateTile.isFree()){
                        Board boardAfterMove = Board.copyOf(board);
                        Tile[][] tilesAfterMove = boardAfterMove.getTiles();
                        Piece alternativePiece = tilesAfterMove[coords.x][coords.y]
                                .getOccupant();
                        alternativePiece.moveTo((BlackTile) tilesAfterMove[candidateTile.getCoords().x][candidateTile.getCoords().y]);
                        tilesAfterMove[tileToJumpOver.getCoords().x][tileToJumpOver.getCoords().y].freeUp();

                        moves.add(CapturingMove.to(alternativePiece, candidateTile.getCoords(), isDirect, tileToJumpOver.getCoords(), null, boardAfterMove));
                        if(recursive) {
                            Board alternativeBoard = Board.copyOf(board);
                            Tile[][] alternativeTiles = alternativeBoard.getTiles();
                            alternativeTiles[tileToJumpOver.getCoords().x][tileToJumpOver.getCoords().y].freeUp();
                            alternativeTiles[coords.x][coords.y]
                                    .getOccupant()
                                    .moveTo((BlackTile) alternativeTiles[candidateTile.getCoords().x][candidateTile.getCoords().y]);
                            moves.addAll(alternativeTiles[candidateTile.getCoords().x][candidateTile.getCoords().y].getOccupant()
                                    .checkForPossibleMoves(alternativeBoard, false));
                        }
                    }
                }catch (ArrayIndexOutOfBoundsException ignore){}
            }
        }
        return moves;
    }

    @Override
    public Pawn clone() throws CloneNotSupportedException {
        return new Pawn(pieceAlliance, occupiedTile);
    }

}
