/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

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
        practicasprofesionales.utilidades.Utilidades.cerrarSesion(event);
    }

    @FXML
    private void btn_reportes(ActionEvent event) {
    }

    @FXML
    private void btn_solicitarProyecto(ActionEvent event) {
    }

    @FXML
    private void btn_documentosIniciales(ActionEvent event) {
    }

    @FXML
    private void btn_evaluarOV(ActionEvent event) {
        String urlMicrosoftForms = "https://forms.office.com/r/TU_ENLACE_AQUI";

        // Verificamos si la computadora soporta la acción de abrir un navegador
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                // Le pedimos al sistema operativo que abra el enlace
                Desktop.getDesktop().browse(new URI(urlMicrosoftForms));
            } catch (Exception e) {
                System.err.println("Error al intentar abrir el navegador: " + e.getMessage());
                // Aquí podrías mostrar un pequeño Label de error o una Alerta al usuario
            }
        } else {
            System.err.println("La función de abrir enlaces no es compatible en este sistema.");
        }
    }

}
