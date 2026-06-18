package practicasprofesionales.utilidades;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author endri
 */
public class Utilidades {

    public static void mostrarAlertaSimple(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.setHeaderText(null);
        alerta.show();
    }

    public static boolean configurarAlerta(String titulo, String encabezado, String contenido, String btn1, String btn2, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);

        ButtonType btnTrue = new ButtonType(btn1);
        ButtonType btnFalse = new ButtonType(btn2, ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnTrue, btnFalse);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == btnTrue;
    }

    public static void cerrarSesion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Utilidades.class.getResource("/practicasprofesionales/vista/GUIInicioSesion.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Prácticas — Inicio de Sesión");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void navegar(ActionEvent event, String ruta, String titulo) {
        try {
            Parent root = FXMLLoader.load(Utilidades.class.getResource(ruta));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Prácticas — " + titulo);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al navegar a: " + ruta);
            e.printStackTrace();
        }
    }
}
