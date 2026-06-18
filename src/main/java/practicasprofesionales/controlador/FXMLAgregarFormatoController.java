/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import practicasprofesionales.modelo.dao.CatalogoDocumentoDAO;
import practicasprofesionales.modelo.dao.FormatoDocumentoDAO;
import practicasprofesionales.modelo.pojo.CatalogoDocumento;
import practicasprofesionales.modelo.pojo.FormatoDocumentacion;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author basa2
 */
public class FXMLAgregarFormatoController implements Initializable {

    @FXML
    private TextField txfRutaArchivo;
    @FXML 
    private ComboBox<CatalogoDocumento> cbxTipoDocumento;
    
    private File archivoSeleccionado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTiposDocumento();
    }    

    @FXML
    private void btnSeleccionar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Documentos (PDF, Word)",
                                        "*.pdf", "*.docx", "*.doc")
                                        );
        Stage stage = (Stage) txfRutaArchivo.getScene().getWindow();
        archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            txfRutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        Stage stage = (Stage) txfRutaArchivo.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        CatalogoDocumento seleccion = cbxTipoDocumento.getValue();
        if (seleccion == null) {
            Utilidades.mostrarAlertaSimple("Error", 
                    "Selecciona un tipo de documento.",
                    Alert.AlertType.WARNING);
            return;
        }
        if (archivoSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Error",
                    "Debes seleccionar un archivo.", 
                    Alert.AlertType.WARNING);
            return;
        }
        FormatoDocumentacion nuevoFormato = new FormatoDocumentacion();
        nuevoFormato.setIdCatalogoDocumento(seleccion.getIdCatalogoDocumento());
        nuevoFormato.setNombreArchivo(archivoSeleccionado.getName());
        nuevoFormato.setFechaRegistro(java.sql.Date.valueOf(java.time.LocalDate.now()));
        try {
            boolean exito = FormatoDocumentoDAO.registrarFormato(nuevoFormato, archivoSeleccionado);
            if (exito) {
                Utilidades.mostrarAlertaSimple("Éxito", "Documento guardado correctamente.", Alert.AlertType.INFORMATION);
                btnCancelar(event);
            }
        } catch (SQLException | FileNotFoundException e) {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudo guardar el archivo: " + e.getMessage(),
                Alert.AlertType.ERROR);
        }
    }
    
    private void cargarTiposDocumento() {
        try {
            ArrayList<CatalogoDocumento> tipos = 
                                         CatalogoDocumentoDAO.obtenerCatalogo();
            cbxTipoDocumento.setItems(FXCollections.observableArrayList(tipos));
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error",
                    "No se pudo cargar el catálogo.", Alert.AlertType.ERROR);
        }
    }
    
}
