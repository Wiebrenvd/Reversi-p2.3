package reversi.cells;

import java.awt.Point;

import framework.actors.Player;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import reversi.boards.Board;

public class PlayerCell extends Cell {

    private GridPane gameTable;

    private StackPane clickablePane;

    public PlayerCell(GridPane gameTable, Point point, Player player, Board board) {
        super(point, player, board);

        this.gameTable = gameTable;
        this.clickablePane = new StackPane();
        this.clickablePane.setAlignment(Pos.CENTER);
        this.clickablePane.setOnMouseClicked(e -> {
            int x = (int) point.getX();
            int y = (int) point.getY();
            System.out.printf("Mouse clicked cell [%d, %d]%n", x, y);

            if (board.getPlayers().get(0).isPlayersTurn() && putPiece(4, x, y, board.getPlayers().get(0), true)) {
                sc.sendCommand("move " + getMoveParameter(x, y));
                if (sc.lastRespContains("OK") != null) {
                    putPiece(4, x, y, board.getPlayers().get(0), false);
                    board.getPlayers().get(0).setPlayersTurn(false);
                    board.getPlayers().get(1).setPlayersTurn(true);
                    board.game.showPlayerScore();
                }
            }
        });

        gameTable.add(clickablePane, point.x, point.y);

    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
        giveColor(player);
    }

    public Circle addCircle(Player player) {


        Circle circle = new Circle(0, 0, 12);

        circle.setFill(player.getColor());
        circle.setStroke(Color.BLACK);


        clickablePane.getChildren().add(circle);

        return circle;
    }

    /**
     * This method will check if this Cell Object has a Circle Object and return it.
     * @return a Circle Object if Cell has a Circle, otherwise null
     */
    public Circle getCircleOK(){
        if (clickablePane.getChildren().size()<=0) return null;
        Circle tmp = (Circle) clickablePane.getChildren().get(0);
        return tmp;
    }

    /**
     * This method will change te color of the Cell Object
     * @param player
     */
    public void giveColor(Player player){
        if (clickablePane.getChildren().size()==0) {
            addCircle(player);
        } else {
            Circle tmp = (Circle) clickablePane.getChildren().get(0);
            tmp.setFill(player.getColor());
        }
    }
}
