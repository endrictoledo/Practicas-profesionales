package practicasprofesionales.servicio;

import java.sql.SQLException;
import java.util.regex.Pattern;
import practicasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import practicasprofesionales.modelo.pojo.OrganizacionVinculada;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;

public class OrganizacionVinculadaService {
    
    // Regla de negocio EX 3: que contenga @ y .com
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+\\.com)$"; 
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static RespuestaOperacion guardarOrganizacion(OrganizacionVinculada ov) {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        
        // EX 1: Campos inválidos o vacíos
        if (ov.getRazonSocial() == null || ov.getRazonSocial().trim().isEmpty() ||
            ov.getDireccion() == null || ov.getDireccion().trim().isEmpty() ||
            ov.getSector() == null || ov.getSector().trim().isEmpty() ||
            ov.getCorreo() == null || ov.getCorreo().trim().isEmpty() ||
            ov.getTelefono() == null || ov.getTelefono().trim().isEmpty()) {
            
            respuesta.setError(true);
            respuesta.setMensaje("Parámetros inválidos o campos incompletos");
            return respuesta;
        }
        
        // EX 3: Formato de correo inválido
        if (!EMAIL_PATTERN.matcher(ov.getCorreo()).matches()) {
            respuesta.setError(true);
            respuesta.setMensaje("Formato de correo electrónico no válido");
            return respuesta;
        }
        
        try {
            // EX 2: Organización existente
            if (OrganizacionVinculadaDAO.verificarOrganizacionExistente(ov.getRazonSocial())) {
                respuesta.setError(true);
                respuesta.setMensaje("Esta organización ya se encuentra registrada en el sistema");
                return respuesta;
            }
            
            // Flujo Normal: Registrar
            return OrganizacionVinculadaDAO.registrarOrganizacion(ov);
            
        } catch (SQLException | NullPointerException e) {
            respuesta.setError(true);
            respuesta.setMensaje("Error de base de datos: " + e.getMessage());
        }
        
        return respuesta;
    }
}
