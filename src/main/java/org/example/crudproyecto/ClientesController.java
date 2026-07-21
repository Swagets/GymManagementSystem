package org.example.crudproyecto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClientesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;

    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colTelefono;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private Cliente clienteSeleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        cargarClientes();

        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clienteSeleccionado = newSelection;
                txtNombre.setText(newSelection.getNombre());
                txtEmail.setText(newSelection.getEmail());
                txtTelefono.setText(newSelection.getTelefono());
            }
        });
    }

    public void cargarClientes() {
        listaClientes.clear();
        String sql = "SELECT * FROM clientes";

        try (Connection con = Conexion.getConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaClientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono")
                ));
            }
            tblClientes.setItems(listaClientes);

        } catch (Exception e) {
            mostrarAlerta("Error de Carga", "No se pudieron cargar los datos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void onAgregarClick() {
        if (txtNombre.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            mostrarAlerta("Campos Requeridos", "Por favor completa Nombre y Email", Alert.AlertType.WARNING);
            return;
        }
        String sql = "INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, txtNombre.getText());
            pst.setString(2, txtEmail.getText());
            pst.setString(3, txtTelefono.getText());
            pst.executeUpdate();

            cargarClientes();
            onLimpiarClick();
            mostrarAlerta("Éxito", "Cliente agregado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error al Agregar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void onEditarClick() {
        if (clienteSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor selecciona un cliente de la tabla.", Alert.AlertType.WARNING);
            return;
        }
        String sql = "UPDATE clientes SET nombre = ?, email = ?, telefono = ? WHERE id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, txtNombre.getText());
            pst.setString(2, txtEmail.getText());
            pst.setString(3, txtTelefono.getText());
            pst.setInt(4, clienteSeleccionado.getId());
            pst.executeUpdate();

            cargarClientes();
            onLimpiarClick();
            mostrarAlerta("Éxito", "Cliente actualizado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error al Editar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void onEliminarClick() {
        if (clienteSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor selecciona un cliente de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, clienteSeleccionado.getId());
            pst.executeUpdate();

            cargarClientes();
            onLimpiarClick();
            mostrarAlerta("Éxito", "Cliente eliminado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error al Eliminar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void onLimpiarClick() {
        txtNombre.clear();
        txtEmail.clear();
        txtTelefono.clear();
        clienteSeleccionado = null;
        tblClientes.getSelectionModel().clearSelection();
    }
    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}