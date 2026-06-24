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
public class ControladorGUIAsignarProyecto implements Initializable {

    @FXML
    private TableView<SolicitudEstudiante> tbl_ProyectosPrioridad;
    @FXML
    private TableColumn<SolicitudEstudiante, Integer> tbc_Prioridad;
    @FXML
    private TableColumn<SolicitudEstudiante, String> tbc_NombreProyecto;
    @FXML
    private TableColumn<SolicitudEstudiante, Integer> tbc_Cupo;
    @FXML
    private DatePicker dtp_FechaInicio;
    @FXML
    private DatePicker dtp_FechaFin;
    private SolicitudPractica solicitudSeleccionada;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbc_Prioridad.setCellValueFactory(
                new PropertyValueFactory<>("prioridad"));
        tbc_NombreProyecto.setCellValueFactory(
                new PropertyValueFactory<>("nombreProyecto"));
        tbc_Cupo.setCellValueFactory(
                new PropertyValueFactory<>("cupoProyecto"));
    }
    
    @FXML
    private void btn_Cancelar(ActionEvent event) {
        cerrar();
    }

    @FXML
    private void btn_Asignar(ActionEvent event) {
        SolicitudEstudiante seleccion =
                tbl_ProyectosPrioridad.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            Utilidades.mostrarAlertaSimple("Error", "Selecciona un proyecto.",
                    Alert.AlertType.WARNING);
            return;
        }
        LocalDate fechaInicio = dtp_FechaInicio.getValue();
        LocalDate fechaFin = dtp_FechaFin.getValue();
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
        
        if (fechaInicio.isBefore(LocalDate.now())) {
            Utilidades.mostrarAlertaSimple("Fecha en el pasado", 
                    "La fecha de inicio es demasiado antigua"
                    + " Por favor revisa el calendario.",
                    Alert.AlertType.WARNING);
            return;
        }
         
        long diasDuracion = java.time.temporal.ChronoUnit.DAYS.between(
                                                        fechaInicio, fechaFin);
        if (diasDuracion < 90) {
            Utilidades.mostrarAlertaSimple("Duración Inválida", 
                    "Las prácticas exigen 420 horas."
                    + "El lapso seleccionado es de solo " + diasDuracion 
                    + " días. "
                    + "Debe durar por lo menos 3 a 6 meses.",
                    Alert.AlertType.WARNING);
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
                        "Asignado correctamente. "
                        + "El cupo se ha actualizado y "
                        + "la solicitud fue marcada como ASIGNADA.",
                        Alert.AlertType.INFORMATION);
                
                try {
                    generarPDF(seleccion, fechaInicio, fechaFin);
                } catch (Exception exPDF) {
                    Utilidades.mostrarAlertaSimple("Aviso del Documento", 
                            "La asignación se guardó "
                          + "pero hubo un problema al crear el PDF."
                          + " Asegúrate de tener cerrado otro archivo en PDF.",
                            Alert.AlertType.WARNING);
                }
                cerrar();
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de Base de Datos", "Ocurrió un error al registrar: " + e.getMessage(), Alert.AlertType.ERROR);
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
        tbl_ProyectosPrioridad.setItems(FXCollections.observableArrayList(
                solicitudSeleccionada.getOpcionesProyecto()));
        }
    }
    
    private void cerrar(){
        Stage stage = (Stage) tbl_ProyectosPrioridad.getScene().getWindow();
        stage.close();
    }
}
