package practicasprofesionales.excepciones;

import java.sql.SQLException;

/**
 *
 * @author endri
 */

public class DAOException extends Exception {
    public DAOException(String message) {
        super(message);
    }

    public DAOException (String message, Throwable cause){
        super (message, cause);
    }
}