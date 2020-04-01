package reversi;

import java.awt.Point;

import framework.server.ServerConnection;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Cell {

    private ReversiPlayer player;

    private Point point;
    private StackPane clickablePane;
    private ServerConnection sc;
    private Board board;

    public Cell(GridPane gameTable, Point point, ReversiPlayer player, Board board) {
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

            if (player.isPlayersTurn() && checkCell(4,x,y,player.getColor(),false)) {
                sc.sendCommand("move "+ getMoveParameter(x, y));
                if (sc.lastRespContains("OK")!= null) putPiece(player);
                player.setPlayersTurn(false);

            }
        });

        gameTable.add(clickablePane, point.x, point.y);

    }

    public int getMoveParameter(int colIndex, int rowIndex) {
        return rowIndex * Settings.tilesY + colIndex;
    }



    public void setPlayer(ReversiPlayer player){this.player = player;}

    public void putPiece(ReversiPlayer player) {
        addCircle(player);
    }

    @SuppressWarnings("Duplicates")
    public Circle addCircle(ReversiPlayer player) {


        Circle circle = new Circle(0, 0, 12);

        circle.setFill(player.getColor());
        circle.setStroke(Color.BLACK);


        clickablePane.getChildren().add(circle);

        return circle;
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
     * @param getter Color, give here the color of the ReversiPlayer, who has the turn.
     * @param check boolean, set true if you only want to check if move is possible.
     *
     * @return true if cell is possible to click
     */
    public boolean checkCell(int direction, int x, int y, Color getter, boolean check){
        if (board.grid[x][y].getCircleOK()!= null && direction==4) return false;
        int counter = 0;
        boolean changable = false;
        for (int s = y-1; s < y+2; s++){
            for (int z = x-1; z < x+2; z++){
                if (s >= 0 && z >= 0 && s < Settings.tilesX && z < Settings.tilesY && counter != 4) {
                    Circle tmpCir = board.grid[z][s].getCircleOK();
                    if (direction==4 && tmpCir != null){
                        if (!tmpCir.getFill().equals(getter) && checkCell(counter,z,s,getter,true)){
                            changable = true;
                            if (!check) {
                                board.grid[z][s].changeColor();
                                checkCell(counter,z,s,getter,false);
                            }
                        }
                    } else if (counter==direction && tmpCir != null){
                        if (tmpCir.getFill().equals(getter)){
                            return true;
                        } else{
                            if (check) return checkCell(counter,z,s,getter,check);
                            else {
                                board.grid[z][s].changeColor();
                                checkCell(counter,z,s,getter,check);
                            }
                        }
                    }
                }
                counter++;
            }
        }
        if (!changable) System.out.println("Move "+direction+" Not Available!");
        else System.out.println("Move "+direction+" is Available!");
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
    public void changeColor(){
        if (clickablePane.getChildren().size()>0) {
            Circle tmp = (Circle) clickablePane.getChildren().get(0);
            if (tmp.getFill().equals(Color.WHITE)) tmp.setFill(Color.BLACK);
            else tmp.setFill(Color.WHITE);
        }
    }
}
