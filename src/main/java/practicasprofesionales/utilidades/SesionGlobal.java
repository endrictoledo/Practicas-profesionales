package practicasprofesionales.utilidades;

import practicasprofesionales.modelo.DTO.Usuario;

public class SesionGlobal {
    private static SesionGlobal instancia;
    private Usuario usuarioActual;

    private SesionGlobal() {}

    public static SesionGlobal getInstancia() {
        if (instancia == null) {
            instancia = new SesionGlobal();
        }
        return instancia;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public void cerrarSesion() {
        usuarioActual = null;
    }
}
