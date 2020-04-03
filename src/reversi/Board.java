package reversi;

import java.awt.Point;
import java.util.ArrayList;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import reversi.games.Game;


public class Board {

    public ServerConnection sc;

    public final Cell[][] grid;

    protected ArrayList<Player> players; //index 0 = ALWAYS userPlayer & Index 1 = ALWAYS oppenentPlayer

    protected Game currentGame;

    public Board(GridPane gameTable, Game game, ArrayList<Player> players) {
        this.players = players;
        this.sc = game.sc;
        this.currentGame = game;
        this.grid = new Cell[Settings.TILESX][Settings.TILESY];

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
                grid[x][y] = new Cell(gameTable, point, null, this);


                // Kijkt of het paneel een spawnpoint is en zet daar een pion neer
                if (Settings.SPAWNPOINTS.containsKey(point)) {
                    setMove(point, (Player) game.getPlayerById(Settings.SPAWNPOINTS.get(point)), false, true);
                }

            }


        }
    }

    @SuppressWarnings("Duplicates")
    public void setMove(Point pos, Player player, boolean update, boolean begin) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= Settings.TILESX || pos.y >= Settings.TILESY) {
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
        return rowIndex * Settings.TILESY + colIndex;
    }

}
