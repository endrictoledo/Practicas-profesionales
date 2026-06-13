package practicasprofesionales.modelo.pojo;

public class RespuestaOperacion {
    private boolean error;
    private String mensaje;
    private Object datos;

    public RespuestaOperacion() {
    }

    public RespuestaOperacion(boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
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
