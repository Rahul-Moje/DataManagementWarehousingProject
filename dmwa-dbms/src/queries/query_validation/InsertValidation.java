package queries.query_validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Utility;
import queries.query_execution.Table;

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
            JSONArray rows = new JSONArray();
            List<String> columns = new ArrayList<String>();
            int count = 0;
            while (m.find()) {
                JSONObject row = new JSONObject();
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
                    rows.put(row);
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

    private String checkTable(Table table, String workspace_folder) {
        String path = ".//workspace//"+workspace_folder+"//metadata//table_info.txt";
        String table_name = table.getTable_name();
        try {
            String file_content_str = Utility.fetch_file_content(path);
            if(Utility.is_not_null_empty(file_content_str)){
                JSONObject file_content = new JSONObject(file_content_str);

                System.out.println("---file_content---"+file_content);

                if(!file_content.keySet().contains(table_name)){
                    return "Table not found.";
                }
                else{
                    JSONObject col_datatype = file_content.getJSONObject(table_name);
                    System.out.println("---col_datatype---"+col_datatype);
                    table.setColumn_to_datatype(col_datatype);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "System error.";
        }
        return null;
    }
}
