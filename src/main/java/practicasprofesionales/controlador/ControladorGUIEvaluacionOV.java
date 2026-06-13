package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

/**
 * Controlador para la pantalla de Evaluación de Organización Vinculada
 */
public class ControladorGUIEvaluacionOV implements Initializable {

    @FXML
    private ToggleGroup tg_satisfaccion;
    @FXML
    private ToggleGroup tg_involucramiento;
    @FXML
    private ToggleGroup tg_objetivos;
    @FXML
    private TextArea txt_sugerencia;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_finalizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de componentes si es necesario
    }    

    @FXML
    private void btn_finalizarOnAction(ActionEvent event) {
        // Lógica futura para guardar en base de datos.
        // Ejemplo de cómo obtener las respuestas:
        // RadioButton seleccionadoSatisfaccion = (RadioButton) tg_satisfaccion.getSelectedToggle();
        // String comentario = txt_sugerencia.getText();
        System.out.println("Finalizando evaluación...");
    }

    @FXML
    private void btn_cancelarOnAction(ActionEvent event) {
        // Lógica para cerrar o regresar al menú anterior
        System.out.println("Evaluación cancelada.");
    }
}
