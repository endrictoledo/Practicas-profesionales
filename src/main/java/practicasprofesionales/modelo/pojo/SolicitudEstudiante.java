/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

/**
 *
 * @author basa2
 */
public class SolicitudEstudiante {
    private int idSolicitudFk;
    private int idEstudianteFk;
    private int idProyecto;
    private int prioridad;
    private Proyecto proyecto;

    public SolicitudEstudiante() {
    }

    public int getIdSolicitudFk() {
        return idSolicitudFk;
    }

    public void setIdSolicitudFk(int idSolicitudFk) {
        this.idSolicitudFk = idSolicitudFk;
    }

    public int getIdEstudianteFk() {
        return idEstudianteFk;
    }

    public void setIdEstudianteFk(int idEstudianteFk) {
        this.idEstudianteFk = idEstudianteFk;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    
    public String getNombreProyecto() {
        return (proyecto != null) ? proyecto.getNombre() : "N/A";
    }

    public int getCupoProyecto() {
        return (proyecto != null) ? proyecto.getCupo() : 0;
    }
    
    
}
