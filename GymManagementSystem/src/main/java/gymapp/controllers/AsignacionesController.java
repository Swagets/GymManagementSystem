package gymapp.controllers;

import gymapp.model.Asignacion;
import gymapp.model.Cliente;
import gymapp.model.Rutina;
import gymapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class AsignacionesController {

    @FXML
    private ComboBox<Cliente> cmbCliente;

    @FXML
    private ComboBox<Rutina> cmbRutina;

    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private DatePicker dpInicio;

    @FXML
    private DatePicker dpFin;

    @FXML
    private TableView<Asignacion> tablaAsignaciones;

    @FXML
    private TableColumn<Asignacion,Integer> colId;

    @FXML
    private TableColumn<Asignacion,String> colCliente;

    @FXML
    private TableColumn<Asignacion,String> colRutina;

    @FXML
    private TableColumn<Asignacion,LocalDate> colInicio;

    @FXML
    private TableColumn<Asignacion,LocalDate> colFin;

    @FXML
    private TableColumn<Asignacion,String> colEstado;

    private final ObservableList<Asignacion> listaAsignaciones =
            FXCollections.observableArrayList();

    private int idSeleccionado = -1;

    @FXML
    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colRutina.setCellValueFactory(new PropertyValueFactory<>("rutina"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarClientes();
        cargarRutinas();

        cmbEstado.getItems().addAll("ACTIVA","FINALIZADA");

        cargarAsignaciones();

        tablaAsignaciones.getSelectionModel().selectedItemProperty().addListener((obs,ant,a)->{

            if(a!=null){

                idSeleccionado=a.getId();

                dpInicio.setValue(a.getFechaInicio());
                dpFin.setValue(a.getFechaFin());

                cmbEstado.setValue(a.getEstado());

                seleccionarCliente(a.getClienteId());

                seleccionarRutina(a.getRutinaId());

            }

        });

    }

    private void cargarClientes(){

        ObservableList<Cliente> lista=FXCollections.observableArrayList();

        try(Connection conn=DatabaseConnection.getConnection();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM clientes")){

            while(rs.next()){

                lista.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getDouble("peso")
                ));

            }

            cmbCliente.setItems(lista);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void cargarRutinas(){

        ObservableList<Rutina> lista=FXCollections.observableArrayList();

        try(Connection conn=DatabaseConnection.getConnection();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM rutinas")){

            while(rs.next()){

                lista.add(new Rutina(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("objetivo"),
                        rs.getInt("duracion")
                ));

            }

            cmbRutina.setItems(lista);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void cargarAsignaciones(){

        listaAsignaciones.clear();

        try(Connection conn=DatabaseConnection.getConnection();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("""

            SELECT a.id,
                   a.cliente_id,
                   a.rutina_id,
                   c.nombre cliente,
                   r.nombre rutina,
                   a.fecha_inicio,
                   a.fecha_fin,
                   a.estado

            FROM asignaciones a

            INNER JOIN clientes c
            ON a.cliente_id=c.id

            INNER JOIN rutinas r
            ON a.rutina_id=r.id

            """)){

            while(rs.next()){

                listaAsignaciones.add(

                        new Asignacion(

                                rs.getInt("id"),
                                rs.getInt("cliente_id"),
                                rs.getInt("rutina_id"),
                                rs.getString("cliente"),
                                rs.getString("rutina"),
                                rs.getDate("fecha_inicio").toLocalDate(),
                                rs.getDate("fecha_fin").toLocalDate(),
                                rs.getString("estado")

                        )

                );

            }

            tablaAsignaciones.setItems(listaAsignaciones);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void seleccionarCliente(int id){

        for(Cliente c:cmbCliente.getItems()){

            if(c.getId()==id){

                cmbCliente.setValue(c);

                break;

            }

        }

    }

    private void seleccionarRutina(int id){

        for(Rutina r:cmbRutina.getItems()){

            if(r.getId()==id){

                cmbRutina.setValue(r);

                break;

            }

        }

    }
    @FXML
    private void guardarAsignacion() {

        if (!validarCampos()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "INSERT INTO asignaciones(cliente_id,rutina_id,fecha_inicio,fecha_fin,estado) VALUES(?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, cmbCliente.getValue().getId());
            ps.setInt(2, cmbRutina.getValue().getId());
            ps.setDate(3, Date.valueOf(dpInicio.getValue()));
            ps.setDate(4, Date.valueOf(dpFin.getValue()));
            ps.setString(5, cmbEstado.getValue());

            ps.executeUpdate();

            mostrarMensaje("Correcto", "Asignación registrada.");

            limpiarCampos();

            cargarAsignaciones();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void actualizarAsignacion() {

        if (idSeleccionado == -1) {

            mostrarMensaje("Aviso", "Seleccione una asignación.");

            return;

        }

        if (!validarCampos()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "UPDATE asignaciones SET cliente_id=?, rutina_id=?, fecha_inicio=?, fecha_fin=?, estado=? WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, cmbCliente.getValue().getId());
            ps.setInt(2, cmbRutina.getValue().getId());
            ps.setDate(3, Date.valueOf(dpInicio.getValue()));
            ps.setDate(4, Date.valueOf(dpFin.getValue()));
            ps.setString(5, cmbEstado.getValue());
            ps.setInt(6, idSeleccionado);

            ps.executeUpdate();

            mostrarMensaje("Correcto", "Asignación actualizada.");

            limpiarCampos();

            cargarAsignaciones();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void eliminarAsignacion() {

        if (idSeleccionado == -1) {

            mostrarMensaje("Aviso", "Seleccione una asignación.");

            return;

        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "DELETE FROM asignaciones WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idSeleccionado);

            ps.executeUpdate();

            mostrarMensaje("Correcto", "Asignación eliminada.");

            limpiarCampos();

            cargarAsignaciones();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void limpiarCampos() {

        cmbCliente.setValue(null);
        cmbRutina.setValue(null);
        cmbEstado.setValue(null);

        dpInicio.setValue(null);
        dpFin.setValue(null);

        idSeleccionado = -1;

        tablaAsignaciones.getSelectionModel().clearSelection();

    }

    private boolean validarCampos() {

        if (cmbCliente.getValue() == null
                || cmbRutina.getValue() == null
                || cmbEstado.getValue() == null
                || dpInicio.getValue() == null
                || dpFin.getValue() == null) {

            mostrarMensaje("Error", "Complete todos los campos.");

            return false;

        }

        if (dpFin.getValue().isBefore(dpInicio.getValue())) {

            mostrarMensaje("Error", "La fecha fin no puede ser menor que la fecha inicio.");

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