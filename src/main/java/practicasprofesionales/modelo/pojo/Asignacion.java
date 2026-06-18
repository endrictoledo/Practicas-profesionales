/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

import java.time.LocalDate;

/**
 *
 * @author basa2
 */
public class Asignacion {
    private int idProyectoFk;
    private int idEstudianteFk;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;

    public Asignacion() {
    }

    public int getIdProyectoFk() {
        return idProyectoFk;
    }

    public void setIdProyectoFk(int idProyectoFk) {
        this.idProyectoFk = idProyectoFk;
    }

    public int getIdEstudianteFk() {
        return idEstudianteFk;
    }

    public void setIdEstudianteFk(int idEstudianteFk) {
        this.idEstudianteFk = idEstudianteFk;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    
    
}
