/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
        btn_seleccionarArchivo.setOnAction(this::btn_seleccionarArchivoOnAction);
    }    

    private void btn_seleccionarArchivoOnAction(javafx.event.ActionEvent event) {
        try {
            // Obtener el contenedor padre usando lookup
            javafx.scene.layout.Pane parentPane = (javafx.scene.layout.Pane) btn_seleccionarArchivo.getScene().lookup("#pn_principal");
            if (parentPane == null) {
                System.err.println("No se encontró el pn_principal en la escena.");
                return;
            }
            
            javafx.scene.layout.Region subVista = (javafx.scene.layout.Region) javafx.fxml.FXMLLoader.load(getClass().getResource("/practicasprofesionales/vista/anadirdocumentospractica/GUIListaSubirDocumentos.fxml"));
            
            subVista.prefWidthProperty().bind(parentPane.widthProperty());
            subVista.prefHeightProperty().bind(parentPane.heightProperty());
            
            parentPane.getChildren().clear();
            parentPane.getChildren().add(subVista);
        } catch (java.io.IOException e) {
            System.err.println("Error al cargar la subvista de Lista Subir Documentos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
