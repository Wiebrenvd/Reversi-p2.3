package reversi.players;

import framework.actors.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class HardAIPlayer extends Player {
    private int counter;
    private int delay;
    private Point[] firstmoves = {new Point(0,0),new Point(7,7), new Point(0,7), new Point(7,0)};
    private String goodPoints = "0;2,1;2,2;2,2;1,2;0" +
                                "0;5,1;5,2;5,2;6,2;7" +
                                "5;7,5;6,5;5,6;5,7;5" +
                                "0;5,1;5,2;5,2;6,2;7";

    private String badPoints =  "0;1,1;1,1;0"+
                                "6;7,6;6,7;6"+
                                "0;6,1;6,1;7"+
                                "6;0,6;1,7;1";

    private ArrayList<Point> secondBest;
    private ArrayList<Point> badMoves;

    public HardAIPlayer(String name) {
        super(name);
        counter = 0;
        delay = 6;
        secondBest = new ArrayList<>();
        for (String point : goodPoints.split(",")){
            int x = Integer.parseInt(point.split(";")[0]);
            int y = Integer.parseInt(point.split(";")[1]);
            secondBest.add(new Point(x,y));
        }
        badMoves = new ArrayList<>();
        for (String point : badPoints.split(",")){
            int x = Integer.parseInt(point.split(";")[0]);
            int y = Integer.parseInt(point.split(";")[1]);
            badMoves.add(new Point(x,y));
        }
    }

    @Override
    public Point doMove(){
        Point output = null;
        try {
            Thread.sleep(500);
            if (counter == delay && playersTurn && possibleMoves != null && possibleMoves.size()>0){
                output = getBestMove(possibleMoves);
                counter = 0;
            }
        }catch (InterruptedException e){
            System.out.println("Could't rest");
        }
        counter++;
        return output;
    }

    public Point getBestMove(ArrayList<Point> arr){
        if (arr.size()==1) return arr.get(0);
        for (Point p : firstmoves){
            for (Point point : arr){
                if (point.equals(p)) return point;
            }
        }
        for (Point p : secondBest){
            for (Point point : arr){
                if (point.equals(p)) return point;
            }
        }
        ArrayList<Point> tmpArr = arr;
        tmpArr.removeAll(badMoves);
        if (tmpArr.size()<1) tmpArr = arr;
        int random = new Random().nextInt(tmpArr.size());

        return tmpArr.get(random);
    }
}
