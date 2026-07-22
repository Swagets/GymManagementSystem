package gymapp.controllers;

import gymapp.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MenuClienteController {

    @FXML
    private Label lblBienvenida;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblEdad;

    @FXML
    private Label lblPeso;

    @FXML
    private Label lblRutina;

    @FXML
    private Label lblObjetivo;

    @FXML
    private Label lblDuracion;

    @FXML
    private Label lblInicio;

    @FXML
    private Label lblFin;

    @FXML
    private Label lblEstado;

    @FXML
    public void initialize() {

        cargarDatosCliente();

    }

    private void cargarDatosCliente() {

        int usuarioId = LoginController.usuarioIdActual;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = """
                    SELECT
                        c.nombre,
                        c.edad,
                        c.peso,
                        r.nombre AS rutina,
                        r.objetivo,
                        r.duracion,
                        a.fecha_inicio,
                        a.fecha_fin,
                        a.estado

                    FROM clientes c

                    LEFT JOIN asignaciones a
                    ON c.id = a.cliente_id

                    LEFT JOIN rutinas r
                    ON a.rutina_id = r.id

                    WHERE c.usuario_id = ?
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, usuarioId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                lblBienvenida.setText(rs.getString("nombre"));
                lblNombre.setText(rs.getString("nombre"));
                lblEdad.setText(String.valueOf(rs.getInt("edad")));
                lblPeso.setText(String.valueOf(rs.getDouble("peso")));

                String rutina = rs.getString("rutina");

                if (rutina == null) {

                    lblRutina.setText("Sin rutina asignada");
                    lblObjetivo.setText("-");
                    lblDuracion.setText("-");
                    lblInicio.setText("-");
                    lblFin.setText("-");
                    lblEstado.setText("Sin asignar");

                } else {

                    lblRutina.setText(rutina);
                    lblObjetivo.setText(rs.getString("objetivo"));
                    lblDuracion.setText(rs.getInt("duracion") + " minutos");
                    lblInicio.setText(String.valueOf(rs.getDate("fecha_inicio")));
                    lblFin.setText(String.valueOf(rs.getDate("fecha_fin")));
                    lblEstado.setText(rs.getString("estado"));

                }

            } else {

                System.out.println("No se encontraron datos del cliente");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @FXML
    private void logout() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/gymapp/views/login.fxml")
            );

            Stage stage = (Stage) javafx.stage.Window.getWindows()
                    .filtered(w -> w.isShowing())
                    .get(0);

            stage.setScene(new Scene(loader.load()));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}