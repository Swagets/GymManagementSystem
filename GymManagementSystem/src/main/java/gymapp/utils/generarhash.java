package gymapp.utils;

import org.mindrot.jbcrypt.BCrypt;

public class generarhash {
    public static void main(String[] args) {

        String clave = "1234";

        String hash = BCrypt.hashpw(clave, BCrypt.gensalt());

        System.out.println(hash);
    }
}
