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
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class ControladorGUISubirDocumento implements Initializable {

    @FXML
    private TextArea txt_contenido;
    @FXML
    private StackPane pn_dropzone;
    @FXML
    private Button btn_seleccionarDocumento;
    @FXML
    private Button btn_guardarBorrador;
    @FXML
    private Button btn_subir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
