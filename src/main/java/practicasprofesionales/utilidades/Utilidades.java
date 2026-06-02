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
}
