/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

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
            //Averigua que bot+on fue presionado 
            Node fuente = (Node) event.getSource();
            Pane parentPane = (Pane) fuente.getScene().lookup("#pn_principal");
            if (parentPane == null) return;
            
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/practicasprofesionales/vista/evaluarreporte/GUISeleccionEstudiante.fxml"));
            Region subVista = cargador.load();
            
            ControladorGUISeleccionEstudiante controlador = cargador.getController();
            controlador.inicializarDatos(tipoReporte);
            
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
