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
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIMenuAdministrador implements Initializable {

    @FXML
    private Pane pne_principal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void btn_cerrarSesion(ActionEvent event) {
        Utilidades.cerrarSesion(event);
    }
    
    private void cargarSubVista(String nombreFxml) {
        try {
            Parent subVista = FXMLLoader.load(getClass().getResource(
                    "/practicasprofesionales/vista/" + nombreFxml));
            pne_principal.getChildren().clear();
            pne_principal.getChildren().add(subVista);
        } catch (IOException e) {
            System.err.println(
                    "Error: No se pudo cargar el archivo FXML: " +
                    nombreFxml);
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_registrarCoordinador(ActionEvent event) {
        cargarSubVista("registrarcoordinador/GUIRegistrarCoordinador.fxml");
    }

    @FXML
    private void btn_registrarProfesor(ActionEvent event) {
    }

    @FXML
    private void btn_usuairos(ActionEvent event) {
    }

    @FXML
    private void btn_Buzon(ActionEvent event) {
    }

}