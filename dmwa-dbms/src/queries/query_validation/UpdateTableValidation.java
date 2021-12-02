package queries.query_validation;

import java.util.ArrayList;
import java.util.List;

import common.Utility;
import queries.query_execution.Table;

public class UpdateTableValidation {

    QueryValidationUtility util;

    public UpdateTableValidation(){
        util = new QueryValidationUtility();
    }

    public String validate(String query, String workfolder_in_db, Table table) {

        query = util.removeLastSemiColon(query);

        List<String> tokens = util.queryTokens(query.toLowerCase().substring(0, query.indexOf("set")));
        if(tokens.size() != 2) {
            return "Syntax error";
        }
        table.setTable_name(tokens.get(1)) ;

        String error = util.check_table_exists(table, workfolder_in_db);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        error = new FilterValidation().validateWhereClause(query, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }


        String set_clause= query.substring(query.indexOf("set")+3);
        if(query.contains("where")){
            set_clause= set_clause.substring(0, set_clause.indexOf("where"));
        }
        error= validateSet(set_clause, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        List<String> columns = new ArrayList<String>(table.getColumn_to_datatype().keySet());
        return util.populateDataFromFile(workfolder_in_db, table, columns);
    }

    private String validateSet(String set_clause, Table table) {

        if(!set_clause.contains("=")){
            return "Invalid set clause";
        }
        
        String lhs_colname = set_clause.split("=")[0].trim();
        String rhs_value = set_clause.split("=")[1].trim();
        System.out.println("----lhs_colname--- "+lhs_colname);
        if(table.getColumn_to_datatype().containsKey(lhs_colname)){
            table.setSet_lhs_column(lhs_colname);
            table.setSet_rhs_value(rhs_value);
        }
        else{
            return "Invalid column in where condition";
        }

        return null;
    }
    
}
