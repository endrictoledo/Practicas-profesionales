/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.utilidades;

import javafx.scene.control.Alert;

/**
 *
 * @author endri
 */
public class Utilidades {
    public static void mostrarAlertaSimple(String titulo, String contenido, Alert.AlertType tipo){
     Alert alerta = new Alert(tipo);   
     alerta.setTitle(titulo);
     alerta.setContentText(contenido);
     alerta.setHeaderText(null);
     alerta.show();
    }
    
    public static void cerrarSesion(javafx.event.ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(Utilidades.class.getResource("/practicasprofesionales/vista/GUIInicioSesion.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Sistema de Prácticas — Inicio de Sesión");
            stage.centerOnScreen();
            stage.show();
        } catch (java.io.IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
