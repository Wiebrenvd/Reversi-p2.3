package Reversi;

import java.awt.*;
import java.util.HashMap;

import Framework.players.Player;
import Framework.game.Board;
import Framework.game.Cell;
import Framework.game.Settings;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameSettings extends Settings {
    public static int tilex = 8;
    public static int tiley = 8;

    public static int playerSize = 24;

    public static HashMap<Point, Integer> sp = new HashMap<>() {{
        put(new Point(3, 3), PLAYER2);
        put(new Point(4, 3), PLAYER1);
        put(new Point(3, 4), PLAYER1);
        put(new Point(4, 4), PLAYER2);
    }};

    public static javafx.scene.paint.Color[] playerColors = new javafx.scene.paint.Color[]{ javafx.scene.paint.Color.BLACK, Color.WHITE };

    public GameSettings() {
        super(sp, tilex, tiley,40,40);
    }


    @Override
    public boolean putPiece(Cell cell, Board board, int direction, int x, int y, Player getter, boolean check) {
        if (board.grid[x][y].getPlayer()!= null && direction==4) return false;
        if (!check) cell.setPlayer(getter);
        int counter = 0;
        boolean changable = false;
        for (int s = y-1; s < y+2; s++){
            for (int z = x-1; z < x+2; z++){
                if (s >= 0 && z >= 0 && s < TILESX && z < TILESY && counter != 4) {
                    Player tmpPlayer = board.grid[z][s].getPlayer();
                    Cell tmpCell = board.grid[z][s];
                    if (direction==4 && tmpPlayer != null){
                        if (!tmpPlayer.equals(getter) && putPiece(cell,board,counter,z,s,getter,true)){
                            changable = true;
                            if (!check) {
                                tmpCell.setPlayer(getter);
                                putPiece(cell,board,counter,z,s,getter,false);
                            }
                        }
                    } else if (counter==direction && tmpPlayer != null){
                        if (tmpPlayer.equals(getter)){
                            return true;
                        } else{
                            if (check) return putPiece(cell,board,counter,z,s,getter,true);
                            else {
                                tmpCell.setPlayer(getter);
                                putPiece(cell,board,counter,z,s,getter,false);
                            }
                        }
                    }
                }
                counter++;
            }
        }
        return changable;
    }

    public int checkGain(Board board, int direction, int x, int y, Player getter, boolean end){
        if (board.grid[x][y].getPlayer()!= null && direction==4) return 0;
        int counter = 0;
        int gain = 0;
        for (int s = y-1; s < y+2; s++){
            for (int z = x-1; z < x+2; z++){
                if (s >= 0 && z >= 0 && s < TILESX && z < TILESY && counter != 4) {
                    Player tmpPlayer = board.grid[z][s].getPlayer();
                    Cell tmpCell = board.grid[z][s];
                    if (direction==4 && tmpPlayer != null){
                        if (!tmpPlayer.equals(getter) && 0 != checkGain(board,counter,z,s,getter,end)){
                            gain += checkGain(board,counter,z,s,getter,end);
                        }
                    } else if (counter==direction && tmpPlayer != null){
                        if (tmpPlayer.equals(getter)){
                            return 1;
                        } else{
                            if (checkGain(board,counter,z,s,getter,end) != 0) return 1 + checkGain(board,counter,z,s,getter,end);
                        }
                    }
                }
                counter++;
            }
        }
        if (gain > 0) gain++;
        return gain;
    }

    @Override
    public Node getPiece(int playerId) {
        if(playerId >= this.playerColors.length)
            return null;

        Color color = this.playerColors[playerId];
        Circle circle = new Circle(0,0,this.playerSize/2);

        circle.setFill(color);
        circle.setStroke(Color.BLACK);

        return circle;
    }

    @Override
    public HashMap<Point, Integer> checkForMoves(Player player, Board board) {
        Cell[][] grid = board.grid;
        HashMap<Point,Integer> moves = new HashMap<>();
        boolean end = true;
        for(int i =0;i<grid.length;i++){
            for(int j =0;j<grid[i].length;j++) {
                Player tmpPlayer = grid[i][j].getPlayer();
                if(tmpPlayer == null) {
                    Point tmpPoint = new Point(i,j);
                    int gain = checkGain(board,4,tmpPoint.x,tmpPoint.y,player,false);
                    if (gain != 0) moves.put(tmpPoint,gain);
                    end = false;
                }
            }
        }
        if (end) return null;
        return moves;
    }
}
