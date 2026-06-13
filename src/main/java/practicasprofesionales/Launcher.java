package practicasprofesionales;

import static practicasprofesionales.utilidades.UtilidadContrasena.hash;

/**
 *
 * @author endri
 */
public class Launcher {

    public static void main(String[] args) {
        ProyectoConstruccion.main(args);

        String miContrasenaPlana = "1234567890";
        String contrasenaHasheada = hash(miContrasenaPlana);

        System.out.println("Tu hash generado es:");
        System.out.println(contrasenaHasheada);
    }
}
