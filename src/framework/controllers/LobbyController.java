package framework.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import framework.LobbyListener;
import framework.Settings;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LobbyController extends Controller implements Initializable {

    private boolean aiON;

    @FXML
    private VBox player_list;

    private ArrayList<String> players;


    private ToggleGroup tg;

    private LobbyListener cl;


    public LobbyController(ServerConnection sc, boolean aiON) {
        super(sc);
        this.aiON = aiON;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.players = new ArrayList<>();

        tg = new ToggleGroup();

        updatePlayerList();
        cl = new LobbyListener(this, sc);
        new Thread(cl).start();

//        pl = new PlayerListener(this, sc);
//        new Thread(pl).start();


    }

    public synchronized void updatePlayerList() {
        sc.sendCommand("get playerlist");
        String response = sc.lastRespContains("SVR PLAYERLIST");

        System.out.println("CPL reponse: " + response);


        String[] playerNames = sc.getArr(response);
        for (String player : playerNames) {
            player = player.replaceAll("^\"|\"$", "");
            if (!players.contains(player)) {
                players.add(player);
                addRadioButton(player);
            }

        }


    }

    private void addRadioButton(String player) {

        RadioButton playerButton = new RadioButton(player);
        playerButton.setUserData(player);
        playerButton.setToggleGroup(tg);
        if (player.equals(Settings.PLAYERNAME)) {
            playerButton.setDisable(true);
        }
        Platform.runLater(() -> player_list.getChildren().add(playerButton));
    }


    @FXML
    private void challengePlayer(ActionEvent event) {
        String chosenPlayer = null;

        if (tg.getSelectedToggle() != null) {
            chosenPlayer = tg.getSelectedToggle().getUserData().toString();
            sc.sendCommand("challenge \"" + chosenPlayer + "\" \"" + Settings.GAMENAME.substring(0, 1).toUpperCase() + Settings.GAMENAME.substring(1) + "\"");
        }

    }


    public void showChallengePopup(LobbyListener ll, String challenger, String challengeNumber) {
        ChallengeController cc = new ChallengeController(sc, ll, challenger, challengeNumber);

        try {
            String path = "/framework/views/challenge.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setController(cc);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);
            stage.setTitle("Je wordt uitgedaagd!");
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        Stage stage = (Stage) player_list.getScene().getWindow();
        startGame(stage, Settings.MULTIPLAYER, Settings.GAMENAME, aiON);

    }


    public void refreshPlayerlist(ActionEvent event) {
        updatePlayerList();
    }
}
