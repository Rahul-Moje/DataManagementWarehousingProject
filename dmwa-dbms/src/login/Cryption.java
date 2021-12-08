package login;
import org.apache.commons.codec.digest.Crypt;

public class Cryption {
    
    static final String salt = "uvdsjdsybpqcs5210qfahwepfcjs6zivem";
    
    /** 
     * matches encoded values to plain value
     * @param plain_input
     * @param encoded_input
     * @return boolean
     */
    public static boolean matches_encoded_value(String plain_input, String encoded_input){
        String hashedValue = encrypt(plain_input);
        return encoded_input.equals(hashedValue);
    }

    
    /** 
     * encode input value
     * @param input
     * @return String
     */
    public static String encrypt(String input){
        return Crypt.crypt(input, salt);
        
    }
    
}
