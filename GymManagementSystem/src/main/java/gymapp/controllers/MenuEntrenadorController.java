package gymapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuEntrenadorController {

    @FXML
    private void openClientes() {
        abrirVentana("/gymapp/views/clientes.fxml", "Gestión de Clientes");
    }

    @FXML
    private void openRutinas() {
        abrirVentana("/gymapp/views/rutinas.fxml", "Gestión de Rutinas");
    }

    @FXML
    private void openAsignaciones() {
        abrirVentana("/gymapp/views/asignaciones.fxml", "Asignación de Rutinas");
    }

    @FXML
    private void logout() {
        abrirVentana("/gymapp/views/login.fxml", "Login");
    }

    private void abrirVentana(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
