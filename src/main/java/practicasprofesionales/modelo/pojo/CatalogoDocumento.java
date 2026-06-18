/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

/**
 *
 * @author basa2
 */
public class CatalogoDocumento {
    private int idCatalogoDocumento;
    private String nombreDocumento;

    public CatalogoDocumento() {
    }

    public int getIdCatalogoDocumento() {
        return idCatalogoDocumento;
    }

    public void setIdCatalogoDocumento(int idCatalogoDocumento) {
        this.idCatalogoDocumento = idCatalogoDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }
    
    @Override
    public String toString() {
        return this.nombreDocumento;
    }
}
