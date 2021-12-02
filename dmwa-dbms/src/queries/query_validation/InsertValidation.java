package queries.query_validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.RetrieveTableInfo;
import common.Utility;
import queries.query_execution.Table;
import queries.query_execution.TableMetaData;

public class InsertValidation {
    
    public String validate(String query, String workspace_folder, Table table) {

        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }

        String substring_before_bracket = query.split("\\(")[0].trim();

        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(substring_before_bracket, " ");
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
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
        // String path = ".//workspace//"+workspace_folder+"//"+table.getTable_name()+".tsv";
        String error = checkTable(table, workspace_folder);
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
                System.out.println("---elements---- "+elements);
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
        System.out.println("---tables_info--- "+tables_info);
        Boolean isTablePresent = false;
        for(TableMetaData table_info : tables_info){

            System.out.println("---ttable_info.getTable_name()---"+table_info.getTable_name()+"---");
            System.out.println("---table_name---"+table_name+"---");
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
