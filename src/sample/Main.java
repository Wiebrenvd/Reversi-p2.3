package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));

        ServerConnection serverConnection = new ServerConnection();
        Controller controller = new Controller(serverConnection);
        loader.setController(controller);

        Parent root = loader.load();
        primaryStage.setTitle("Group ONE: Reversi Client");
        primaryStage.setScene(new Scene(root, 420, 240));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
