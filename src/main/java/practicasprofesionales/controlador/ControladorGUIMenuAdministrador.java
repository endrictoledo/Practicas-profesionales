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
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUIMenuAdministrador implements Initializable {

    @FXML
    private MenuItem mniUsuarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializaciones al abrir el menú administrador
    }

    @FXML
    private void mniUsuarios(ActionEvent event) {
        cargarVentanaRegistroCoordinador();
    }
    
    private void cargarVentanaRegistroCoordinador() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource(
                  "/practicasprofesionales/vista/registrarcoordinador/"
                + "FXMLRegistrarCoordinador.fxml"));
            Parent vista = cargador.load();
            Scene scene = new Scene(vista);
            Stage stageRegistro = new Stage();
            stageRegistro.setScene(scene);
            stageRegistro.setTitle("Registrar Coordinador");
            stageRegistro.initModality(Modality.APPLICATION_MODAL);
            stageRegistro.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar la ventana de Registro"
                             + " de Coordinador: " + e.getMessage());
            e.printStackTrace();
        }
    }
}