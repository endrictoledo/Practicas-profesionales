package practicasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.TipoUsuario;
import practicasprofesionales.modelo.dao.UsuarioDAO;
import practicasprofesionales.modelo.DTO.Usuario;

/**
 * Controlador de la vista InicioSesionGUI
 */
public class ControladorGUIInicioSesion implements Initializable {

    @FXML 
    private TextField txt_correo;
    @FXML 
    private PasswordField txt_contrasena;
    @FXML 
    private Label lbl_error;
    @FXML 
    private Button btn_ingresar;

    private static final String FXML_COORDINADOR = "/practicasprofesionales/vista/GUIMenuCoordinador.fxml";
    private static final String FXML_ADMINISTRADOR = "/practicasprofesionales/vista/GUIMenuAdministrador.fxml";
    private static final String FXML_PROFESOR     = "/practicasprofesionales/vista/GUIMenuProfesor.fxml";
    private static final String FXML_ESTUDIANTE   = "/practicasprofesionales/vista/GUIMenuPracticante.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_correo.textProperty().addListener((obs, o, n) -> limpiarError());
        txt_contrasena.textProperty().addListener((obs, o, n) -> limpiarError());
    }
    
    @FXML
    private void manejarInicioSesion(ActionEvent event) {
        String correo = txt_correo.getText().trim();
        String contrasena = txt_contrasena.getText();

        if (!validarCampos(correo, contrasena)) return;

        habilitarFormulario(false);

        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.ingresar(correo, contrasena);

            if (usuario != null) {
                practicasprofesionales.utilidades.SesionGlobal.getInstancia().setUsuarioActual(usuario);
                navegarAMenu(usuario);
            } else {
                txt_contrasena.clear();
                mostrarError("Correo o contraseña incorrectos. Intenta de nuevo.");
            }

        } catch (ExcepcionDAO e) {
            mostrarError("Error de conexión con la base de datos. Verifica tu configuración.");
            System.err.println("[ControladorInicioSesion] ExcepcionDAO: " + e.getMessage());
        } finally {
            habilitarFormulario(true);
        }
    }

    private void navegarAMenu(Usuario usuario) {
        TipoUsuario tipo = usuario.getTipoUsuario(); 
        String rutaFxml = resolverRutaFxml(tipo);
        String titulo    = resolverTitulo(tipo);

        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFxml));
            Stage stage = (Stage) btn_ingresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            mostrarError("No se pudo cargar el módulo de " + tipo.getDbValue() + ". Contacta al administrador.");
            System.err.println("[ControladorInicioSesion] No se encontró FXML: " + rutaFxml + " — " + e.getMessage());
        } catch (NullPointerException e) {
            mostrarError("El módulo para '" + tipo.getDbValue() + "' aún no está disponible.");
            System.err.println("[ControladorInicioSesion] FXML no encontrado (null): " + rutaFxml);
        }
    }

    private String resolverRutaFxml(TipoUsuario tipo) {
        return switch (tipo) {
            case COORDINADOR   -> FXML_COORDINADOR;
            case ADMINISTRADOR -> FXML_ADMINISTRADOR;
            case PROFESOR      -> FXML_PROFESOR;
            case ESTUDIANTE    -> FXML_ESTUDIANTE;
        };
    }

    private String resolverTitulo(TipoUsuario tipo) {
        return switch (tipo) {
            case COORDINADOR   -> "Sistema de Prácticas — Coordinador";
            case ADMINISTRADOR -> "Sistema de Prácticas — Administrador";
            case PROFESOR      -> "Sistema de Prácticas — Profesor";
            case ESTUDIANTE    -> "Sistema de Prácticas — Estudiante";
        };
    }

    private boolean validarCampos(String correo, String contrasena) {
        if (correo.isEmpty() && contrasena.isEmpty()) {
            mostrarError("Por favor ingresa tu correo y contraseña.");
            return false;
        }
        if (correo.isEmpty()) {
            mostrarError("El correo electrónico es obligatorio.");
            return false;
        }
        if (!correo.contains("@")) {
            mostrarError("Ingresa un correo electrónico válido.");
            return false;
        }
        if (contrasena.isEmpty()) {
            mostrarError("La contraseña es obligatoria.");
            return false;
        }
        return true;
    }

    private void mostrarError(String mensaje) {
        lbl_error.setText(mensaje);
    }

    private void limpiarError() {
        lbl_error.setText("");
    }

    private void habilitarFormulario(boolean habilitado) {
        txt_correo.setDisable(!habilitado);
        txt_contrasena.setDisable(!habilitado);
        btn_ingresar.setDisable(!habilitado);
        btn_ingresar.setText(habilitado ? "Ingresar" : "Verificando...");
    }
}
