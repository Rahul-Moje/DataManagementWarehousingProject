package login;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class UserProfileIO {

    String file_name = "USER_PROFILE.txt";
    public LoginRegisterStatus add_user(UserLoginDetails userLoginDetails) {

        
            JSONObject file_content;
            try {
                file_content = fetch_file_content();
            } catch (IOException e1) {
                e1.printStackTrace();
                return LoginRegisterStatus.SYSTEM_ERROR;
            }     
            Optional<String> exisitng_username=null;

            if(file_content!=null){
                Set<String> usernames = file_content.keySet();
                exisitng_username = usernames.stream()
                                            .filter(username -> userLoginDetails.getUsername().equalsIgnoreCase(username))
                                            .findFirst();
            }
                
            if(file_content==null || exisitng_username == null || exisitng_username.isEmpty()){
                try {
                    write_in_file(userLoginDetails, file_content);
                    return LoginRegisterStatus.SUCCESS;
                } catch (IOException e) {
                    e.printStackTrace();
                    return LoginRegisterStatus.SYSTEM_ERROR;
                }
            }
            else{
                return LoginRegisterStatus.USER_ALREADY_EXISTS;
            }
            

                
       
    }

    private JSONObject fetch_file_content() throws IOException{
        File file = new File(file_name);
        if (file.exists()){
            String info="";
           
            InputStream inputStream = new FileInputStream(file_name);
            info = IOUtils.toString(inputStream, "UTF-8");
            
            return new JSONObject(info);
        }
        return null;
    }

    private void write_in_file(UserLoginDetails userLoginDetails, JSONObject file_content) throws IOException{
        FileWriter fileWriter = new FileWriter(file_name, false);
        JSONObject value_part = new JSONObject();
        value_part.put("password", Cryption.encrypt(userLoginDetails.getPassword()));
        value_part.put("security_question", userLoginDetails.getSecurity_question());
        value_part.put("security_answer", userLoginDetails.getSecurity_answer());

        if(file_content == null){
            file_content = new JSONObject();
        }

        file_content.put(Cryption.encrypt(userLoginDetails.getUsername()), value_part);
        fileWriter.write(file_content.toString());
        fileWriter.flush();
        fileWriter.close();


    }

    public UserLoginDetails check_credentials(String username, String password) {
        JSONObject file_content;
        String username_lowercase = username.toLowerCase();
        try {
            file_content = fetch_file_content();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if(file_content!=null){
            for(String username_from_file : file_content.keySet()){
                if(Cryption.matches_encoded_value(username_lowercase, username_from_file)){
                    JSONObject login_details = file_content.getJSONObject(username_from_file);
                    String password_from_file= login_details.getString("password");
                    if(Cryption.matches_encoded_value(password, password_from_file)){
                        UserLoginDetails userLoginDetails = new UserLoginDetails(
                                                        username_from_file, 
                                                        password_from_file, 
                                                        login_details.getString("security_question"), 
                                                        login_details.getString("security_answer"));
                        return userLoginDetails;
                    }   
                }
            }
        }
        return null;
    }
    
}
