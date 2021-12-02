package queries.query_validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.RetrieveTableInfo;
import common.Utility;
import queries.query_execution.Table;
import queries.query_execution.TableMetaData;

public class InsertValidation {

    QueryValidationUtility util;

    public InsertValidation(){
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

        //check if table exists
        table.setTable_name(tokens.get(2)) ;
        String error = util.check_table_exists(table, workspace_folder);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        //validate the values part
        return validate_values(query, table);

    }

    private String validate_values(String query, Table table) {
        
        try{
            Pattern pattern = Pattern.compile("\\(.*?\\)");
            Matcher m = pattern.matcher(query);
            List<HashMap<String,String>> rows = new ArrayList<>();
            List<String> columns = new ArrayList<String>();
            int count = 0;
            while (m.find()) {
                HashMap<String,String> row = new HashMap<>();
                String str = m.group();
                String str_between_brackets = (String) str.subSequence(1, str.length()-1);
                String[] elements = str_between_brackets.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for(int i = 0; i<elements.length; ++i){
                    String val = elements[i].trim();
                    if(val.startsWith("\"") || val.startsWith("\'")){
                        val = (String) val.subSequence(1, val.length()-1);
                    }
                    if(count == 0){
                        columns.add(val.toLowerCase());
                    }
                    else{
                        row.put(columns.get(i), val);
                    }
                }
                if(count > 0){
                    rows.add(row);
                }
                count++;
            }
            table.setValues(rows);
        }
        catch(Exception e){
            e.printStackTrace();
            return "Syntax error";
        }
        
        return null;
    }

    public String checkTable(Table table, String workspace_folder) {
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
}
