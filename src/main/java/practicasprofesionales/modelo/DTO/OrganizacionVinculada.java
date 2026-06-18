package practicasprofesionales.modelo.DTO;

/**
 *
 * @author endri
 */
public class OrganizacionVinculada {
    private int idOrganizacionVinculada;
    private String razonSocial;
    private String direccion;
    private String sector;
    private String correo;
    private String telefono;

    public OrganizacionVinculada() {
    }

    public OrganizacionVinculada(String razonSocial, String direccion, String sector, String correo, String telefono) {
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.sector = sector;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getIdOrganizacionVinculada() {
        return idOrganizacionVinculada;
    }

    public void setIdOrganizacionVinculada(int idOrganizacionVinculada) {
        this.idOrganizacionVinculada = idOrganizacionVinculada;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
