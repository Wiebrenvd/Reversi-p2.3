package reversi.cells;

import java.awt.Point;

import framework.actors.Player;
import framework.server.ServerConnection;
import reversi.Settings;
import reversi.boards.Board;

public abstract class Cell {

    protected Player player;

    protected Point point;

    protected ServerConnection sc;
    protected Board board;

    public Cell(Point point, Player player, Board board) {
        this.player = player;
        this.point = point;
        this.board = board;
        this.sc = board.sc;


    }

    public int getMoveParameter(int colIndex, int rowIndex) {
        return rowIndex * Settings.TILESY + colIndex;
    }


    public abstract void setPlayer(Player player);

    public Player getPlayer() {
        return this.player;
    }


    @SuppressWarnings("Duplicates")


    /**
     * -------------
     * | 0 | 1 | 2 |
     * -------------
     * | 3 | X | 5 |
     * -------------
     * | 6 | 7 | 8 |
     * -------------
     * This method will check if a turn is possible & if check=false, it will do the move.
     * @param direction see table above x -> number, will go that direction to check. if number=4, it will check if the cell is possible to click.
     * @param x Integer, give here the x-coordinates of point X.
     * @param y Integer, give here the y-coordinates of point X.
     * @param getter Color, give here the color of the Player, who has the turn.
     * @param check boolean, set true if you only want to check if move is possible.
     *
     * @return true if cell is possible to click
     */
    public boolean putPiece(int direction, int x, int y, Player getter, boolean check) {
        if (board.grid[x][y].getPlayer() != null && direction == 4) return false;
        if (!check) setPlayer(getter);
        int counter = 0;
        boolean changable = false;
        for (int s = y - 1; s < y + 2; s++) {
            for (int z = x - 1; z < x + 2; z++) {
                if (s >= 0 && z >= 0 && s < Settings.TILESX && z < Settings.TILESY && counter != 4) {
                    Player tmpPlayer = board.grid[z][s].getPlayer();
                    Cell tmpCell = board.grid[z][s];
                    if (direction == 4 && tmpPlayer != null) {
                        if (!tmpPlayer.equals(getter) && putPiece(counter, z, s, getter, true)) {
                            changable = true;
                            if (!check) {
                                tmpCell.setPlayer(getter);
                                putPiece(counter, z, s, getter, false);
                            }
                        }
                    } else if (counter == direction && tmpPlayer != null) {
                        if (tmpPlayer.equals(getter)) {
                            return true;
                        } else {
                            if (check) return putPiece(counter, z, s, getter, true);
                            else {
                                tmpCell.setPlayer(getter);
                                putPiece(counter, z, s, getter, false);
                            }
                        }
                    }
                }
                counter++;
            }
        }
        if (!changable) System.out.println("Move " + direction + " Not Available!");
        else System.out.println("Move " + direction + " is Available!");
        return changable;
    }


}
