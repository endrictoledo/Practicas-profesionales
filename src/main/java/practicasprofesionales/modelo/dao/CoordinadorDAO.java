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
import practicasprofesionales.modelo.pojo.Coordinador;
import practicasprofesionales.modelo.pojo.RespuestaOperacion;
import practicasprofesionales.utilidades.UtilidadContrasena;

/**
 *
 * @author basa2
 */
public class CoordinadorDAO {

    public static RespuestaOperacion registrarCoordinador(
                             Coordinador nuevoCoordinador) throws SQLException {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        Connection conexionBD = ConexionBD.obtenerConexion();
        if (conexionBD != null) {
            try {
                conexionBD.setAutoCommit(false);
                int idUsuario = registrarUsuarioBD(conexionBD,
                                                   nuevoCoordinador);
                if (idUsuario > 0) {
                    int idPersonal = registrarPersonal(conexionBD,
                                                   nuevoCoordinador, idUsuario);
                    if (idPersonal > 0) {
                        boolean rolAsignado = registrarRolBD(conexionBD,
                                                                 idPersonal, 3);
                        if (rolAsignado) {
                            conexionBD.commit();
                            respuesta.setIsError(false);
                            respuesta.setMensaje("Coordinador registrado"
                                                 + " exitosamente.");
                            return respuesta;
                        }
                    }
                }
                conexionBD.rollback();
                respuesta.setIsError(true);
                respuesta.setMensaje("No se pudo completar el registro"
                                                         + " del coordinador.");

            } catch (SQLException e) {
                conexionBD.rollback();
                throw e; 
            } finally {
                conexionBD.setAutoCommit(true);
                conexionBD.close();
            }
        } else {
            throw new SQLException("No hay conexión a la base de datos.");
        }
        return respuesta;
    }
    
    private static int registrarUsuarioBD(Connection conexionBD,
                                  Coordinador coordinador) throws SQLException {
        String correo = coordinador.getCorreo(); 
        String hashContrasena = UtilidadContrasena.hash(
                                                   coordinador.getContrasena());
        String sentencia = "INSERT INTO usuario (correoInstitucional, "
                       + "contraseña, estado, tipoUsuario) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pSentencia = conexionBD.prepareStatement(
                                sentencia, Statement.RETURN_GENERATED_KEYS)) {
            pSentencia.setString(1, correo);
            pSentencia.setString(2, hashContrasena);
            pSentencia.setInt(3, 1);
            pSentencia.setString(4, "COORDINADOR");

            int filasAfectadas = pSentencia.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet llaves = pSentencia.getGeneratedKeys();
                if (llaves.next()) return llaves.getInt(1);
            }
        }
        return -1;
    }

    private static int registrarPersonal(Connection conexionBD, 
                   Coordinador coordinador, int idUsuario) throws SQLException {
        String sentencia = "INSERT INTO personal_practicas (nombreProfesor,"
                + " apellidoPaterno, apellidoMaterno, numeroPersonal,"
                + " cubiculo, Estado_Personal_idEstado_Personal,"
                + " Usuario_idUsuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS)) {
            pSentencia.setString(1, coordinador.getNombre());
            pSentencia.setString(2, coordinador.getApellidoPaterno());
            pSentencia.setString(3, coordinador.getApellidoMaterno());
            pSentencia.setString(4, coordinador.getNoPersonal());
            pSentencia.setString(5, coordinador.getCubiculo());
            pSentencia.setInt(6, 1);
            pSentencia.setInt(7, idUsuario);

            int filasAfectadas = pSentencia.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet llaves = pSentencia.getGeneratedKeys();
                if (llaves.next()) return llaves.getInt(1);
            }
        }
        return -1;
    }

    private static boolean registrarRolBD(Connection conexionBD, int idProfesor,
                                                int idRol) throws SQLException {
        String sentencia = "INSERT INTO personal_tiene_rol "
                + "(Rol_idRol, Personal_practicas_id_proefesor)"
                + " VALUES (?, ?)";
        
        try (PreparedStatement pSentencia = conexionBD.prepareStatement(
                                                                   sentencia)) {
            pSentencia.setInt(1, idRol);
            pSentencia.setInt(2, idProfesor);
            int filasAfectadas = pSentencia.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
}
