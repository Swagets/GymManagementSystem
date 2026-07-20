package org.example.crudproyecto;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClientesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;

    @FXML private TableView<?> tblClientes;
    @FXML private TableColumn<?, ?> colId;
    @FXML private TableColumn<?, ?> colNombre;
    @FXML private TableColumn<?, ?> colEmail;
    @FXML private TableColumn<?, ?> colTelefono;

    @FXML
    private void onAgregarClick() {
        System.out.println("Clic en Agregar: " + txtNombre.getText());
    }

    @FXML
    private void onEditarClick() {
        System.out.println("Clic en Editar");
    }

    @FXML
    private void onEliminarClick() {
        System.out.println("Clic en Eliminar");
    }

    @FXML
    private void onLimpiarClick() {
        txtNombre.clear();
        txtEmail.clear();
        txtTelefono.clear();
    }
}