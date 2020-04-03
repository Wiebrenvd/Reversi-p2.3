package framework.controllers;

import java.lang.reflect.Constructor;

import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reversi.Settings;

public class GamemodeController extends Controller {

    private ServerConnection sc;
    private String gName;


    public GamemodeController(ServerConnection sc, String gName) {
        this.sc = sc;
        this.gName = gName;
    }

    @FXML
    public void chooseSingleplayer(ActionEvent event) {
        changeScene(event, "/framework/views/difficulty.fxml", this);
    }

    @FXML
    public void playMultiplayer(ActionEvent event) {
        sc.sendCommand("subscribe " + gName.substring(0, 1).toUpperCase() + gName.substring(1)); // Subscribe heeft blijkbaar "Reversi" nodig ipv "reversi"
        startGamemode(event, Settings.MULTIPLAYER);
    }


    @FXML
    public void playEasyMode(ActionEvent event) {
        sc.sendCommand("subscribe " + gName.substring(0, 1).toUpperCase() + gName.substring(1));
        startGamemode(event, Settings.EASY);
    }

    @FXML
    public void playHardMode(ActionEvent event) {
        sc.sendCommand("subscribe " + gName.substring(0, 1).toUpperCase() + gName.substring(1));
        startGamemode(event, Settings.HARD);
    }

    public void startGamemode(ActionEvent event, int gamemode) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + gName + "/views/GameView.fxml"));

        try {
            Class<?> Controllers = Class.forName(gName + ".controllers.GameController");
            Constructor<?> cons = Controllers.getConstructor(ServerConnection.class, int.class);

            loader.setController(cons.newInstance(sc, gamemode));

            Parent root = (Parent) loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("/" + gName + "/styles/Style.css").toExternalForm());

            stage.setScene(rScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
