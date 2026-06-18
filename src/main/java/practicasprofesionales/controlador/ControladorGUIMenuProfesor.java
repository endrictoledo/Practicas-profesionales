/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIMenuProfesor implements Initializable {

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
    private void btnEvaluarReportes(ActionEvent event) {
        try {
            Region vista = (Region) FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/evaluarreporte/GUIListaReportes.fxml"));
            vista.prefWidthProperty().bind(pn_principal.widthProperty());
            vista.prefHeightProperty().bind(pn_principal.heightProperty());
            pn_principal.getChildren().clear();
            pn_principal.getChildren().add(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnFormatos(ActionEvent event) {
        cargarSubVista("añadirformato/FXMLListaFormatos.fxml");
    }

    @FXML
    private void btnBuzon(ActionEvent event) {
    }

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Utilidades.cerrarSesion(event);
    }

    
    private void cargarSubVista(String nombreFxml) {
        try {
            Parent subVista = FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/" + nombreFxml));
            pn_principal.getChildren().clear();
            pn_principal.getChildren().add(subVista);
        } catch (IOException e) {
            System.err.println("Error crítico: No se pudo cargar el archivo FXML: " + nombreFxml);
            e.printStackTrace();
        }
    }

}
