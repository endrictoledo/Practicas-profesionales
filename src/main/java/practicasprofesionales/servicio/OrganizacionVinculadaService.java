package practicasprofesionales.servicio;

import java.sql.SQLException;
import java.util.regex.Pattern;
import practicasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import practicasprofesionales.modelo.pojo.OrganizacionVinculada;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;

public class OrganizacionVinculadaService {

    private static final String CORREO_REGEX = "^[A-Za-z0-9+_.-]+@(.+\\.com)$";
    private static final Pattern PATRON_CORREO = Pattern.compile(CORREO_REGEX);

    public static RespuestaOperacion guardarOrganizacion(OrganizacionVinculada ov) {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        if (verificarDatosValidos(ov)) {

            respuesta.setIsError(true);
            respuesta.setMensaje("Parámetros inválidos o campos incompletos");
            return respuesta;
        }

        if (!PATRON_CORREO.matcher(ov.getCorreo()).matches()) {
            respuesta.setIsError(true);
            respuesta.setMensaje("Formato de correo electrónico no válido");
            return respuesta;
        }
        try {

            if (OrganizacionVinculadaDAO.verificarOrganizacionExistente(ov.getRazonSocial())) {
                respuesta.setIsError(true);
                respuesta.setMensaje("Esta organización ya se encuentra registrada en el sistema");
                return respuesta;
            }

            return OrganizacionVinculadaDAO.registrarOrganizacion(ov);

        } catch (SQLException | NullPointerException e) {
            respuesta.setIsError(true);
            respuesta.setMensaje("Error de base de datos: " + e.getMessage());
        }

        return respuesta;
    }

    private static boolean verificarDatosValidos(OrganizacionVinculada ov) {

        if (ov.getRazonSocial() == null || ov.getRazonSocial().trim().isEmpty()
                || ov.getDireccion() == null || ov.getDireccion().trim().isEmpty()
                || ov.getSector() == null || ov.getSector().trim().isEmpty()
                || ov.getCorreo() == null || ov.getCorreo().trim().isEmpty()
                || ov.getTelefono() == null || ov.getTelefono().trim().isEmpty()) {
            return false;
        }
        return true;
    }

}
