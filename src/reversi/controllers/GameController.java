package reversi.controllers;

import framework.controllers.Controller;
import framework.server.ServerConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;

import reversi.Board;

public class GameController extends Controller implements Initializable {
    @FXML
    private GridPane gameTable;

    private ServerConnection serverConnection;
    private Board board;

    public GameController(ServerConnection sc){
        this.serverConnection = sc;

    }

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.board = new Board(gameTable);
    }



}