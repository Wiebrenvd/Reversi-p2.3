package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class ReversiUIController implements Initializable {
    @FXML
    private GridPane reversiTable;

    private ServerConnection serverConnection;
    private Pane[][] reversiBoard;

    public ReversiUIController(ServerConnection sc){
        this.serverConnection = sc;
        this.reversiBoard = new Pane[8][8];
    }

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    }

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    private void addPane(int colIndex, int rowIndex) {
        reversiBoard[colIndex][rowIndex] = new Pane();
        reversiBoard[colIndex][rowIndex].setOnMouseClicked(e -> {
            serverConnection.sendCommand("move " + (rowIndex*8+colIndex));
            System.out.printf("Mouse clicked cell [%d, %d]%n", colIndex, rowIndex);
            reversiBoard[colIndex][rowIndex].getChildren().add(new Circle(18,15,12));
        });
        reversiTable.add(reversiBoard[colIndex][rowIndex], colIndex, rowIndex);
    }
}
