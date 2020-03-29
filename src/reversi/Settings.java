package reversi;

import java.awt.*;
import java.util.HashMap;
import javafx.scene.paint.Color;

public class Settings {
    public static int PLAYER1 = 0;
    public static int PLAYER2 = 1;

    public static int tilesX = 8;
    public static int tilesY = 8;

    public static double boardWidth = 800f;
    public static double boardHeight = 600f;

    public static Color[] PlayerColors = { Color.BLACK, Color.WHITE };

    public static Color Player1Color = Color.BLACK;
    public static Color Player2Color = Color.WHITE;

    public static double PlayerSize = 24.0;


    public static HashMap<Point, Integer> SpawnPoints = new HashMap<Point, Integer>() {{
        put(new Point(3, 3), PLAYER1);
        put(new Point(4, 3), PLAYER2);
        put(new Point(3, 4), PLAYER2);
        put(new Point(4, 4), PLAYER1);
    }};
}
