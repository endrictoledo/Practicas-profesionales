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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUISeleccionEstudiante implements Initializable {

    @FXML
    private TextField txt_buscar;
    @FXML
    private Button btn_buscar;
    @FXML
    private VBox vb_listaEstudiantes;
    @FXML
    private StackPane pn_panelBlancoDerecho;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btn_asignarProrroga(ActionEvent event) {
    }
    
}
