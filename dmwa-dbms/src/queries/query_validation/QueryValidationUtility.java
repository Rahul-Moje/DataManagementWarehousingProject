package queries.query_validation;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import common.Constants;
import common.RetrieveTableInfo;
import common.Utility;
import queries.query_execution.Table;
import queries.query_execution.TableMetaData;

public class QueryValidationUtility {
    
    
    /** 
     * remove semicolon from the end
     * @param query
     * @return String
     */
    public String removeLastSemiColon(String query){
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }
        return query;
    }

    
    /** 
     * return the string token
     * @param query
     * @return List<String>
     */
    public List<String> queryTokens(String query){
        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(query, " ");
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }

    
    /** 
     * check if database exists
     * @param db_name
     * @param workspace_folder
     * @return String
     */
    public String check_db_exists(String db_name, String workspace_folder){
        String path = ".//workspace//"+workspace_folder+"//"+db_name;
        File file = new File(path);
        if(file.exists()){
            return "Database " + db_name + " already exists.";
        }
        return null;
    }

    
    /** 
     * check if a table exists
     * @param table
     * @param workspace_folder
     * @return String
     */
    public String check_table_exists(Table table, String workspace_folder) {
        String table_name = table.getTable_name();
        List<TableMetaData> tables_info = RetrieveTableInfo.getTables(workspace_folder);
        Boolean isTablePresent = false;
        for(TableMetaData table_info : tables_info){
            if(table_info.getTable_name().equalsIgnoreCase(table_name)){
                // System.out.println("---table_info--- "+table_info.toString());
                isTablePresent=true;
                table.setColumn_to_datatype(table_info.getCol_datatype());
                table.setPrimary_keys(table_info.getPrimary_keys());
                table.setUnique_columns(table_info.getUnique_columns());
                table.setNot_null_columns(table_info.getNot_null_columns());
                table.setColumn_to_referencetable_to_column(table_info.getColumn_to_referencetable_to_column());
                break;
            }
        }
        if(isTablePresent == false){
            return "Table not found";
        }
        return null;
    }

    
    /** 
     * populate data from file as list of map
     * @param workfolder_in_db
     * @param table
     * @param column_names_from_query
     * @return String
     */
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
                        if(val.startsWith("\"") || val.startsWith("'")){
                            val = val.substring(1, val.length()-1);
                        }
                        
                        if(count == 0){
                            val = val.toLowerCase();
                            columns.add(val.toLowerCase());
                        }
                        else {
                            String col_name = columns.get(i);
                            if(column_names_from_query.contains(col_name)){
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

    
    /** 
     * validate foreign key
     * @param table
     * @param workspace_folder
     * @return String
     */
    public String validate_foreign_key_constraint(Table table, String workspace_folder){

        HashMap<String,HashMap<String,String>> column_to_referencetable_to_column = table.getColumn_to_referencetable_to_column();

        HashMap<String,TableMetaData> table_to_tableinfo= RetrieveTableInfo.getMapOfTableNameToInfo(workspace_folder);
        if(column_to_referencetable_to_column!=null && column_to_referencetable_to_column.size()>0){

            for(String fk: column_to_referencetable_to_column.keySet()){
                String datatype = table.getColumn_to_datatype().get(fk);
                HashMap<String,String> table_col = column_to_referencetable_to_column.get(fk);
                for(String referenced_table: table_col.keySet()){

                    String referenced_column = table_col.get(referenced_table);
                    Table table_for_query = new Table();
                    TableMetaData tmd = table_to_tableinfo.get(referenced_table);
                    table_for_query.setTable_name(tmd.getTable_name());
                    table_for_query.setColumn_to_datatype(tmd.getCol_datatype());

                    populateDataFromFile(workspace_folder, table_for_query, Arrays.asList(referenced_column) );
                    for(HashMap<String,String> row: table.getValues()){
                        String value1 = row.get(fk);
                        int count = 0;
                        for(HashMap<String,String> inner_row: table_for_query.getValues()){
                            String value2 = inner_row.get(referenced_column);
                            switch(datatype){
                                case "nvarchar":
                                    if(value1.equalsIgnoreCase(value2)){
                                        ++count;
                                    }
                                    break;
                                case "integer":
                                case "float":
                                    BigDecimal numCellValue = BigDecimal.ZERO;
                                    BigDecimal numRhsValue = BigDecimal.ZERO;
                                    int comparedValue = 0;
                                    if (!value1.isBlank()) {
                                        numCellValue = new BigDecimal(value1);
                                    }
                                    if (value2 != null && !value2.isBlank()) {
                                        numRhsValue = new BigDecimal(value2);
                                        comparedValue = numCellValue.compareTo(numRhsValue);
                                    }
                                    if(comparedValue==0){
                                        ++count;
                                    }
                                    break;
                                case "date":
                                    if(Date.valueOf(value1)==Date.valueOf(value2)){
                                        ++count;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        if(count == 0){
                            return "Referenced table does not contain the foriegn key";
                        }
                    }

                }
            }
        }
        return null;
    }

    
    /** 
     * validate primary key and unique key
     * @param table
     * @param workspace_folder
     * @return String
     */
    public String validate_primary_key_constraint(Table table, String workspace_folder) {
        
        Table table_clone = new Table();
        table_clone.setTable_name(table.getTable_name());
        table_clone.setColumn_to_datatype(table.getColumn_to_datatype());
        
        populateDataFromFile(workspace_folder, table_clone, table.getPrimary_keys());

        if(table_clone.getValues().size()>0){
            List<String> pk_vals = new ArrayList<>();
            List<String> u_vals = new ArrayList<>();

            for(HashMap<String,String> existing_table_row: table_clone.getValues()){

                if(table.getPrimary_keys().size()>0){
                    String pk_val="";
                    for(String pk : table.getPrimary_keys()){
                        pk_val+=existing_table_row.get(pk);    
                    }
                    pk_vals.add(pk_val);
                }

                if(table.getUnique_columns().size()>0){
                    String u_val="";
                    for(String uk : table.getUnique_columns()){
                        u_val+=existing_table_row.get(uk);    
                    }
                    u_vals.add(u_val);
                }
                
            }

            for(HashMap<String,String> new_table_row: table.getValues()){
                if(table.getPrimary_keys().size()>0){
                    String pk_val="";
                    for(String pk : table.getPrimary_keys()){
                        pk_val+=new_table_row.get(pk);    
                    }
                    if(pk_vals.contains(pk_val)){
                        return "primary key constraint";
                    }
                    pk_vals.add(pk_val);
                }

                if(table.getUnique_columns().size()>0){
                    String u_val="";
                    for(String uk : table.getUnique_columns()){
                        u_val+=new_table_row.get(uk);    
                    }
                    if(u_vals.contains(u_val)){
                        return "unique key constraint";
                    }
                    u_vals.add(u_val);
                }
            }
        }
        
        
        return null;
    }
}
