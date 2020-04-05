package reversi.cells;

import java.awt.Point;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import reversi.Settings;
import reversi.boards.Board;
import reversi.players.OnlinePlayer;

public class Cell {

    private Player player;

    private Point point;
    private StackPane clickablePane;
    private ServerConnection sc;
    private Board board;

    public Cell(GridPane gameTable, Point point, Player player, Board board) {
        this.player = player;
        this.point = point;
        this.board = board;
        this.sc = board.sc;

        this.clickablePane = new StackPane();
        this.clickablePane.setAlignment(Pos.CENTER);
        this.clickablePane.setOnMouseClicked(e -> {
            int x = (int) point.getX();
            int y = (int) point.getY();
            System.out.printf("Mouse clicked cell [%d, %d]%n", x, y);

            if (board.players.get(0).isPlayersTurn() && putPiece(4,x,y,board.players.get(0),true)) {
                boolean online = board.players.get(1) instanceof OnlinePlayer;
                if (online) sc.sendCommand("move "+ getMoveParameter(x, y));
                if (putPiece(4,x,y,board.players.get(0),false)){
                    board.players.get(0).setPlayersTurn(false);
                    board.players.get(1).setPlayersTurn(true);
                }
            }
        });

        this.clickablePane.getStyleClass().add("cell");
        gameTable.add(clickablePane, point.x, point.y);

    }

    public int getMoveParameter(int colIndex, int rowIndex) {
        return rowIndex * Settings.TILESY + colIndex;
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
        Circle circle = new Circle(0, 0, 12);

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
    public boolean putPiece(int direction, int x, int y, Player getter, boolean check){
        if (board.grid[x][y].getPlayer()!= null && direction==4) return false;
        if (!check) setPlayer(getter);
        int counter = 0;
        boolean changable = false;
        for (int s = y-1; s < y+2; s++){
            for (int z = x-1; z < x+2; z++){
                if (s >= 0 && z >= 0 && s < Settings.TILESX && z < Settings.TILESY && counter != 4) {
                    Player tmpPlayer = board.grid[z][s].getPlayer();
                    Cell tmpCell = board.grid[z][s];
                    if (direction==4 && tmpPlayer != null){
                        if (!tmpPlayer.equals(getter) && putPiece(counter,z,s,getter,true)){
                            changable = true;
                            if (!check) {
                                tmpCell.setPlayer(getter);
                                putPiece(counter,z,s,getter,false);
                            }
                        }
                    } else if (counter==direction && tmpPlayer != null){
                        if (tmpPlayer.equals(getter)){
                            return true;
                        } else{
                            if (check) return putPiece(counter,z,s,getter,true);
                            else {
                                tmpCell.setPlayer(getter);
                                putPiece(counter,z,s,getter,false);
                            }
                        }
                    }
                }
                counter++;
            }
        }
//        if (!changable) System.out.println("Move "+direction+" Not Available!");
//        else System.out.println("Move "+direction+" is Available!");
        return changable;
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