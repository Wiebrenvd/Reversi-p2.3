package reversi.controllers;

import framework.actors.Player;
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


import reversi.boards.Board;
import reversi.Settings;
//import reversi.games.AIGame;
import reversi.games.Game;
import reversi.players.EasyAIPlayer;
import reversi.players.HardAIPlayer;
import reversi.players.OfflinePlayer;
import reversi.players.OnlinePlayer;

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
    public Button btnForfeit;

    public ArrayList<Label> labels;

    private ServerConnection sc;
    private Board playerBoard;
    private final int gamemode;
    private Game game;

    private boolean setThisAI;


    public GameController(ServerConnection sc, int gamemode, boolean aiOn){
        this.sc = sc;
        this.gamemode = gamemode;
        this.game = null;
        this.setThisAI = aiOn;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        AIGame aiGame = null;
        Player user;
        if (setThisAI){
            user = new HardAIPlayer(sc.getLoginName()+" (HARD AI)");
        } else {
            user = new OfflinePlayer(sc.getLoginName());
        }
        Player opp;
        switch (gamemode) {
            case Settings.MULTIPLAYER:
                opp = new OnlinePlayer(sc);
                this.game = new Game(this, sc, user, opp);
                break;
            case Settings.EASY:
                opp = new EasyAIPlayer("Makkelijke Computer");
                this.game = new Game(this, sc, user, opp);
                break;
            case Settings.HARD:
                opp = new HardAIPlayer("Moeilijke Computer");
                this.game = new Game(this, sc, user, opp);
                break;
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

    public GridPane getGameTable() {
        return gameTable;
    }
}