/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package practicasprofesionales.utilidades;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author endri
 */
public class UtilidadContrasenaTest {

    public UtilidadContrasenaTest() {
    }

    @Test
    public void testHash() {
        System.out.println("Prueba de Hash");
        String plainPassword = "MiPasswordSeguro123";
        String result = UtilidadContrasena.hash(plainPassword);

        assertNotNull("El hash no debería ser nulo", result);
        assertNotEquals("El hash no debería ser igual a la contraseña plana", plainPassword, result);
        assertTrue("El hash debe tener formato de BCrypt", result.startsWith("$2a$"));
    }

    @Test
    public void testVerify() {
        System.out.println("Prueba de Verify");
        String plainPassword = "MiPasswordSeguro123";

        String storedHash = UtilidadContrasena.hash(plainPassword);

        boolean resultTrue = UtilidadContrasena.verify(plainPassword, storedHash);
        assertTrue("Debería retornar true si las contraseñas coinciden", resultTrue);

        boolean resultFalse = UtilidadContrasena.verify("ContraseñaIncorrecta", storedHash);
        assertFalse("Debería retornar false si la contraseña es incorrecta", resultFalse);
    }
}
