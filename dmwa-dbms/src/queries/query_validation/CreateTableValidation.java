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


    private String validate_foreign_keys(Table table, String workspace_folder) {
        List<TableMetaData> tables_metadata = RetrieveTableInfo.getTables(workspace_folder);
        HashMap<String,HashMap<String,String>> column_to_referencetable_to_column = table.getColumn_to_referencetable_to_column();
        if(column_to_referencetable_to_column!=null && column_to_referencetable_to_column.size()>0){
            for(String fk: column_to_referencetable_to_column.keySet()){
                HashMap<String,String> table_col = column_to_referencetable_to_column.get(fk);
                for(String referenced_table: table_col.keySet()){
                    // System.out.println("---referenced_table---"+referenced_table);
                    String refereneced_column = table_col.get(referenced_table);

                    // System.out.println("---refereneced_column---"+refereneced_column);
                    boolean match_flag = false;
                    for(TableMetaData tmd: tables_metadata){

                        // System.out.println("---tmd--- "+tmd.toString());
                        if(tmd.getTable_name().equalsIgnoreCase(referenced_table)){
                            if(tmd.getCol_datatype().containsKey(refereneced_column)){
                                match_flag = true;
                                break;
                            }
                        }
                    }
                    if(match_flag == false){
                        return "incorrect referenced table name/ column name";
                    }

                }
                    
            }
        }
        return null;
    }

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

    private String validate_values(String query, Table table) {
        
        try{
            HashMap<String,HashMap<String,String>> column_to_referencetable_to_column = new HashMap<>();
            int start_index = query.indexOf("(");
            int last_index = query.lastIndexOf(")");
            String str_between_brackets = query.substring(start_index+1, last_index);
            
            Pattern pattern = Pattern.compile("\\(.*?\\)");
            // Matcher m = pattern.matcher(query);
            
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

                        // System.out.println("---pkeys--"+pkeys);
                        table.setPrimary_keys(Arrays.asList(pkeys));
                    }
                    else{
                        return "syntax error near primary key";
                    }
                        
                }
                else if(ele.toLowerCase().startsWith("foreign key")){
                    // String fk_regex = "foreign key [a-zA-z0-9]+ references [a-zA-z0-9]+$[a-zA-z0-9]+$";
                    // Pattern  foreign_key_pattern = Pattern.compile(fk_regex);
                    // if(!Pattern.matches(fk_regex, ele.toLowerCase())){
                    //     return "foreign key syntax error";
                    // }

                    // System.out.println("----ele---- "+ele);
                    // Matcher fk_matcher = foreign_key_pattern.matcher(ele.toLowerCase());
                    // while(fk_matcher.find()){
                    try{
                        String fk_str = ele.toLowerCase();
                        // System.out.println("----fk_str---- "+fk_str);
                        String fk_temp = fk_str.substring(fk_str.indexOf("foreign key")+"foreign key".length(), 
                                                        fk_str.indexOf("references")).trim();
                        // System.out.println("---fk_temp--"+fk_temp);
                        String referenced_table = fk_str.substring(fk_str.indexOf("references")+"references".length());
                        // System.out.println("---referenced_table--"+referenced_table);
                        referenced_table= referenced_table.substring(0, referenced_table.indexOf("(")).trim();
                        // System.out.println("---referenced_table--"+referenced_table);
                        String referenced_column = fk_str.substring(fk_str.indexOf("(")+1, fk_str.lastIndexOf(")")).trim();

                        // System.out.println("---referenced_column--"+referenced_column);
                        column_to_referencetable_to_column.put(fk_temp, new HashMap<>());
                        column_to_referencetable_to_column.get(fk_temp).put(referenced_table, referenced_column);

                        // System.out.println("---column_to_referencetable_to_column--- "+column_to_referencetable_to_column.toString());
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
