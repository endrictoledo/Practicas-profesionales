/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionales.modelo.dao.FormatoDocumentoDAO;
import practicasprofesionales.modelo.pojo.FormatoDocumentacion;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author basa2
 */
public class ControladorGUIListaFormatos implements Initializable {

    @FXML 
    private TableView<FormatoDocumentacion> tbl_Formatos;
    @FXML
    private TableColumn<FormatoDocumentacion, String> tbc_Nombre;
    @FXML 
    private TableColumn<FormatoDocumentacion, String> tbc_Estado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarFormatos();
    }  
    
    private void configurarTabla() {
        tbc_Nombre.setCellValueFactory(new PropertyValueFactory<>(
                                                        "nombreArchivo"));
        tbc_Estado.setCellValueFactory(new PropertyValueFactory<>(
                                                        "nombreTipoDocumento"));
        tbl_Formatos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                descargarArchivoSeleccionado();
            }
        });
    }
    
    @FXML
    private void btn_AgregarDocumento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/practicasprofesionales/"
                  + "vista/añadirformato/GUIAgregarFormato.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agregar Formato");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarFormatos(); 

        } catch (IOException e) {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudo abrir la ventana para agregar formato.", 
                Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void cargarFormatos() {
        try {
            ArrayList<FormatoDocumentacion> listaFormatos = 
                                          FormatoDocumentoDAO.obtenerFormatos();
            ObservableList<FormatoDocumentacion> listaObservable = 
                               FXCollections.observableArrayList(listaFormatos);
            tbl_Formatos.setItems(listaObservable);
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudieron cargar los formatos: " + e.getMessage(), 
                Alert.AlertType.ERROR);
        }
    }
    
        private void descargarArchivoSeleccionado() {
        FormatoDocumentacion seleccion = 
                            tbl_Formatos.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            return;
        }
        try {
            byte[] archivoBytes = FormatoDocumentoDAO.obtenerArchivoPorId(
                                                      seleccion.getIdFormato());
            if (archivoBytes != null && archivoBytes.length > 0) {
                javafx.stage.FileChooser fileChooser = 
                                                new javafx.stage.FileChooser();
                fileChooser.setTitle("Guardar Formato");
                fileChooser.setInitialFileName(seleccion.getNombreArchivo());
                
                java.io.File archivoDestino = fileChooser.showSaveDialog(
                                        tbl_Formatos.getScene().getWindow());
                if (archivoDestino != null) {
                    try (java.io.FileOutputStream fileimput = 
                                 new java.io.FileOutputStream(archivoDestino)) {
                        fileimput.write(archivoBytes);
                    }
                    Utilidades.mostrarAlertaSimple("Éxito", 
                            "El formato se ha descargado correctamente en tu computadora.", 
                            Alert.AlertType.INFORMATION);
                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop.getDesktop().open(archivoDestino);
                    }
                }
            } else {
                Utilidades.mostrarAlertaSimple("Error", 
                        "El archivo no existe o está dañado en la base de datos.", 
                        Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de descarga", 
                    "Ocurrió un error al intentar descargar el archivo: " 
                    + e.getMessage(), 
                    Alert.AlertType.ERROR);
        } catch (IOException es){
            Utilidades.mostrarAlertaSimple("Error de descarga", 
                    "Ocurrió un error al intentar descargar el archivo: " 
                    + es.getMessage(), 
                    Alert.AlertType.ERROR);
        }
    }
    
}
