package Framework.controllers;

import Framework.players.Player;
import Framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;


//import Framework.games.AIGame;
import Framework.game.Game;
import Framework.players.EasyAIPlayer;
import Framework.players.HardAIPlayer;
import Framework.players.OfflinePlayer;
import Framework.players.OnlinePlayer;
import Framework.game.Settings;

public class GameController extends Framework.controllers.Controller implements Initializable {
    @FXML
    public GridPane gameTable;

    @FXML
    public Label lblPlayer1, lblPlayer2, lblStatus, scorep1, scorep2, title;

    @FXML
    public Button btnForfeit, btn_lobby;

    public Settings settings;

    public int wonMatches;
    public int playedMatches;

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
        this.wonMatches = 0;
        this.playedMatches = 0;
        try {
            this.settings = (Settings) Class.forName(settings.getPath(gameName) +".GameSettings").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        changeScene(event, "/Framework/views/lobby.fxml", new Framework.controllers.LobbyController(sc, setThisAI));
    }


    public void turnToPlayerOne(boolean bool) {
        lblPlayer1.setUnderline(bool);
        lblPlayer2.setUnderline(!bool);
    }

    public void startGame(){
        String playerName = " ( G: " + this.playedMatches + " - W: " + this.wonMatches + " ) " + sc.getLoginName();
        if (setThisAI){
            user = new HardAIPlayer(playerName + " (HARD AI)");
        } else {
            user = new OfflinePlayer(playerName);
        }
        switch (gamemode) {
            case Settings.MULTIPLAYER:
                opp = new OnlinePlayer(sc);
                break;
            case Settings.EASY:
                opp = new EasyAIPlayer("Makkelijke Computer");
                break;
            case Settings.HARD:
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
            game.endGameMessage(3);
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