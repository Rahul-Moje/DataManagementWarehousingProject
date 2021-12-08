package queries.query_validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.RetrieveTableInfo;
import common.Utility;
import queries.query_execution.Table;
import queries.query_execution.TableMetaData;

public class CreateTableValidation {

    final List<String> valid_data_type = new ArrayList<String>(
                                        Arrays.asList("nvarchar", "integer", "float", "date"));
    
    QueryValidationUtility util;

    public CreateTableValidation(){
        util = new QueryValidationUtility();
    }

    
    /** 
     * validate create table query
     * @param query
     * @param workspace_folder
     * @param table
     * @return String
     */
    public String validate(String query, String workspace_folder, Table table) {

        query = util.removeLastSemiColon(query);

        String substring_before_bracket = query.split("\\(")[0].trim();
        List<String> tokens = util.queryTokens(substring_before_bracket);
        if(tokens.size() != 3) {
            return "Syntax error";
        }
        
        long count_opening_bracket = query.chars().filter(ch -> ch == '(').count();
        long count_closing_bracket = query.chars().filter(ch -> ch == ')').count();
        if(count_closing_bracket != count_opening_bracket || !query.endsWith(")")){
            return "Syntax error";
        }

        //check for duplicate table name
        table.setTable_name(tokens.get(2)) ;
        String message = util.check_table_exists(table, workspace_folder);
        if(message == null){
            return "Table already exists in database";
        }

        //validate the values part
        String error= validate_values(query, table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        //validate primary keys
        error= validate_primary_keys(table);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        //validate foreign keys
        error= validate_foreign_keys(table, workspace_folder);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        return null;

    }


    
    /** 
     * validate foreign keys
     * @param table
     * @param workspace_folder
     * @return String
     */
    private String validate_foreign_keys(Table table, String workspace_folder) {

        HashMap<String, TableMetaData> table_to_metadata = RetrieveTableInfo.getMapOfTableNameToInfo(workspace_folder);
        HashMap<String,HashMap<String,String>> column_to_referencetable_to_column = table.getColumn_to_referencetable_to_column();
        if(column_to_referencetable_to_column!=null && column_to_referencetable_to_column.size()>0){
            for(String fk: column_to_referencetable_to_column.keySet()){
                String datatype_of_main_tablecol = table.getColumn_to_datatype().get(fk);
                HashMap<String,String> table_col = column_to_referencetable_to_column.get(fk);
                for(String referenced_table: table_col.keySet()){

                    String refereneced_column = table_col.get(referenced_table);
                    if(!table_to_metadata.containsKey(referenced_table)){
                        return referenced_table+ ": reference table does not exist";
                    }
                    TableMetaData tmd = table_to_metadata.get(referenced_table);

                    if(!tmd.getCol_datatype().containsKey(refereneced_column)){
                        return refereneced_column+": referenced column does not exist in "+referenced_table;
                    }

                    if(!tmd.getCol_datatype().get(refereneced_column).equalsIgnoreCase(datatype_of_main_tablecol)){
                        return "incompatible datatypes of foreign key "+fk+" and "+refereneced_column;
                    }

                }
                    
            }
        }
        return null;
    }

    
    /** 
     * validate primary keys
     * @param table
     * @return String
     */
    private String validate_primary_keys (Table table) {
        if(table.getPrimary_keys()!=null && table.getPrimary_keys().size()>0){

            for(String pk: table.getPrimary_keys()){
                if(!table.getColumn_to_datatype().containsKey(pk)){
                    return String.format("Incorrect primary key= %s", pk);
                }
            }
        }
        return null;     
    }

    
    /** 
     * validate the table column and data types
     * @param query
     * @param table
     * @return String
     */
    private String validate_values(String query, Table table) {
        
        try{
            HashMap<String,HashMap<String,String>> column_to_referencetable_to_column = new HashMap<>();
            int start_index = query.indexOf("(");
            int last_index = query.lastIndexOf(")");
            String str_between_brackets = query.substring(start_index+1, last_index);
            
            Pattern pattern = Pattern.compile("\\(.*?\\)");
            
            String[] elements = str_between_brackets.trim().split(",");
            for(String ele : elements){
                ele = ele.trim();
                if(ele.toLowerCase().startsWith("primary key")){
                    Matcher primary_keys_matcher = pattern.matcher(ele);
                    if(primary_keys_matcher.find()){
                        String match_str = primary_keys_matcher.group(0);
                        match_str = (String) match_str.subSequence(1, match_str.length()-1);
                        String[] pkeys = match_str.trim().split(",");
                        Arrays.stream(pkeys).map(String::trim).toArray(unused -> pkeys);
                        table.setPrimary_keys(Arrays.asList(pkeys));
                    }
                    else{
                        return "syntax error near primary key";
                    }
                        
                }
                else if(ele.toLowerCase().startsWith("foreign key")){
                    try{
                        String fk_str = ele.toLowerCase();
                        String fk_temp = fk_str.substring(fk_str.indexOf("foreign key")+"foreign key".length(), 
                                                        fk_str.indexOf("references")).trim();
                        String referenced_table = fk_str.substring(fk_str.indexOf("references")+"references".length());
                        referenced_table= referenced_table.substring(0, referenced_table.indexOf("(")).trim();
                        String referenced_column = fk_str.substring(fk_str.indexOf("(")+1, fk_str.lastIndexOf(")")).trim();
                        column_to_referencetable_to_column.put(fk_temp, new HashMap<>());
                        column_to_referencetable_to_column.get(fk_temp).put(referenced_table, referenced_column);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        return "foreign key syntax error";
                    }
                }
                else{
                    String[] column_and_datatype = ele.trim().split(" ");
                    String column = column_and_datatype[0];
                    String datatype = column_and_datatype[1];
                    if(!valid_data_type.contains(datatype.toLowerCase())){
                        return "Incorrect datatype "+datatype+". Valid data types are "+String.join(", ", valid_data_type);
                    }
                    table.add_to_column_to_datatype(column, datatype);

                    if(ele.toLowerCase().contains("not null")){
                        table.addNot_null_column(column);
                    }

                    if(ele.toLowerCase().contains("unique")){
                        table.addUnique_column(column);
                    }
                }
            }
            
            if(column_to_referencetable_to_column.size()>0){
                table.setColumn_to_referencetable_to_column(column_to_referencetable_to_column);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return "Syntax error";
        }
        
        return null;
    }
    
}
