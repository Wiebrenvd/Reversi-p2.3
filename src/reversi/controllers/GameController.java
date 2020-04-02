package reversi.controllers;

import framework.controllers.Controller;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import reversi.Board;
import reversi.Game;

public class GameController extends Controller implements Initializable {
    @FXML
    public GridPane gameTable;

    @FXML
    public Label lblPlayer1, lblPlayer2;

    @FXML
    public Label scorep1, scorep2;

    @FXML
    public Label lblStatus;

    @FXML
    private Button btnForfeit;

    public ArrayList<Label> labels;

    private ServerConnection serverConnection;
    private Board board;
    private Game game;


    @FXML
    void forfeitGame(ActionEvent event) {
        serverConnection.sendCommand("forfeit");
        Platform.runLater(()->{
            game.endGame();
        });

    }

    public GameController(ServerConnection sc){
        this.serverConnection = sc;

    }

    //Bron: https://stackoverflow.com/questions/50012463/how-can-i-click-a-gridpane-cell-and-have-it-perform-an-action
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        this.board = new Board(gameTable);
        this.game = new Game(this, serverConnection);
        Thread gameThread = new Thread(this.game);
        gameThread.start();
    }

    public void turnToPlayerOne(boolean bool) {
        lblPlayer1.setUnderline(bool);
        lblPlayer2.setUnderline(!bool);
    }

    public void setStatus(String s) {
        lblStatus.setText(s);
    }

    public String getStatus() {
        return lblStatus.getText();
    }

    public Label getLblPlayer1() {
        return lblPlayer1;
    }

    public void setLblPlayer1(Label lblPlayer1) {
        this.lblPlayer1 = lblPlayer1;
    }

    public Label getLblPlayer2() {
        return lblPlayer2;
    }

    public void setLblPlayer2(Label lblPlayer2) {
        this.lblPlayer2 = lblPlayer2;
    }
}