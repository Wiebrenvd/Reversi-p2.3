package reversi.boards;

import java.awt.Point;
import java.util.ArrayList;

import framework.actors.Player;
import framework.server.ServerConnection;
import reversi.cells.Cell;
import reversi.Settings;
import reversi.games.Game;

public abstract class Board {

    public ServerConnection sc;

    public Cell[][] grid;

    public Game game;


    public Board(Game game) {
        this.game = game;



    }

    public void setMove(Point pos, Player player, boolean update, boolean begin) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= Settings.TILESX || pos.y >= Settings.TILESY) {
            System.out.printf("position: [%d,%d] is outside the playerBoard!", pos.x, pos.y);
            return;
        }

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


    public ArrayList<Player> getPlayers() {
        return game.getPlayers();
    }
}
