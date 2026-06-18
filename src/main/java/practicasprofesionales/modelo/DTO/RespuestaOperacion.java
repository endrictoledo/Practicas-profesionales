package practicasprofesionales.modelo.DTO;

/**
 *
 * @author endri
 */
public class RespuestaOperacion {

    private boolean isError;
    private String mensaje;
    private Object datos;

    public RespuestaOperacion() {
    }

    public RespuestaOperacion(boolean isError, String mensaje) {
        this.isError = isError;
        this.mensaje = mensaje;
    }

    public RespuestaOperacion(boolean isError, String mensaje, Object datos) {
        this.isError = isError;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public boolean isError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getDatos() {
        return datos;
    }

    public void setDatos(Object datos) {
        this.datos = datos;
    }

}
