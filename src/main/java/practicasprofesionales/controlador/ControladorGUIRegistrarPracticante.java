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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.TipoUsuario;
import practicasprofesionales.modelo.dao.EstudianteDAO;
import practicasprofesionales.modelo.dao.SeccionEEDAO;
import practicasprofesionales.modelo.dao.UsuarioDAO;
import practicasprofesionales.modelo.pojo.Estudiante;
import practicasprofesionales.modelo.pojo.SeccionEE;
import practicasprofesionales.modelo.pojo.Usuario;
import practicasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author basa2
 */
public class ControladorGUIRegistrarPracticante implements Initializable {

    @FXML
    private TextField txf_Matricula;
    @FXML
    private TextField txf_NombreEstudiante;
    @FXML
    private TextField txf_AvanceCrediticio;
    @FXML
    private TextField txf_Promedio;
    @FXML
    private ComboBox<SeccionEE> cmb_SeccionEE;
    @FXML
    private TextField txf_Correo;
    @FXML
    private PasswordField pwf_Contrasena;
    @FXML
    private PasswordField pwf_ConfirmarContrasena;
    
    private ObservableList<SeccionEE> listaSecciones;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarSecciones();
    }
    
    private void cargarSecciones() {
        try {
            ArrayList<SeccionEE> seccionesBD = SeccionEEDAO.obtenerSecciones();
            listaSecciones = FXCollections.observableArrayList(seccionesBD);
            cmb_SeccionEE.setItems(listaSecciones);
        } catch (Exception e) {
            Utilidades.mostrarAlertaSimple("Error",
                "No se pudieron cargar las secciones.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_Cancelar(ActionEvent event) {
        if (Utilidades.mostrarAlertaConfirmacion("Cancelar",
                                        "¿Deseas limpiar el formulario?")) {
            limpiarFormulario();
        }
    }

    @FXML
    private void btn_Registrar(ActionEvent event) {
        if (!sonCamposValidos()){ 
            return;
        }
        if (!esCorreoValido()) {
            return; 
        }
        try{
            if(existeMatricula() == true){
                return;
            }
            if(existeCorreo() == true){
                return;
            }
        }catch(SQLException e){
             Utilidades.mostrarAlertaSimple("Error de conexión",
                    "No fue posible conectarce a la base de datos y"
                    + " comprobar que los datos no sean duplicados.",
                    Alert.AlertType.WARNING);
        }
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
                "Confirmar Registro", 
                "¿Estás seguro de que deseas registrar "
              + "la información de este alumno?"
        );
        if (confirmar) {
            guardarPracticante();
        }
    }
    
    private boolean sonCamposValidos() {
        String matricula = txf_Matricula.getText().trim();
        String nombre = txf_NombreEstudiante.getText().trim();
        String avance = txf_AvanceCrediticio.getText().trim();
        String promedio = txf_Promedio.getText().trim();
        String correo = txf_Correo.getText().trim();
        String pass = pwf_Contrasena.getText();
        String passConf = pwf_ConfirmarContrasena.getText(); 

        if (matricula.isEmpty() || nombre.isEmpty() || avance.isEmpty() || 
            promedio.isEmpty() || correo.isEmpty() || pass.isEmpty() ||
            passConf.isEmpty() || cmb_SeccionEE.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Campos Vacíos",
                    "Por favor llena todos los campos.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if(!validarFormatoMatricula(matricula)){
            Utilidades.mostrarAlertaSimple("Formato incorrecto", 
                    "La matrícula debe empezar por la letra 's' "
                  + "seguida de 8 números",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            Utilidades.mostrarAlertaSimple("Formato incorrecto", 
                    "El nombre solo debe contener letras.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (nombre.length() > 45) { 
            Utilidades.mostrarAlertaSimple("Límite excedido",
                    "El nombre no puede exceder los 45 caracteres.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (!avance.matches("\\d+") || !promedio.matches("\\d+\\.?\\d*")) {
            Utilidades.mostrarAlertaSimple("Formatos inválidos",
                    "Avance y promedio deben ser valores numéricos.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            int avanceInt = Integer.parseInt(avance);
            if (avanceInt < 70 || avanceInt > 100) {
                Utilidades.mostrarAlertaSimple("Error de Avance", 
                    "El avance crediticio debe ser mayor o igual a 70 "
                  + "y máximo 100.", 
                    Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Formato inválido", 
                "El avance debe ser un número entero.", 
                Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            double promedioDouble = Double.parseDouble(promedio);
            if (promedioDouble < 0.0 || promedioDouble > 10.0) {
                Utilidades.mostrarAlertaSimple("Error de Promedio", 
                    "El promedio debe ser un valor entre 0.00 y 10.00", 
                    Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Formato inválido", 
                "El formato del promedio no es válido, deben ser numeros.", 
                Alert.AlertType.WARNING);
            return false;
        }
        
        if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
            Utilidades.mostrarAlertaSimple("Correo inválido",
              "Por favor ingresa un formato de correo electrónico válido.",
               Alert.AlertType.WARNING);
            return false;
        }
        
        if (correo.length() > 60) {
            Utilidades.mostrarAlertaSimple("Límite excedido",
              "El correo no puede exceder los 60 caracteres.",
               Alert.AlertType.WARNING);
            return false;
        }

        if (pass.length() < 8 || pass.length() > 30) {
            Utilidades.mostrarAlertaSimple("Contraseña",
                    "La contraseña debe tener entre 8 y 30 caracteres.",
                    Alert.AlertType.WARNING);
            return false;
        }
        if (!pass.equals(passConf)) {
            Utilidades.mostrarAlertaSimple("Contraseñas no coinciden",
                    "Las contraseñas ingresadas no son iguales."
                            + " Verifica e inténtalo de nuevo.",
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }
    
    private void limpiarFormulario() {
        txf_Matricula.clear();
        txf_NombreEstudiante.clear();
        txf_AvanceCrediticio.clear();
        txf_Promedio.clear();
        txf_Correo.clear();
        pwf_Contrasena.clear();
        cmb_SeccionEE.getSelectionModel().clearSelection();
        pwf_ConfirmarContrasena.clear();
    }

    private void guardarPracticante() {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCorreo(txf_Correo.getText().trim().toLowerCase());
            nuevoUsuario.setContrasenaPlana(pwf_Contrasena.getText().trim());
            nuevoUsuario.setTipoUsuario(TipoUsuario.ESTUDIANTE);
            nuevoUsuario.setEstado(1);
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int idUsuarioGenerado = usuarioDAO.registerUser(nuevoUsuario);
            
            Estudiante nuevoEstudiante = new Estudiante();
            nuevoEstudiante.setMatricula(txf_Matricula.getText().trim());
            nuevoEstudiante.setNombreEstudiante(
                    txf_NombreEstudiante.getText().trim());
            nuevoEstudiante.setAvanceCrediticio(Integer.parseInt(
                    txf_AvanceCrediticio.getText().trim()));
            nuevoEstudiante.setPromedio(Double.parseDouble(
                    txf_Promedio.getText().trim()));
            nuevoEstudiante.setIdSeccionEE(
                    cmb_SeccionEE.getValue().getIdSeccionEE());
            nuevoEstudiante.setIdUsuario(idUsuarioGenerado); 
            nuevoEstudiante.setIdEstadoEstudiante(1);

            EstudianteDAO.registrarEstudianteConExpediente(nuevoEstudiante);
            Utilidades.mostrarAlertaSimple("Éxito",
                    "Practicante registrado correctamente",
                    Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } catch (SQLException e) {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo registrar: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (ExcepcionDAO ex){
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo registrar: " + ex.getMessage(),
                    Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }
    
    private boolean esCorreoValido() {
        String correoLimpio = txf_Correo.getText().trim().toLowerCase();
        
        if (!correoLimpio.endsWith("uv.mx")) {
            Utilidades.mostrarAlertaSimple("Correo inválido", 
                "El correo debe ser una cuenta institucional "
              + "de la Universidad Veracruzana (terminación .uv.mx).", 
                Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    private boolean existeMatricula() throws SQLException {
        String matriculaIngresada = txf_Matricula.getText().trim();
        boolean matriculaOcupada = EstudianteDAO.existeMatriculaActiva(matriculaIngresada);
        if (matriculaOcupada) {
            Utilidades.mostrarAlertaSimple("Matrícula Duplicada", 
                "Ya existe un estudiante activo registrado con la matrícula: " 
                + matriculaIngresada, 
                Alert.AlertType.WARNING);
            return true;
        }
        return false;
    }

    private boolean existeCorreo() throws SQLException {
        String correoIngresado = txf_Correo.getText().trim();
        boolean correoOcupado = UsuarioDAO.existeCorreoActivo(correoIngresado);
        if (correoOcupado) {
            Utilidades.mostrarAlertaSimple("Correo Duplicado", 
                "El correo institucional " + correoIngresado 
                + " ya se encuentra en uso por un estudiante activo.", 
                Alert.AlertType.WARNING);
            return true;
        }
        return false;
    }
    
    public boolean validarFormatoMatricula(String matricula) {
        return matricula != null && matricula.matches("^[sS]\\d{8}$");
    }
}
