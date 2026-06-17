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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIMenuPracticante implements Initializable {
    
    @FXML
    private Pane pn_principal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void btnBuzon(ActionEvent event) {
    }
    
    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Utilidades.cerrarSesion(event);
    }
    
    @FXML
    private void btn_reportes(ActionEvent event) {
    }
    
    @FXML
    private void btn_solicitarProyecto(ActionEvent event) {
        cambiarVista("/practicasprofesionales/vista/solicitarproyecto/GUIListaProyectos.fxml");
    }
    
    @FXML
    private void btn_documentosIniciales(ActionEvent event) {
        cambiarVista("/practicasprofesionales/vista/anadirdocumentospractica/GUIDocumentos.fxml");
    }
    
    @FXML
    private void btn_evaluarOV(ActionEvent event) {
        cambiarVista("/practicasprofesionales/vista/evaluarov/GUIEvaluacionOV.fxml");
    }
    
    private void cambiarVista(String ruta) {
        try {
            Region subVista = (Region) javafx.fxml.FXMLLoader.load(getClass().getResource(ruta));
            
            subVista.prefWidthProperty().bind(pn_principal.widthProperty());
            subVista.prefHeightProperty().bind(pn_principal.heightProperty());
            
            pn_principal.getChildren().clear();
            pn_principal.getChildren().add(subVista);
        } catch (java.io.IOException e) {
            System.err.println("Error al cargar la nueva vista" + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
