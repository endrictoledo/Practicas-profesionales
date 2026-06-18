package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.OrganizacionVinculada;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;

public class OrganizacionVinculadaDAO {

    public static boolean verificarOrganizacionExistente(String razonSocial)
            throws SQLException, NullPointerException {
        Connection conexion = ConexionBD.obtenerConexion();
        int count = 0;
        if (conexion != null) {
            String consulta = "SELECT COUNT(*) FROM organizacion_vinculada"
                    + " WHERE razonSocial = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, razonSocial);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            conexion.close();
            return (count > 0);
        }
        throw new SQLException("Error de conexión a la base de datos.");
    }

    public static RespuestaOperacion registrarOrganizacion(
           OrganizacionVinculada ov) throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        Connection conexionDB = ConexionBD.obtenerConexion();
        if (conexionDB != null) {
            String sentencia = "INSERT INTO organizacion_vinculada (razonSocial,"
                    + " direccion, sector, correo, telefono)"
                    + " VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = conexionDB.prepareStatement(sentencia);
            ps.setString(1, ov.getRazonSocial());
            ps.setString(2, ov.getDireccion());
            ps.setString(3, ov.getSector());
            ps.setString(4, ov.getCorreo());
            ps.setString(5, ov.getTelefono());

            int filasAfectadas = ps.executeUpdate();
            conexionDB.close();
            if (filasAfectadas > 0) {
                respuesta.setIsError(false);
                respuesta.setMensaje("Registro exitoso");
            } else {
                respuesta.setIsError(true);
                respuesta.setMensaje("Lo sentimos, la información no pudo ser "
                        + "guardada, intente nuevamente.");
            }
            return respuesta;
        }
        throw new SQLException("Error de conexión a la base de datos.");
    }
    
    public static ArrayList<OrganizacionVinculada> obtenerOrganizaciones() 
                                                           throws SQLException {
        ArrayList<OrganizacionVinculada> lista = new ArrayList<>();
        String consulta = "SELECT idOrganizacionVinculada, razonSocial, "
                + " direccion, sector, correo, telefono "
                + " FROM organizacion_vinculada";
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(consulta);
             ResultSet rs = sentencia.executeQuery()) {
            
            while (rs.next()) {
                OrganizacionVinculada org = new OrganizacionVinculada();
                org.setIdOrganizacionVinculada(rs.getInt(
                                                    "idOrganizacionVinculada"));
                org.setRazonSocial(rs.getString("razonSocial"));
                org.setDireccion(rs.getString("direccion"));
                org.setSector(rs.getString("sector"));
                org.setCorreo(rs.getString("correo"));
                org.setTelefono(rs.getString("telefono"));
                lista.add(org);
            }
        }
        return lista;
    }
}
