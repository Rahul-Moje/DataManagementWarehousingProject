package queries.query_validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Constants;
import common.Utility;
import queries.query_execution.Table;

public class SelectValidation {

    public String validate(String query, String workfolder_in_db, Table table) {
        
        query = query.toLowerCase();
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }
        String error = validateTable(query, table, workfolder_in_db);
        // System.out.println("----table------1: "+table.toString());
        if(Utility.is_not_null_empty(error)){
            return error;
        }
        
        error = new FilterValidation().validateWhereClause(query, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        List<String> column_names_from_query = getColumns(query, table);
        // System.out.println("----column_names_from_query------: "+column_names_from_query);
        return populateDataFromFile(workfolder_in_db, table, column_names_from_query);

        

    }

    public String populateDataFromFile(String workfolder_in_db, Table table, List<String> column_names_from_query) {
        
        //validate the columns from query with actual ones
        for(String col_from_query: column_names_from_query){
            if(!table.getColumn_to_datatype().has(col_from_query)) {
                return "Incorrect columns name: " + col_from_query;
            }
        }
        //end

        String path = ".//workspace//"+workfolder_in_db+"//"+table.getTable_name()+Constants.DATA_FILE_EXTENSION;
        String content;
        try {
            content = Utility.fetch_file_content(path);
        } catch (IOException e) {
            e.printStackTrace();
            return "Empty table";
        }
        try{
            if(Utility.is_not_null_empty(content)){
                JSONArray values = new JSONArray();
                List<String> columns = new ArrayList<String>();
                int count = 0;
                String[] rows = content.split(Constants.LINE_SEPARATOR);
                for(String row: rows){
                    String[] cell_data = row.split(Constants.DELIMITER);
                    JSONObject row_json = new JSONObject();
                    for(int i = 0; i<cell_data.length; ++i){
                        String val = cell_data[i].trim();
                        
                        if(count == 0){
                            val = val.toLowerCase();
                            columns.add(val.toLowerCase());
                        }
                        else {
                            String col_name = columns.get(i);
                            if(column_names_from_query.contains(col_name)){
                                //add logic for whereclause
                                row_json.put(col_name, val);
                            }
                        }
                    }
                    if(count > 0){
                        values.put(row_json);
                    }
                    count++;
                        
                }
                table.setValues(values);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return "System error";
        }
        
        // System.out.println("----table------2: "+table.toString());
        return null;
    }

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

            InsertValidation validator = new InsertValidation();
            return validator.checkTable(table, workspace_folder);
        }
        catch(Exception e){
            e.printStackTrace();
            return "Syntax error";
        }
        
    }

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
