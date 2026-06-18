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
public class ControladorGUIListaSubirDocumentos implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void cargarSubirDocumento(String nombreDocumento, ActionEvent event) {
        try {
            Node fuente = (Node) event.getSource();
            Pane panelPadre = (Pane) fuente.getScene().lookup("#pn_principal"); //Este escala y descubre cuál es el contenedor central
            if (panelPadre == null) {
                return;
            }

            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/practicasprofesionales/vista/anadirdocumentospractica/GUISubirDocumento.fxml"));
            Region subVista = cargador.load();

            ControladorGUISubirDocumento controlador = cargador.getController();
            controlador.inicializarDatos(nombreDocumento);

            subVista.prefWidthProperty().bind(panelPadre.widthProperty());
            subVista.prefHeightProperty().bind(panelPadre.heightProperty());

            panelPadre.getChildren().clear();
            panelPadre.getChildren().add(subVista);
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
