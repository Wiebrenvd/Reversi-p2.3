package framework.controllers;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;


import framework.settings.ReversiSettings;
//import framework.games.AIGame;
import framework.games.Game;
import framework.players.EasyAIPlayer;
import framework.players.HardAIPlayer;
import framework.players.OfflinePlayer;
import framework.players.OnlinePlayer;
import framework.settings.Settings;
import framework.settings.TicTacToeSettings;
import javafx.stage.Stage;

public class GameController extends Controller implements Initializable {
    @FXML
    public GridPane gameTable;

    @FXML
    public Label lblPlayer1, lblPlayer2, lblStatus, scorep1, scorep2, title;

    @FXML
    public Button btnForfeit, btn_lobby;

    public Settings settings;

    private final int gamemode;
    private Thread gameThread;
    private String gameName;
    private Game game;

    private Player opp;
    private Player user;

    private boolean setThisAI;


    public GameController(ServerConnection sc, int gamemode, boolean aiOn, String gameName){
        super(sc);
        this.gameName = gameName;
        this.gamemode = gamemode;
        this.game = null;
        this.setThisAI = aiOn;
        if (gameName.equals("reversi")) {
            this.settings = new ReversiSettings();
        }else if (gameName.equals("tictactoe")){
            this.settings = new TicTacToeSettings();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (gamemode!=0) btn_lobby.setDisable(true);
        title.setText(gameName.substring(0,1).toUpperCase()+gameName.substring(1)+" ☺☻");
        startGame();
    }

    @FXML
    void goLobby(ActionEvent event) {
        if (game != null) game.endGame();
        changeScene(event, "/framework/views/lobby.fxml", new LobbyController(sc, setThisAI));
    }


    public void turnToPlayerOne(boolean bool) {
        lblPlayer1.setUnderline(bool);
        lblPlayer2.setUnderline(!bool);
    }

    public void startGame(){
        if (setThisAI){
            user = new HardAIPlayer(sc.getLoginName()+" (HARD AI)");
        } else {
            user = new OfflinePlayer(sc.getLoginName());
        }
        switch (gamemode) {
            case ReversiSettings.MULTIPLAYER:
                opp = new OnlinePlayer(sc);
                break;
            case ReversiSettings.EASY:
                opp = new EasyAIPlayer("Makkelijke Computer");
                break;
            case ReversiSettings.HARD:
                opp = new HardAIPlayer("Moeilijke Computer");
                break;
        }

        this.game = new Game(this, sc, user, opp);
        gameThread = new Thread(this.game);
        gameThread.start();
    }

    @FXML
    void forfeitGame(ActionEvent event) {
        if (btnForfeit.getText().equals("Zoek Nieuw Spel")){
            startGame();
            btnForfeit.setText("Verlaat game");
            return;
        }
        if (opp instanceof OnlinePlayer)sc.sendCommand("forfeit");
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