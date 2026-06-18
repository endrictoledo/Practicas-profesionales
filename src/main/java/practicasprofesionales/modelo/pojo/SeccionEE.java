/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.pojo;

/**
 *
 * @author basa2
 */
public class SeccionEE {
    private int idSeccionEE;
    private String nrcEE;

    public SeccionEE() {
    }

    public int getIdSeccionEE() {
        return idSeccionEE;
    }

    public void setIdSeccionEE(int idSeccionEE) {
        this.idSeccionEE = idSeccionEE;
    }

    public String getNrcEE() {
        return nrcEE;
    }

    public void setNrcEE(String nrcEE) {
        this.nrcEE = nrcEE;
    }
    
    @Override
    public String toString() {
        return this.nrcEE;
    }
}
