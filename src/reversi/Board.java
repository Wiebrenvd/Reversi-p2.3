package reversi;

import java.awt.Point;

import framework.server.ServerConnection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class Board {

    private final Cell[][] grid;

    private ServerConnection sc;

    public Board(GridPane gameTable) {

        this.grid = new Cell[Settings.tilesX][Settings.tilesY];

        RowConstraints rowConstraints;
        ColumnConstraints colConstraints;

        // Maakt alle kolommen
        for (int i = 0; i < Settings.tilesX; i++) {
            colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(40);
            colConstraints.setMaxWidth(40);
            gameTable.getColumnConstraints().add(colConstraints);

        }

        // Maakt alle rows
        for (int i = 0; i < Settings.tilesY; i++) {
            rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(40);
            rowConstraints.setMaxHeight(40);
            gameTable.getRowConstraints().add(rowConstraints);
        }

        // Voegt een paneel in elke cell die klikbaar is
        // Kijkt daarna of de paneel een spawnpoint is en zet daar een pion neer
        for (int x = 0; x < Settings.tilesX; x++) {
            for (int y = 0; y < Settings.tilesY; y++) {
                Point point = new Point(x, y);
                grid[x][y] = new Cell(gameTable, point);


                if (Settings.SpawnPoints.containsKey(point))
                    setMove(point, Settings.SpawnPoints.get(point), false);
            }


        }
    }

    @SuppressWarnings("Duplicates")
    public void setMove(Point pos, int player, boolean update) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= Settings.tilesX || pos.y >= Settings.tilesY) {
            System.out.printf("position: [%d,%d] is outside the board!", pos.x, pos.y);
            return;
        }

        if (grid[pos.x][pos.y].getPlayer() != null) {
            System.out.printf("position: [%d,%d] is already taken!", pos.x, pos.y);
            return;
        }

        if (update) {
            sc.sendCommand("move " + getMoveParameter(pos.x, pos.y));
        }


        grid[pos.x][pos.y].putPiece();
//      grid[pos.x][pos.y].putPiece(player); // TODO


    }


    public int getMoveParameter(int colIndex, int rowIndex) {
        return rowIndex * Settings.tilesY + colIndex;
    }


}