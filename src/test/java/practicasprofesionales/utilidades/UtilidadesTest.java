/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package practicasprofesionales.utilidades;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author endri
 */
public class UtilidadesTest {
    
    public UtilidadesTest() {
    }

    @Test
    public void testMostrarAlertaSimple() {
        System.out.println("mostrarAlertaSimple");
        String titulo = "";
        String contenido = "";
        Alert.AlertType tipo = null;
        Utilidades.mostrarAlertaSimple(titulo, contenido, tipo);
        fail("The test case is a prototype.");
    }

    @Test
    public void testConfigurarAlerta() {
        System.out.println("configurarAlerta");
        String titulo = "";
        String encabezado = "";
        String contenido = "";
        String btn1 = "";
        String btn2 = "";
        Alert.AlertType tipoAlerta = null;
        boolean expResult = false;
        boolean result = Utilidades.configurarAlerta(titulo, encabezado, contenido, btn1, btn2, tipoAlerta);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCerrarSesion() {
        System.out.println("cerrarSesion");
        ActionEvent event = null;
        Utilidades.cerrarSesion(event);
        fail("The test case is a prototype.");
    }

    @Test
    public void testNavegar() {
        System.out.println("navegar");
        ActionEvent event = null;
        String ruta = "";
        String titulo = "";
        Utilidades.navegar(event, ruta, titulo);
        fail("The test case is a prototype.");
    }

    @Test
    public void testMostrarAlertaConfirmacion() {
        System.out.println("mostrarAlertaConfirmacion");
        String titulo = "";
        String contenido = "";
        boolean expResult = false;
        boolean result = Utilidades.mostrarAlertaConfirmacion(titulo, contenido);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
