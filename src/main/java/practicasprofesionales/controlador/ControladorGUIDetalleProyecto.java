package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controlador para la vista del detalle completo de un proyecto
 */
public class ControladorGUIDetalleProyecto implements Initializable {

    @FXML
    private Label lbl_nombreProyecto;
    @FXML
    private Label lbl_responsabilidades;
    @FXML
    private Label lbl_objetivo;
    @FXML
    private Label lbl_actividades;
    @FXML
    private Label lbl_duracion;
    @FXML
    private Label lbl_horario;
    @FXML
    private Label lbl_cupos;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_anadir;
    @FXML
    private Label lbl_descripcionGeneral;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void btn_cancelar(ActionEvent event) {
    }

    @FXML
    private void btn_anadir(ActionEvent event) {
    }
}
