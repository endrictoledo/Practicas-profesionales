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
public class FXMLListaFormatosController implements Initializable {

    @FXML 
    private TableView<FormatoDocumentacion> tblFormatos;
    @FXML
    private TableColumn<FormatoDocumentacion, String> colNombre;
    @FXML 
    private TableColumn<FormatoDocumentacion, String> colEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarFormatos();
    }  
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>(
                                                        "nombreArchivo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>(
                                                        "nombreTipoDocumento"));
    }
    
    @FXML
    private void btnAgregarDocumento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/practicasprofesionales/"
                  + "vista/añadirformato/FXMLAgregarFormato.fxml"));
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
            tblFormatos.setItems(listaObservable);
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudieron cargar los formatos: " + e.getMessage(), 
                Alert.AlertType.ERROR);
        }
    }
    
}
