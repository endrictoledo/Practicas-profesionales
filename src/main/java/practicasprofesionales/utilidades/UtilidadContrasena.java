package practicasprofesionales.utilidades;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author endri
 */
public class UtilidadContrasena {

    private static final int WORK_FACTOR = 12;

    private UtilidadContrasena() {
    }

    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser vacía");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
    }

    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        boolean result;
        try {
            result = BCrypt.checkpw(plainPassword, storedHash);
        } catch (IllegalArgumentException e) {
            result = false;
        }
        return result;
    }
    // Añade esto temporalmente para generar tu hash

    public static void main(String[] args) {
        String miContrasenaPlana = "1234567890";
        String contrasenaHasheada = hash(miContrasenaPlana);

        System.out.println("Tu hash generado es:");
        System.out.println(contrasenaHasheada);
    }

}
