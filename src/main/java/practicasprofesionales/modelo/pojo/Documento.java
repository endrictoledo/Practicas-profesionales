package practicasprofesionales.modelo.pojo;

import java.sql.Date;

public class Documento {
    private int idDocumento;
    private Date fecha;
    private String calificacion;
    private int idEstadoDocumento;
    private int idCatalogoDocumento;
    private int idExpediente;
    private byte[] archivoFisico;
    private String estadoDocumento;
    private String nombreDocumento;
    private String nombreEstudiante;
    private String matriculaEstudiante;


    public Documento() {
    }

    public int getIdDocumento() { return idDocumento; }
    public void setIdDocumento(int idDocumento) { this.idDocumento = idDocumento; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getCalificacion() { return calificacion; }
    public void setCalificacion(String calificacion) { this.calificacion = calificacion; }

    public int getIdEstadoDocumento() { return idEstadoDocumento; }
    public void setIdEstadoDocumento(int idEstadoDocumento) { this.idEstadoDocumento = idEstadoDocumento; }

    public int getIdCatalogoDocumento() { return idCatalogoDocumento; }
    public void setIdCatalogoDocumento(int idCatalogoDocumento) { this.idCatalogoDocumento = idCatalogoDocumento; }

    public int getIdExpediente() { return idExpediente; }
    public void setIdExpediente(int idExpediente) { this.idExpediente = idExpediente; }

    public byte[] getArchivoFisico() { return archivoFisico; }
    public void setArchivoFisico(byte[] archivoFisico) { this.archivoFisico = archivoFisico; }

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(String estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getMatriculaEstudiante() {
        return matriculaEstudiante;
    }

    public void setMatriculaEstudiante(String matriculaEstudiante) {
        this.matriculaEstudiante = matriculaEstudiante;
    }
    
    
}
