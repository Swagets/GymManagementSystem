package gymapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuEntrenadorController {

    @FXML
    private void openClientes() {
        abrirVentana("/gymapp/views/gestionClientes.fxml", "Gestión de Clientes");
    }

    @FXML
    private void openRutinas() {
        abrirVentana("/gymapp/views/gestionRutinas.fxml", "Gestión de Rutinas");
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

            Stage stage = new Stage();

            stage.setTitle(titulo);

            stage.setScene(new Scene(loader.load()));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}