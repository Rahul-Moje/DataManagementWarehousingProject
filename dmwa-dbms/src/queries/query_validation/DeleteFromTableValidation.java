package queries.query_validation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import common.Utility;
import queries.query_execution.Table;

public class DeleteFromTableValidation {

    public String validate(String query, String workfolder_in_db, Table table) {
        
        boolean has_where_clause = query.contains("where");
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }

        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer;
        if(has_where_clause){
            tokenizer = new StringTokenizer(query.toLowerCase().substring(0, query.indexOf("where")), " ");
        }
        else{
            tokenizer = new StringTokenizer(query, " "); 
        }
        while(tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken().trim());
        }
        if(tokens.size() != 3) {
            return "Syntax error";
        }
        table.setTable_name(tokens.get(2)) ;

        String error = validateTable(table, workfolder_in_db);
        // System.out.println("----table------1: "+table.toString());
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        error = new FilterValidation().validateWhereClause(query, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        return null;

    }

    private String validateTable(Table table, String workspace_folder) {
        try{
            InsertValidation validator = new InsertValidation();
            return validator.checkTable(table, workspace_folder);
        }
        catch(Exception e){
            e.printStackTrace();
            return "Syntax error";
        }
        
    }
    
}
