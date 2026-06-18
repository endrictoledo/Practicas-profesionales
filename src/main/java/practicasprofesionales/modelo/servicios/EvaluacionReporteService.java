package practicasprofesionales.modelo.servicios;

import java.util.ArrayList;
import java.util.List;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.DocumentoDAO;
import practicasprofesionales.modelo.DTO.ReporteEstudiante;
import practicasprofesionales.modelo.DTO.RespuestaOperacion;

public class EvaluacionReporteService {

    private DocumentoDAO documentoDAO;

    public EvaluacionReporteService() {
        this.documentoDAO = new DocumentoDAO();
    }

    public List<ReporteEstudiante> obtenerReportesPorTipo(String tipoReporte) throws ExcepcionDAO {
        int idCatalogo = documentoDAO.obtenerIdCatalogoDocumento(tipoReporte);
        if (idCatalogo != -1) {
            return documentoDAO.obtenerReportesPorCatalogo(idCatalogo);
        }
        return new ArrayList<>();
    }

    public RespuestaOperacion asignarCalificacion(ReporteEstudiante reporte, String calificacionTxt, String observacion) {
        if (calificacionTxt == null || calificacionTxt.trim().isEmpty()) {
            return new RespuestaOperacion(true, "Valor inválido. Ingrese una calificación válida.");
        }

        try {
            double calificacion = Double.parseDouble(calificacionTxt);
            if (calificacion < 0 || calificacion > 10) {
                return new RespuestaOperacion(true, "Valor inválido. Ingrese una calificación válida entre 0 y 10.");
            }
        } catch (NumberFormatException e) {
            return new RespuestaOperacion(true, "Valor numérico inválido. Ingrese una calificación válida.");
        }

        try {
            boolean exito = documentoDAO.actualizarCalificacionYObservacion(reporte.getIdDocumento(), calificacionTxt, observacion);
            if (exito) {

                reporte.setCalificacion(calificacionTxt);
                reporte.setObservacion(observacion);
                return new RespuestaOperacion(false, "Reporte evaluado exitosamente");
            } else {
                return new RespuestaOperacion(true, "No se pudo guardar la calificación en la base de datos.");
            }
        } catch (ExcepcionDAO e) {
            e.printStackTrace();
            return new RespuestaOperacion(true, "Error interno de base de datos: " + e.getMessage());
        }
    }
}
