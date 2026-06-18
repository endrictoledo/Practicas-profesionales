/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionales.modelo.dao.EncargadoDAO;
import practicasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import practicasprofesionales.modelo.dao.ProyectoDAO;
import practicasprofesionales.modelo.pojo.Encargado;
import practicasprofesionales.modelo.pojo.OrganizacionVinculada;
import practicasprofesionales.modelo.pojo.Proyecto;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author basa2
 */
public class FXMLRegistrarProyectoController implements Initializable {
    @FXML
    private TextField txfNombreProyecto;
    @FXML
    private TextArea txaDescripcion;
    @FXML
    private TextField txfObjetivo;
    @FXML
    private TextField txfNombreEncargado;
    @FXML
    private TextField txfApellidoPaEncargado;
    @FXML
    private TextField txfCupo;
    @FXML
    private TextField txfApellidoMaEncargado;
    @FXML
    private TextField txfCargo;
    @FXML
    private TextField txfCorreoEncargado;
    @FXML
    private ComboBox<OrganizacionVinculada> cbxOrganizaciones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarOrganizaciones();
    }
    
    private void cargarOrganizaciones() {
        try {
            ArrayList<OrganizacionVinculada> organizacionesBD = 
                    OrganizacionVinculadaDAO.obtenerOrganizaciones();
            ObservableList<OrganizacionVinculada> listaOrganizaciones = 
                    FXCollections.observableArrayList(organizacionesBD);
            cbxOrganizaciones.setItems(listaOrganizaciones);
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de conexión", 
                "No se pudo cargar la lista de organizaciones vinculadas.", 
                Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnRegistrar(ActionEvent event) {
        if (!sonCamposValidos()) {
            return; // El método ya mostró la alerta, cortamos ejecución
        }

        boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Confirmar Registro", 
                "¿Estás seguro de que deseas registrar este proyecto y su encargado?");
        
        if (confirmar) {
            guardarInformacion();
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Cancelar", 
                "¿Estás seguro de que deseas salir? Los datos no guardados se perderán.");
        if (confirmar) {
            limpiarFormulario();
        }
    }
    
   private void guardarInformacion() {
        int idOrganizacion = cbxOrganizaciones.getValue().getIdOrganizacionVinculada();
        try {
            Encargado encargadoNuevo = serializarEncargado(idOrganizacion);
            int idEncargadoGenerado = EncargadoDAO.registrarEncargado(encargadoNuevo);
            if (idEncargadoGenerado > 0) {
                Proyecto proyectoNuevo = serializarProyecto(idOrganizacion, idEncargadoGenerado);
                int resultadoProyecto = ProyectoDAO.registrarProyecto(proyectoNuevo);
                if (resultadoProyecto > 0) {
                    Utilidades.mostrarAlertaSimple("Registro exitoso", 
                            "El proyecto y el encargado se han registrado correctamente.", 
                            Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                } else {
                    Utilidades.mostrarAlertaSimple("Error", "No se pudo registrar el proyecto.", Alert.AlertType.ERROR);
                }
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo registrar al encargado.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de Base de Datos", 
                "Ha ocurrido un error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Encargado serializarEncargado(int idOrganizacion) {
        Encargado encargado = new Encargado();
        encargado.setNombreEncargado(txfNombreEncargado.getText().trim());
        encargado.setApellidoPaterno(txfApellidoPaEncargado.getText().trim());
        encargado.setApellidoMaterno(txfApellidoMaEncargado.getText().trim());
        encargado.setCargo(txfCargo.getText().trim());
        encargado.setCorreoElectronico(txfCorreoEncargado.getText().trim());
        encargado.setIdOrganizacionVinculada(idOrganizacion);
        return encargado;
    }

    private Proyecto serializarProyecto(int idOrganizacion, int idEncargado) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(txfNombreProyecto.getText().trim());
        proyecto.setDescripcion(txaDescripcion.getText().trim());
        proyecto.setObjetivo(txfObjetivo.getText().trim());
        proyecto.setCupo(Integer.parseInt(txfCupo.getText().trim()));
        proyecto.setIdOrganizacionVinculada(idOrganizacion);
        proyecto.setIdEncargado(idEncargado);
        return proyecto;
    }
    
    private boolean sonCamposValidos() {
        String nombreP = txfNombreProyecto.getText().trim();
        String descP = txaDescripcion.getText().trim();
        String objP = txfObjetivo.getText().trim();
        String cupoStr = txfCupo.getText().trim();
        String nombreE = txfNombreEncargado.getText().trim();
        String apellidoPaE = txfApellidoPaEncargado.getText().trim();
        String apellidoMaE = txfApellidoMaEncargado.getText().trim();
        String cargo = txfCargo.getText().trim();
        String correo = txfCorreoEncargado.getText().trim();
        if (cbxOrganizaciones.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Organización requerida",
                    "Por favor selecciona una Organización Vinculada.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (nombreP.isEmpty() || descP.isEmpty() ||
                objP.isEmpty() || cupoStr.isEmpty() ||
                nombreE.isEmpty() || apellidoPaE.isEmpty() ||
                cargo.isEmpty() || correo.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos vacíos",
                    "Por favor completa todos los campos obligatorios.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            int cupoValue = Integer.parseInt(cupoStr);
            if (cupoValue <= 0) {
                Utilidades.mostrarAlertaSimple("Cupo inválido", 
                        "El cupo debe ser un número mayor a cero.",
                        Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Formato de cupo", "El cupo debe ser un número entero válido.", Alert.AlertType.WARNING);
            return false;
        }
        
        if (nombreP.length() > 45 || objP.length() > 45 
                                                    || descP.length() > 255) {
            Utilidades.mostrarAlertaSimple("Límite excedido",
                    "La información del proyecto excede los límites "
                        + "de caracteres permitidos.", Alert.AlertType.WARNING);
            return false;
        }
        if (!nombreE.matches("\\D+") || !apellidoPaE.matches("\\D+") ||
                    (!apellidoMaE.isEmpty() && !apellidoMaE.matches("\\D+"))) {
            Utilidades.mostrarAlertaSimple("Formato incorrecto",
              "El nombre y apellidos del encargado no deben contener números.",
               Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private void cerrar() {
        Stage stageActual = (Stage) txfNombreProyecto.getScene().getWindow();
        stageActual.close();
    }
    
    private void limpiarFormulario() {
        txfNombreProyecto.clear();
        txaDescripcion.clear();
        txfObjetivo.clear();
        txfCupo.clear();
        cbxOrganizaciones.getSelectionModel().clearSelection();
        txfNombreEncargado.clear();
        txfApellidoPaEncargado.clear();
        txfApellidoMaEncargado.clear();
        txfCargo.clear();
        txfCorreoEncargado.clear();
    }
}
