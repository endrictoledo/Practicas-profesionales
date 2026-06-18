package practicasprofesionales.controlador;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import practicasprofesionales.modelo.DTO.Proyecto;
import practicasprofesionales.modelo.DTO.RespuestaOperacion;
import practicasprofesionales.modelo.servicios.SolicitarProyectoService;
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
    private Label lbl_descripcionGeneral;

    private Proyecto proyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void inicializarDatos(Proyecto proyecto) {
        this.proyecto = proyecto;

        if (lbl_nombreProyecto != null) {
            lbl_nombreProyecto.setText(proyecto.getNombre());
        }
        if (lbl_descripcionGeneral != null) {
            lbl_descripcionGeneral.setText(proyecto.getDescripcion());
        }
        if (lbl_objetivo != null) {
            lbl_objetivo.setText(proyecto.getObjetivo());
        }
        if (lbl_responsabilidades != null) {
            lbl_responsabilidades.setText(proyecto.getResponsabilidades());
        }
        if (lbl_actividades != null) {
            lbl_actividades.setText(proyecto.getActividades());
        }
        if (lbl_duracion != null) {
            lbl_duracion.setText(proyecto.getDuracion());
        }
        if (lbl_horario != null) {
            lbl_horario.setText(proyecto.getHorario());
        }
        if (lbl_cupos != null) {
            lbl_cupos.setText(String.valueOf(proyecto.getCupos()));
        }
    }

    @FXML
    private void btn_cancelar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Seguro que desea cancelar?");

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnSi) {
            regresarAListaProyectos(event);
        }
    }

    @FXML
    private void btn_anadir(ActionEvent event) {
        SolicitarProyectoService service = new SolicitarProyectoService();
        RespuestaOperacion respuesta = service.solicitarProyecto(proyecto);

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            regresarAListaProyectos(event);
        } else {
            Utilidades.mostrarAlertaSimple("Atención", respuesta.getMensaje(), Alert.AlertType.WARNING);
            if (respuesta.getMensaje().contains("cupo")) {
                regresarAListaProyectos(event);
            }
        }
    }

    private void regresarAListaProyectos(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Pane parentPane = (Pane) source.getScene().lookup("#pn_principal");

            Region subVista = (Region) FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/solicitarproyecto/GUIListaProyectos.fxml"));
            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());

            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
