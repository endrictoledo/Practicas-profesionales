/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import practicasprofesionales.modelo.pojo.ReporteEstudiante;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.modelo.servicios.EvaluacionReporteService;
import practicasprofesionales.utilidades.Utilidades;

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
    @FXML
    private Label lbl_fecha;
    
    private ReporteEstudiante reporte;
    private EvaluacionReporteService service;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializarDatos(ReporteEstudiante reporte) {
        this.reporte = reporte;
        this.service = new EvaluacionReporteService();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lbl_fecha.setText(LocalDate.now().format(formato));

        if (reporte.getCalificacion() != null && !reporte.getCalificacion().equals("N/A")) {
            txt_calificacion.setText(reporte.getCalificacion());
        }
        if (reporte.getObservacion() != null) {
            txt_observaciones.setText(reporte.getObservacion());
        }
    }

    @FXML
    private void btn_descargar(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar documento");
            fileChooser.setInitialFileName(reporte.getNombreDocumento() + "_" + reporte.getNombreEstudiante() + ".pdf");

            File file = fileChooser.showSaveDialog(btn_descargar.getScene().getWindow());
            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(reporte.getArchivoFisico());
                }
                Utilidades.mostrarAlertaSimple("Éxito", "Descarga exitosa", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "Error al descargar el archivo", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_guardarOnAction(ActionEvent event) {
        String cal = txt_calificacion.getText();
        String obs = txt_observaciones.getText();

        RespuestaOperacion respuesta = service.asignarCalificacion(reporte, cal, obs);

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            regresarAListaReportes();
        } else {
            Utilidades.mostrarAlertaSimple("Valor inválido", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void regresarAListaReportes() {
        try {
            Pane parentPane = (Pane) btn_descargar.getScene().lookup("#pn_principal");
            Region vista = (Region) FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/evaluarreporte/GUIListaReportes.fxml"));
            vista.prefWidthProperty().bind(parentPane.widthProperty());
            vista.prefHeightProperty().bind(parentPane.heightProperty());
            parentPane.getChildren().clear();
            parentPane.getChildren().add(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
