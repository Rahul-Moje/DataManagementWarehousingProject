package queries.query_validation;

import java.util.List;

import common.Utility;
import queries.query_execution.Table;

public class DropTableValidation {

    QueryValidationUtility util;

    public DropTableValidation(){
        util = new QueryValidationUtility();
    }

    public String validate(String query, String workspace_folder, Table table) {

        query = util.removeLastSemiColon(query);

        List<String> tokens = util.queryTokens(query);
        if(tokens.size() != 3) {
            return "Syntax error";
        }

        //check if table exists
        table.setTable_name(tokens.get(2)) ;
        String error= util.check_table_exists(table, workspace_folder);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        return Utility.return_if_foreign_key(table, workspace_folder);
        
    }
    
}
