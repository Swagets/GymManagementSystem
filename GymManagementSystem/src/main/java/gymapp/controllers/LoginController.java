package gymapp.controllers;

import gymapp.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    // Guarda el usuario que inició sesión
    public static int usuarioIdActual;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;


    @FXML
    private void handleLogin() {

        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Buscamos solamente por usuario
            String sql = "SELECT id, rol, password FROM usuarios WHERE usuario=?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, usuario);

            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {

                String passwordBD = rs.getString("password");


                // Verifica la contraseña usando BCrypt
                if (BCrypt.checkpw(password, passwordBD)) {


                    // Guardamos el id del usuario conectado
                    usuarioIdActual = rs.getInt("id");

                    String rol = rs.getString("rol");


                    System.out.println("Login correcto");
                    System.out.println("Usuario ID: " + usuarioIdActual);
                    System.out.println("Rol: " + rol);


                    Stage stage = (Stage) txtUsuario.getScene().getWindow();


                    if ("entrenador".equalsIgnoreCase(rol)) {


                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/gymapp/views/menuEntrenador.fxml")
                        );

                        stage.setScene(new Scene(loader.load()));


                    } else if ("cliente".equalsIgnoreCase(rol)) {


                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/gymapp/views/menuCliente.fxml")
                        );

                        stage.setScene(new Scene(loader.load()));

                    }


                } else {

                    System.out.println("Usuario o contraseña incorrectos");

                }


            } else {

                System.out.println("Usuario o contraseña incorrectos");

            }


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}