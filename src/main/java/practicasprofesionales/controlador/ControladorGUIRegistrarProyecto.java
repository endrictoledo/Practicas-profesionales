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
public class ControladorGUIRegistrarProyecto implements Initializable {
    @FXML
    private TextField txf_NombreProyecto;
    @FXML
    private TextArea txf_Descripcion;
    @FXML
    private TextField txf_Objetivo;
    @FXML
    private TextField txf_NombreEncargado;
    @FXML
    private TextField txf_ApellidoPaEncargado;
    @FXML
    private TextField txf_Cupo;
    @FXML
    private TextField txf_ApellidoMaEncargado;
    @FXML
    private TextField txf_Cargo;
    @FXML
    private TextField txf_CorreoEncargado;
    @FXML
    private ComboBox<OrganizacionVinculada> cbx_Organizaciones;

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
            cbx_Organizaciones.setItems(listaOrganizaciones);
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de conexión", 
                "No se pudo cargar la lista de organizaciones vinculadas.", 
                Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_Registrar(ActionEvent event) {
        if (!sonCamposValidos()) {
            return;
        }
        try {
            if(existeProyectoDuplicado()){
                return;
            }
        }catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de Base de Datos", 
                    e.getMessage(), Alert.AlertType.ERROR);
        }
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
                "Confirmar Registro", 
                "¿Estás seguro de que deseas registrar este proyecto y"
                + " su encargado?");
        if (confirmar) {
            guardarInformacion();
        }
    }

    @FXML
    private void btn_Cancelar(ActionEvent event) {
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Cancelar", 
                "¿Estás seguro de que deseas salir? Los datos no guardados"
                + " se perderán.");
        if (confirmar) {
            limpiarFormulario();
        }
    }
    
   private void guardarInformacion() {
        int idOrganizacion = 
                     cbx_Organizaciones.getValue().getIdOrganizacionVinculada();
        try {
            Encargado encargadoNuevo = serializarEncargado(idOrganizacion);
            int idEncargadoGenerado = EncargadoDAO.registrarEncargado(
                                                                encargadoNuevo);
            if (idEncargadoGenerado > 0) {
                Proyecto proyectoNuevo = serializarProyecto(idOrganizacion, 
                                                           idEncargadoGenerado);
                int resultadoProyecto = ProyectoDAO.registrarProyecto(
                                                                 proyectoNuevo);
                if (resultadoProyecto > 0) {
                    Utilidades.mostrarAlertaSimple("Registro exitoso", 
                            "El proyecto y el encargado se han registrado"
                            + " correctamente.", 
                            Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                } else {
                    Utilidades.mostrarAlertaSimple("Error",
                            "No se pudo registrar el proyecto.",
                            Alert.AlertType.ERROR);
                }
            } else {
                Utilidades.mostrarAlertaSimple("Error", 
                        "No se pudo registrar al encargado.",
                        Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de Base de Datos", 
                "Ha ocurrido un error al guardar: " + e.getMessage(), 
                Alert.AlertType.ERROR);
        }
    }

    private Encargado serializarEncargado(int idOrganizacion) {
        Encargado encargado = new Encargado();
        encargado.setNombreEncargado(txf_NombreEncargado.getText().trim());
        encargado.setApellidoPaterno(txf_ApellidoPaEncargado.getText().trim());
        encargado.setApellidoMaterno(txf_ApellidoMaEncargado.getText().trim());
        encargado.setCargo(txf_Cargo.getText().trim());
        encargado.setCorreoElectronico(txf_CorreoEncargado.getText().trim());
        encargado.setIdOrganizacionVinculada(idOrganizacion);
        return encargado;
    }

    private Proyecto serializarProyecto(int idOrganizacion, int idEncargado) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(txf_NombreProyecto.getText().trim());
        proyecto.setDescripcion(txf_Descripcion.getText().trim());
        proyecto.setObjetivo(txf_Objetivo.getText().trim());
        proyecto.setCupo(Integer.parseInt(txf_Cupo.getText().trim()));
        proyecto.setIdOrganizacionVinculada(idOrganizacion);
        proyecto.setIdEncargado(idEncargado);
        return proyecto;
    }
    
    private boolean sonCamposValidos() {
        String nombreP = txf_NombreProyecto.getText().trim();
        String descP = txf_Descripcion.getText().trim();
        String objP = txf_Objetivo.getText().trim();
        String cupoStr = txf_Cupo.getText().trim();
        
        String nombreE = txf_NombreEncargado.getText().trim();
        String apellidoPaE = txf_ApellidoPaEncargado.getText().trim();
        String apellidoMaE = txf_ApellidoMaEncargado.getText().trim();
        String cargo = txf_Cargo.getText().trim();
        String correo = txf_CorreoEncargado.getText().trim();

        if (cbx_Organizaciones.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Organización requerida",
                    "Por favor selecciona una Organización Vinculada.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (nombreP.isEmpty() || descP.isEmpty() || objP.isEmpty() 
                                                        || cupoStr.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos vacíos (Proyecto)",
                    "Por favor completa todos los campos del Proyecto.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (nombreE.isEmpty() || apellidoPaE.isEmpty() || cargo.isEmpty() 
                                                        || correo.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos vacíos (Encargado)",
                    "Por favor completa todos los campos obligatorios "
                            + "del Encargado.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (!cupoStr.matches("\\d+")) {
            Utilidades.mostrarAlertaSimple("Formato de cupo",
                    "El cupo debe ser un número entero válido",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        int cupoValue = Integer.parseInt(cupoStr);
        
        if (cupoValue <= 0) {
            Utilidades.mostrarAlertaSimple("Cupo inválido", 
                    "El cupo debe ser un número mayor a cero.",
                    Alert.AlertType.WARNING);
            return false;
        }
        if (nombreP.length() > 45){
            Utilidades.mostrarAlertaSimple("Límite excedido",
                    "El nombre no puede exceder los 45 caracteres.", 
                    Alert.AlertType.WARNING);
            return false;
        }
        if (objP.length() > 45) {
            Utilidades.mostrarAlertaSimple("Límite excedido",
                    "El objetivo no puede exceder los 45 caracteres.", 
                    Alert.AlertType.WARNING);
            return false;
        }
        if(descP.length() > 255){
            Utilidades.mostrarAlertaSimple("Límite excedido",
                    "La información de la descripcion no puede "
                            + "exceder los 255 caracteres", 
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (!nombreE.matches("\\D+") || !apellidoPaE.matches("\\D+") ||
                    (!apellidoMaE.isEmpty() && !apellidoMaE.matches("\\D+"))) {
            Utilidades.mostrarAlertaSimple("Formato incorrecto",
              "El nombre y apellidos del encargado no deben contener números.",
               Alert.AlertType.WARNING);
            return false;
        }
        
        if (nombreE.length() > 45 || apellidoPaE.length() > 45 
                                                || apellidoMaE.length() > 45) {
            Utilidades.mostrarAlertaSimple("Límite excedido",
              "El nombre y apellidos del encargado no pueden"
                      + " rebasar los 45 caracteres.",
               Alert.AlertType.WARNING);
            return false;
        }
        
        if (cargo.length() > 45) {
            Utilidades.mostrarAlertaSimple("Límite excedido",
              "El cargo del encargado no puede rebasar los 45 caracteres.",
               Alert.AlertType.WARNING);
            return false;
        }
        
        if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Utilidades.mostrarAlertaSimple("Correo inválido",
              "Por favor ingresa un correo electrónico válido ",
               Alert.AlertType.WARNING);
            return false;
        }
        
        if (correo.length() > 45) {
            Utilidades.mostrarAlertaSimple("Límite excedido",
              "El correo del encargado no puede rebasar los 60 caracteres.",
               Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private boolean existeProyectoDuplicado() throws SQLException {
        String nombreIngresado = txf_NombreProyecto.getText().trim();
        int idOrg = 
             cbx_Organizaciones.getValue().getIdOrganizacionVinculada();
        boolean estaDuplicado = ProyectoDAO.existeProyecto(
                                                nombreIngresado, idOrg);
        if (estaDuplicado) {
            Utilidades.mostrarAlertaSimple("Proyecto duplicado", 
                "La organización seleccionada ya tiene un proyecto"
                + " registrado con el nombre '" + nombreIngresado + "'", 
                Alert.AlertType.WARNING);
            return true;
        }
        return false;
    }
    
    private void cerrar() {
        Stage stageActual = (Stage) txf_NombreProyecto.getScene().getWindow();
        stageActual.close();
    }
    
    private void limpiarFormulario() {
        txf_NombreProyecto.clear();
        txf_Descripcion.clear();
        txf_Objetivo.clear();
        txf_Cupo.clear();
        cbx_Organizaciones.getSelectionModel().clearSelection();
        txf_NombreEncargado.clear();
        txf_ApellidoPaEncargado.clear();
        txf_ApellidoMaEncargado.clear();
        txf_Cargo.clear();
        txf_CorreoEncargado.clear();
    }
}
