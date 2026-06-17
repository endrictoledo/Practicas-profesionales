package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.Proyecto;

public class ProyectoDAO {

    public List<Proyecto> obtenerProyectosDisponibles() throws ExcepcionDAO {
        List<Proyecto> proyectos = new ArrayList<>();
        // Hacemos JOIN con organizacion_vinculada y encargadoproyecto para traer los nombres reales
        String query = "SELECT p.idProyecto, p.nombre, p.descripcion, p.objetivo, "
                     + "p.cupos, p.responsabilidades, p.actividades, p.duracion, p.horario, "
                     + "o.nombre AS nombreOrganizacion, "
                     + "CONCAT(e.nombreEncargado, ' ', e.apellidoPaterno, ' ', e.apellidoMaterno) AS nombreEncargado "
                     + "FROM proyecto p "
                     + "JOIN organizacion_vinculada o ON p.Organizacion_vinculada_id_organizacion_vinculada = o.id_organizacion_vinculada "
                     + "LEFT JOIN encargadoproyecto e ON p.EncargadoProyecto_idEncargadoProyecto = e.idEncargadoProyecto "
                     + "WHERE p.cupos > 0";
                     
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setIdProyecto(rs.getInt("idProyecto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setObjetivo(rs.getString("objetivo"));
                p.setCupos(rs.getInt("cupos"));
                p.setResponsabilidades(rs.getString("responsabilidades"));
                p.setActividades(rs.getString("actividades"));
                p.setDuracion(rs.getString("duracion"));
                p.setHorario(rs.getString("horario"));
                p.setNombreOrganizacion(rs.getString("nombreOrganizacion"));
                p.setNombreEncargado(rs.getString("nombreEncargado"));
                proyectos.add(p);
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al obtener los proyectos disponibles", e);
        }
        return proyectos;
    }

    public int verificarCupos(int idProyecto, String nombreProyecto) throws ExcepcionDAO {
        String queryCupos = "SELECT cupos FROM proyecto WHERE idProyecto = ?";
        String queryOcupados = "SELECT COUNT(*) AS ocupados FROM solicitud_practica WHERE nombreProyecto = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion()) {
            int cuposTotales = 0;
            int ocupados = 0;
            
            try (PreparedStatement stmt1 = conn.prepareStatement(queryCupos)) {
                stmt1.setInt(1, idProyecto);
                try (ResultSet rs1 = stmt1.executeQuery()) {
                    if (rs1.next()) {
                        cuposTotales = rs1.getInt("cupos");
                    } else {
                        return 0; // No existe
                    }
                }
            }
            
            try (PreparedStatement stmt2 = conn.prepareStatement(queryOcupados)) {
                stmt2.setString(1, nombreProyecto);
                try (ResultSet rs2 = stmt2.executeQuery()) {
                    if (rs2.next()) {
                        ocupados = rs2.getInt("ocupados");
                    }
                }
            }
            
            return cuposTotales - ocupados;
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al verificar los cupos del proyecto", e);
        }
    }
}
