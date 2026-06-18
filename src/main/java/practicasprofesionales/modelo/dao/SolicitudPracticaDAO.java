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
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.Proyecto;
import practicasprofesionales.modelo.pojo.SolicitudEstudiante;
import practicasprofesionales.modelo.pojo.SolicitudPractica;

/**
 *
 * @author basa2
 */
public class SolicitudPracticaDAO {
    public static ArrayList<SolicitudPractica> obtenerTodasLasSolicitudes() 
                                                           throws SQLException {
        ArrayList<SolicitudPractica> solicitudes = new ArrayList<>();
        String sql = "SELECT sp.idSolicitud, e.idEstudiante, e.matricula, "
                + " e.nombreEstudiante, se.prioridad, p.idProyecto,"
                + " p.nombre, p.cupo, ov.razonSocial, ep.nombreEncargado,"
                + " ep.apellidoPaterno, ep.apellidoMaterno "
                + "FROM solicitud_practica sp "
                + "JOIN estudiante e "
                + "ON sp.Estudiante_idEstudiante = e.idEstudiante "
                + "JOIN solicitud_estudiante se "
                + "ON sp.idSolicitud = se.idSolicitudPractica "
                + "JOIN proyecto p "
                + "ON se.idProyecto = p.idProyecto "
                + "JOIN organizacion_vinculada ov "
                + "ON p.Organizacion_vinculada_id_organizacion_vinculada = "
                + "ov.idOrganizacionVinculada "
                + "JOIN encargadoproyecto ep "
                + "ON p.EncargadoProyecto_idEncargadoProyecto = "
                + "ep.idEncargadoProyecto "
                + "WHERE sp.estado = 'PENDIENTE' "
                + "ORDER BY sp.idSolicitud, se.prioridad ASC";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            SolicitudPractica solicitudActual = null;
            int idSolicitudAnterior = -1;

            while (rs.next()) {
                int idSolicitudActual = rs.getInt("idSolicitud");
                if (idSolicitudActual != idSolicitudAnterior) {
                    solicitudActual = new SolicitudPractica();
                    solicitudActual.setIdSolicitud(idSolicitudActual);
                    solicitudActual.setIdEstudiante(rs.getInt("idEstudiante"));
                    solicitudActual.setNombreEstudiante(rs.getString(
                                                           "nombreEstudiante"));
                    solicitudActual.setMatricula(rs.getString("matricula"));
                    solicitudActual.setOpcionesProyecto(new ArrayList<>());
                    solicitudes.add(solicitudActual);
                    idSolicitudAnterior = idSolicitudActual;
                }

                SolicitudEstudiante se = new SolicitudEstudiante();
                se.setPrioridad(rs.getInt("prioridad"));

                Proyecto proyecto = new Proyecto();
                proyecto.setIdProyecto(rs.getInt("idProyecto"));
                proyecto.setNombre(rs.getString("nombre"));
                proyecto.setCupo(rs.getInt("cupo"));
                proyecto.setNombreEmpresa(rs.getString("razonSocial"));
                String nombreEncargado = rs.getString("nombreEncargado") + " " + 
                                         rs.getString("apellidoPaterno") + " " + 
                                         rs.getString("apellidoMaterno");
                proyecto.setNombreResponsable(nombreEncargado);

                se.setProyecto(proyecto);
                solicitudActual.getOpcionesProyecto().add(se);
            }
        }
        return solicitudes;
    }
    
    public boolean registrarSolicitud(int idEstudiante, Proyecto proyecto)
                                                           throws ExcepcionDAO {
        String query = "INSERT INTO solicitud_practica (nombreProyecto,"
                    + " razonSocialOrganizacion, Estudiante_idEstudiante) "
                    + "VALUES (?, ?, ?)";
                     
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, proyecto.getNombre());
            stmt.setString(2, proyecto.getNombreEmpresa());
            stmt.setInt(3, idEstudiante);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al registrar la solicitud"
                    + " de práctica en la base de datos", e);
        }
    }
}
