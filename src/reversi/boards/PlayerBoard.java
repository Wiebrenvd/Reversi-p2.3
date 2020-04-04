package reversi.boards;

import java.awt.Point;

import framework.actors.Player;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import reversi.cells.PlayerCell;
import reversi.Settings;
import reversi.games.Game;


public class PlayerBoard extends Board {



    public PlayerBoard(GridPane gameTable, Game game) {
        super(game);




        this.grid = new PlayerCell[Settings.TILESX][Settings.TILESY];

        RowConstraints rowConstraints;
        ColumnConstraints colConstraints;

        // Maakt alle kolommen
        for (int i = 0; i < Settings.TILESX; i++) {
            colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(40);
            colConstraints.setMaxWidth(40);
            gameTable.getColumnConstraints().add(colConstraints);

        }

        // Maakt alle rows
        for (int i = 0; i < Settings.TILESY; i++) {
            rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(40);
            rowConstraints.setMaxHeight(40);
            gameTable.getRowConstraints().add(rowConstraints);
        }

        // Voegt een paneel in elke cell die klikbaar is
        for (int x = 0; x < Settings.TILESX; x++) {
            for (int y = 0; y < Settings.TILESY; y++) {
                Point point = new Point(x, y);
                grid[x][y] = new PlayerCell(gameTable, point, null, this);


                // Kijkt of het paneel een spawnpoint is en zet daar een pion neer
                if (Settings.SPAWNPOINTS.containsKey(point)) {
                    setMove(point, (Player) game.getPlayerById(Settings.SPAWNPOINTS.get(point)), false, true);
                }
            }
        }
    }








}
