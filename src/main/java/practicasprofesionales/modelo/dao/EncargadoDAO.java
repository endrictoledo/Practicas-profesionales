/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.Encargado;

/**
 *
 * @author basa2
 */
public class EncargadoDAO {
    
    public static int registrarEncargado(Encargado encargado) throws SQLException {
        String sentencia = "INSERT INTO encargadoproyecto (nombreEncargado,"
                + " apellidoPaterno, apellidoMaterno, cargo, correoElectronico,"
                + " Organizacion_vinculada_idOrganizacionVinculada)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        int idGenerado = -1;
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pSentencia = conexion.prepareStatement(
                                  sentencia, Statement.RETURN_GENERATED_KEYS)) {
            pSentencia.setString(1, encargado.getNombreEncargado());
            pSentencia.setString(2, encargado.getApellidoPaterno());
            pSentencia.setString(3, encargado.getApellidoMaterno());
            pSentencia.setString(4, encargado.getCargo());
            pSentencia.setString(5, encargado.getCorreoElectronico());
            pSentencia.setInt(6, encargado.getIdOrganizacionVinculada());
            int filasAfectadas = pSentencia.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet llaves = pSentencia.getGeneratedKeys();
                if (llaves.next()) {
                    idGenerado = llaves.getInt(1);
                }
            }
        }
        return idGenerado;
    }
}
