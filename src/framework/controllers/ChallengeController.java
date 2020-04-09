package framework.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import framework.LobbyListener;
import framework.Settings;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChallengeController extends Controller implements Initializable {
    private String challenger;
    private LobbyListener ll;
    private String challengeNumber;

    @FXML
    public Label challenge_label;

    public ChallengeController(ServerConnection sc, LobbyListener ll, String challenger, String challengeNumber) {
        super(sc);
        this.challenger = challenger;
        this.challengeNumber = challengeNumber;
        this.ll = ll;
    }

    @FXML
    public void accept(ActionEvent event) {
        sc.sendCommand("challenge accept " + challengeNumber);
        close(event);
    }

    @FXML
    public void deny(ActionEvent event) {
        ll.setHasChallenge(false);
        close(event);
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Platform.runLater(stage::close);
    }

    public void setLabel(String string) {
        challenge_label.setText(string);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        challenge_label.setText(String.format("%s daagt je uit! (Nr. %s)", challenger, challengeNumber));
    }
}
