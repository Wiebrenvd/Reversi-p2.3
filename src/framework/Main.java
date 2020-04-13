package Framework;

import java.io.IOException;

import Framework.controllers.Controller;
import Framework.controllers.IntroScreenController;
import Framework.server.ServerConnection;
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Framework/views/introScreen.fxml"));
        ServerConnection sc = new ServerConnection();
        Controller introControl = new IntroScreenController(sc);
        loader.setController(introControl);
        try {
            Parent root = loader.load();
            Scene rScene = new Scene(root);

            rScene.getStylesheets().add(getClass().getResource("src/Style.css").toExternalForm());

            primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) event -> {
                Platform.exit();
                System.exit(0);
            }); // Stops process when exiting application

            primaryStage.setTitle("Group ONE: game Client");
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
