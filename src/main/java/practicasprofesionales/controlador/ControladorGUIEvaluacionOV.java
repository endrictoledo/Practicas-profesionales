package practicasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import practicasprofesionales.modelo.DTO.RespuestaOperacion;
import practicasprofesionales.modelo.servicios.EvaluacionOVService;
import practicasprofesionales.utilidades.SesionGlobal;
import practicasprofesionales.utilidades.Utilidades;

public class ControladorGUIEvaluacionOV implements Initializable {

    @FXML
    private ToggleGroup tg_satisfaccion;
    @FXML
    private ToggleGroup tg_involucramiento;
    @FXML
    private ToggleGroup tg_objetivos;
    @FXML
    private TextArea txt_sugerencia;
    private Button btn_finalizar;

    private EvaluacionOVService service;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.service = new EvaluacionOVService();
    }

    @FXML
    private void btn_finalizar(ActionEvent event) {
        String satisfaccion = tg_satisfaccion.getSelectedToggle() != null
                ? ((RadioButton) tg_satisfaccion.getSelectedToggle()).getText() : null;

        String involucramiento = tg_involucramiento.getSelectedToggle() != null
                ? ((RadioButton) tg_involucramiento.getSelectedToggle()).getText() : null;

        String objetivos = tg_objetivos.getSelectedToggle() != null
                ? ((RadioButton) tg_objetivos.getSelectedToggle()).getText() : null;

        String sugerencia = txt_sugerencia.getText();

        int idUsuario = SesionGlobal.getInstancia().getUsuarioActual() != null
                ? SesionGlobal.getInstancia().getUsuarioActual().getIdUsuario() : -1;

        RespuestaOperacion respuesta = service.procesarEvaluacion(idUsuario, satisfaccion, involucramiento, objetivos, sugerencia);

        if (respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Campos incompletos", respuesta.getMensaje(), Alert.AlertType.WARNING);
        } else {

            byte[] archivoBytes = (byte[]) respuesta.getDatos();
            navegarPantallaExito(archivoBytes, event);
        }
    }

    @FXML
    private void btn_cancelar(ActionEvent event) {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de salida");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea salir sin guardar los cambios? Los datos ingresados se perderán.");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            limpiarCampos();
        }
    }

    private void navegarPantallaExito(byte[] archivoBytes, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionales/vista/evaluarov/GUIExitoEvaluacion.fxml"));
            Parent root = loader.load();

            ControladorGUIExitoEvaluacion controlador = loader.getController();
            controlador.inicializarDatos(archivoBytes);

            Stage stage = (Stage) btn_finalizar.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la pantalla de éxito.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        if (tg_satisfaccion.getSelectedToggle() != null) {
            tg_satisfaccion.getSelectedToggle().setSelected(false);
        }
        if (tg_involucramiento.getSelectedToggle() != null) {
            tg_involucramiento.getSelectedToggle().setSelected(false);
        }
        if (tg_objetivos.getSelectedToggle() != null) {
            tg_objetivos.getSelectedToggle().setSelected(false);
        }
        txt_sugerencia.clear();
    }

}
