package practicasprofesionales.excepciones;

import java.sql.SQLException;

/**
 *
 * @author endri
 */

public class ExcepcionDAO extends Exception {
    public ExcepcionDAO(String mensaje) {
        super(mensaje);
    }

    public ExcepcionDAO(String mensaje, Throwable causa){
        super (mensaje, causa);
    }
}