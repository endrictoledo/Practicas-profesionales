/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package practicasprofesionales.controlador;

import java.net.URL;
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
    private TextField txtMatricula;
    @FXML
    private TextField txtNombreEstudiante;
    @FXML
    private TextField txtAvanceCrediticio;
    @FXML
    private TextField txtPromedio;
    @FXML
    private ComboBox<SeccionEE> cmbSeccionEE;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField pwfContrasena;
    
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
            cmbSeccionEE.setItems(listaSecciones);
        } catch (Exception e) {
            Utilidades.mostrarAlertaSimple("Error",
                "No se pudieron cargar las secciones.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        if (Utilidades.mostrarAlertaConfirmacion("Cancelar",
                                        "¿Deseas limpiar el formulario?")) {
            limpiarFormulario();
        }
    }

    @FXML
    private void btnRegistrar(ActionEvent event) {
        if (!sonCamposValidos()){ 
            return;
        }
        if (!esCorreoValido()) {
            return; 
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
        String matricula = txtMatricula.getText().trim();
        String nombre = txtNombreEstudiante.getText().trim();
        String avance = txtAvanceCrediticio.getText().trim();
        String promedio = txtPromedio.getText().trim();
        String correo = txtCorreo.getText().trim();
        String pass = pwfContrasena.getText();

        if (matricula.isEmpty() || nombre.isEmpty() || avance.isEmpty() || 
            promedio.isEmpty() || correo.isEmpty() || pass.isEmpty() ||
                                            cmbSeccionEE.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Campos Vacíos",
                    "Por favor llena todos los campos.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if(!validarFormatoMatricula(matricula)){
            Utilidades.mostrarAlertaSimple("Formato incorrecto", 
                    "La matrícula debe empezar por la letra 's' "
                  + "seguida de números",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (!nombre.matches("^[a-zA-Z\\s]+$")) {
            Utilidades.mostrarAlertaSimple("Formato incorrecto", 
                    "El nombre solo debe contener letras.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (!avance.matches("\\d+") || !promedio.matches("\\d+\\.?\\d*")) {
            Utilidades.mostrarAlertaSimple("Formatos inválidos",
                    "Avance y promedio deben ser números.",
                    Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            int avanceInt = Integer.parseInt(avance);
            if (avanceInt < 70 || avanceInt > 100) {
                Utilidades.mostrarAlertaSimple("Error de Avance", 
                    "El avance crediticio debe ser mayor a 70", 
                    Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Formato inválido", 
                "El avance debe ser un número entero.", 
                Alert.AlertType.WARNING);
            return false;
        }

        if (pass.length() < 8 || pass.length() > 30) {
            Utilidades.mostrarAlertaSimple("Contraseña",
                    "La contraseña debe tener entre 8 y 30 caracteres.",
                    Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private void limpiarFormulario() {
        txtMatricula.clear();
        txtNombreEstudiante.clear();
        txtAvanceCrediticio.clear();
        txtPromedio.clear();
        txtCorreo.clear();
        pwfContrasena.clear();
        cmbSeccionEE.getSelectionModel().clearSelection();
    }

    private void guardarPracticante() {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCorreo(txtCorreo.getText().trim());
            nuevoUsuario.setContrasenaPlana(pwfContrasena.getText().trim());
            nuevoUsuario.setTipoUsuario(TipoUsuario.ESTUDIANTE);
            nuevoUsuario.setEstado(1);
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int idUsuarioGenerado = usuarioDAO.registerUser(nuevoUsuario);
            Estudiante nuevoEstudiante = new Estudiante();
            nuevoEstudiante.setMatricula(txtMatricula.getText().trim());
            nuevoEstudiante.setNombreEstudiante(
                    txtNombreEstudiante.getText().trim());
            nuevoEstudiante.setAvanceCrediticio(Integer.parseInt(
                    txtAvanceCrediticio.getText().trim()));
            nuevoEstudiante.setPromedio(Double.parseDouble(
                    txtPromedio.getText().trim()));
            nuevoEstudiante.setIdSeccionEE(
                    cmbSeccionEE.getValue().getIdSeccionEE());
            nuevoEstudiante.setIdUsuario(idUsuarioGenerado); 
            nuevoEstudiante.setIdEstadoEstudiante(1);

            EstudianteDAO.registrarEstudianteConExpediente(nuevoEstudiante);
            Utilidades.mostrarAlertaSimple("Éxito",
                    "Practicante registrado correctamente",
                    Alert.AlertType.INFORMATION);
            limpiarFormulario();

        } catch (Exception e) {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo registrar: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private boolean esCorreoValido() {
        String correoLimpio = txtCorreo.getText().trim().toLowerCase();
        
        if (!correoLimpio.endsWith("uv.mx")) {
            Utilidades.mostrarAlertaSimple("Correo inválido", 
                "El correo debe ser una cuenta institucional "
              + "de la Universidad Veracruzana (terminación @uv.mx).", 
                Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    public boolean validarFormatoMatricula(String matricula) {
        return matricula != null && matricula.matches("^[sS]\\d{8,9}$");
    }
}
