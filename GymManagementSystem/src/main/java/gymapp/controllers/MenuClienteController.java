package gymapp.controllers;

import javafx.fxml.FXML;

public class MenuClienteController {

    @FXML
    private void showDatos() {
        System.out.println("Mostrar datos del cliente");
    }

    @FXML
    private void showRutina() {
        System.out.println("Mostrar rutina del cliente");
    }

    @FXML
    private void logout() {
        System.out.println("Cerrar sesión");
        // Regresar al login.fxml
    }
}
