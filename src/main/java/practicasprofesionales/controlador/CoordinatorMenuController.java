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

/**
 * FXML Controller class
 *
 * @author endri
 */
public class CoordinatorMenuController implements Initializable {

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
    private void btnConsultarProfesor(ActionEvent event) {
    }

    @FXML
    private void btn_registrarOrganizacion(ActionEvent event) {
        cargarSubVista("GUIRegistroOV");
    }

    private void cargarSubVista(String nombreFxml) {
        try {

            Parent subVista = FXMLLoader.load(getClass().getResource("/vistas/" + nombreFxml));
            
            pn_principal.getChildren().clear();
            
            pn_principal.getChildren().add(subVista);
            
        } catch (IOException e) {
            System.err.println("Error crítico: No se pudo cargar el archivo FXML -> " + nombreFxml);
            e.printStackTrace();
        }
    }
}
