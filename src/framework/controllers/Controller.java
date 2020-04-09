package framework.controllers;

import java.io.IOException;
import java.lang.reflect.Constructor;

import framework.server.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    public ServerConnection sc;


    public Controller(ServerConnection sc) {
        this.sc = sc;

    }









    protected void changeScene(ActionEvent event, String fxmlPath) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent loader = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(loader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void changeScene(ActionEvent event, String fxmlPath, Controller controller) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            Parent parent = loader.load();
            Scene rScene = new Scene(parent);
            rScene.getStylesheets().add(getClass().getResource("../Style.css").toExternalForm());

            stage.setScene(rScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void startGame(ActionEvent event, int gamemode, String gameName, boolean aiON) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + gameName + "/views/GameView.fxml"));

        try {
            Class<?> Controllers = Class.forName(gameName + ".controllers.GameController");
            Constructor<?> cons = Controllers.getConstructor(ServerConnection.class, int.class, boolean.class);

            loader.setController(cons.newInstance(sc, gamemode, aiON));

            Parent root = (Parent) loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("/" + gameName + "/styles/Style.css").toExternalForm());

            stage.setScene(rScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void startGame(Stage stage, int gamemode, String gameName, boolean aiON) {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + gameName + "/views/GameView.fxml"));

        try {
            Class<?> Controllers = Class.forName(gameName + ".controllers.GameController");
            Constructor<?> cons = Controllers.getConstructor(ServerConnection.class, int.class, boolean.class);

            loader.setController(cons.newInstance(sc, gamemode, aiON));

            Parent root = (Parent) loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("/" + gameName + "/styles/Style.css").toExternalForm());

            stage.setScene(rScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
