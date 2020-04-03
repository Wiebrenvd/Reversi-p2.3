package reversi;

import java.awt.*;
import java.util.HashMap;
import javafx.scene.paint.Color;

public class Settings {

    public static String host = "localhost";
    public static int port = 7789;

    public final static int MULTIPLAYER = 0;
    public final static int EASY = 1;
    public final static int HARD = 2;

    public static int PLAYER1 = 0;
    public static int PLAYER2 = 1;

    public static int TILESX = 8;
    public static int TILESY = 8;

    public static Color PLAYER1COLOR = Color.BLACK;
    public static Color PLAYER2COLOR = Color.WHITE;


    public static HashMap<Point, Integer> SPAWNPOINTS = new HashMap<>() {{
        put(new Point(3, 3), PLAYER2);
        put(new Point(4, 3), PLAYER1);
        put(new Point(3, 4), PLAYER1);
        put(new Point(4, 4), PLAYER2);
    }};
}
