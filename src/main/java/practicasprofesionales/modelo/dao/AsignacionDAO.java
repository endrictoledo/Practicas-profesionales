/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import practicasprofesionales.modelo.ConexionBD;

/**
 *
 * @author basa2
 */
public class AsignacionDAO {
    public static boolean registrarAsignacion(int idProyecto, int idEstudiante,
            String fechaInicio, String fechaFinal) throws SQLException {
        String sql = "INSERT INTO asignacion (id_proyecto_fk,"
                + " Estudiante_idEstudiante, fechaInicio, fechaFinal)"
                + " VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idProyecto);
            ps.setInt(2, idEstudiante);
            ps.setString(3, fechaInicio);
            ps.setString(4, fechaFinal);
            
            return ps.executeUpdate() > 0;
        }
    }
}
