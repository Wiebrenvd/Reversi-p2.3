package framework.settings;

import framework.actors.Player;
import framework.boards.Board;
import framework.cells.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TicTacToeSettings extends Settings {
    public static int tilex = 3;
    public static int tiley = 3;

    private static String goodMovesStr = "0;0,0;2,2;2,2;0,1;1";
    private ArrayList<Point> goodMoves;

    public TicTacToeSettings() {
        super(null, tilex, tiley,83,83);
        showScore = false;
        goodMoves = new ArrayList<>();
        for (String move : goodMovesStr.split(",")){
            int x = Integer.parseInt(move.split(";")[0]);
            int y = Integer.parseInt(move.split(";")[1]);
            goodMoves.add(new Point(x,y));
        }
    }


    @Override
    public boolean putPiece(Cell cell, Board board, int direction, int x, int y, Player getter, boolean check) {
        if (cell.getPlayer()== null) {
            if (!check) cell.setPlayer(getter);
            return true;
        }
        return false;
    }

    @Override
    public HashMap<Point, Integer> checkForMoves(Player player, Board board) {
        System.out.println("-----------------------------------------------");
        Cell[][] grid = board.grid;
        HashMap<Point,Integer> moves = new HashMap<>();
        //check
        for(int i =0;i<grid.length;i++){
            for(int j =0;j<grid[i].length;j++) {
                System.out.println("------ Cell "+i+","+j+" ------ ");
                Player tmpPlayer = grid[i][j].getPlayer();
                if(tmpPlayer == null) {
                    Point tmpPoint = new Point(i,j);
                    int gain = checkGain(board,4,i,j,player,false);
                    System.out.println("gain "+i+","+j+": "+gain);
                    moves.put(tmpPoint,gain);
                } else{
                    System.out.println(tmpPlayer.getName()+" Noooo "+i+","+j+" : "+checkGain(board,4,i,j,tmpPlayer,true));
                    if (checkGain(board,4,i,j,tmpPlayer,true)>=11) return null;
                }
            }
        }
        if (moves.containsKey(new Point(1,1))) moves.replace(new Point(1,1),10);
//        System.out.println(moves.toString());
        return moves;
    }

    @Override
    public int checkGain(Board board, int direction, int x, int y, Player getter, boolean end) {
        if ((board.grid[x][y].getPlayer()!= null && direction==4) && !end) return 0;
        int counter = 0;
        int gain = 0;

        for (int s = y-1; s < y+2; s++){
            for (int z = x-1; z < x+2; z++){
                if (s >= 0 && z >= 0 && s < TILESX && z < TILESY && counter != 4) {
                    Point tmpPoint = new Point(x,y);
                    Player tmpPlayer = board.grid[z][s].getPlayer();
                    if (direction==4 && tmpPlayer != null){
                        if (!goodMoves.contains(tmpPoint)){
                            Point tmpPoint1 = new Point(z,s);
                            if (!goodMoves.contains(tmpPoint1)) continue;
                        }

                        System.out.println("Counter is " + counter);
                        System.out.println("name: "+tmpPlayer.getName());
                        if (tmpPlayer.equals(getter)){
                            gain += 1 + checkGain(board,counter,z,s,getter,end);
                        } else {
                            if (!end) gain += 2 + checkGain(board,counter,z,s,tmpPlayer,end);
                        }

                    } else if (counter==direction && tmpPlayer != null){
                        if (tmpPlayer.equals(getter)){
                            return 10;
                        }
                    }
                }
                counter++;
            }
        }
        return gain;
    }
}
