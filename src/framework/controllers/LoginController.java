package framework.controllers;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//import reversi.controllers.GameController;


public class LoginController extends Controller implements Initializable {

    ServerConnection sc;

    @FXML
    private Button loginBtn, connect_btn;

    @FXML
    private TextField nameInput, hostIP_input, port_input;

    @FXML
    private Label status_lbl;

    @FXML
    private ChoiceBox<String> gameChoiceBox;

    public LoginController(ServerConnection sc) {
        this.sc = sc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String regularChoice = "Please select a game";
        gameChoiceBox.getItems().add(regularChoice);
        gameChoiceBox.setValue(regularChoice);

        connectToServer();
    }

    @FXML
    void login(ActionEvent event) {
        String command = "login ";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (nameInput.getText().trim().length() == 0) {
            showTooltip(stage, loginBtn, "Please fill in a login name!", null);
            return;
        } else {
            if (status_lbl.getText().equals("Offline")) {
                showTooltip(stage, loginBtn, "Please check if you are online!", null);
                return;
            } else {
                if (gameChoiceBox.getValue().contains("Please select")) {
                    showTooltip(stage, loginBtn, "Please select a game!", null);
                    return;
                }
                String loginName = nameInput.getText();
                command += loginName;
                sc.setLoginName(loginName);
                sc.sendCommand(command);
            }
        }

        if (!sc.showLastResponse().equals("OK")) {
            showTooltip(stage, loginBtn, "Cannot connect to the Server... \nPlease restart the application", null);
        } else {
            sc.sendCommand("subscribe " + gameChoiceBox.getValue());
            loginSucceed(event);
        }
    }


    @FXML
    private void connectToServer() {
        String host = hostIP_input.getText();
        int port = Integer.parseInt(port_input.getText());
        if (status_lbl.getText().equals("Offline")) {
            if (sc.startConnection(host, port)) {
                status_lbl.setText("Online");
                status_lbl.setTextFill(Color.GREEN);
                getGamelist();
            }
        }
    }

    public void loginSucceed(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        String gName = this.getGamename(gameChoiceBox.getSelectionModel().getSelectedItem());
        gName = getGamename(gName);
        System.out.println(gName);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + gName + "/views/GameView.fxml"));

        try {
            Class<?> Controllers = Class.forName(gName + ".controllers.GameController");
            Constructor<?> cons = Controllers.getConstructor(ServerConnection.class);

            loader.setController(cons.newInstance(sc));

            Parent root = (Parent)loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("/" + gName + "/styles/Style.css").toExternalForm());

            stage.setScene(rScene);
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        try {
//            changeScene(event, "/reversi/GameView.fxml", new reversi.controllers.GameController(sc));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    public String getGamename(String key) {
        String name = "";
        if (key.contains("-")) {
            String[] words = key.split("[_-]");

            for (String sWord : words) {
                name += sWord.substring(0, 1).toUpperCase();
                name += sWord.substring(1);
            }
        } else {
            name = key;
        }

        return name.toLowerCase();
    }

    private void getGamelist() {
        sc.sendCommand("get gamelist");
        String response = sc.showLastResponse();

        String[] gamelist = sc.getArr(response);
        for (String game : gamelist) {
            gameChoiceBox.getItems().add(game.substring(1, game.length() - 1));
        }
    }

    //https://stackoverflow.com/questions/17405688/javafx-activate-a-tooltip-with-a-button
    public static void showTooltip(Stage owner, Control control, String tooltipText,
                                   ImageView tooltipGraphic) {
        Point2D p = control.localToScene(0.0, 0.0);

        final Tooltip customTooltip = new Tooltip();
        customTooltip.setText(tooltipText);

        control.setTooltip(customTooltip);
        customTooltip.setAutoHide(true);

        customTooltip.show(owner, p.getX()
                + control.getScene().getX() + control.getScene().getWindow().getX(), p.getY()
                + control.getScene().getY() + control.getScene().getWindow().getY());

    }
}
