package queries.query_validation;

import java.util.List;

public class UseDatabaseValidation {

    QueryValidationUtility util;

    public UseDatabaseValidation(){
        util = new QueryValidationUtility();
    }

    
    /** 
     * validate use database query
     * @param query
     * @param workspace_folder
     * @return String
     */
    public String validate(String query, String workspace_folder) {

        query = util.removeLastSemiColon(query);

        List<String> tokens = util.queryTokens(query);
        if(tokens.size() != 3) {
            return "Syntax error. Query has more than 3 tokens.";
        }
        else{
            if(util.check_db_exists(tokens.get(2), workspace_folder) == null) {
                return "Database does not exist";
            }
        }
        return null;
    }
    
}
