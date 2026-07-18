package gymapp.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println(getClass().getResource("/gymapp/views/login.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gymapp/views/login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
        primaryStage.setTitle("Gym Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}