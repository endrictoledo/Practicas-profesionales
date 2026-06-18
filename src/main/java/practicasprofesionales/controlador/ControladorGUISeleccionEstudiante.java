/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import practicasprofesionales.modelo.DTO.ReporteEstudiante;
import practicasprofesionales.modelo.servicios.EvaluacionReporteService;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUISeleccionEstudiante implements Initializable {

    private TextField txt_buscar;
    @FXML
    private VBox vb_listaEstudiantes;
    @FXML
    private StackPane pn_panelBlancoDerecho;

    private List<ReporteEstudiante> listaReportesOriginal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vb_listaEstudiantes.getChildren().clear();

    }

    public void inicializarDatos(String tipoReporte) {
        try {
            EvaluacionReporteService service = new EvaluacionReporteService();
            listaReportesOriginal = service.obtenerReportesPorTipo(tipoReporte);
            mostrarEstudiantes(listaReportesOriginal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarEstudiantes(List<ReporteEstudiante> reportes) {
        vb_listaEstudiantes.getChildren().clear();

        if (reportes.isEmpty()) {
            Label etiquetaVacia = new Label("No hay resultados para mostrar.");
            vb_listaEstudiantes.getChildren().add(etiquetaVacia);
            return;
        }

        for (ReporteEstudiante reporte : reportes) {
            Button btn = new Button(reporte.getNombreEstudiante() + " - " + reporte.getMatricula());
            btn.setMaxWidth(Double.MAX_VALUE);

            btn.setOnAction(e -> {
                if (reporte.getArchivoFisico() == null || reporte.getArchivoFisico().length == 0) {
                    Utilidades.mostrarAlertaSimple("Error", "Reporte no encontrado. Intente de nuevo", Alert.AlertType.ERROR);
                    return;
                }
                cargarAsignarCalificacion(reporte);
            });

            vb_listaEstudiantes.getChildren().add(btn);
        }
    }

    private void cargarAsignarCalificacion(ReporteEstudiante reporte) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionales/vista/evaluarreporte/GUIAsignarCalificacion.fxml"));
            Region subVista = loader.load();

            ControladorGUIAsignarCalificacion controlador = loader.getController();
            controlador.inicializarDatos(reporte);

            subVista.prefWidthProperty().bind(pn_panelBlancoDerecho.widthProperty());
            subVista.prefHeightProperty().bind(pn_panelBlancoDerecho.heightProperty());

            pn_panelBlancoDerecho.getChildren().clear();
            pn_panelBlancoDerecho.getChildren().add(subVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_asignarProrroga(ActionEvent event) {

    }

}
