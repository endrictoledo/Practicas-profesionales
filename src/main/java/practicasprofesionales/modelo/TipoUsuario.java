
package practicasprofesionales.modelo;

/**
 *
 * @author endri
 */
public enum TipoUsuario {
    
    ESTUDIANTE("estudiante"),
    COORDINADOR("coordinador"),
    PROFESOR("profesor"),
    ADMINISTRADOR("administrador");

    private final String dbValue;

    TipoUsuario(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static TipoUsuario fromDbValue(String value) {
        for (TipoUsuario type : values()) {
            if (type.dbValue.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de usuario desconocido: " + value);
    }
}
