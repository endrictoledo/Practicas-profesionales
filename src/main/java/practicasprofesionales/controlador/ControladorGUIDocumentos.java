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
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIDocumentos implements Initializable {

    @FXML
    private Button btn_seleccionarArchivo;
    @FXML
    private Button btn_verFormatos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void btn_seleccionarArchivo(ActionEvent event) {
        try {
            //Me sirve para obetener el contenedor padre usando el método lookup
            Pane panelPadre = (Pane) btn_seleccionarArchivo.getScene().lookup("#pn_principal");
            if (panelPadre == null) {
                System.err.println("No se encontró el pn_principal en la escena.");
                return;
            }

            Region subVista = (Region) FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/anadirdocumentospractica/GUIListaSubirDocumentos.fxml"));

            subVista.prefWidthProperty().bind(panelPadre.widthProperty());
            subVista.prefHeightProperty().bind(panelPadre.heightProperty());

            panelPadre.getChildren().clear();
            panelPadre.getChildren().add(subVista);
        } catch (java.io.IOException e) {
            System.err.println("Error al cargar la nueva vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
