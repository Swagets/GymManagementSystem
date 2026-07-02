package gymapp.controllers;

import gymapp.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT rol FROM usuarios WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                System.out.println("Login correcto: " + rol);

                if ("ENTRENADOR".equals(rol)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gymapp/views/menuEntrenador.fxml"));
                    Stage stage = (Stage) txtUsuario.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                } else if ("CLIENTE".equals(rol)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gymapp/views/menuCliente.fxml"));
                    Stage stage = (Stage) txtUsuario.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                }
            } else {
                System.out.println("Credenciales inválidas");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


