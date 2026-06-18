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
import java.util.Map;
import java.util.HashMap;
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

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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
                String nombreEncargado = rs.getString("nombreEncargado") + " "
                        + rs.getString("apellidoPaterno") + " "
                        + rs.getString("apellidoMaterno");
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

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(query)) {

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

    public static Map<Integer, Integer> obtenerSeleccionesEstudiante(int idEstudiante) throws ExcepcionDAO {
        Map<Integer, Integer> selecciones = new HashMap<>();
        String sql = "SELECT se.idProyecto, se.prioridad "
                + "FROM solicitud_practica sp "
                + "JOIN solicitud_estudiante se ON sp.idSolicitud = se.idSolicitudPractica "
                + "WHERE sp.Estudiante_idEstudiante = ? AND sp.estado = 'PENDIENTE'";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    selecciones.put(rs.getInt("idProyecto"), rs.getInt("prioridad"));
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al obtener las selecciones del estudiante", e);
        }
        return selecciones;
    }

    public static boolean registrarSolicitudMultipe(int idEstudiante,
            List<SolicitudEstudiante> selecciones) throws ExcepcionDAO {
        String queryVerificar = "SELECT idSolicitud FROM solicitud_practica WHERE Estudiante_idEstudiante = ? AND estado = 'PENDIENTE'";
        String sentenciaPractica = "INSERT INTO solicitud_practica (Estudiante_idEstudiante, estado) VALUES (?, 'PENDIENTE')";
        String sentenciaEliminarAnteriores = "DELETE FROM solicitud_estudiante WHERE idSolicitudPractica = ?";
        String sentenciaEstudiante = "INSERT INTO solicitud_estudiante (idSolicitudPractica, idEstudiante, idProyecto, prioridad) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            int idSolicitudPractica = -1;
            try (PreparedStatement psVerificar = conn.prepareStatement(queryVerificar)) {
                psVerificar.setInt(1, idEstudiante);
                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next()) {
                        idSolicitudPractica = rs.getInt("idSolicitud");
                    }
                }
            }

            if (idSolicitudPractica != -1) {

                try (PreparedStatement psEliminar = conn.prepareStatement(sentenciaEliminarAnteriores)) {
                    psEliminar.setInt(1, idSolicitudPractica);
                    psEliminar.executeUpdate();
                }
            } else {

                try (PreparedStatement psPractica = conn.prepareStatement(sentenciaPractica, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psPractica.setInt(1, idEstudiante);
                    psPractica.executeUpdate();
                    try (ResultSet rs = psPractica.getGeneratedKeys()) {
                        if (rs.next()) {
                            idSolicitudPractica = rs.getInt(1);
                        } else {
                            throw new SQLException("Falló la creación de la solicitud, no se obtuvo el ID.");
                        }
                    }
                }
            }

            try (PreparedStatement psEstudiante = conn.prepareStatement(sentenciaEstudiante)) {
                for (SolicitudEstudiante seleccion : selecciones) {
                    psEstudiante.setInt(1, idSolicitudPractica);
                    psEstudiante.setInt(2, idEstudiante);
                    psEstudiante.setInt(3, seleccion.getProyecto().getIdProyecto());
                    psEstudiante.setInt(4, seleccion.getPrioridad());
                    psEstudiante.addBatch();
                }
                psEstudiante.executeBatch();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new ExcepcionDAO("Error al registrar la solicitud múltiple en la base de datos", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
