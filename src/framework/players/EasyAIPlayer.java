package framework.players;

import framework.actors.Player;

import java.awt.*;
import java.util.Random;

public class EasyAIPlayer extends Player {
    private int counter;
    private int delay;
    public EasyAIPlayer(String name) {
        super(name);
        counter = 0;
        delay = 3;
    }

    @Override
    public Point doMove(){
        int highest = 0;
        Point output = null;
        try {
            Thread.sleep(25);
            if (counter == delay && playersTurn && possibleMoves != null && possibleMoves.size()>0){
                for (Point p : possibleMoveGain.keySet()){
                    if (possibleMoveGain.get(p) >= highest){
                        output = p;
                        highest = possibleMoveGain.get(p);
                    }
                }
//                int move = new Random().nextInt(possibleMoves.size());
//                output = possibleMoves.get(move);
                counter = 0;
            }
        }catch (InterruptedException e){
            System.out.println("Could't rest");
        }
        counter++;
        return output;
    }
}
