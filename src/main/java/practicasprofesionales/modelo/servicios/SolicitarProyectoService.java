package practicasprofesionales.modelo.servicios;

import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.ProyectoDAO;
import practicasprofesionales.modelo.dao.SolicitudProyectoDAO;
import practicasprofesionales.modelo.DTO.Proyecto;
import practicasprofesionales.modelo.DTO.RespuestaOperacion;
import practicasprofesionales.modelo.DTO.Usuario;
import practicasprofesionales.utilidades.SesionGlobal;

/**
 *
 * @author endri
 */
public class SolicitarProyectoService {
    private ProyectoDAO proyectoDAO;
    private SolicitudProyectoDAO solicitudDAO;

    public SolicitarProyectoService() {
        this.proyectoDAO = new ProyectoDAO();
        this.solicitudDAO = new SolicitudProyectoDAO();
    }

    public RespuestaOperacion solicitarProyecto(Proyecto proyecto) {
        try {
            Usuario usuarioSesion = SesionGlobal.getInstancia().getUsuarioActual();
            if (usuarioSesion == null) {
                return new RespuestaOperacion(true, "Error de sesión: Inicie sesión de nuevo.");
            }
            
            int idEstudiante = solicitudDAO.obtenerIdEstudiantePorUsuario(usuarioSesion.getIdUsuario());
            if (idEstudiante == -1) {
                return new RespuestaOperacion(true, "Error de sistema: Estudiante no encontrado.");
            }

            int cuposDisponibles = proyectoDAO.verificarCupos(proyecto.getIdProyecto(), proyecto.getNombre());
            if (cuposDisponibles <= 0) {
                return new RespuestaOperacion(true, "Lo sentimos, el cupo de este proyecto llegó al límite");
            }

            boolean registrado = solicitudDAO.registrarSolicitud(idEstudiante, proyecto);
            if (registrado) {
                return new RespuestaOperacion(false, "El proyecto fue agregado con éxito");
            } else {
                return new RespuestaOperacion(true, "Error de sistema: No se pudo registrar su solicitud en este momento. Intente más tarde");
            }
            
        } catch (ExcepcionDAO e) {
            e.printStackTrace();
            return new RespuestaOperacion(true, "Error de sistema: No se pudo registrar su solicitud en este momento. Intente más tarde");
        }
    }
}
