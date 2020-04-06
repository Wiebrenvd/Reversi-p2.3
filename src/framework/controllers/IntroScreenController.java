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

public class IntroScreenController extends Controller {

    private ServerConnection sc;

    public IntroScreenController(ServerConnection sc) {
        this.sc = sc;
    }

    @FXML
    public void chooseSingleplayer(ActionEvent event) {
        changeScene(event, "/framework/views/difficulty.fxml", this);
    }

    @FXML
    public void playMultiplayer(ActionEvent event) {
        Controller controller = new LoginController(sc,Settings.MULTIPLAYER);
        changeScene(event,"/framework/views/login.fxml", controller);
    }


    @FXML
    public void playEasyMode(ActionEvent event) {
        Controller controller = new LoginController(sc,Settings.EASY);
        changeScene(event,"/framework/views/login.fxml", controller);
    }

    @FXML
    public void playHardMode(ActionEvent event) {
        Controller controller = new LoginController(sc,Settings.HARD);
        changeScene(event,"/framework/views/login.fxml", controller);
    }




}
