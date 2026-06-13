package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * Controlador para la pantalla de éxito al enviar una evaluación.
 */
public class ControladorGUIExitoEvaluacion implements Initializable {

    @FXML
    private Button btn_descargar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Lógica de inicialización
    }    

    @FXML
    private void btn_descargarOnAction(ActionEvent event) {
        // Lógica futura para generar y descargar el documento PDF/Word de la evaluación
        System.out.println("Descargando documento de evaluación...");
    }
}
