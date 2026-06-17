/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIAsignarCalificacion implements Initializable {

    @FXML
    private Button btn_descargar;
    @FXML
    private TextField txt_calificacion;
    @FXML
    private TextArea txt_observaciones;
    @FXML
    private Button btn_guardar;

    private practicasprofesionales.modelo.pojo.ReporteEstudiante reporte;
    private practicasprofesionales.modelo.servicios.EvaluacionReporteService service;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initData(practicasprofesionales.modelo.pojo.ReporteEstudiante reporte) {
        this.reporte = reporte;
        this.service = new practicasprofesionales.modelo.servicios.EvaluacionReporteService();

        if (reporte.getCalificacion() != null && !reporte.getCalificacion().equals("N/A")) {
            txt_calificacion.setText(reporte.getCalificacion());
        }
        if (reporte.getObservacion() != null) {
            txt_observaciones.setText(reporte.getObservacion());
        }
    }

    @FXML
    private void btn_descargarOnAction(javafx.event.ActionEvent event) {
        try {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Guardar documento");
            fileChooser.setInitialFileName(reporte.getNombreDocumento() + "_" + reporte.getNombreEstudiante() + ".pdf");

            java.io.File file = fileChooser.showSaveDialog(btn_descargar.getScene().getWindow());
            if (file != null) {
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                    fos.write(reporte.getArchivoFisico());
                }
                practicasprofesionales.utilidades.Utilidades.mostrarAlertaSimple("Éxito", "Descarga exitosa", javafx.scene.control.Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            practicasprofesionales.utilidades.Utilidades.mostrarAlertaSimple("Error", "Error al descargar el archivo", javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_guardarOnAction(javafx.event.ActionEvent event) {
        String cal = txt_calificacion.getText();
        String obs = txt_observaciones.getText();

        RespuestaOperacion respuesta = service.asignarCalificacion(reporte, cal, obs);

        if (!respuesta.isError()) {
            practicasprofesionales.utilidades.Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), javafx.scene.control.Alert.AlertType.INFORMATION);
            regresarAListaReportes();
        } else {
            practicasprofesionales.utilidades.Utilidades.mostrarAlertaSimple("Valor inválido", respuesta.getMensaje(), javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    private void regresarAListaReportes() {
        try {
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) btn_descargar.getScene().lookup("#pn_principal");
            javafx.scene.layout.Region vista = (javafx.scene.layout.Region) javafx.fxml.FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/evaluarreporte/GUIListaReportes.fxml"));
            vista.prefWidthProperty().bind(parentPane.widthProperty());
            vista.prefHeightProperty().bind(parentPane.heightProperty());
            parentPane.getChildren().clear();
            parentPane.getChildren().add(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
