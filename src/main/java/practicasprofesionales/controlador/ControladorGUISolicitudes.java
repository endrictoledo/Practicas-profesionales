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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.SolicitudPracticaDAO;
import practicasprofesionales.modelo.pojo.SolicitudPractica;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author basa2
 */
public class ControladorGUISolicitudes implements Initializable {

    @FXML
    private TableView<SolicitudPractica> tbl_Solicitudes;
    @FXML
    private TableColumn<SolicitudPractica, String> tbc_Estudiante;
    @FXML
    private TableColumn<SolicitudPractica, String> tbc_Matricula;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbc_Matricula.setCellValueFactory(new PropertyValueFactory<>(
                "matricula"));
        tbc_Estudiante.setCellValueFactory(new PropertyValueFactory<>(
                "nombreEstudiante"));
        cargarSolicitudes();
        javafx.scene.control.Label mensajeVacio = 
                    new javafx.scene.control.Label(
                                "No hay solicitudes pendientes por revisar.");
        tbl_Solicitudes.setPlaceholder(mensajeVacio);
    }
    
    private void cargarSolicitudes() {
        try {
            ArrayList<SolicitudPractica> lista = 
                    SolicitudPracticaDAO.obtenerTodasLasSolicitudes();
            tbl_Solicitudes.setItems(FXCollections.observableArrayList(lista));
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error",
                    "No se pudieron cargar las solicitudes: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnVerDetalles(ActionEvent event) {
        SolicitudPractica seleccionada = 
                tbl_Solicitudes.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Utilidades.mostrarAlertaSimple("Selección",
                    "Selecciona una solicitud primero.",
                    Alert.AlertType.WARNING);
            return;
        }
        abrirDetalleSolicitud(seleccionada);
    }
    
    private void abrirDetalleSolicitud(SolicitudPractica solicitud) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                                "/practicasprofesionales/vista/"
                                + "asignarproyecto/GUIAsignarProyecto.fxml"));
            Parent root = loader.load();
            ControladorGUIAsignarProyecto controller = loader.getController();
            controller.setSolicitudSeleccionada(solicitud);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Asignar Proyecto");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarSolicitudes();
        } catch (IOException e) {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo abrir la ventana de asignación.", 
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
}
