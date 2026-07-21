package gymapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuClienteController {

    @FXML
    private void logout() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gymapp/views/login.fxml"));

            Stage stage = (Stage) javafx.stage.Window.getWindows().filtered(w -> w.isShowing()).get(0);

            stage.setScene(new Scene(loader.load()));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}