package framework;

import java.io.IOException;

import framework.controllers.Controller;
import framework.controllers.OptionsController;
import framework.server.ServerConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/framework/views/connection.fxml"));
        ServerConnection sc = new ServerConnection();
        Controller introControl = new OptionsController(sc);
        loader.setController(introControl);
        try {
            Parent root = loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

            primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) event -> {
                Platform.exit();
                System.exit(0);
            }); // Stops process when exiting application

            primaryStage.setTitle("ITV2B1 Reversi");
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
