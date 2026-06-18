package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.DTO.Proyecto;

/**
 *
 * @author endri
 */
public class SolicitudProyectoDAO {

    public boolean registrarSolicitud(int idEstudiante, Proyecto proyecto) throws ExcepcionDAO {
        String query = "INSERT INTO solicitud_practica (nombreProyecto, razonSocialOrganizacion, Estudiante_idEstudiante) "
                     + "VALUES (?, ?, ?)";
                     
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, proyecto.getNombre());
            stmt.setString(2, proyecto.getNombreOrganizacion());
            stmt.setInt(3, idEstudiante);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al registrar la solicitud de práctica en la base de datos", e);
        }
    }
    
    public int obtenerIdEstudiantePorUsuario(int idUsuario) throws ExcepcionDAO {
        String query = "SELECT idEstudiante FROM estudiante WHERE Usuario_idUsuario = ?";
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
