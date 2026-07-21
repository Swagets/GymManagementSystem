package org.example.crudproyecto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
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

    @FXML
    public void initialize() {
        // Mapear las columnas del TableView con los atributos de Cliente.java
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        cargarClientes();
    }

    public void cargarClientes() {
        listaClientes.clear();
        String sql = "SELECT * FROM clientes";

        try (Connection con = Conexion.getConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");

                listaClientes.add(new Cliente(id, nombre, email, telefono));
            }

            tblClientes.setItems(listaClientes);

        } catch (Exception e) {
            System.err.println("Error al cargar clientes desde MySQL: " + e.getMessage());
        }
    }

    @FXML
    private void onAgregarClick() {
        System.out.println("Clic en Agregar");
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