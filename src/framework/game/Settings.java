package Framework.game;

import Framework.players.Player;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import Framework.game.Board;
import Framework.game.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Settings {
    public static String host = "localhost";
    public static int port = 7789;

    public final static int MULTIPLAYER = 0;
    public final static int EASY = 1;
    public final static int HARD = 2;

    public static int PLAYER1 = 0;
    public static int PLAYER2 = 1;

    public static int TILESX;
    public static int TILESY;

    public static int WIDTH;
    public static int HEIGHT;

    public static String PLAYERNAME = "default";
    public static String GAMENAME;

    public boolean showScore = true;

    public static HashMap<Point, Integer> SPAWNPOINTS;

    public Settings(HashMap<Point, Integer> sp, int x, int y,int width, int height) {
        SPAWNPOINTS = sp;
        TILESX = x;
        TILESY = y;
        WIDTH = width;
        HEIGHT = height;
    }

    private void setShowScore(boolean bool) {
        this.showScore = bool;
    }

    public abstract boolean putPiece(Cell cell, Board board, int direction, int x, int y, Player getter, boolean check);

    public abstract HashMap<Point, Integer> checkForMoves(Player player, Board board);

    public abstract int checkGain(Board board, int direction, int x, int y, Player getter, boolean end);

    public abstract Node getPiece(int id);

    public static String getPath(String gName){
        String name = "";
        String[] words = gName.split("[_-]");

        for(String sWord : words){
            name += sWord.substring(0, 1).toUpperCase();
            name += sWord.substring(1);
        }

        return name;
    }

    public String getPath(){
        return this.getPath(GAMENAME);
    }
}
