/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java 
 * to edit this template
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
public class ControladorGUIAgregarFormato implements Initializable {

    @FXML
    private TextField txf_RutaArchivo;
    @FXML 
    private ComboBox<CatalogoDocumento> cbx_TipoDocumento;
    
    private File archivoSeleccionado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTiposDocumento();
    }    

    @FXML
    private void btn_Seleccionar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Documentos (PDF, Word)",
                                        "*.pdf", "*.docx", "*.doc")
                                        );
        Stage stage = (Stage) txf_RutaArchivo.getScene().getWindow();
        archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            txf_RutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    private void btn_Cancelar(ActionEvent event) {
        cerrar();
    }

    @FXML
    private void btn_Guardar(ActionEvent event) {
        if (!validarCampos()) {
            return; 
        }
        CatalogoDocumento seleccion = cbx_TipoDocumento.getValue();
         try {
            if (FormatoDocumentoDAO.existeFormatoDelCatalogo(
                                          seleccion.getIdCatalogoDocumento())) {
                Utilidades.mostrarAlertaSimple("Formato Duplicado", 
                    "Ya existe un formato registrado en el sistema de nombre: " 
                    + seleccion.getNombreDocumento() + ".\n"
                    + "Eliminar la anterior plantilla", 
                    Alert.AlertType.WARNING);
                return;
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de conexión", 
                "No se pudo verificar en la base de datos"
                + " si el formato ya existe: " + e.getMessage(), 
                Alert.AlertType.ERROR);
            return;
        }
        FormatoDocumentacion nuevoFormato = new FormatoDocumentacion();
        nuevoFormato.setIdCatalogoDocumento(seleccion.getIdCatalogoDocumento());
        nuevoFormato.setNombreArchivo(archivoSeleccionado.getName());
        nuevoFormato.setFechaRegistro(java.sql.Date.valueOf(
                                                    java.time.LocalDate.now()));
        try {
            boolean exito = FormatoDocumentoDAO.registrarFormato(nuevoFormato,
                                                           archivoSeleccionado);
            if (exito) {
                Utilidades.mostrarAlertaSimple("Éxito",
                        "Documento guardado correctamente.", 
                        Alert.AlertType.INFORMATION);
                cerrar();
            }
        } catch (SQLException | FileNotFoundException e) {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudo guardar el archivo: " + e.getMessage(),
                Alert.AlertType.ERROR);
        }
    }
    
    private boolean validarCampos() {
        CatalogoDocumento seleccion = cbx_TipoDocumento.getValue();
        if (seleccion == null) {
            Utilidades.mostrarAlertaSimple("Error", 
                    "Selecciona un tipo de documento del menú desplegable.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (archivoSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Error",
                    "Debes seleccionar un archivo de tu computadora para subir.", 
                    Alert.AlertType.WARNING);
            return false;
        }
        
        String nombreArchivo = archivoSeleccionado.getName();
        if (nombreArchivo.length() > 45) {
            Utilidades.mostrarAlertaSimple("Nombre muy largo",
                    "El nombre del archivo (" + nombreArchivo.length() 
                    + " ) supera el máximo de 45 caracteres. "
                  + "Cambie el nombre a uno mas pequeño", 
                    Alert.AlertType.WARNING);
            return false;
        }
        
        long limiteBytes = 20L * 1024 * 1024;
        if (archivoSeleccionado.length() > limiteBytes) {
            Utilidades.mostrarAlertaSimple("Archivo demasiado grande",
                    "El documento excede el límite permitido de 20 MB. "
                  + "Por favor comprime tu archivo o selecciona uno más ligero.", 
                    Alert.AlertType.WARNING);
            return false;
        }
        
        String nombreMinusculas = nombreArchivo.toLowerCase();
        if (!nombreMinusculas.endsWith(".pdf") 
                                        && !nombreMinusculas.endsWith(".docx") 
                                        && !nombreMinusculas.endsWith(".doc")) {
            Utilidades.mostrarAlertaSimple("Formato no permitido",
                    "Solo se permite subir archivos con formato PDF o Word (.pdf, .docx, .doc).", 
                    Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private void cerrar(){
        Stage stage = (Stage) txf_RutaArchivo.getScene().getWindow();
        stage.close();
    }
    private void cargarTiposDocumento() {
        try {
            ArrayList<CatalogoDocumento> tipos = 
                                         CatalogoDocumentoDAO.obtenerCatalogo();
            cbx_TipoDocumento.setItems(FXCollections.observableArrayList(tipos));
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error",
                    "No se pudo cargar el catálogo.", Alert.AlertType.ERROR);
        }
    }
    
}
