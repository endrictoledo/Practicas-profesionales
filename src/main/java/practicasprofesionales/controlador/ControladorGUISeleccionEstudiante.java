/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUISeleccionEstudiante implements Initializable {

    @FXML
    private TextField txt_buscar;
    @FXML
    private Button btn_buscar;
    @FXML
    private VBox vb_listaEstudiantes;
    @FXML
    private StackPane pn_panelBlancoDerecho;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vb_listaEstudiantes.getChildren().clear();
    }    

    public void initData(String tipoReporte) {
        try {
            practicasprofesionales.modelo.servicios.EvaluacionReporteService service = new practicasprofesionales.modelo.servicios.EvaluacionReporteService();
            java.util.List<practicasprofesionales.modelo.pojo.ReporteEstudiante> reportes = service.obtenerReportesPorTipo(tipoReporte);
            
            vb_listaEstudiantes.getChildren().clear();
            
            if (reportes.isEmpty()) {
                javafx.scene.control.Label lblEmpty = new javafx.scene.control.Label("No hay estudiantes que hayan entregado este reporte aún.");
                lblEmpty.setStyle("-fx-text-fill: #64748b; -fx-padding: 20;");
                vb_listaEstudiantes.getChildren().add(lblEmpty);
                return;
            }

            for (practicasprofesionales.modelo.pojo.ReporteEstudiante reporte : reportes) {
                Button btn = new Button(reporte.getNombreEstudiante() + " - " + reporte.getMatricula());
                btn.setStyle("-fx-background-color: white; -fx-border-color: #cbd5e1; -fx-padding: 10 20; -fx-cursor: hand; -fx-text-fill: #334155; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;");
                btn.setMaxWidth(Double.MAX_VALUE);
                
                btn.setOnAction(e -> {
                    if (reporte.getArchivoFisico() == null || reporte.getArchivoFisico().length == 0) {
                        Utilidades.mostrarAlertaSimple("Error", "Reporte no encontrado. Intente de nuevo", javafx.scene.control.Alert.AlertType.ERROR);
                        return; // EX1
                    }
                    cargarAsignarCalificacion(reporte);
                });
                
                vb_listaEstudiantes.getChildren().add(btn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarAsignarCalificacion(practicasprofesionales.modelo.pojo.ReporteEstudiante reporte) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/practicasprofesionales/vista/evaluarreporte/GUIAsignarCalificacion.fxml"));
            javafx.scene.layout.Region subVista = loader.load();
            
            ControladorGUIAsignarCalificacion controlador = loader.getController();
            controlador.initData(reporte);
            
            subVista.prefWidthProperty().bind(pn_panelBlancoDerecho.widthProperty());
            subVista.prefHeightProperty().bind(pn_panelBlancoDerecho.heightProperty());
            
            pn_panelBlancoDerecho.getChildren().clear();
            pn_panelBlancoDerecho.getChildren().add(subVista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_asignarProrroga(ActionEvent event) {
        // Funcionalidad no requerida por el momento según solicitud del usuario.
    }
}
