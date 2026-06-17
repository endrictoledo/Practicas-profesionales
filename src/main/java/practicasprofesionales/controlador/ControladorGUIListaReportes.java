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

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIListaReportes implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private void cargarSeleccionEstudiante(String tipoReporte, ActionEvent event) {
        try {
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) source.getScene().lookup("#pn_principal");
            if (parentPane == null) return;
            
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/practicasprofesionales/vista/evaluarreporte/GUISeleccionEstudiante.fxml"));
            javafx.scene.layout.Region subVista = loader.load();
            
            ControladorGUISeleccionEstudiante controlador = loader.getController();
            controlador.initData(tipoReporte);
            
            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());
            
            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_calificarParcial(ActionEvent event) {
        cargarSeleccionEstudiante("Reporte Parcial", event);
    }

    @FXML
    private void btn_calificarMensual(ActionEvent event) {
        cargarSeleccionEstudiante("Reporte Mensual 1", event);
    }

    @FXML
    private void btn_calificarFinal(ActionEvent event) {
        cargarSeleccionEstudiante("Evaluación Final", event);
    }
}
