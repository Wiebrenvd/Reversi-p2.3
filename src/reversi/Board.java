package reversi;

import java.awt.Point;
import java.util.ArrayList;

import framework.server.ServerConnection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class Board {

    public ServerConnection sc;

    protected final Cell[][] grid;

    protected ArrayList<ReversiPlayer> players; //index 0 = ALWAYS userPlayer & Index 1 = ALWAYS oppenentPlayer

    protected Game currentGame;

    public Board(GridPane gameTable, Game game, ArrayList<ReversiPlayer> players) {
        this.players = players;
        this.sc = game.sc;
        this.currentGame = game;
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
        for (int x = 0; x < Settings.tilesX; x++) {
            for (int y = 0; y < Settings.tilesY; y++) {
                Point point = new Point(x, y);
                grid[x][y] = new Cell(gameTable, point, null, this);


                // Kijkt of het paneel een spawnpoint is en zet daar een pion neer
                if (Settings.SpawnPoints.containsKey(point)) {
                    setMove(point, game.getPlayerById(Settings.SpawnPoints.get(point)), false, true);
                }

            }


        }
    }

    @SuppressWarnings("Duplicates")
    public void setMove(Point pos, ReversiPlayer player, boolean update, boolean begin) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= Settings.tilesX || pos.y >= Settings.tilesY) {
            System.out.printf("position: [%d,%d] is outside the board!", pos.x, pos.y);
            return;
        }

        //Steentje moet uiteindelijk van kleur veranderen
//        if (grid[pos.x][pos.y].getPlayer() != null) {
//            System.out.printf("position: [%d,%d] is already taken!", pos.x, pos.y);
//            return;
//        }

        if (update) {
            sc.sendCommand("move " + getMoveParameter(pos.x, pos.y));

        }

        if (!begin) {
            grid[pos.x][pos.y].putPiece(4,pos.x,pos.y,player,false);
            return;
        }

        grid[pos.x][pos.y].setPlayer(player);

    }




    public int getMoveParameter(int colIndex, int rowIndex) {
        return rowIndex * Settings.tilesY + colIndex;
    }

}
