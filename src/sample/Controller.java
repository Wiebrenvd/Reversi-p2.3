package sample;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField nameInput;

    private ServerConnection sc;

    public Controller(ServerConnection sc){
        this.sc = sc;
    }

    @FXML
    void logToServer(ActionEvent event) {
        String command = "login ";
        command += nameInput.getText();
        System.out.println(command);
        sc.sendCommand(command);

        if (sc.showResponse().equals("OK")){
            sc.sendCommand("subscribe Reversi");
            loginSucceed(event);
        }
    }

    public void loginSucceed(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reversiUI.fxml"));
        ReversiUIController reversiUIController = new ReversiUIController(sc);
        loader.setController(reversiUIController);
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println("Couldn't load the FXML file...");
        }

    }

}