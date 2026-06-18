package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import practicasprofesionales.modelo.pojo.OrganizacionVinculada;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.servicio.OrganizacionVinculadaService;
import practicasprofesionales.utilidades.Utilidades;

public class ControladorGUIRegistroOV implements Initializable {

 
    @FXML
    private TextField txt_nombreOrg;
    @FXML
    private TextField txt_direccion;
    @FXML
    private ComboBox<String> cb_sector;
    @FXML
    private TextField txt_telefono;
    @FXML
    private TextField txt_correo;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_guardar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> sectores = FXCollections.observableArrayList(
                                                "Público", "Privado", "Social");
        cb_sector.setItems(sectores);
    }

    @FXML
    private void btn_cancelarOnAction(ActionEvent event) {

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que desea cancelar el registro?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                limpiarCampos();
            }
        });
    }

    @FXML
    private void btn_guardarOnAction(ActionEvent event) {
        OrganizacionVinculada ov = new OrganizacionVinculada();
        ov.setRazonSocial(txt_nombreOrg.getText());
        ov.setDireccion(txt_direccion.getText());
        ov.setCorreo(txt_correo.getText());
        ov.setTelefono(txt_telefono.getText());

        if (cb_sector.getValue() != null) {
            ov.setSector(cb_sector.getValue());
        } else {
            ov.setSector("");
        }

        RespuestaOperacion respuesta =
                OrganizacionVinculadaService.guardarOrganizacion(ov);
        validarRespuesta(respuesta);

    }

    private void validarRespuesta(RespuestaOperacion respuesta) {
        if (respuesta.isError()) {
            if (respuesta.getMensaje().contains("Parámetros inválidos")) {
                Utilidades.mostrarAlertaSimple("Error de validación",
                        "Parámetros inválidos o campos incompletos",
                        Alert.AlertType.ERROR);
            } else if (respuesta.getMensaje().contains(
                                                "ya se encuentra registrada")) {
                Utilidades.mostrarAlertaSimple("Organización duplicada",
                   "Esta organización ya se encuentra registrada en el sistema",
                   Alert.AlertType.WARNING);
            } else if (respuesta.getMensaje().contains(
                                   "Formato de correo electrónico no válido")) {
                Utilidades.mostrarAlertaSimple("Formato incorrecto", 
                        "Formato de correo electrónico no válido",
                        Alert.AlertType.ERROR);
                txt_correo.setStyle("-fx-border-color: red; -fx-padding: 10;");
            } else {
                Utilidades.mostrarAlertaSimple("Error", 
                        respuesta.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Registro Exitoso",
                    "Registro exitoso", Alert.AlertType.INFORMATION);
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        txt_nombreOrg.clear();
        txt_direccion.clear();
        cb_sector.getSelectionModel().clearSelection();
        txt_telefono.clear();
        txt_correo.clear();
        txt_correo.setStyle("-fx-padding: 10;");
    }
}
