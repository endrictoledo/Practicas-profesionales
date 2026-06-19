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
 * 
 */
public class ControladorGUIMenuCoordinador implements Initializable {

    @FXML
    private Pane pn_principal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void accionBtnRegistrarProyecto(ActionEvent event) {
        cargarSubVista("registrarproyecto/FXMLRegistrarProyecto.fxml");
    }

    @FXML
    private void accionBtnRegistrarOrganizacion(ActionEvent event) {
        cargarSubVista("registrarorganizacionvinculada/GUIRegistroOV.fxml");
    }
    
    @FXML
    private void accionBtnRegistrarPracticante(ActionEvent event){
        cargarSubVista("registrarpracticante/FXMLRegistrarPracticante.fxml");
    }
    
    @FXML
    private void btnSolicitudesProyecto(ActionEvent event) {
        cargarSubVista("asignarproyecto/FXMLSolicitudes.fxml");
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

    @FXML
    private void btn_cerrar(ActionEvent event) {
        Utilidades.cerrarSesion(event);
    }

}
