package reversi.controllers;

import framework.controllers.Controller;
import framework.server.ServerConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController extends Controller implements Initializable {
    @FXML
    private GridPane reversiTable;

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
    private Pane[][] reversiBoard;
    private boolean gameFound = false;
    public GameController(ServerConnection sc){
        this.serverConnection = sc;
        this.reversiBoard = new Pane[8][8];
    }

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeABoard();
    }

    public void makeABoard(){
        int numCols = 8 ;
        int numRows = 8 ;

        for (int i = 0 ; i < numCols ; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            reversiTable.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0 ; i < numRows ; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            reversiTable.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0 ; i < numCols ; i++) {
            for (int j = 0; j < numRows; j++) {
                addPane(i, j);
            }
        }



        reversiBoard[3][3].getChildren().add(addCircle("white"));
        reversiBoard[4][3].getChildren().add(addCircle("black"));
        reversiBoard[4][4].getChildren().add(addCircle("white"));
        reversiBoard[3][4].getChildren().add(addCircle("black"));
        System.out.println(reversiTable.getChildren().size());
    }

    public Circle addCircle(String color){
        Circle circle = new Circle(18,15,12);
        if (color.equals("white")) {
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);
        }
        return circle;
    }

    public void addCircleToGrid(int col, int row, String color){
        reversiBoard[col][row].getChildren().add(addCircle(color));
    }

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    private void addPane(int colIndex, int rowIndex) {
        reversiBoard[colIndex][rowIndex] = new Pane();
        reversiBoard[colIndex][rowIndex].setOnMouseClicked(e -> {
            System.out.printf("Mouse clicked cell [%d, %d]%n", colIndex, rowIndex);
            serverConnection.sendCommand("move " + (rowIndex*8+colIndex));
            if (serverConnection.showLastResponse().equals("OK")) {
                reversiBoard[colIndex][rowIndex].getChildren().add(addCircle("black"));
            }
        });
        reversiTable.add(reversiBoard[colIndex][rowIndex], colIndex, rowIndex);
    }
}
