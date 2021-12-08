package queries.query_validation;

import java.util.ArrayList;
import java.util.List;

import common.Utility;
import queries.query_execution.Table;

public class DeleteFromTableValidation {

    QueryValidationUtility util;

    public DeleteFromTableValidation(){
        util = new QueryValidationUtility();
    }

    
    /** 
     * validate delete from query
     * @param query
     * @param workfolder_in_db
     * @param table
     * @return String
     */
    public String validate(String query, String workfolder_in_db, Table table) {
        
        query = util.removeLastSemiColon(query);

        List<String> tokens = new ArrayList<String>();
        boolean has_where_clause = query.contains("where");
        if(has_where_clause){
            tokens = util.queryTokens(query.toLowerCase().substring(0, query.indexOf("where")));
        }
        else{
            tokens = util.queryTokens(query);
        }
        if(tokens.size() != 3) {
            return "Syntax error";
        }
        table.setTable_name(tokens.get(2)) ;

        String error = util.check_table_exists(table, workfolder_in_db);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        error = new FilterValidation().validateWhereClause(query, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        List<String> columns = new ArrayList<String>(table.getColumn_to_datatype().keySet());
        return util.populateDataFromFile(workfolder_in_db, table, columns);

    }
    
}
