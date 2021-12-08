package queries.query_validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ExistingDatabseValidation {

    
    /** 
     * @param query
     * @param workspace_folder
     * @return String
     */
    public String validate(String query, String workspace_folder) {
       
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }

        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(query, " ");
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
        if(tokens.size() != 1) {
            return "Syntax error.";
        }
        else{
            return check_db_exists(tokens.get(0), workspace_folder);
        }
    }

    
    /** 
     * @param db_name
     * @param workspace_folder
     * @return String
     */
    public String check_db_exists(String db_name, String workspace_folder){
        String path = ".//workspace//"+workspace_folder+"//"+db_name;
        File file = new File(path);
        if(!file.exists()){
            return "Database " + db_name + " not exists.";
        }
        return null;
    }

}
