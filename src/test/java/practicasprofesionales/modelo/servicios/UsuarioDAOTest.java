/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.servicios;

import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import practicasprofesionales.modelo.dao.UsuarioDAO;


/**
 *
 * @author basa2
 */
public class UsuarioDAOTest {
    
    public UsuarioDAOTest() {
    }
    
    @Test
    public void testExisteCorreoActivo_CasoExitoso() {
        System.out.println("Ejecutando Test: existeCorreoActivo (Debería ser Verdadero)");
        String correoReal = "aaaaa@uv.mx"; 
        try {
            boolean resultado = UsuarioDAO.existeCorreoActivo(correoReal);
            assertTrue("El método debe devolver TRUE porque el correo sí existe", resultado);
        } catch (SQLException e) {
            fail("La prueba falló por un error de SQL: " + e.getMessage());
        }
    }
    
    @Test
    public void testExisteCorreoActivo_CasoInexistente() {
        System.out.println("Ejecutando Test: existeCorreoActivo (Debería ser Falso)");
        String correoFalso = "este_correo_no_existe_jamas_12345@uv.mx";
        try {
            boolean resultado = UsuarioDAO.existeCorreoActivo(correoFalso);
            assertFalse("El método debe devolver FALSE porque el correo es inventado", resultado);
        } catch (SQLException e) {
            fail("La prueba falló por un error de SQL: " + e.getMessage());
        }
    }
}
