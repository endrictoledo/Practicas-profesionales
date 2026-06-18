package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.Documento;
import practicasprofesionales.modelo.pojo.ReporteEstudiante;

public class DocumentoDAO {

    public boolean registrarDocumento(Documento documento) throws ExcepcionDAO {
        String query = "INSERT INTO documento (fecha, Calificacion,"
                    + " Estado_documento_idEstado_documento, "
                    + "Catalogo_documento_idCatalogo_documento,"
                    + " Expediente_id_expediente, archivoFisico) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setDate(1, documento.getFecha());
            ps.setString(2, documento.getCalificacion());
            ps.setInt(3, documento.getIdEstadoDocumento());
            ps.setInt(4, documento.getIdCatalogoDocumento());
            ps.setInt(5, documento.getIdExpediente());
            ps.setBytes(6, documento.getArchivoFisico());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new ExcepcionDAO(
                    "Error al registrar el documento en la base de datos", e);
        }
    }
    
    
    public int obtenerIdExpedientePorUsuario(int idUsuario) 
                                                           throws ExcepcionDAO {
        String consulta = "SELECT exp.idExpediente FROM expediente exp "
                    + "JOIN estudiante e "
                    + "ON exp.Estudiante_idEstudiante = e.idEstudiante "
                    + "WHERE e.Usuario_idUsuario = ?";
                     
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(consulta)) {
             
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idExpediente");
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO(
                    "Error al buscar el expediente del estudiante", e);
        }
        return -1; 
    }

    public int obtenerIdCatalogoDocumento(String nombreDocumento)
                                                        throws ExcepcionDAO {
        String consulta = "SELECT idCatalogoDocumento "
                + "FROM catalogo_documento WHERE nombreDocumento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setString(1, nombreDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idCatalogoDocumento");
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al buscar catálogo de documento", e);
        }
        return -1;
    }

    public List<ReporteEstudiante> obtenerReportesPorCatalogo(int idCatalogo)
                                                           throws ExcepcionDAO {
        List<ReporteEstudiante> reportes = new ArrayList<>();
        String consulta = "SELECT d.idDocumento, e.nombreEstudiante,"
                    + " e.matricula, c.nombreDocumento, "
                    + "d.fecha, d.Calificacion, d.observacion, d.archivoFisico "
                    + "FROM documento d "
                    + "JOIN expediente exp "
                    + "ON d.Expediente_id_expediente = exp.idExpediente "
                    + "JOIN estudiante e "
                    + "ON exp.Estudiante_idEstudiante = e.idEstudiante "
                    + "JOIN catalogo_documento c "
                    + "ON d.Catalogo_documento_idCatalogo_documento = "
                    + "c.idCatalogoDocumento "
                    + "WHERE d.Catalogo_documento_idCatalogo_documento = ? "
                    + "AND d.archivoFisico IS NOT NULL";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setInt(1, idCatalogo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteEstudiante reporte = new ReporteEstudiante();
                    reporte.setIdDocumento(rs.getInt("idDocumento"));
                    reporte.setNombreEstudiante(rs.getString(
                                                           "nombreEstudiante"));
                    reporte.setMatricula(rs.getString("matricula"));
                    reporte.setNombreDocumento(rs.getString("nombreDocumento"));
                    reporte.setFecha(rs.getDate("fecha"));
                    reporte.setCalificacion(rs.getString("Calificacion"));
                    reporte.setObservacion(rs.getString("observacion"));
                    reporte.setArchivoFisico(rs.getBytes("archivoFisico"));
                    reportes.add(reporte);
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO(
                                  "Error al buscar reportes de estudiantes", e);
        }
        return reportes;
    }

    public boolean actualizarCalificacionYObservacion(int idDocumento,
                  String calificacion, String observacion) throws ExcepcionDAO {
        String consulta = "UPDATE documento SET Calificacion = ?, "
                        + "observacion = ? WHERE idDocumento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setString(1, calificacion);
            ps.setString(2, observacion);
            ps.setInt(3, idDocumento);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al actualizar la calificación "
                                            + "y observación del reporte", e);
        }
    }
}
