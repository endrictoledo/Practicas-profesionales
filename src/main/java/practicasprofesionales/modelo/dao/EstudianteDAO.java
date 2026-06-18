/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.Estudiante;

/**
 *
 * @author basa2
 */
public class EstudianteDAO {
    public static int registrarEstudiante(Estudiante estudiante)
                                                           throws SQLException {
        String sql = "INSERT INTO estudiante (matricula,"
                + " nombreEstudiante, avanceCrediticio, promedio, "
                   + "Usuario_idUsuario, Seccion_EE_idSeccion_EE,"
                + " Estado_Estudiante_idEstado_Estudiante) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, estudiante.getMatricula());
            ps.setString(2, estudiante.getNombreEstudiante());
            ps.setInt(3, estudiante.getAvanceCrediticio());
            ps.setDouble(4, estudiante.getPromedio());
            ps.setInt(5, estudiante.getIdUsuario());
            ps.setInt(6, estudiante.getIdSeccionEE());
            ps.setInt(7, estudiante.getIdEstadoEstudiante());

            return ps.executeUpdate();
        }
    }
    
    public static int registrarEstudianteConExpediente(Estudiante estudiante)
                                                           throws SQLException {
        String sqlEstudiante = "INSERT INTO estudiante (matricula,"
                + " nombreEstudiante, avanceCrediticio, "
                + "promedio, Usuario_idUsuario, Seccion_EE_idSeccion_EE, "
                + "Estado_Estudiante_idEstado_Estudiante) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlExpediente = "INSERT INTO expediente "
                + "(Estado_Expediente_idEstado_Expediente, "
                + "Estudiante_idEstudiante) "
                + "VALUES (?, ?)";

        Connection conexion = null;
        int resultado = 0;
        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);
            try (PreparedStatement psEst = conexion.prepareStatement(
                              sqlEstudiante, Statement.RETURN_GENERATED_KEYS)) {
                psEst.setString(1, estudiante.getMatricula());
                psEst.setString(2, estudiante.getNombreEstudiante());
                psEst.setInt(3, estudiante.getAvanceCrediticio());
                psEst.setDouble(4, estudiante.getPromedio());
                psEst.setInt(5, estudiante.getIdUsuario());
                psEst.setInt(6, estudiante.getIdSeccionEE());
                psEst.setInt(7, estudiante.getIdEstadoEstudiante());
                psEst.executeUpdate();
                try (ResultSet rs = psEst.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idEstudianteGenerado = rs.getInt(1);
                        try (PreparedStatement psExp =
                                    conexion.prepareStatement(sqlExpediente)) {
                            psExp.setInt(1, 1);
                            psExp.setInt(2, idEstudianteGenerado);
                            resultado = psExp.executeUpdate();
                        }
                    }
                }
            }
            conexion.commit();
        } catch (SQLException e) {
            if (conexion != null) conexion.rollback();
            throw e;
        } finally {
            if (conexion != null) {
                conexion.setAutoCommit(true);
                conexion.close();
            }
        }
        return resultado;
    }
    
    public static int obtenerIdEstudiantePorUsuario(int idUsuario)
                                                           throws ExcepcionDAO {
        String query = "SELECT idEstudiante FROM estudiante "
                + "WHERE Usuario_idUsuario = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idEstudiante");
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al obtener ID de estudiante", e);
        }
        return -1;
    }
}
