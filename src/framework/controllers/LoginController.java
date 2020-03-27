package framework.controllers;

import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public abstract class LoginController extends Controller {

    @FXML
    private TextField nameInput;

    protected ServerConnection sc;

    @FXML
    protected String sendLogin(ActionEvent event) {
        String command = "login ";
        command += nameInput.getText().replaceAll("[^a-zA-Z0-9\\s+]", "");;
        System.out.println(command);
        sc.sendCommand(command);

        return sc.showResponse();
    }

    public abstract void loginSucceed(ActionEvent event);

}
