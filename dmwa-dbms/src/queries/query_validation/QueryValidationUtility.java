package queries.query_validation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import common.Constants;
import common.RetrieveTableInfo;
import common.Utility;
import queries.query_execution.Table;
import queries.query_execution.TableMetaData;

public class QueryValidationUtility {
    
    public String removeLastSemiColon(String query){
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }
        return query;
    }

    public List<String> queryTokens(String query){
        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(query, " ");
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }

    public String check_db_exists(String db_name, String workspace_folder){
        String path = ".//workspace//"+workspace_folder+"//"+db_name;
        File file = new File(path);
        if(file.exists()){
            return "Database " + db_name + " already exists.";
        }
        return null;
    }

    public String check_table_exists(Table table, String workspace_folder) {
        String table_name = table.getTable_name();
        List<TableMetaData> tables_info = RetrieveTableInfo.getTables(workspace_folder);
        Boolean isTablePresent = false;
        for(TableMetaData table_info : tables_info){
            if(table_info.getTable_name().equalsIgnoreCase(table_name)){
                isTablePresent=true;
                table.setColumn_to_datatype(table_info.getCol_datatype());
                break;
            }
        }
        if(isTablePresent == false){
            return "Table not found";
        }
        return null;
    }

    public String populateDataFromFile(String workfolder_in_db, Table table, List<String> column_names_from_query) {
        
        //validate the columns from query with actual ones
        for(String col_from_query: column_names_from_query){
            if(!table.getColumn_to_datatype().containsKey(col_from_query)) {
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
                List<HashMap<String,String>> values = new ArrayList<>();
                List<String> columns = new ArrayList<String>();
                int count = 0;
                String[] rows = content.split(Constants.LINE_SEPARATOR);
                for(String row: rows){
                    String[] cell_data = row.split(Constants.DELIMITER);
                    HashMap<String,String> row_map = new HashMap<>();
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
                                row_map.put(col_name, val);
                            }
                        }
                    }
                    if(count > 0){
                        values.add(row_map);
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
        
        return null;
    }
}
