package reversi.cells;

import java.awt.Point;

import framework.actors.Player;
import javafx.scene.layout.GridPane;
import reversi.boards.Board;

public class AICell extends Cell {
    public AICell(GridPane gameTable, Point point, Player player, Board board) {
        super(point, player, board);
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }
}
