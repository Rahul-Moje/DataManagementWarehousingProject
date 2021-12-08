package queries.query_validation;

import java.util.List;

public class CreateDatabaseValidation {

    QueryValidationUtility util;
    
    public CreateDatabaseValidation(){
        util = new QueryValidationUtility();
    }

    
    /** 
     * validate create database query
     * @param query
     * @param workspace_folder
     * @return String
     */
    public String validate(String query, String workspace_folder) {

        query = util.removeLastSemiColon(query);

        List<String> tokens = util.queryTokens(query);
        if(tokens.size() != 3) {
            return "Syntax error.";
        }
        else{
            return util.check_db_exists(tokens.get(2), workspace_folder);
        }
    }
    
}
