package framework.Reversi;

import framework.controllers.Controller;
import framework.controllers.LoginController;
import framework.server.ServerConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/framework/login.fxml"));
        ServerConnection sc = new ServerConnection();
        Controller lc = new LoginController(sc);
        loader.setController(lc);
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Group ONE: reversi Client");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
