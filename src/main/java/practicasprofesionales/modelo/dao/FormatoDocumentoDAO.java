/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.pojo.FormatoDocumentacion;

/**
 *
 * @author basa2
 */
public class FormatoDocumentoDAO {
    public static boolean registrarFormato(FormatoDocumentacion formato,
                      File archivo) throws SQLException, FileNotFoundException {
        String sql = "INSERT INTO formato_documento (nombreDocumento,"
                + " archivo, idCatalogoDocumento, fecha)"
                + " VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, formato.getNombreArchivo());
            FileInputStream fis = new FileInputStream(archivo);
            ps.setBinaryStream(2, fis, (int) archivo.length());
            ps.setInt(3, formato.getIdCatalogoDocumento());
            ps.setDate(4, formato.getFechaRegistro());
            return ps.executeUpdate() > 0;
        }
    }
    
    public static ArrayList<FormatoDocumentacion> obtenerFormatos()
                                                           throws SQLException {
        ArrayList<FormatoDocumentacion> lista = new ArrayList<>();
        String sql = "SELECT fd.idFormato_documento, fd.nombreDocumento,"
                + " fd.fecha, cd.nombreDocumento AS tipoDocumento "
                + "FROM formato_documento fd "
                + "JOIN catalogo_documento cd"
                + " ON fd.idCatalogoDocumento = cd.idCatalogoDocumento";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FormatoDocumentacion formato = new FormatoDocumentacion();
                formato.setIdFormato(rs.getInt("idFormato_documento"));
                formato.setNombreArchivo(rs.getString("nombreDocumento"));
                formato.setFechaRegistro(rs.getDate("fecha"));
                formato.setNombreTipoDocumento(rs.getString("tipoDocumento"));

                lista.add(formato);
            }
        }
        return lista;
    }
    
    public static byte[] obtenerArchivoPorId(int idFormato) throws SQLException{
        String sql = "SELECT archivo FROM formato_documento "
                + "WHERE idFormato_documento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFormato);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("archivo");
                }
            }
        }
        return null;
    }
    
    public static boolean existeFormatoDelCatalogo(int idCatalogoDocumento) 
                                                          throws SQLException {
        boolean existe = false;
        String sql = "SELECT COUNT(*) AS total FROM formato_documento "
                   + "WHERE idCatalogoDocumento = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
                            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCatalogoDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("total") > 0) {
                        existe = true;
                    }
                }
            }
        }
        return existe;
    }
}
