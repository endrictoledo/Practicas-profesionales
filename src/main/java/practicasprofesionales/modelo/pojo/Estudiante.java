/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

/**
 *
 * @author basa2
 */
public class Estudiante {
    private String matricula;
    private String nombreEstudiante;
    private int avanceCrediticio;
    private double promedio;
    private int idUsuario;
    private int idSeccionEE; 
    private int idEstadoEstudiante; 

    public Estudiante() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public int getAvanceCrediticio() {
        return avanceCrediticio;
    }

    public void setAvanceCrediticio(int avanceCrediticio) {
        this.avanceCrediticio = avanceCrediticio;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdSeccionEE() {
        return idSeccionEE;
    }

    public void setIdSeccionEE(int idSeccionEE) {
        this.idSeccionEE = idSeccionEE;
    }

    public int getIdEstadoEstudiante() {
        return idEstadoEstudiante;
    }

    public void setIdEstadoEstudiante(int idEstadoEstudiante) {
        this.idEstadoEstudiante = idEstadoEstudiante;
    }
    
    
}
