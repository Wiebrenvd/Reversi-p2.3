package Framework.controllers;

import java.io.IOException;
import java.lang.reflect.Constructor;

import Framework.game.Settings;
import Framework.server.ServerConnection;
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
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        try {
            Parent loader = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(loader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void changeScene(ActionEvent event, String fxmlPath, Controller controller) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            Parent parent = loader.load();
            Scene rScene = new Scene(parent);
            rScene.getStylesheets().add(getClass().getResource("../src/Style.css").toExternalForm());

            stage.setScene(rScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void startGame(ActionEvent event, int gamemode, String gameName, boolean aiON) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String pName = Settings.getPath(gameName);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Framework/views/GameView.fxml"));

        try {
            Class<?> Controllers = Class.forName("Framework.controllers.GameController");
            Constructor<?> cons = Controllers.getConstructor(ServerConnection.class, int.class, boolean.class, String.class);

            loader.setController(cons.newInstance(sc, gamemode, aiON, gameName));

            Parent root = (Parent) loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("/" + pName + "/src/Style.css").toExternalForm());

            stage.setScene(rScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void startGame(Stage stage, int gamemode, String gameName, boolean aiON) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Framework/views/GameView.fxml"));
        String pName = Settings.getPath(gameName);

        try {
            Class<?> Controllers = Class.forName("Framework.controllers.GameController");
            Constructor<?> cons = Controllers.getConstructor(ServerConnection.class, int.class, boolean.class, String.class);

            loader.setController(cons.newInstance(sc, gamemode, aiON, gameName));

            Parent root = (Parent) loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("/" + pName + "/src/Style.css").toExternalForm());

            stage.setScene(rScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
