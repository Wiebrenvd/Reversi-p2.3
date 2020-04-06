package reversi.boards;

import java.awt.Point;
import java.util.ArrayList;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import reversi.Settings;
import reversi.cells.Cell;
import reversi.games.Game;


public class Board {

    public ServerConnection sc;

    public final Cell[][] grid;

    public ArrayList<Player> players; //index 0 = ALWAYS userPlayer & Index 1 = ALWAYS oppenentPlayer

    public Game currentGame;

    protected GridPane gameTable;

    public Board(GridPane gameTable, Game game, ArrayList<Player> players) {
        this.players = players;
        this.sc = game.sc;
        this.currentGame = game;
        this.gameTable = gameTable;
        this.grid = new Cell[Settings.TILESX][Settings.TILESY];

        RowConstraints rowConstraints;
        ColumnConstraints colConstraints;

        // Maakt alle kolommen
        for (int i = 0; i < Settings.TILESX; i++) {
            if (gameTable.getColumnConstraints().size()<Settings.TILESX) {
                colConstraints = new ColumnConstraints();
                colConstraints.setMinWidth(40);
                colConstraints.setMaxWidth(40);
                gameTable.getColumnConstraints().add(colConstraints);
            }
        }

        // Maakt alle rows
        for (int i = 0; i < Settings.TILESY; i++) {
            if (gameTable.getRowConstraints().size()<Settings.TILESY) {
                rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(40);
                rowConstraints.setMaxHeight(40);
                gameTable.getRowConstraints().add(rowConstraints);
            }
        }

        // Voegt een paneel in elke cell die klikbaar is
        for (int x = 0; x < Settings.TILESX; x++) {
            for (int y = 0; y < Settings.TILESY; y++) {
                Point point = new Point(x, y);
                grid[x][y] = new Cell(gameTable, point, null, this);


                // Kijkt of het paneel een spawnpoint is en zet daar een pion neer
                if (Settings.SPAWNPOINTS.containsKey(point)) {
                    setMove(point, game.getPlayerById(Settings.SPAWNPOINTS.get(point)), false, true,false);
                }

            }
        }

        gameTable.getStyleClass().add("grid");
    }

    @SuppressWarnings("Duplicates")
    public boolean setMove(Point pos, Player player, boolean check, boolean begin, boolean toServer) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= Settings.TILESX || pos.y >= Settings.TILESY) {
            System.out.printf("position: [%d,%d] is outside the board!", pos.x, pos.y);
            return false;
        }

        if (check) {
            return grid[pos.x][pos.y].putPiece(4,pos.x,pos.y,player,true);

        } else if (!check && !begin) {
            boolean done = grid[pos.x][pos.y].putPiece(4,pos.x,pos.y,player,false);
            if (done){
                if (toServer) sc.sendCommand("move "+getMoveParameter(pos.x,pos.y));
                return done;
            }
        }

        if (begin) {
            grid[pos.x][pos.y].setPlayer(player);
        }

        return false;
    }




    public int getMoveParameter(int colIndex, int rowIndex) {
        return rowIndex * Settings.TILESY + colIndex;
    }

}