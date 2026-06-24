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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import practicasprofesionales.modelo.dao.CoordinadorDAO;
import practicasprofesionales.modelo.dao.UsuarioDAO;
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
    private TextField txf_Nombre;
    @FXML
    private TextField txf_ApellidoPa;
    @FXML
    private TextField txf_ApellidoMa;
    @FXML
    private TextField txf_NoPersonal;
    @FXML
    private TextField txf_Correo;
    @FXML
    private PasswordField txf_Contrasena;
    @FXML
    private TextField txf_Cubiculo;
    @FXML
    private PasswordField txf_ConfirmarContrasena;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void btn_Registrar(ActionEvent event) {
        if (!validarCampos()) {
            return; 
        }
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
                "Confirmar Registro", 
                "Estás seguro de que deseas registrar"
                + " la información de este coordinador?"
        );
        if (confirmar) {
            guardarCoordinador();
        }
    }

    @FXML
    private void btn_Cancelar(ActionEvent event) {
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
                "Cancelar Registro", 
                "Estás seguro de que deseas cancelar? "
                + "Se perderán los datos capturados."
        );
        if (confirmar) {
            limpiarFormulario(); 
        }
    }

    private boolean validarCampos(){
        if (!sonCamposValidos()) {
            return false; 
        }
        if(!coincidenContraseñas()){
            return false;
        }
        if (!esCorreoValido()) {
            return false; 
        }
        try{
            if (existeCorreoAsignado() == true) {
                return false;
            }
            if (existeNoPersonal() == true) {
                return false;
            }
            if(existeCoordinadorActivo() == true){
                return false;
            }
        }  catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de base de datos", 
                    e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
    
    private boolean sonCamposValidos() {
        String nombre = txf_Nombre.getText().trim();
        String apellidoPa = txf_ApellidoPa.getText().trim();
        String apellidoMa = txf_ApellidoMa.getText().trim();
        String noPersonal = txf_NoPersonal.getText().trim();
        String cubiculo = txf_Cubiculo.getText().trim();
        String contrasena = txf_Contrasena.getText();
        String contraseñaConf = txf_ConfirmarContrasena.getText();
        if (nombre.isEmpty() || apellidoPa.isEmpty() || apellidoMa.isEmpty() || 
            noPersonal.isEmpty() || cubiculo.isEmpty() || contrasena.isEmpty()
            || contraseñaConf.isEmpty()){
            
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
                        + " únicamente entre 5 y 10 números", 
                Alert.AlertType.WARNING);
            return false;
        }
        if (!cubiculo.matches("\\d+")) {
            Utilidades.mostrarAlertaSimple("Formato incorrecto", 
                "El cubículo debe contener únicamente números.", 
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
    
    private boolean esCorreoValido() {
        String correoLimpio = txf_Correo.getText().trim().toLowerCase();
        
        if (!correoLimpio.endsWith("uv.mx")) {
            Utilidades.mostrarAlertaSimple("Correo inválido", 
                "El correo debe ser una cuenta institucional "
              + "de la Universidad Veracruzana (terminación uv.mx).", 
                Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
     private boolean existeCoordinadorActivo() throws SQLException {
        Coordinador coord = CoordinadorDAO.existeCoordinadorActivo();
        if (coord != null) {
            Utilidades.mostrarAlertaSimple("No se puede registrar "
                    + "otro Coordinador", 
                    "Ya existe un coordinador activo: " + coord.getNombre()
                    + " " + coord.getApellidoPaterno() + " " 
                    + coord.getApellidoMaterno() + " \n" 
                    + "Con número de personal: "+ coord.getNoPersonal()
                    , Alert.AlertType.WARNING);
            return true;
        }
        return false;
    }
    
    private boolean existeNoPersonal() throws SQLException{
        String noPersonalIngresado = txf_NoPersonal.getText().trim();
        boolean numeroOcupado = CoordinadorDAO.existeNumeroPersonal(
                                                    noPersonalIngresado);
        if (numeroOcupado) {
            Utilidades.mostrarAlertaSimple(
                "Número de Personal Duplicado", 
                "El número de personal " + noPersonalIngresado 
                + " ya se encuentra registrado en el sistema bajo otro "
                + "usuario.", 
                Alert.AlertType.WARNING
            );
            return true;
        }
        return false;
    }
    
    private boolean coincidenContraseñas() {
        String contrasena = txf_Contrasena.getText();
        String confirmarContrasena = txf_ConfirmarContrasena.getText();
        if (!contrasena.equals(confirmarContrasena)) {
            Utilidades.mostrarAlertaSimple("Contraseñas no coinciden", 
                "Las contraseñas ingresadas no son iguales."
              + " Verifica que estén bien escritas e inténtalo de nuevo", 
                Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private boolean existeCorreoAsignado() throws SQLException {
        String correoIngresado = txf_Correo.getText().trim();
        boolean correoOcupado = UsuarioDAO.existeCorreo(correoIngresado);        
        if (correoOcupado) {
            Utilidades.mostrarAlertaSimple(
                "Correo Duplicado", 
                "El correo institucional " + correoIngresado 
                        + " ya se encuentra registrado."
                        + " Por favor, verifica la información o "
                        + "usa un correo diferente.", 
                Alert.AlertType.WARNING
            );
            return true;
        }
        return false;
    }
    
    private void guardarCoordinador() {
        try {
            Coordinador nuevoCoordinador = serializarCoordinador();
            RespuestaOperacion respuesta = CoordinadorDAO.registrarCoordinador(
                                                              nuevoCoordinador);
            if (!respuesta.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito",
                        "Coordinador registrado exitosamente",
                        Alert.AlertType.INFORMATION);
                cerrar();
            } else {
                Utilidades.mostrarAlertaSimple("Error",
                        respuesta.getMensaje(), Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error de base de datos", 
                    e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private Coordinador serializarCoordinador(){
        Coordinador nuevoCoordinador = new Coordinador();
        nuevoCoordinador.setNombre(txf_Nombre.getText());
        nuevoCoordinador.setApellidoPaterno(txf_ApellidoPa.getText());
        nuevoCoordinador.setApellidoMaterno(txf_ApellidoMa.getText());
        nuevoCoordinador.setNoPersonal(txf_NoPersonal.getText());
        nuevoCoordinador.setCorreo(txf_Correo.getText());
        nuevoCoordinador.setCubiculo(txf_Cubiculo.getText());
        nuevoCoordinador.setContrasena(txf_Contrasena.getText());
        nuevoCoordinador.setEstado("Activo");
        return nuevoCoordinador;
    }
    
    private void limpiarFormulario() {
        txf_Nombre.clear();
        txf_ApellidoPa.clear();
        txf_ApellidoMa.clear();
        txf_NoPersonal.clear();
        txf_Correo.clear();
        txf_Contrasena.clear();
        txf_Cubiculo.clear();
        txf_ConfirmarContrasena.clear();
    }
    
    private void cerrar() {
        Stage stage = (Stage) txf_Nombre.getScene().getWindow();
        stage.close();
    }
}
