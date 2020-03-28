package framework;

import java.io.IOException;

import framework.controllers.Controller;
import framework.server.ServerConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import framework.controllers.LoginController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/framework/login.fxml"));
        ServerConnection sc = new ServerConnection();
        Controller lc = new LoginController(sc);
        loader.setController(lc);
        try {
            Parent root = loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

            primaryStage.setTitle("Group ONE: reversi Client");
            primaryStage.setScene(rScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
