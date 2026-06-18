package practicasprofesionales.modelo.pojo;

import practicasprofesionales.modelo.TipoUsuario;

/**
 *
 * @author endri
 */
public class Usuario {

    private int idUsuario;
    private String correo;
    private String contrasenaPlana;
    
    private TipoUsuario tipoUsuario;
    private boolean activo;
    private int estado;
    public Usuario() {
        this.activo = true;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenaPlana() {
        return contrasenaPlana;
    }

    public void setContrasenaPlana(String contrasenaPlana) {
        this.contrasenaPlana = contrasenaPlana;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    
}
