package practicasprofesionales.modelo.servicios;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.dao.DocumentoDAO;
import practicasprofesionales.modelo.pojo.Documento;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.utilidades.SesionGlobal;
import practicasprofesionales.modelo.pojo.Usuario;

public class SubirDocumentoService {
    
    private DocumentoDAO documentoDAO;

    public SubirDocumentoService() {
        this.documentoDAO = new DocumentoDAO();
    }

    public RespuestaOperacion procesarSubidaDocumento(File archivo, String tipoDocumento) {
        if (archivo == null || !archivo.exists()) {
            return new RespuestaOperacion(true, "No se seleccionó ningún archivo válido.");
        }
        
        try {
            // Obtener el usuario actual para sacar su ID y buscar su expediente
            Usuario usuarioSesion = SesionGlobal.getInstancia().getUsuarioActual();
            if (usuarioSesion == null) {
                return new RespuestaOperacion(true, "Error: No hay sesión activa. Por favor inicie sesión nuevamente.");
            }

            // Buscar ID expediente
            int idExpediente = documentoDAO.obtenerIdExpedientePorUsuario(usuarioSesion.getIdUsuario());
            if (idExpediente == -1) {
                return new RespuestaOperacion(true, "El estudiante actual no tiene un expediente asignado.");
            }

            // Obtener el ID del catálogo dinámicamente
            int idCatalogo = documentoDAO.obtenerIdCatalogoDocumento(tipoDocumento);
            if (idCatalogo == -1) {
                // Como las categorías podrían no existir en la base de datos de prueba actual, las intentamos registrar o lanzamos error
                return new RespuestaOperacion(true, "No se encontró el tipo de documento '" + tipoDocumento + "' en la base de datos de catálogos.");
            }

            // Leer archivo a bytes
            byte[] archivoBytes = Files.readAllBytes(archivo.toPath());

            // Validar que no pase de 20MB (20 * 1024 * 1024 bytes)
            if (archivoBytes.length > 20971520) {
                return new RespuestaOperacion(true, "El archivo es demasiado grande. El máximo permitido es 20MB.");
            }

            // Crear el objeto Documento
            Documento doc = new Documento();
            doc.setFecha(new Date(System.currentTimeMillis()));
            doc.setCalificacion("N/A");
            doc.setIdEstadoDocumento(1); // 1 es 'Entregado' / 'En Revisión'
            doc.setIdCatalogoDocumento(idCatalogo);
            doc.setIdExpediente(idExpediente);
            doc.setArchivoFisico(archivoBytes);

            // Registrar en base de datos
            documentoDAO.registrarDocumento(doc);

            return new RespuestaOperacion(false, "Archivo cargado exitosamente");

        } catch (ExcepcionDAO e) {
            e.printStackTrace();
            return new RespuestaOperacion(true, "Error interno de base de datos: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new RespuestaOperacion(true, "Error al leer el archivo desde el disco duro: " + e.getMessage());
        }
    }
}
