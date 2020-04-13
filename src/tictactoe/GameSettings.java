package TicTacToe;

import Framework.players.Player;
import Framework.game.Board;
import Framework.game.Cell;
import Framework.game.Settings;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameSettings extends Settings {
    public static int tilex = 3;
    public static int tiley = 3;

    public static double playerSize = 64;

    private static String goodMovesStr = "0;0,0;2,2;2,2;0,1;1";
    private ArrayList<Point> goodMoves;

    public static String[] playerTypes = new String[]{"O", "X"};

    public GameSettings() {
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
        Cell[][] grid = board.grid;
        HashMap<Point,Integer> moves = new HashMap<>();
        //check
        for(int i =0;i<grid.length;i++){
            for(int j =0;j<grid[i].length;j++) {
                Player tmpPlayer = grid[i][j].getPlayer();
                if(tmpPlayer == null) {
                    Point tmpPoint = new Point(i,j);
                    int gain = checkGain(board,4,i,j,player,false);
                    moves.put(tmpPoint,gain);
                } else{
                    if (checkGain(board,4,i,j,tmpPlayer,true)>=14) return null;
                }
            }
        }
        if (moves.containsKey(new Point(1,1))) moves.replace(new Point(1,1),10);

        return moves;
    }

    @Override
    public int checkGain(Board board, int direction, int x, int y, Player getter, boolean end) {
        if ((board.grid[x][y].getPlayer()!= null && direction==4) && !end) return 0;
        int counter = 0;
        int gain = 0;
        int tmpGain = 0;
        int start = 0;
        Player actPlayer = board.grid[x][y].getPlayer();
        if (actPlayer!=null){
            if (actPlayer.equals(getter)) start = 2;
            else start = -2;
        }

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

                        if (tmpPlayer.equals(getter)){
                            tmpGain = start + 2 + checkGain(board,counter,z,s,getter,end);
                            if (tmpGain >= Math.abs(gain)) gain = tmpGain;
                        } else {
                            tmpGain = start -2 - checkGain(board,counter,z,s,tmpPlayer,end);
                            if (!end && Math.abs(tmpGain) >= Math.abs(gain)) gain = tmpGain;
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
        return Math.abs(gain);
    }

    @Override
    public Node getPiece(int playerId){
        if(playerId >= this.playerTypes.length)
            return null;

        String fName = this.playerTypes[playerId];

        ImageView fImage = new ImageView("TicTacToe/src/images/" + fName + ".png");

        fImage.setFitWidth(this.playerSize);
        fImage.setFitHeight(this.playerSize);

        return fImage;
    }
}
