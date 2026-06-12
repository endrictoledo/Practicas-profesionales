/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicasprofesionales.modelo.dao;

//import practicasprofesionales.dataacces.ConfigDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import practicasprofesionales.excepciones.DAOException;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.TipoUsuario;
import practicasprofesionales.modelo.pojo.Usuario;
import practicasprofesionales.utilidades.PasswordHasher;

/**
 *
 * @author endri
 */
public class UsuarioDAO {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

    private static final String SQL_INSERT
            = "INSERT INTO usuario (gmail, contrasena, tipoUsuario, estadoActividad) "
            + "VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE_PASSWORD
            = "UPDATE usuario SET contrasena = ? WHERE idUsuario = ?";

    private static final String SQL_DEACTIVATE
            = "UPDATE usuario SET estadoActividad = 'inactivo' WHERE idUsuario = ?";

    public int registerUser(Usuario usuario) throws DAOException {
        validateUserForRegistration(usuario);

        String hashedPassword = PasswordHasher.hash(usuario.getContrasenaPlana());

        try (Connection connection = ConexionBD.obtenerConexion(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, usuario.getCorreo().trim().toLowerCase());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, usuario.getTipoUsuario().getDbValue());
            preparedStatement.setString(4, usuario.isActivo() ? "activo" : "inactivo");

            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    logger.info(() -> "Usuario registrado — id: " + generatedId
                            + ", tipo: " + usuario.getTipoUsuario().getDbValue());
                    return generatedId;
                }
            }
            throw new DAOException("No se generó ID para el usuario", null);

        } catch (SQLException e) {
            if (isDuplicateEntry(e)) {
                throw new DAOException(
                        "El correo electrónico ya está registrado en el sistema.", e);
            }
            throw new DAOException("Error al registrar usuario", e);
        }
    }

    public Usuario login(String gmail, String plainPassword) throws DAOException {
        if (gmail == null || gmail.isBlank() || plainPassword == null || plainPassword.isBlank()) {
            return null;
        }

        String sentencia = "SELECT u.idUsuario, u.gmail, u.contrasena, u.tipoUsuario, eu.nombreEstado AS estadoActividad "
                + "FROM usuario u JOIN estado_usuario eu on u.estado = eu.idEstadoUsuario "
                + "WHERE u.gmail = ? AND eu.nombreEstado = 'ACTIVO'";
        try (Connection connection = ConexionBD.obtenerConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {

            preparedStatement.setString(1, gmail.trim().toLowerCase());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("contrasena");

                    if (PasswordHasher.verify(plainPassword, storedHash)) {
                        Usuario usuario = mapResultSet(resultSet);
                        logger.info(() -> "Login exitoso — gmail: " + gmail
                                + ", tipo: " + usuario.getTipoUsuario().getDbValue());
                        return usuario;
                    }
                }
            }
            logger.warning(() -> "Intento de login fallido — gmail: " + gmail);
            return null;

        } catch (SQLException e) {
            System.err.println("SQL ERROR: " + e.getMessage());
            throw new DAOException("Error de base de datos durante el login", e);
        }
    }

    public boolean updatePassword(int idUsuario, String newPlainPassword) throws DAOException {
        if (newPlainPassword == null || newPlainPassword.isBlank()) {
            throw new DAOException("La nueva contraseña no puede estar vacía", null);
        }

        String newHash = PasswordHasher.hash(newPlainPassword);

        try (Connection connection = ConexionBD.obtenerConexion(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PASSWORD)) {

            preparedStatement.setString(1, newHash);
            preparedStatement.setInt(2, idUsuario);

            int rows = preparedStatement.executeUpdate();
            logger.info(() -> "Contraseña actualizada — idUsuario: " + idUsuario);
            return rows > 0;

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar la contraseña", e);
        }
    }

    public boolean deactivateUser(int idUsuario) throws DAOException {
        try (Connection connection = ConexionBD.obtenerConexion(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_DEACTIVATE)) {

            preparedStatement.setInt(1, idUsuario);
            int rows = preparedStatement.executeUpdate();
            logger.info(() -> "Usuario desactivado — idUsuario: " + idUsuario);
            return rows > 0;

        } catch (SQLException e) {
            throw new DAOException("Error al desactivar usuario", e);
        }
    }

    private Usuario mapResultSet(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultSet.getInt("idUsuario"));
        usuario.setCorreo(resultSet.getString("gmail"));
        usuario.setTipoUsuario(TipoUsuario.fromDbValue(resultSet.getString("tipoUsuario")));
        usuario.setActivo("activo".equals(resultSet.getString("estadoActividad")));
        return usuario;
    }

    private void validateUserForRegistration(Usuario user) throws DAOException {
        if (user == null) {
            throw new DAOException("El usuario no puede ser null", null);
        }
        if (user.getCorreo()== null || user.getCorreo().isBlank()) {
            throw new DAOException("El gmail del usuario es obligatorio", null);
        }
        if (user.getContrasenaPlana()== null || user.getContrasenaPlana().isBlank()) {
            throw new DAOException("La contraseña del usuario es obligatoria", null);
        }
        if (user.getTipoUsuario()== null) {
            throw new DAOException("El tipo de usuario es obligatorio", null);
        }
    }

    private boolean isDuplicateEntry(SQLException e) {
        return e.getErrorCode() == 1062;
    }
}
