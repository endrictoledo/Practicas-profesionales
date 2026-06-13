package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.Documento;

public class DocumentoDAO {

    public boolean registrarDocumento(Documento documento) throws ExcepcionDAO {
        String query = "INSERT INTO documento (fecha, Calificacion, Estado_documento_idEstado_documento, "
                     + "Catalogo_documento_idCatalogo_documento, Expediente_id_expediente, archivoFisico) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setDate(1, documento.getFecha());
            stmt.setString(2, documento.getCalificacion());
            stmt.setInt(3, documento.getIdEstadoDocumento());
            stmt.setInt(4, documento.getIdCatalogoDocumento());
            stmt.setInt(5, documento.getIdExpediente());
            stmt.setBytes(6, documento.getArchivoFisico());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al registrar el documento en la base de datos", e);
        }
    }
    
    // Método auxiliar para buscar el id_expediente basado en el id_usuario del estudiante
    public int obtenerIdExpedientePorUsuario(int idUsuario) throws ExcepcionDAO {
        String query = "SELECT exp.idExpediente FROM expediente exp "
                     + "JOIN estudiante e ON exp.Estudiante_idEstudiante = e.idEstudiante "
                     + "WHERE e.Usuario_idUsuario = ?";
                     
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idExpediente");
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al buscar el expediente del estudiante", e);
        }
        return -1; // No encontrado
    }

    public int obtenerIdCatalogoDocumento(String nombreDocumento) throws ExcepcionDAO {
        String query = "SELECT idCatalogoDocumento FROM catalogo_documento WHERE nombreDocumento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombreDocumento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idCatalogoDocumento");
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al buscar catálogo de documento", e);
        }
        return -1;
    }
}
