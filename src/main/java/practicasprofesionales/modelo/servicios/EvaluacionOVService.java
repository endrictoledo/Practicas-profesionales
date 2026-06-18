package practicasprofesionales.modelo.servicios;

import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.DocumentoDAO;
import practicasprofesionales.modelo.DTO.Documento;
import practicasprofesionales.modelo.DTO.RespuestaOperacion;

import java.nio.charset.StandardCharsets;
import java.sql.Date;

/**
 *
 * @author endri
 */
public class EvaluacionOVService {

    private final DocumentoDAO documentoDAO;

    public EvaluacionOVService() {
        this.documentoDAO = new DocumentoDAO();
    }

    public RespuestaOperacion procesarEvaluacion(int idUsuario, String satisfaccion, String involucramiento, String objetivos, String sugerencia) {
        
        if (satisfaccion == null || satisfaccion.trim().isEmpty() ||
            involucramiento == null || involucramiento.trim().isEmpty() ||
            objetivos == null || objetivos.trim().isEmpty() ||
            sugerencia == null || sugerencia.trim().isEmpty()) {
            return new RespuestaOperacion(true, "Por favor, responda todas las preguntas para continuar.");
        }

        try {
            int idExpediente = documentoDAO.obtenerIdExpedientePorUsuario(idUsuario);
            if (idExpediente == -1) {
                return new RespuestaOperacion(true, "El estudiante no tiene un expediente asignado. No se puede guardar la evaluación.");
            }

            
            String contenidoArchivo = "=== EVALUACIÓN DE ORGANIZACIÓN VINCULADA ===\n\n" +
                                      "1. Satisfacción con la estadía: " + satisfaccion + "/10\n" +
                                      "2. Nivel de involucramiento: " + involucramiento + "\n" +
                                      "3. Cumplimiento de objetivos: " + objetivos + "\n" +
                                      "4. Sugerencias / Comentarios:\n" + sugerencia + "\n\n" +
                                      "===========================================";

            byte[] archivoBytes = contenidoArchivo.getBytes(StandardCharsets.UTF_8);

            
            int idCatalogo = documentoDAO.obtenerIdCatalogoDocumento("Evaluación OV");
            if (idCatalogo == -1) {
                return new RespuestaOperacion(true, "No se encontró el tipo de documento 'Evaluación OV' en la base de datos.");
            }

            
            Documento doc = new Documento();
            doc.setFecha(new Date(System.currentTimeMillis()));
            doc.setCalificacion("N/A");
            doc.setIdEstadoDocumento(1); 
            doc.setIdCatalogoDocumento(idCatalogo);
            doc.setIdExpediente(idExpediente);
            doc.setArchivoFisico(archivoBytes);

            boolean exito = documentoDAO.registrarDocumento(doc);

            if (exito) {
                
                return new RespuestaOperacion(false, "Has realizado la evaluación correctamente.", archivoBytes);
            } else {
                return new RespuestaOperacion(true, "No se pudo registrar la evaluación en la base de datos.");
            }

        } catch (ExcepcionDAO e) {
            e.printStackTrace();
            return new RespuestaOperacion(true, "Error interno de base de datos: " + e.getMessage());
        }
    }
}
