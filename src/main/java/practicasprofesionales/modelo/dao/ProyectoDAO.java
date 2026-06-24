/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author basa2
 */
public class ProyectoDAO {
    
    public static int registrarProyecto(Proyecto proyecto) throws SQLException {
        String sentencia = "INSERT INTO proyecto (nombre, descripcion,"
          + " objetivo, cupo, Organizacion_vinculada_id_organizacion_vinculada,"
          + " EncargadoProyecto_idEncargadoProyecto)"
          + " VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
            PreparedStatement pSentencia = conexion.prepareStatement(sentencia))
                                                                               {
            pSentencia.setString(1, proyecto.getNombre());
            pSentencia.setString(2, proyecto.getDescripcion());
            pSentencia.setString(3, proyecto.getObjetivo());
            pSentencia.setInt(4, proyecto.getCupo());
            pSentencia.setInt(5, proyecto.getIdOrganizacionVinculada());
            pSentencia.setInt(6, proyecto.getIdEncargado());
            return pSentencia.executeUpdate();
        }
    }
    
    public List<Proyecto> obtenerProyectosDisponibles() throws ExcepcionDAO {
        List<Proyecto> proyectos = new ArrayList<>();

        String consulta = "SELECT p.idProyecto, p.nombre,"
                + " p.descripcion, p.objetivo, "
                + "p.cupo, p.responsabilidades, p.actividades,"
                + " p.duracion, p.horario, "
                + "o.razonSocial AS nombreOrganizacion, "
                + "CONCAT(e.nombreEncargado, ' ', e.apellidoPaterno,"
                + " ' ', e.apellidoMaterno) AS nombreEncargado "
                + "FROM proyecto p "
                + "JOIN organizacion_vinculada o "
                + "ON p.Organizacion_vinculada_id_organizacion_vinculada ="
                + " o.idOrganizacionVinculada "
                + "LEFT JOIN encargadoproyecto e "
                + "ON p.EncargadoProyecto_idEncargadoProyecto ="
                + " e.idEncargadoProyecto "
                + "WHERE p.cupo > 0";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setIdProyecto(rs.getInt("idProyecto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setObjetivo(rs.getString("objetivo"));
                p.setCupo(rs.getInt("cupo"));
                p.setResponsabilidades(rs.getString("responsabilidades"));
                p.setActividades(rs.getString("actividades"));
                p.setDuracion(rs.getString("duracion"));
                p.setHorario(rs.getString("horario"));
                p.setNombreEmpresa(rs.getString("nombreOrganizacion"));
                p.setNombreResponsable(rs.getString("nombreEncargado"));
                proyectos.add(p);
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO(
                    "Error al obtener los proyectos disponibles", e);
        }
        return proyectos;
    }

    public int verificarCupos(int idProyecto, String nombreProyecto)
                                                          throws ExcepcionDAO {
        String consultaCupos = "SELECT cupo FROM proyecto WHERE idProyecto = ?";
        String consultaOcupados = "SELECT COUNT(*) AS ocupados "
                + "FROM solicitud_practica WHERE nombreProyecto = ?";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            int cuposTotales = 0;
            int ocupados = 0;

            try (PreparedStatement ps1 = conn.prepareStatement(consultaCupos)) {
                ps1.setInt(1, idProyecto);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    if (rs1.next()) {
                        cuposTotales = rs1.getInt("cupo");
                    } else {
                        return 0; 
                    }
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(
                                                            consultaOcupados)) {
                ps2.setString(1, nombreProyecto);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        ocupados = rs2.getInt("ocupados");
                    }
                }
            }

            return cuposTotales - ocupados;
        } catch (SQLException e) {
            throw new ExcepcionDAO(
                    "Error al verificar los cupos del proyecto", e);
        }
    }
    
    public static boolean existeProyecto(String nombreProyecto,
                                       int idOrganizacion) throws SQLException {
        boolean existe = false;
        String sentencia = "SELECT COUNT(*) AS total FROM proyecto "
                + "WHERE nombre = ? "
                + "AND Organizacion_vinculada_id_organizacion_vinculada = ?";
        try (Connection conn = ConexionBD.obtenerConexion()) {
            try (PreparedStatement ps = conn.prepareStatement(sentencia)) {

                ps.setString(1, nombreProyecto);
                ps.setInt(2, idOrganizacion);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getInt("total") > 0) {
                            existe = true;
                        }
                    }
                }
            }
        }

        return existe;
    }
}
