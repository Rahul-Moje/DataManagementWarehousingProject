package queries.query_validation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import queries.query_execution.Table;

public class DropTableValidation {

    public String validate(String query, String workspace_folder, Table table) {
        
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }

        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(query, " ");
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
        if(tokens.size() != 3) {
            return "Syntax error";
        }

        //check if table exists
        table.setTable_name(tokens.get(2)) ;
        InsertValidation validator = new InsertValidation();
        return validator.checkTable(table, workspace_folder);
        
    }
    
}
