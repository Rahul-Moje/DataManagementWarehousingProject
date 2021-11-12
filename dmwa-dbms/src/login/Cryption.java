package login;
import org.apache.commons.codec.digest.Crypt;

public class Cryption {

    public static boolean matches_encoded_value(String plain_input, String encoded_input){
        String hashedValue = Crypt.crypt(plain_input, encoded_input);
        return encoded_input.equals(hashedValue);
    }

    public static String encrypt(String input){
        return Crypt.crypt(input);
        
    }
    
}
