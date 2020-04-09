package framework.cells;

import java.awt.Point;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import framework.games.Game;
import framework.settings.ReversiSettings;
import framework.boards.Board;
import framework.players.OnlinePlayer;

public class Cell {

    private Player player;

    private Point point;
    private StackPane clickablePane;
    private ServerConnection sc;
    private Board board;
    private Game currentGame;

    public Cell(GridPane gameTable, Point point, Player player, Board board) {
        this.player = player;
        this.point = point;
        this.board = board;
        this.sc = board.sc;
        this.currentGame = board.currentGame;

        this.clickablePane = new StackPane();
        this.clickablePane.setAlignment(Pos.CENTER);
        this.clickablePane.setOnMouseClicked(e -> {
            int x = (int) point.getX();
            int y = (int) point.getY();
            System.out.printf("Mouse clicked cell [%d, %d]%n", x, y);

            if (board.players.get(0).isPlayersTurn() && putPiece(this.board,4,x,y,board.players.get(0),true)) {
                boolean online = board.players.get(1) instanceof OnlinePlayer;
                if (online) sc.sendCommand("move "+ getMoveParameter(x, y));
                if (putPiece(this.board,4,x,y,board.players.get(0),false)){
                    board.players.get(0).setPlayersTurn(false);
                    board.players.get(1).setPlayersTurn(true);
                }
            }
        });

        this.clickablePane.getStyleClass().add("cell");
        gameTable.add(clickablePane, point.x, point.y);

    }

    public int getMoveParameter(int colIndex, int rowIndex) {
        return rowIndex * ReversiSettings.TILESY + colIndex;
    }


    /**
     * This method set a player to this Cell object.
     * @param player Player, add this player to the Cell object.
     */
    public void setPlayer(Player player){
        this.player = player;
        giveColor(player);
    }

    public Player getPlayer(){
        return this.player;
    }


    /**
     * This method will add a circle to a plane.
     * @param player Player, the circle takes the color of this player.
     */
    public void addCircle(Player player) {
        Circle circle = new Circle(0, 0, ((currentGame.settings.HEIGHT/2)-3));

        circle.setFill(player.getColor());
        circle.setStroke(Color.BLACK);


        clickablePane.getChildren().add(circle);
    }

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
    public boolean putPiece(Board board, int direction, int x, int y, Player getter, boolean check){
        return currentGame.settings.putPiece(this,board,direction,x,y,getter,check);
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