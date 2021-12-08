package queries.query_validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Utility;
import queries.query_execution.Table;

public class SelectValidation {

    QueryValidationUtility util;

    public SelectValidation(){
        util = new QueryValidationUtility();
    }

    
    /** 
     * validate select query
     * @param query
     * @param workfolder_in_db
     * @param table
     * @return String
     */
    public String validate(String query, String workfolder_in_db, Table table) {
        
        query = query.toLowerCase();
        query = util.removeLastSemiColon(query);

        String error = validateTable(query, table, workfolder_in_db);
        if(Utility.is_not_null_empty(error)){
            return error;
        }
        
        error = new FilterValidation().validateWhereClause(query, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        List<String> column_names_from_query = getColumns(query, table);
        // System.out.println("----column_names_from_query------: "+column_names_from_query);
        return util.populateDataFromFile(workfolder_in_db, table, column_names_from_query);

        

    }

    
    /** 
     * validate table name
     * @param query
     * @param table
     * @param workspace_folder
     * @return String
     */
    private String validateTable(String query, Table table, String workspace_folder) {
        try{
            String table_name="";
            if(query.contains("where")){
                table_name = query.substring(query.indexOf("from")+4, query.indexOf("where")-1).trim();
            }
            else{
                table_name = query.substring(query.indexOf("from")+4).trim();
            }
            table.setTable_name(table_name);
            return util.check_table_exists(table, workspace_folder);
        }
        catch(Exception e){
            e.printStackTrace();
            return "Syntax error";
        }
        
    }

    
    /** 
     * create list of columns in the select query
     * @param query
     * @param table
     * @return List<String>
     */
    private List<String> getColumns(String query, Table table) {
        String coma_separated_columns = query.substring(query.indexOf("select")+6, query.indexOf("from")-1).trim();
		if(coma_separated_columns.equals("*")){
            return new ArrayList<String>(table.getColumn_to_datatype().keySet());
        }
        else{
            return new ArrayList<String>(Arrays.asList(coma_separated_columns.split(",")));
        }
    }
    
}
