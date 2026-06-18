/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.SeccionEE;

/**
 *
 * @author basa2
 */
public class SeccionEEDAO {

    public static ArrayList<SeccionEE> obtenerSecciones() throws SQLException {
        ArrayList<SeccionEE> lista = new ArrayList<>();
        String consulta = "SELECT idSeccionEE, nrcEE FROM seccion_ee";
        
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(consulta);
             ResultSet rs = sentencia.executeQuery()) {
            
            while (rs.next()) {
                SeccionEE seccion = new SeccionEE();
                seccion.setIdSeccionEE(rs.getInt("idSeccionEE"));
                seccion.setNrcEE(rs.getString("nrcEE"));
                lista.add(seccion);
            }
        }
        return lista;
    }
    
}
