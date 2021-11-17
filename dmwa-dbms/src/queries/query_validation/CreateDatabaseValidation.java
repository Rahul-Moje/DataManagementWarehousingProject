package queries.query_validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CreateDatabaseValidation {

    public String validate(String query, String workspace_folder) {

        
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }

        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(query, " ");
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
        if(tokens.size() != 3) {
            return "Syntax error.";
        }
        else{
            return check_db_exists(tokens.get(2), workspace_folder);
        }
    }

    public String check_db_exists(String db_name, String workspace_folder){
        String path = ".//workspace//"+workspace_folder+"//"+db_name;
        File file = new File(path);
        if(file.exists()){
            return "Database " + db_name + " already exists.";
        }
        return null;
    }
    
}
