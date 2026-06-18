/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import practicasprofesionales.modelo.dao.AsignacionDAO;
import practicasprofesionales.modelo.pojo.SolicitudEstudiante;
import practicasprofesionales.modelo.pojo.SolicitudPractica;
import practicasprofesionales.utilidades.GeneradorPDF;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author basa2
 */
public class FXMLAsignarProyectoController implements Initializable {

    @FXML
    private TableView<SolicitudEstudiante> tblProyectosPrioridad;
    @FXML
    private TableColumn<SolicitudEstudiante, Integer> colPrioridad;
    @FXML
    private TableColumn<SolicitudEstudiante, String> colNombreProyecto;
    @FXML
    private TableColumn<SolicitudEstudiante, Integer> colCupo;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    private SolicitudPractica solicitudSeleccionada;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPrioridad.setCellValueFactory(
                new PropertyValueFactory<>("prioridad"));
        colNombreProyecto.setCellValueFactory(
                new PropertyValueFactory<>("nombreProyecto"));
        colCupo.setCellValueFactory(
                new PropertyValueFactory<>("cupoProyecto"));
    }
    
    @FXML
    private void btnCancelar(ActionEvent event) {
        Stage stage = (Stage) tblProyectosPrioridad.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnAsignar(ActionEvent event) {
        SolicitudEstudiante seleccion = tblProyectosPrioridad.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            Utilidades.mostrarAlertaSimple("Error", "Selecciona un proyecto.",
                    Alert.AlertType.WARNING);
            return;
        }
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();
        if (fechaInicio == null || fechaFin == null) {
            Utilidades.mostrarAlertaSimple("Error",
                    "Debes seleccionar ambas fechas (inicio y fin).",
                    Alert.AlertType.ERROR);
            return;
        }
        if (fechaFin.isBefore(fechaInicio)) {
            Utilidades.mostrarAlertaSimple("Error",
                    "La fecha de fin no puede ser anterior a la de inicio.",
                    Alert.AlertType.ERROR);
            return;
        }
        if (seleccion.getProyecto().getCupo() <= 0) {
            Utilidades.mostrarAlertaSimple("Error",
                    "Este proyecto ya no tiene cupo disponible.",
                    Alert.AlertType.ERROR);
            return;
        }
        try {
            boolean exito = AsignacionDAO.registrarAsignacion(
                seleccion.getProyecto().getIdProyecto(),
                solicitudSeleccionada.getIdEstudiante(),
                fechaInicio.toString(),
                fechaFin.toString()
            );

            if (exito) {
                Utilidades.mostrarAlertaSimple("Éxito", 
                        "Asignado correctamente.", Alert.AlertType.INFORMATION);
                generarPDF(seleccion, fechaInicio, fechaFin);
                btnCancelar(event);
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error",
                    "Error en BD: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public SolicitudPractica getSolicitudSeleccionada() {
        return solicitudSeleccionada;
    }
    
    public void generarPDF(SolicitudEstudiante seleccion, LocalDate fechaInicio,
                                                           LocalDate fechaFin) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                                                              "PDF", "*.pdf"));
        fileChooser.setInitialFileName("CartaAceptacion_" +
                          solicitudSeleccionada.getNombreEstudiante() + ".pdf");

        File archivoDestino = fileChooser.showSaveDialog(null);

        if (archivoDestino != null) {
            try {
                GeneradorPDF.generarCartaAceptacion(solicitudSeleccionada,
                              seleccion, fechaInicio, fechaFin, archivoDestino);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivoDestino);
                }
            } catch (Exception e) {
                Utilidades.mostrarAlertaSimple("Error",
                        "No se pudo generar el PDF: " + e.getMessage(),
                        Alert.AlertType.ERROR);
            }
        }
    }

    public void setSolicitudSeleccionada(
                                      SolicitudPractica solicitudSeleccionada) {
        this.solicitudSeleccionada = solicitudSeleccionada;
        if (solicitudSeleccionada != null &&
                solicitudSeleccionada.getOpcionesProyecto() != null) {
        tblProyectosPrioridad.setItems(FXCollections.observableArrayList(
                solicitudSeleccionada.getOpcionesProyecto()));
        }
    }
}
