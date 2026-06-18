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
import practicasprofesionales.modelo.pojo.CatalogoDocumento;

/**
 *
 * @author basa2
 */
public class CatalogoDocumentoDAO {
    public static ArrayList<CatalogoDocumento> obtenerCatalogo() throws SQLException {
        ArrayList<CatalogoDocumento> catalogo = new ArrayList<>();
        String sql = "SELECT idCatalogoDocumento, nombreDocumento FROM catalogo_documento";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CatalogoDocumento doc = new CatalogoDocumento();
                doc.setIdCatalogoDocumento(rs.getInt("idCatalogoDocumento"));
                doc.setNombreDocumento(rs.getString("nombreDocumento"));
                catalogo.add(doc);
            }
        }
        return catalogo;
    }
}
