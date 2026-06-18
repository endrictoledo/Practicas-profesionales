/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

/**
 *
 * @author basa2
 */
public class Encargado {
    private int idEncargadoProyecto;
    private String nombreEncargado;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String cargo;
    private String correoElectronico;
    private int idOrganizacionVinculada;

    public Encargado() {
    }

    public Encargado(int idEncargadoProyecto, String nombreEncargado, String apellidoPaterno, String apellidoMaterno, String cargo, String correoElectronico, int idOrganizacionVinculada) {
        this.idEncargadoProyecto = idEncargadoProyecto;
        this.nombreEncargado = nombreEncargado;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.cargo = cargo;
        this.correoElectronico = correoElectronico;
        this.idOrganizacionVinculada = idOrganizacionVinculada;
    }

    public int getIdEncargadoProyecto() {
        return idEncargadoProyecto;
    }

    public void setIdEncargadoProyecto(int idEncargadoProyecto) {
        this.idEncargadoProyecto = idEncargadoProyecto;
    }

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public void setNombreEncargado(String nombreEncargado) {
        this.nombreEncargado = nombreEncargado;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public int getIdOrganizacionVinculada() {
        return idOrganizacionVinculada;
    }

    public void setIdOrganizacionVinculada(int idOrganizacionVinculada) {
        this.idOrganizacionVinculada = idOrganizacionVinculada;
    }

}
