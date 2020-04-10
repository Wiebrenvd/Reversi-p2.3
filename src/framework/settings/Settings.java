package framework.settings;

import framework.actors.Player;
import javafx.scene.paint.Color;
import framework.boards.Board;
import framework.cells.Cell;

import java.awt.*;
import java.util.HashMap;

public abstract class Settings {
    public static String host = "localhost";
    public static int port = 7789;

    public final static int MULTIPLAYER = 0;
    public final static int EASY = 1;
    public final static int HARD = 2;

    public static int PLAYER1 = 0;
    public static int PLAYER2 = 1;

    public static Color PLAYER1COLOR = Color.BLACK;
    public static Color PLAYER2COLOR = Color.WHITE;

    public static int TILESX;
    public static int TILESY;

    public static int WIDTH;
    public static int HEIGHT;

    public static String PLAYERNAME = "default";
    public static String GAMENAME = "default";

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
}
