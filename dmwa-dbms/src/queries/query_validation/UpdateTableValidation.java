package queries.query_validation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import common.Utility;
import queries.query_execution.Table;

public class UpdateTableValidation {

    public String validate(String query, String workfolder_in_db, Table table) {

        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }

        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(query.toLowerCase().substring(0, query.indexOf("set")), " ");
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken().trim());
        }
        if(tokens.size() != 2) {
            return "Syntax error";
        }
        table.setTable_name(tokens.get(1)) ;

        String error = validateTable(table, workfolder_in_db);
        // System.out.println("----table------1: "+table.toString());
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        error = new FilterValidation().validateWhereClause(query, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }


        String set_clause= query.substring(query.indexOf("set")+3);
        System.out.println("---set_clause--- "+set_clause);
        if(query.contains("where")){
            System.out.println(query.indexOf("where"));
            set_clause= set_clause.substring(0, set_clause.indexOf("where"));
            System.out.println("---set_clause--- "+set_clause);
        }
         
        error= validateSet(set_clause, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        List<String> columns = new ArrayList<String>(table.getColumn_to_datatype().keySet());
        return new SelectValidation().populateDataFromFile(workfolder_in_db, table, columns);
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
