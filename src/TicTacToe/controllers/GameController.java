package TicTacToe.controllers;

import framework.controllers.Controller;
import framework.server.ServerConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import TicTacToe.Settings;

public class GameController extends Controller implements Initializable {
    @FXML
    private GridPane gameTable;

    @FXML
    private Label playerBlack;

    @FXML
    private Label playerWhite;

    @FXML
    private Label pointsBlack;

    @FXML
    private Label pointsWhite;

    @FXML
    private RadioButton on;

    @FXML
    private RadioButton off;

    private ServerConnection serverConnection;
    private Pane[][] gameBoard;
    private boolean gameFound = false;

    private HashMap<Point, Integer> tileBoard;

    public GameController(ServerConnection sc){
        this.serverConnection = sc;

        this.gameBoard = new Pane[Settings.tilesX][Settings.tilesY];
        this.tileBoard = new HashMap<Point, Integer>();
    }

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeABoard();
    }

    public void makeABoard(){
        RowConstraints rowConstraints;
        ColumnConstraints colConstraints;

        for (int i = 0; i < Settings.tilesX ; i++) {
            colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            gameTable.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < Settings.tilesY ; i++) {
            rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gameTable.getRowConstraints().add(rowConstraints);
        }

        for (int x = 0 ; x < Settings.tilesX ; x++) {
            for (int y = 0; y < Settings.tilesY; y++) {
                Point tPos = new Point(x,y);

                addPane(x, y);
            }
        }
    }

    public boolean setMove(Point pos, int player, boolean update){
        if(pos.x < 0 || pos.y < 0 || pos.x >= Settings.tilesX || pos.y >= Settings.tilesY) {
            System.out.printf("position: [%d,%d] is outside the board!", pos.x, pos.y);

            return false;
        }

        if(tileBoard.containsKey(pos)) {
            System.out.printf("position: [%d,%d] is already token!", pos.x, pos.y);

            return false;
        }

        if(update) {
            serverConnection.sendCommand("move " + this.getPlace(pos));

            //if(!serverConnection.showLastResponse().equals("OK"))
                //return false;
        }

        //store movement
        tileBoard.put(pos, player);

        Color pColor = Settings.PlayerColors[player];
        addCircle(gameBoard[pos.x][pos.y], pColor);

        return true;
    }

    public Circle addCircle(Pane pane, Color color){
        Circle circle = new Circle(0,0,12);

        circle.setFill(color);
        circle.setStroke(Color.BLACK);

        pane.getChildren().add(circle);

        return circle;
    }

    public int getPlace(Point pos) {
        return this.getPlace(pos.x, pos.y);
    }

    public int getPlace(int colIndex, int rowIndex) {
        return rowIndex * Settings.tilesY  + colIndex;
    }

    public Point getPos(int num) {
        int row = (int)Math.round(num/Settings.tilesX);
        int col = num % Settings.tilesX;

        return new Point(col, row);
    }

    /*public void addCircleToGrid(int col, int row, String color){
        reversiBoard[col][row].getChildren().add(addCircle(color));
    }*/

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    private Pane addPane(int colIndex, int rowIndex) {
        Pane pPane = new StackPane();

        StackPane.setAlignment(pPane, Pos.CENTER);

        pPane.setOnMouseClicked(e -> {
            System.out.printf("Mouse clicked cell [%d, %d]%n", colIndex, rowIndex);

            Point pPos = new Point(colIndex, rowIndex);

            if(!this.setMove(pPos, Settings.PLAYER1, true)){

            }
        });

        gameBoard[colIndex][rowIndex] = pPane;
        gameTable.add(pPane, colIndex, rowIndex);

        return pPane;
    }
}
