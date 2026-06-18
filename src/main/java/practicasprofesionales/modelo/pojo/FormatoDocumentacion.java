/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

import java.sql.Date;

/**
 *
 * @author basa2
 */
public class FormatoDocumentacion {
    private int idFormato;
    private String nombreArchivo;
    private byte[] archivo;
    private int idCatalogoDocumento;
    private String nombreTipoDocumento;
    private Date fechaRegistro;

    public FormatoDocumentacion() {
    }

    public int getIdFormato() {
        return idFormato;
    }

    public void setIdFormato(int idFormato) {
        this.idFormato = idFormato;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public int getIdCatalogoDocumento() {
        return idCatalogoDocumento;
    }

    public void setIdCatalogoDocumento(int idCatalogoDocumento) {
        this.idCatalogoDocumento = idCatalogoDocumento;
    }

    public String getNombreTipoDocumento() {
        return nombreTipoDocumento;
    }

    public void setNombreTipoDocumento(String nombreTipoDocumento) {
        this.nombreTipoDocumento = nombreTipoDocumento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
}
