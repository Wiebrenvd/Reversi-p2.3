package framework.framework.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    protected void changeScene(ActionEvent event, String fxmlPath) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        try {
            Parent loader = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(loader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void changeScene(ActionEvent event, String fxmlPath, framework.controllers.Controller controller) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
