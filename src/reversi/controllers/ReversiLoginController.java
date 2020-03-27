package reversi.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import framework.controllers.LoginController;
import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReversiLoginController extends LoginController implements Initializable {

    @FXML
    private Button loginBtn, connect_btn;

    @FXML
    private TextField nameInput, hostIP_input, port_input;

    @FXML
    private Label status_lbl;

    @FXML
    private ChoiceBox<String> gameChoiceBox;

    public ReversiLoginController(ServerConnection sc) {
        super.sc = sc;
    }

    @FXML
    public void login(ActionEvent event) {
        String response = sendLogin(event);
        if (response.equals("OK")){
            sc.sendCommand("subscribe reversi");
            loginSucceed(event);
        }

    }

    @FXML
    void connectToServer(ActionEvent event) {
        String host = hostIP_input.getText();
        int port = Integer.parseInt(port_input.getText());
        if (status_lbl.getText().equals("Offline")){
            if (sc.startConnection(host,port)){
                status_lbl.setText("Online");
                status_lbl.setTextFill(Color.GREEN);
                getGamelist();
            }
        }
    }

    public void loginSucceed(ActionEvent event){
        changeScene(event, "/reversi/reversi.fxml", new GameController(sc));
    }

    private void getGamelist() {
        sc.sendCommand("get gamelist");
        String response = sc.showLastResponse();

        String[] gamelist = sc.getDicOrArr(response);
        for (String game : gamelist){
            gameChoiceBox.getItems().add(game.substring(1,game.length()-1));
        }
    }

    @FXML
    void logToServer(ActionEvent event) {
        String command = "login ";
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        if (nameInput.getText().trim().length() == 0){
            showTooltip(stage,loginBtn,"Please fill in a login name!", null);
            return;
        } else {
            if (status_lbl.getText().equals("Offline")){
                showTooltip(stage,loginBtn,"Please check if you are online!", null);
                return;
            } else {
                String loginName = nameInput.getText();
                command += loginName;
                sc.setLoginName(loginName);
                sc.sendCommand(command);
            }
        }

        if (!sc.showLastResponse().equals("OK")){
            showTooltip(stage,loginBtn,"Cannot connect to the Server... \nPlease restart the application", null);
        } else {
            if (gameChoiceBox.getValue().contains("Please select")){
                showTooltip(stage,loginBtn,"Please select a game!", null);
                return;
            }

            sc.sendCommand("subscribe " + gameChoiceBox.getValue());
            loginSucceed(event);
        }
    }


    //https://stackoverflow.com/questions/17405688/javafx-activate-a-tooltip-with-a-button
    public static void showTooltip(Stage owner, Control control, String tooltipText,
                                   ImageView tooltipGraphic)
    {
        Point2D p = control.localToScene(0.0, 0.0);

        final Tooltip customTooltip = new Tooltip();
        customTooltip.setText(tooltipText);

        control.setTooltip(customTooltip);
        customTooltip.setAutoHide(true);

        customTooltip.show(owner, p.getX()
                + control.getScene().getX() + control.getScene().getWindow().getX(), p.getY()
                + control.getScene().getY() + control.getScene().getWindow().getY());

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String regularChoice = "Please select a game";
        gameChoiceBox.getItems().add(regularChoice);
        gameChoiceBox.setValue(regularChoice);
    }

}
