package gymapp.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String encriptar(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verificar(String password, String passwordEncriptada) {
        return BCrypt.checkpw(password, passwordEncriptada);
    }
}