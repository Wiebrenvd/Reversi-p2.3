package framework.controllers;

import framework.Settings;
import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OptionsController extends Controller {

    public OptionsController(ServerConnection sc) {
        super(sc);
    }

    @FXML
    public void chooseSingleplayer(ActionEvent event) {
        changeScene(event, "/framework/views/difficulty.fxml", this);
    }

    @FXML
    public void playMultiplayer(ActionEvent event) {
        Controller controller = new LoginController(sc, Settings.MULTIPLAYER);
        changeScene(event, "/framework/views/login.fxml", controller);
    }


    @FXML
    public void playEasyMode(ActionEvent event) {
        Controller controller = new LoginController(sc, Settings.EASY);
        changeScene(event, "/framework/views/login.fxml", controller);
    }

    @FXML
    public void playHardMode(ActionEvent event) {
        Controller controller = new LoginController(sc, Settings.HARD);
        changeScene(event, "/framework/views/login.fxml", controller);
    }


}
