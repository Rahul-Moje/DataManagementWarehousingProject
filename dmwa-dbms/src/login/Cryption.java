package login;
import org.apache.commons.codec.digest.Crypt;

public class Cryption {
    
    static String salt = "uvdsjdsybpqcs5210qfahwepfcjs6zivem";
    public static boolean matches_encoded_value(String plain_input, String encoded_input){
        String hashedValue = encrypt(plain_input);
        return encoded_input.equals(hashedValue);
    }

    public static String encrypt(String input){
        // System.out.println("input--- "+input);
        return Crypt.crypt(input, salt);
        
    }
    
}
