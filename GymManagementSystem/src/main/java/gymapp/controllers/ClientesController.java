package gymapp.controllers;

import gymapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class    ClientesController {

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, Integer> colEdad;
    @FXML
    private TableColumn<Cliente, Double> colPeso;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        cargarClientes();
    }

    @FXML
    private void cargarClientes() {
        listaClientes.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM clientes")) {

            while (rs.next()) {
                listaClientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getDouble("peso")
                ));
            }
            tablaClientes.setItems(listaClientes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void agregarCliente() {
        System.out.println("Agregar cliente");
        // Aquí abrirías un formulario para ingresar datos
    }

    @FXML
    private void editarCliente() {
        System.out.println("Editar cliente seleccionado");
    }

    @FXML
    private void eliminarCliente() {
        System.out.println("Eliminar cliente seleccionado");
    }
}
