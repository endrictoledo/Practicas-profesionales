/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.servicios;

import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import practicasprofesionales.modelo.dao.EstudianteDAO;

/**
 *
 * @author basa2
 */

public class EstudianteDAOTest {
    @Test
    public void testExisteMatriculaActiva_Exito() {
        String matriculaReal = "s20015678"; 
        try {
            boolean resultado = EstudianteDAO.existeMatriculaActiva(matriculaReal);
            assertTrue("Debe devolver TRUE porque la matrícula existe", resultado);
        } catch (SQLException e) {
            fail("Error SQL: " + e.getMessage());
        }
    }

    @Test
    public void testExisteMatriculaActiva_Falso() {
        String matriculaFalsa = "s00000000"; 
        try {
            boolean resultado = EstudianteDAO.existeMatriculaActiva(matriculaFalsa);
            assertFalse("Debe devolver FALSE porque la matrícula es inventada", resultado);
        } catch (SQLException e) {
            fail("Error SQL: " + e.getMessage());
        }
    }
}