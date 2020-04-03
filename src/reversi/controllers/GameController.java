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
import reversi.Settings;
import reversi.ai.EasyAI;
import reversi.ai.HardAI;
import reversi.games.AIGame;
import reversi.games.Game;

public class GameController extends Controller implements Initializable {
    private final int gamemode;
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

    private ServerConnection sc;
    private Board board;
    private Game game;




    public GameController(ServerConnection sc, int gamemode){
        this.sc = sc;
        this.gamemode = gamemode;
        this.game = null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AIGame aiGame = null;
        switch (gamemode) {
            case Settings.MULTIPLAYER:
                this.game = new Game(this, sc);
                break;
            case Settings.EASY:
                aiGame = new AIGame(this, new ServerConnection(), new EasyAI());
                this.game = new Game(this, sc);
                break;
            case Settings.HARD:
                aiGame = new AIGame(this, new ServerConnection(), new HardAI());
                this.game = new Game(this, sc);
                break;
        }

        if (aiGame != null) {
            new Thread(aiGame).start(); // Start een game in de achtergrond voor de ai
        }
        new Thread(this.game).start();
    }


    public void turnToPlayerOne(boolean bool) {
        lblPlayer1.setUnderline(bool);
        lblPlayer2.setUnderline(!bool);
    }

    @FXML
    void forfeitGame(ActionEvent event) {
        sc.sendCommand("forfeit");
        Platform.runLater(()->{
            game.endGame();
        });

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