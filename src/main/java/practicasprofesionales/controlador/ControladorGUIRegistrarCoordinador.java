/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import practicasprofesionales.modelo.dao.CoordinadorDAO;
import practicasprofesionales.modelo.pojo.Coordinador;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.utilidades.Utilidades;
import static practicasprofesionales.utilidades.Utilidades.mostrarAlertaSimple;

/**
 * FXML Controller class
 *
 * @author basa2
 */
public class ControladorGUIRegistrarCoordinador implements Initializable {

    @FXML
    private TextField txfNombre;
    @FXML
    private TextField txfApellidoPa;
    @FXML
    private TextField txfApellidoMa;
    @FXML
    private TextField txfNoPersonal;
    @FXML
    private TextField txfCorreo;
    @FXML
    private TextField txfContrasena;
    @FXML
    private TextField txfCubiculo;
    @FXML
    private ComboBox<String> cbxTurno;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void btnRegistrar(ActionEvent event) {
        if (!sonCamposValidos()) {
            return; 
        }
        if (!esCorreoValido()) {
            return; 
        }
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
                "Confirmar Registro", 
                "¿Estás seguro de que deseas registrar"
                        + " la información de este coordinador?"
        );
        if (confirmar) {
            guardarCoordinador();
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
                "Cancelar Registro", 
                "¿Estás seguro de que deseas cancelar? "
                        + "Se perderán los datos capturados."
        );
        
        if (confirmar) {
            cerrarVentana(); 
        }
    }

    private boolean sonCamposValidos() {
        String nombre = txfNombre.getText().trim();
        String apellidoPa = txfApellidoPa.getText().trim();
        String apellidoMa = txfApellidoMa.getText().trim();
        String noPersonal = txfNoPersonal.getText().trim();
        String cubiculo = txfCubiculo.getText().trim();
        String contrasena = txfContrasena.getText();
        if (nombre.isEmpty() || apellidoPa.isEmpty() || apellidoMa.isEmpty() || 
            noPersonal.isEmpty() || cubiculo.isEmpty() || contrasena.isEmpty()){
            
            Utilidades.mostrarAlertaSimple("Campos vacíos", 
                "Por favor, completa toda la información requerida.", 
                Alert.AlertType.WARNING);
            return false;
        }
        if (!nombre.matches("\\D+") || !apellidoPa.matches("\\D+")
                                               || !apellidoMa.matches("\\D+")) {
            Utilidades.mostrarAlertaSimple("Formato incorrecto", 
                "El nombre y los apellidos no deben contener números.", 
                Alert.AlertType.WARNING);
            return false;
        }
        if (nombre.length() > 30 || apellidoPa.length() > 30 
                                                  || apellidoMa.length() > 30) {
             Utilidades.mostrarAlertaSimple("Límite excedido", 
               "El nombre y los apellidos no pueden superar los 30 caracteres.", 
               Alert.AlertType.WARNING);
            return false;
        }
        if (!noPersonal.matches("\\d{5,10}")) {
            Utilidades.mostrarAlertaSimple("No. Personal inválido", 
                "El número de personal debe contener"
                        + " únicamente entre 5 y 10 dígitos numéricos.", 
                Alert.AlertType.WARNING);
            return false;
        }
        if (cubiculo.length() > 20) {
            Utilidades.mostrarAlertaSimple("Límite excedido", 
                "El cubículo no puede superar los 20 caracteres.", 
                Alert.AlertType.WARNING);
            return false;
        }
        if (contrasena.length() < 8 || contrasena.length() > 30) {
            Utilidades.mostrarAlertaSimple("Contraseña inválida", 
                "La contraseña debe tener entre 8 y 30 caracteres.", 
                Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void guardarCoordinador() {
        try {
            Coordinador nuevoCoordinador = new Coordinador();
            nuevoCoordinador.setNombre(txfNombre.getText());
            nuevoCoordinador.setApellidoPaterno(txfApellidoPa.getText());
            nuevoCoordinador.setApellidoMaterno(txfApellidoMa.getText());
            nuevoCoordinador.setNoPersonal(txfNoPersonal.getText());
            nuevoCoordinador.setCorreo(txfCorreo.getText());
            nuevoCoordinador.setCubiculo(txfCubiculo.getText());
            nuevoCoordinador.setContrasena(txfContrasena.getText());
            nuevoCoordinador.setEstado("Activo");

            RespuestaOperacion respuesta = CoordinadorDAO.registrarCoordinador(
                                                              nuevoCoordinador);
            
            if (!respuesta.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito",
                        "Coordinador registrado exitosamente",
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error",
                        respuesta.getMensaje(), Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de base de datos", 
                    e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean esCorreoValido() {
        String correoLimpio = txfCorreo.getText().trim().toLowerCase();
        
        if (!correoLimpio.endsWith("uv.mx")) {
            Utilidades.mostrarAlertaSimple("Correo inválido", 
                "El correo debe ser una cuenta institucional "
              + "de la Universidad Veracruzana (terminación @uv.mx).", 
                Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) txfNombre.getScene().getWindow();
        stage.close();
    }
}
