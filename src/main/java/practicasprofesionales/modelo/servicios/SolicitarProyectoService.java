package practicasprofesionales.modelo.servicios;

import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.ProyectoDAO;
import practicasprofesionales.modelo.dao.SolicitudProyectoDAO;
import practicasprofesionales.modelo.pojo.Proyecto;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.modelo.pojo.Usuario;
import practicasprofesionales.utilidades.SesionGlobal;

public class SolicitarProyectoService {
    private ProyectoDAO proyectoDAO;
    private SolicitudProyectoDAO solicitudDAO;

    public SolicitarProyectoService() {
        this.proyectoDAO = new ProyectoDAO();
        this.solicitudDAO = new SolicitudProyectoDAO();
    }

    public RespuestaOperacion solicitarProyecto(Proyecto proyecto) {
        try {
            // 1. Obtener estudiante actual
            Usuario usuarioSesion = SesionGlobal.getInstancia().getUsuarioActual();
            if (usuarioSesion == null) {
                return new RespuestaOperacion(true, "Error de sesión: Inicie sesión de nuevo.");
            }
            
            int idEstudiante = solicitudDAO.obtenerIdEstudiantePorUsuario(usuarioSesion.getIdUsuario());
            if (idEstudiante == -1) {
                return new RespuestaOperacion(true, "Error de sistema: Estudiante no encontrado.");
            }

            // 2. Verificar Cupo (EX 1)
            int cuposDisponibles = proyectoDAO.verificarCupos(proyecto.getIdProyecto(), proyecto.getNombre());
            if (cuposDisponibles <= 0) {
                return new RespuestaOperacion(true, "Lo sentimos, el cupo de este proyecto llegó al límite");
            }

            // 3. Registrar Solicitud (EX 2 si falla)
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
