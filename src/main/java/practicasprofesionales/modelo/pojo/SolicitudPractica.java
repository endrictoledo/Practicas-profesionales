/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

import java.util.ArrayList;

/**
 *
 * @author basa2
 */
public class SolicitudPractica {
    private int idSolicitud;
    private int idEstudiante;
    private ArrayList<SolicitudEstudiante> opcionesProyecto;
    private String nombreEstudiante;
    private String matricula;

    public SolicitudPractica() {
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public ArrayList<SolicitudEstudiante> getOpcionesProyecto() {
        return opcionesProyecto;
    }

    public void setOpcionesProyecto(ArrayList<SolicitudEstudiante> 
                                                            opcionesProyecto) {
        this.opcionesProyecto = opcionesProyecto;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    
}
