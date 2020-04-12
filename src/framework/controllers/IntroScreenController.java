package framework.controllers;

import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import reversi.GameSettings;

public class IntroScreenController extends Controller {


    public IntroScreenController(ServerConnection sc) {
        super(sc);
    }

    @FXML
    public void chooseSingleplayer(ActionEvent event) {
        changeScene(event, "/framework/views/difficulty.fxml", this);
    }

    @FXML
    public void playMultiplayer(ActionEvent event) {
        Controller controller = new LoginController(sc, GameSettings.MULTIPLAYER);
        changeScene(event,"/framework/views/login.fxml", controller);
    }


    @FXML
    public void playEasyMode(ActionEvent event) {
        Controller controller = new LoginController(sc, GameSettings.EASY);
        changeScene(event,"/framework/views/login.fxml", controller);
    }

    @FXML
    public void playHardMode(ActionEvent event) {
        Controller controller = new LoginController(sc, GameSettings.HARD);
        changeScene(event,"/framework/views/login.fxml", controller);
    }




}
