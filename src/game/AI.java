package game;

import java.util.LinkedList;

public class AI {

    //to be improved of course
    public Move findBestMove(LinkedList<Move> moves){
        Move move=moves.pop();
        for(int i=0;i<Math.random()*moves.size();i++){
            move=moves.pop();
        }
        return move;
    }
}
