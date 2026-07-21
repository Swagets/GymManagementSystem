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

public class RutinasController {

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

    private ObservableList<Rutina> listaRutinas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        cargarRutinas();
    }

    @FXML
    private void cargarRutinas() {
        listaRutinas.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rutinas")) {

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
    private void agregarRutina() {
        System.out.println("Agregar rutina");
        // Aquí abrirías un formulario para ingresar datos
    }

    @FXML
    private void editarRutina() {
        System.out.println("Editar rutina seleccionada");
    }

    @FXML
    private void eliminarRutina() {
        System.out.println("Eliminar rutina seleccionada");
    }
}
