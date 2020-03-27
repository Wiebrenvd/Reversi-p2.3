package reversi.controllers;

import framework.controllers.LoginController;
import framework.server.ServerConnection;
import javafx.event.ActionEvent;

public class ReversiLoginController extends LoginController {

    public ReversiLoginController(ServerConnection sc) {
        super.sc = sc;
    }

    public void login(ActionEvent event) {
        String response = sendLogin(event);
        if (response.equals("OK")){
            sc.sendCommand("subscribe reversi");
            loginSucceed(event);
        }

    }

    public void loginSucceed(ActionEvent event){
        changeScene(event, "/reversi/reversi.fxml", new GameController(sc));
    }


}
