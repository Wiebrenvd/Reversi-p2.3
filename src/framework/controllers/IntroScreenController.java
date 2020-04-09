package framework.controllers;

import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import framework.settings.ReversiSettings;

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
        Controller controller = new LoginController(sc, ReversiSettings.MULTIPLAYER);
        changeScene(event,"/framework/views/login.fxml", controller);
    }


    @FXML
    public void playEasyMode(ActionEvent event) {
        Controller controller = new LoginController(sc, ReversiSettings.EASY);
        changeScene(event,"/framework/views/login.fxml", controller);
    }

    @FXML
    public void playHardMode(ActionEvent event) {
        Controller controller = new LoginController(sc, ReversiSettings.HARD);
        changeScene(event,"/framework/views/login.fxml", controller);
    }




}
