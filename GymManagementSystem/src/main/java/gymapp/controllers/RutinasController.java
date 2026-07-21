package gymapp.controllers;

import gymapp.model.Rutina;
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

public class RutinasController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtObjetivo;

    @FXML
    private TextField txtDuracion;

    @FXML
    private TableView<Rutina> tablaRutinas;

    @FXML
    private TableColumn<Rutina, Integer> colId;

    @FXML
    private TableColumn<Rutina, String> colNombre;

    @FXML
    private TableColumn<Rutina, String> colObjetivo;

    @FXML
    private TableColumn<Rutina, Integer> colDuracion;

    private final ObservableList<Rutina> listaRutinas = FXCollections.observableArrayList();

    private int idSeleccionado = -1;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        cargarRutinas();

        tablaRutinas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, rutina) -> {

            if (rutina != null) {

                idSeleccionado = rutina.getId();

                txtNombre.setText(rutina.getNombre());
                txtObjetivo.setText(rutina.getObjetivo());
                txtDuracion.setText(String.valueOf(rutina.getDuracion()));

            }

        });

    }

    private void cargarRutinas() {

        listaRutinas.clear();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM rutinas")) {

            while (rs.next()) {

                listaRutinas.add(new Rutina(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("objetivo"),
                        rs.getInt("duracion")
                ));

            }

            tablaRutinas.setItems(listaRutinas);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void guardarRutina() {

        if (!validarCampos()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "INSERT INTO rutinas(nombre,objetivo,duracion) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtObjetivo.getText());
            ps.setInt(3, Integer.parseInt(txtDuracion.getText()));

            ps.executeUpdate();

            mostrarMensaje("Correcto", "Rutina registrada.");

            limpiarCampos();

            cargarRutinas();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void actualizarRutina() {

        if (idSeleccionado == -1) {

            mostrarMensaje("Aviso", "Seleccione una rutina.");

            return;

        }

        if (!validarCampos()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "UPDATE rutinas SET nombre=?, objetivo=?, duracion=? WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtObjetivo.getText());
            ps.setInt(3, Integer.parseInt(txtDuracion.getText()));
            ps.setInt(4, idSeleccionado);

            ps.executeUpdate();

            mostrarMensaje("Correcto", "Rutina actualizada.");

            limpiarCampos();

            cargarRutinas();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void eliminarRutina() {

        if (idSeleccionado == -1) {

            mostrarMensaje("Aviso", "Seleccione una rutina.");

            return;

        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "DELETE FROM rutinas WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idSeleccionado);

            ps.executeUpdate();

            mostrarMensaje("Correcto", "Rutina eliminada.");

            limpiarCampos();

            cargarRutinas();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean validarCampos() {

        if (txtNombre.getText().isBlank()
                || txtObjetivo.getText().isBlank()
                || txtDuracion.getText().isBlank()) {

            mostrarMensaje("Error", "Complete todos los campos.");

            return false;

        }

        try {

            int dias = Integer.parseInt(txtDuracion.getText());

            if (dias <= 0) {

                mostrarMensaje("Error", "La duración debe ser mayor que cero.");

                return false;

            }

        } catch (Exception e) {

            mostrarMensaje("Error", "La duración debe ser numérica.");

            return false;

        }

        return true;

    }

    private void limpiarCampos() {

        txtNombre.clear();
        txtObjetivo.clear();
        txtDuracion.clear();

        idSeleccionado = -1;

        tablaRutinas.getSelectionModel().clearSelection();

    }

    private void mostrarMensaje(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(titulo);

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();

    }

}