package framework.boards;

import java.awt.Point;
import java.util.ArrayList;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import framework.cells.Cell;
import framework.games.Game;


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
        this.grid = new Cell[currentGame.settings.TILESX][currentGame.settings.TILESY];

        RowConstraints rowConstraints;
        ColumnConstraints colConstraints;

        // Maakt alle kolommen
        for (int i = 0; i < currentGame.settings.TILESX; i++) {
            if (gameTable.getColumnConstraints().size()< currentGame.settings.TILESX) {
                colConstraints = new ColumnConstraints();
                colConstraints.setMinWidth(currentGame.settings.WIDTH);
                colConstraints.setMaxWidth(currentGame.settings.WIDTH);
                gameTable.getColumnConstraints().add(colConstraints);
            }
        }

        // Maakt alle rows
        for (int i = 0; i < currentGame.settings.TILESY; i++) {
            if (gameTable.getRowConstraints().size()< currentGame.settings.TILESY) {
                rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(currentGame.settings.HEIGHT);
                rowConstraints.setMaxHeight(currentGame.settings.HEIGHT);
                gameTable.getRowConstraints().add(rowConstraints);
            }
        }

        // Voegt een paneel in elke cell die klikbaar is
        for (int x = 0; x < currentGame.settings.TILESX; x++) {
            for (int y = 0; y < currentGame.settings.TILESY; y++) {
                Point point = new Point(x, y);
                grid[x][y] = new Cell(gameTable, point, null, this);


                // Kijkt of het paneel een spawnpoint is en zet daar een pion neer
                if (currentGame.settings.SPAWNPOINTS != null) {
                    if (currentGame.settings.SPAWNPOINTS.containsKey(point)) {
                        setMove(point, game.getPlayerById(currentGame.settings.SPAWNPOINTS.get(point)), false, true, false);
                    }
                }
            }
        }

        gameTable.getStyleClass().add("grid");
    }

    @SuppressWarnings("Duplicates")
    public boolean setMove(Point pos, Player player, boolean check, boolean begin, boolean toServer) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= currentGame.settings.TILESX || pos.y >= currentGame.settings.TILESY) {
            System.out.printf("position: [%d,%d] is outside the board!", pos.x, pos.y);
            return false;
        }

        if (check) {
            return grid[pos.x][pos.y].putPiece(this,4,pos.x,pos.y,player,true);

        } else if (!check && !begin) {
            boolean done = grid[pos.x][pos.y].putPiece(this,4,pos.x,pos.y,player,false);
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
        return rowIndex * currentGame.settings.TILESY + colIndex;
    }

}