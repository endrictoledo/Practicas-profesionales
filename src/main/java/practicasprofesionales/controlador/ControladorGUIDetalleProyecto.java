package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.utilidades.Utilidades;

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

    private practicasprofesionales.modelo.pojo.Proyecto proyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void initData(practicasprofesionales.modelo.pojo.Proyecto proyecto) {
        this.proyecto = proyecto;
        
        if(lbl_nombreProyecto != null) lbl_nombreProyecto.setText(proyecto.getNombre());
        if(lbl_descripcionGeneral != null) lbl_descripcionGeneral.setText(proyecto.getDescripcion());
        if(lbl_objetivo != null) lbl_objetivo.setText(proyecto.getObjetivo());
        if(lbl_responsabilidades != null) lbl_responsabilidades.setText(proyecto.getResponsabilidades());
        if(lbl_actividades != null) lbl_actividades.setText(proyecto.getActividades());
        if(lbl_duracion != null) lbl_duracion.setText(proyecto.getDuracion());
        if(lbl_horario != null) lbl_horario.setText(proyecto.getHorario());
        if(lbl_cupos != null) lbl_cupos.setText(String.valueOf(proyecto.getCupos()));
    }

    @FXML
    private void btn_cancelar(ActionEvent event) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Seguro que desea cancelar?");
        
        javafx.scene.control.ButtonType btnSi = new javafx.scene.control.ButtonType("Sí");
        javafx.scene.control.ButtonType btnNo = new javafx.scene.control.ButtonType("No", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(btnSi, btnNo);
        
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnSi) {
            regresarAListaProyectos(event);
        }
    }

    @FXML
    private void btn_anadir(ActionEvent event) {
        practicasprofesionales.modelo.servicios.SolicitarProyectoService service = new practicasprofesionales.modelo.servicios.SolicitarProyectoService();
        RespuestaOperacion respuesta = service.solicitarProyecto(proyecto);
        
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), javafx.scene.control.Alert.AlertType.INFORMATION);
            regresarAListaProyectos(event);
        } else {
            Utilidades.mostrarAlertaSimple("Atención", respuesta.getMensaje(), javafx.scene.control.Alert.AlertType.WARNING);
            if (respuesta.getMensaje().contains("cupo")) {
                regresarAListaProyectos(event);
            }
        }
    }

    private void regresarAListaProyectos(ActionEvent event) {
        try {
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) source.getScene().lookup("#pn_principal");
            
            javafx.scene.layout.Region subVista = (javafx.scene.layout.Region) javafx.fxml.FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/solicitarproyecto/GUIListaProyectos.fxml"));
            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());
            
            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
