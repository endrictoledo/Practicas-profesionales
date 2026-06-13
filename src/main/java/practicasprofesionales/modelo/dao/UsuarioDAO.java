package practicasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import practicasprofesionales.excepciones.ExcepcionDAO;
import practicasprofesionales.modelo.ConexionBD;
import practicasprofesionales.modelo.TipoUsuario;
import practicasprofesionales.modelo.pojo.Usuario;
import practicasprofesionales.utilidades.UtilidadContrasena;

public class UsuarioDAO {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

    // Se asume que estado 1 es ACTIVO y 2 es INACTIVO según la base de datos
    private static final String SQL_INSERT
            = "INSERT INTO usuario (correoInstitucional, contraseña, tipoUsuario, estado) "
            + "VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE_PASSWORD
            = "UPDATE usuario SET contraseña = ? WHERE idUsuario = ?";

    private static final String SQL_DEACTIVATE
            = "UPDATE usuario SET estado = 2 WHERE idUsuario = ?"; // 2 = Inactivo

    public int registerUser(Usuario usuario) throws ExcepcionDAO {
        validateUserForRegistration(usuario);

        String hashedPassword = UtilidadContrasena.hash(usuario.getContrasenaPlana());

        try (Connection connection = ConexionBD.obtenerConexion(); 
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, usuario.getCorreo().trim().toLowerCase());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, usuario.getTipoUsuario().getDbValue());
            preparedStatement.setInt(4, usuario.isActivo() ? 1 : 2); // 1 = Activo, 2 = Inactivo

            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    logger.info(() -> "Usuario registrado — id: " + generatedId
                            + ", tipo: " + usuario.getTipoUsuario().getDbValue());
                    return generatedId;
                }
            }
            throw new ExcepcionDAO("No se generó ID para el usuario", null);

        } catch (SQLException e) {
            if (isDuplicateEntry(e)) {
                throw new ExcepcionDAO("El correo electrónico ya está registrado en el sistema.", e);
            }
            throw new ExcepcionDAO("Error al registrar usuario", e);
        }
    }

    public Usuario login(String correoInstitucional, String plainPassword) throws ExcepcionDAO {
        if (correoInstitucional == null || correoInstitucional.isBlank() || plainPassword == null || plainPassword.isBlank()) {
            return null;
        }

        String sentencia = "SELECT u.idUsuario, u.correoInstitucional, u.contraseña, u.tipoUsuario, eu.nombreEstado AS estadoActividad "
                + "FROM usuario u JOIN estado_usuario eu on u.estado = eu.idEstadoUsuario "
                + "WHERE u.correoInstitucional = ? AND eu.nombreEstado = 'ACTIVO'";
                
        try (Connection connection = ConexionBD.obtenerConexion(); 
             PreparedStatement preparedStatement = connection.prepareStatement(sentencia)) {

            preparedStatement.setString(1, correoInstitucional.trim().toLowerCase());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("contraseña");

                    if (UtilidadContrasena.verify(plainPassword, storedHash)) {
                        Usuario usuario = mapResultSet(resultSet);
                        logger.info(() -> "Login exitoso — correo: " + correoInstitucional
                                + ", tipo: " + usuario.getTipoUsuario().getDbValue());
                        return usuario;
                    }
                }
            }
            logger.warning(() -> "Intento de login fallido — correo: " + correoInstitucional);
            return null;

        } catch (SQLException e) {
            System.err.println("SQL ERROR: " + e.getMessage());
            throw new ExcepcionDAO("Error de base de datos durante el login", e);
        }
    }

    public boolean updatePassword(int idUsuario, String newPlainPassword) throws ExcepcionDAO {
        if (newPlainPassword == null || newPlainPassword.isBlank()) {
            throw new ExcepcionDAO("La nueva contraseña no puede estar vacía", null);
        }

        String newHash = UtilidadContrasena.hash(newPlainPassword);

        try (Connection connection = ConexionBD.obtenerConexion(); 
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PASSWORD)) {

            preparedStatement.setString(1, newHash);
            preparedStatement.setInt(2, idUsuario);

            int rows = preparedStatement.executeUpdate();
            logger.info(() -> "Contraseña actualizada — idUsuario: " + idUsuario);
            return rows > 0;

        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al actualizar la contraseña", e);
        }
    }

    public boolean deactivateUser(int idUsuario) throws ExcepcionDAO {
        try (Connection connection = ConexionBD.obtenerConexion(); 
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DEACTIVATE)) {

            preparedStatement.setInt(1, idUsuario);
            int rows = preparedStatement.executeUpdate();
            logger.info(() -> "Usuario desactivado — idUsuario: " + idUsuario);
            return rows > 0;

        } catch (SQLException e) {
            throw new ExcepcionDAO("Error al desactivar usuario", e);
        }
    }

    private Usuario mapResultSet(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultSet.getInt("idUsuario"));
        usuario.setCorreo(resultSet.getString("correoInstitucional"));
        usuario.setTipoUsuario(TipoUsuario.fromDbValue(resultSet.getString("tipoUsuario")));
        usuario.setActivo("ACTIVO".equalsIgnoreCase(resultSet.getString("estadoActividad")));
        return usuario;
    }

    private void validateUserForRegistration(Usuario user) throws ExcepcionDAO {
        if (user == null) {
            throw new ExcepcionDAO("El usuario no puede ser null", null);
        }
        if (user.getCorreo()== null || user.getCorreo().isBlank()) {
            throw new ExcepcionDAO("El correo del usuario es obligatorio", null);
        }
        if (user.getContrasenaPlana()== null || user.getContrasenaPlana().isBlank()) {
            throw new ExcepcionDAO("La contraseña del usuario es obligatoria", null);
        }
        if (user.getTipoUsuario()== null) {
            throw new ExcepcionDAO("El tipo de usuario es obligatorio", null);
        }
    }

    private boolean isDuplicateEntry(SQLException e) {
        return e.getErrorCode() == 1062;
    }
}
