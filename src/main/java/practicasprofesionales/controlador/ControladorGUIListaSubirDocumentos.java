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
public class ControladorGUIListaSubirDocumentos implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private void cargarSubirDocumento(String nombreDocumento, ActionEvent event) {
        try {
            javafx.scene.Node source = (javafx.scene.Node) event.getSource();
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) source.getScene().lookup("#pn_principal");
            if (parentPane == null) return;
            
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/practicasprofesionales/vista/anadirdocumentospractica/GUISubirDocumento.fxml"));
            javafx.scene.layout.Region subVista = loader.load();
            
            ControladorGUISubirDocumento controlador = loader.getController();
            controlador.initData(nombreDocumento);
            
            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());
            
            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_verDetallesSolicitud(ActionEvent event) {
        cargarSubirDocumento("Solicitud de práctica", event);
    }

    @FXML
    private void btn_verDetallesOficio(ActionEvent event) {
        cargarSubirDocumento("Oficio de aceptación", event);
    }

    @FXML
    private void btn_verDetallesHorario(ActionEvent event) {
        cargarSubirDocumento("Horario", event);
    }

    @FXML
    private void btn_verDetallesCronograma(ActionEvent event) {
        cargarSubirDocumento("Cronograma", event);
    }

    @FXML
    private void btn_verDetallesAutoevaluación(ActionEvent event) {
        cargarSubirDocumento("Autoevaluación", event);
    }

    @FXML
    private void btn_verDetallesEvaluacionOV(ActionEvent event) {
        cargarSubirDocumento("Evaluación OV", event);
    }
    
}
