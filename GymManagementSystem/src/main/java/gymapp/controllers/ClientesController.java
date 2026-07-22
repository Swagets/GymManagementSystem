package gymapp.controllers;

import gymapp.model.Cliente;
import gymapp.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

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
import javafx.scene.control.PasswordField;

public class ClientesController {


    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtPeso;


    // Nuevos campos para crear cuenta
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;



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



    private final ObservableList<Cliente> listaClientes =
            FXCollections.observableArrayList();



    private int idSeleccionado = -1;



    @FXML
    public void initialize() {


        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colEdad.setCellValueFactory(
                new PropertyValueFactory<>("edad")
        );

        colPeso.setCellValueFactory(
                new PropertyValueFactory<>("peso")
        );


        cargarClientes();



        tablaClientes.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, cliente) -> {


                    if(cliente != null){


                        idSeleccionado = cliente.getId();


                        txtNombre.setText(
                                cliente.getNombre()
                        );


                        txtEdad.setText(
                                String.valueOf(cliente.getEdad())
                        );


                        txtPeso.setText(
                                String.valueOf(cliente.getPeso())
                        );

                    }

                });

    }





    private void cargarClientes(){


        listaClientes.clear();


        try(
                Connection conn = DatabaseConnection.getConnection();

                Statement stmt = conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(
                                "SELECT * FROM clientes"
                        )
        ){


            while(rs.next()){


                listaClientes.add(
                        new Cliente(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getInt("edad"),
                                rs.getDouble("peso")
                        )
                );

            }



            tablaClientes.setItems(listaClientes);



        }catch(Exception e){

            e.printStackTrace();

        }


    }





    @FXML
    private void guardarCliente(){


        if(!validarCampos())
            return;



        try(Connection conn =
                    DatabaseConnection.getConnection()) {



            // ===============================
            // 1. Crear usuario
            // ===============================


            String sqlUsuario =
                    "INSERT INTO usuarios(usuario,password,rol) VALUES(?,?,?)";


            PreparedStatement psUsuario =
                    conn.prepareStatement(
                            sqlUsuario,
                            Statement.RETURN_GENERATED_KEYS
                    );


            psUsuario.setString(
                    1,
                    txtUsuario.getText()
            );


            String passwordHash = BCrypt.hashpw(
                    txtPassword.getText(),
                    BCrypt.gensalt()
            );


            psUsuario.setString(
                    2,
                    passwordHash
            );


            psUsuario.setString(
                    3,
                    "cliente"
            );


            psUsuario.executeUpdate();



            ResultSet rs =
                    psUsuario.getGeneratedKeys();



            int usuarioId = 0;


            if(rs.next()){

                usuarioId = rs.getInt(1);

            }




            // ===============================
            // 2. Crear cliente relacionado
            // ===============================


            String sqlCliente =
                    "INSERT INTO clientes(nombre,edad,peso,usuario_id) VALUES(?,?,?,?)";


            PreparedStatement psCliente =
                    conn.prepareStatement(sqlCliente);



            psCliente.setString(
                    1,
                    txtNombre.getText()
            );


            psCliente.setInt(
                    2,
                    Integer.parseInt(txtEdad.getText())
            );


            psCliente.setDouble(
                    3,
                    Double.parseDouble(txtPeso.getText())
            );


            psCliente.setInt(
                    4,
                    usuarioId
            );



            psCliente.executeUpdate();




            mostrarMensaje(
                    "Éxito",
                    "Cliente registrado correctamente"
            );



            limpiarCampos();

            cargarClientes();



        }catch(Exception e){
            if (e.getMessage().contains("Duplicate")
                    || e.getMessage().contains("duplicate")
                    || e.getMessage().contains("usuarios.usuario")
                    || e.getMessage().contains("for key 'usuario'")) {

                mostrarMensaje(
                        "Error",
                        "El nombre de usuario ya existe. Escoja otro."
                );
            } else {
                e.printStackTrace();

                mostrarMensaje(
                        "Error",
                        "No se pudo registrar el cliente."
                );
            }
        }
    }

    @FXML
    private void actualizarCliente(){


        if(idSeleccionado == -1){
            mostrarMensaje(
                    "Aviso",
                    "Seleccione un cliente"
            );
            return;
        }
        if(!validarActualizar())
            return;
        try(Connection conn =
                    DatabaseConnection.getConnection()){
            String sql =
                    "UPDATE clientes SET nombre=?, edad=?, peso=? WHERE id=?";
            PreparedStatement ps =
                    conn.prepareStatement(sql);
            ps.setString(
                    1,
                    txtNombre.getText()
            );
            ps.setInt(
                    2,
                    Integer.parseInt(txtEdad.getText())
            );
            ps.setDouble(
                    3,
                    Double.parseDouble(txtPeso.getText())
            );
            ps.setInt(
                    4,
                    idSeleccionado
            );

            ps.executeUpdate();

            mostrarMensaje(
                    "Éxito",
                    "Cliente actualizado"
            );

            limpiarCampos();

            cargarClientes();
        }catch(Exception e){

            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarCliente() {

        if (idSeleccionado == -1) {

            mostrarMensaje(
                    "Aviso",
                    "Seleccione un cliente"
            );

            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Obtener el usuario asociado al cliente
            String sqlBuscar =
                    "SELECT usuario_id FROM clientes WHERE id=?";

            PreparedStatement psBuscar =
                    conn.prepareStatement(sqlBuscar);

            psBuscar.setInt(1, idSeleccionado);

            ResultSet rs = psBuscar.executeQuery();

            int usuarioId = 0;

            if (rs.next()) {
                usuarioId = rs.getInt("usuario_id");
            }

            // Eliminar primero las asignaciones del cliente
            String sqlAsignaciones =
                    "DELETE FROM asignaciones WHERE cliente_id=?";

            PreparedStatement psAsignaciones =
                    conn.prepareStatement(sqlAsignaciones);

            psAsignaciones.setInt(1, idSeleccionado);

            psAsignaciones.executeUpdate();

            // Eliminar el cliente
            String sqlCliente =
                    "DELETE FROM clientes WHERE id=?";

            PreparedStatement psCliente =
                    conn.prepareStatement(sqlCliente);

            psCliente.setInt(1, idSeleccionado);

            psCliente.executeUpdate();

            // Eliminar el usuario
            String sqlUsuario =
                    "DELETE FROM usuarios WHERE id=?";

            PreparedStatement psUsuario =
                    conn.prepareStatement(sqlUsuario);

            psUsuario.setInt(1, usuarioId);

            psUsuario.executeUpdate();

            mostrarMensaje(
                    "Éxito",
                    "Cliente eliminado correctamente"
            );

            limpiarCampos();

            cargarClientes();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @FXML
    private void limpiarCampos(){


        txtNombre.clear();

        txtEdad.clear();

        txtPeso.clear();


        txtUsuario.clear();

        txtPassword.clear();



        idSeleccionado = -1;



        tablaClientes.getSelectionModel()
                .clearSelection();


    }








    private boolean validarCampos(){
        if(
                txtNombre.getText().isBlank()
                        ||
                        txtEdad.getText().isBlank()
                        ||
                        txtPeso.getText().isBlank()
                        ||
                        txtUsuario.getText().isBlank()
                        ||
                        txtPassword.getText().isBlank()
        ){
            mostrarMensaje(
                    "Error",
                    "Complete todos los campos"
            );

            return false;

        }
        try{
            Integer.parseInt(
                    txtEdad.getText()
            );
            Double.parseDouble(
                    txtPeso.getText()
            );
        }catch(Exception e){
            mostrarMensaje(
                    "Error",
                    "Edad y peso deben ser números"
            );


            return false;
        }
        return true;
    }
    private boolean validarActualizar() {

        if (txtNombre.getText().isBlank()
                || txtEdad.getText().isBlank()
                || txtPeso.getText().isBlank()) {

            mostrarMensaje("Error", "Complete nombre, edad y peso");

            return false;
        }

        try {

            Integer.parseInt(txtEdad.getText());
            Double.parseDouble(txtPeso.getText());

        } catch (Exception e) {

            mostrarMensaje("Error", "Edad y peso deben ser números");

            return false;
        }

        return true;
    }
    private void mostrarMensaje(
            String titulo,
            String mensaje
    ){
        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}