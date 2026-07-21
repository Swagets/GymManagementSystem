package gymapp.controllers;

import gymapp.model.Cliente;
import gymapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClientesController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtPeso;

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

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    private int idSeleccionado = -1;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        cargarClientes();

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, cliente) -> {

            if (cliente != null) {

                idSeleccionado = cliente.getId();

                txtNombre.setText(cliente.getNombre());
                txtEdad.setText(String.valueOf(cliente.getEdad()));
                txtPeso.setText(String.valueOf(cliente.getPeso()));

            }

        });

    }

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
    private void guardarCliente() {

        if (!validarCampos()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "INSERT INTO clientes(nombre,edad,peso) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNombre.getText());
            ps.setInt(2, Integer.parseInt(txtEdad.getText()));
            ps.setDouble(3, Double.parseDouble(txtPeso.getText()));

            ps.executeUpdate();

            mostrarMensaje("Éxito", "Cliente registrado.");

            limpiarCampos();

            cargarClientes();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void actualizarCliente() {

        if (idSeleccionado == -1) {

            mostrarMensaje("Aviso", "Seleccione un cliente.");

            return;

        }

        if (!validarCampos()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "UPDATE clientes SET nombre=?, edad=?, peso=? WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNombre.getText());
            ps.setInt(2, Integer.parseInt(txtEdad.getText()));
            ps.setDouble(3, Double.parseDouble(txtPeso.getText()));
            ps.setInt(4, idSeleccionado);

            ps.executeUpdate();

            mostrarMensaje("Éxito", "Cliente actualizado.");

            limpiarCampos();

            cargarClientes();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void eliminarCliente() {

        if (idSeleccionado == -1) {

            mostrarMensaje("Aviso", "Seleccione un cliente.");

            return;

        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "DELETE FROM clientes WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idSeleccionado);

            ps.executeUpdate();

            mostrarMensaje("Éxito", "Cliente eliminado.");

            limpiarCampos();

            cargarClientes();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void limpiarCampos() {

        txtNombre.clear();
        txtEdad.clear();
        txtPeso.clear();

        idSeleccionado = -1;

        tablaClientes.getSelectionModel().clearSelection();

    }

    private boolean validarCampos() {

        if (txtNombre.getText().isBlank()
                || txtEdad.getText().isBlank()
                || txtPeso.getText().isBlank()) {

            mostrarMensaje("Error", "Complete todos los campos.");

            return false;

        }

        try {

            Integer.parseInt(txtEdad.getText());

            Double.parseDouble(txtPeso.getText());

        } catch (Exception e) {

            mostrarMensaje("Error", "Edad y peso deben ser numéricos.");

            return false;

        }

        return true;

    }

    private void mostrarMensaje(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(titulo);

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();

    }

}