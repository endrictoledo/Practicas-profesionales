/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author endri
 */
public class ConexionBD {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String NOMBRE_BD = "practicasprofesionales";
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    private static final String USUARIO_BD = "root";
    private static final String PASSWORD = "325102099";

    public static Connection obtenerConexion() throws SQLException {
        try {
            String URL_CONEXION = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + NOMBRE_BD
                    + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";

            Class.forName(DRIVER);

            return DriverManager.getConnection(URL_CONEXION, USUARIO_BD, PASSWORD);
        } catch (ClassNotFoundException e) {

            throw new SQLException("No se encontró driver de MySQL: " + e.getMessage(), e);
        }

    }
}
