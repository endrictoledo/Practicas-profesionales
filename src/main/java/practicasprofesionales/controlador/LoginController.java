/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import practicasprofesionales.excepciones.DAOException;
import practicasprofesionales.modelo.TipoUsuario;
import practicasprofesionales.modelo.dao.UsuarioDAO;
import practicasprofesionales.modelo.pojo.Usuario;

/**
 * FXML Controller class
 *
 * @author endri
 */
public class LoginController implements Initializable {

    @FXML 
    private TextField txtEmail;
    @FXML 
    private PasswordField txtPassword;
    @FXML 
    private Label lblError;
    @FXML 
    private Button btnLogin;

    private static final String FXML_COORDINATOR = "/practicasprofesionales/vista/CoordinatorMenuGUI.fxml";
    private static final String FXML_ADMIN       = "/practicasprofesionales/vista/FXMLPanelAdministrador.fxml";
    private static final String FXML_TEACHER     = "/practicasprofesionales/vista/teacherMenu.fxml"; // Asume que este archivo existe o se creará
    private static final String FXML_STUDENT     = "/practicasprofesionales/vista/StudentMenuGUI.fxml"; // Asume que este archivo existe o se creará 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtEmail.textProperty().addListener((obs, o, n) -> clearError());
        txtPassword.textProperty().addListener((obs, o, n) -> clearError());
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String email    = txtEmail.getText().trim();
        String password = txtPassword.getText();

        if (!validateFields(email, password)) return;

        setFormEnabled(false);

        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario user = dao.login(email, password);

            if (user != null) {
                navigateToMenu(user);
            } else {
                showError("Correo o contraseña incorrectos. Intenta de nuevo.");
                txtPassword.clear();
            }

        } catch (DAOException e) {
            showError("Error de conexión con la base de datos. Verifica tu configuración.");
            System.err.println("[LoginController] DAOException: " + e.getMessage());
        } finally {
            setFormEnabled(true);
        }
    }

    private void navigateToMenu(Usuario user) {
        TipoUsuario type = user.getTipoUsuario(); 
        String fxmlPath = resolveFxmlPath(type);
        String title    = resolveTitle(type);

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            showError("No se pudo cargar el módulo de " + type.getDbValue() + ". Contacta al administrador.");
            System.err.println("[LoginController] No se encontró FXML: " + fxmlPath + " — " + e.getMessage());
        } catch (NullPointerException e) {
            showError("El módulo para '" + type.getDbValue() + "' aún no está disponible.");
            System.err.println("[LoginController] FXML no encontrado (null): " + fxmlPath);
        }
    }

    private String resolveFxmlPath(TipoUsuario type) { //Usa un switch moderno para obtener la ruta correcta 
        return switch (type) {
            case COORDINADOR  -> FXML_COORDINATOR;
            case ADMINISTRADOR -> FXML_ADMIN;
            case PROFESOR     -> FXML_TEACHER;
            case ESTUDIANTE   -> FXML_STUDENT;
        };
    }

    private String resolveTitle(TipoUsuario type) {
        return switch (type) {
            case COORDINADOR   -> "Sistema de Prácticas — Coordinador";
            case ADMINISTRADOR -> "Sistema de Prácticas — Administrador";
            case PROFESOR      -> "Sistema de Prácticas — Profesor";
            case ESTUDIANTE    -> "Sistema de Prácticas — Estudiante";
        };
    }

    private boolean validateFields(String email, String password) {
        if (email.isEmpty() && password.isEmpty()) {
            showError("Por favor ingresa tu correo y contraseña.");
            return false;
        }
        if (email.isEmpty()) {
            showError("El correo electrónico es obligatorio.");
            return false;
        }
        if (!email.contains("@")) {
            showError("Ingresa un correo electrónico válido.");
            return false;
        }
        if (password.isEmpty()) {
            showError("La contraseña es obligatoria.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        lblError.setText(message);
    }

    private void clearError() {
        lblError.setText("");
    }

    private void setFormEnabled(boolean enabled) {
        txtEmail.setDisable(!enabled);
        txtPassword.setDisable(!enabled);
        btnLogin.setDisable(!enabled);
        btnLogin.setText(enabled ? "Ingresar" : "Verificando...");
    }
    
}
