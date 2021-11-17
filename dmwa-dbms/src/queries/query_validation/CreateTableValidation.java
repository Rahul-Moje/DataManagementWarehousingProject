package queries.query_validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import common.Utility;
import queries.query_execution.Table;

public class CreateTableValidation {

    final List<String> valid_data_type = new ArrayList<String>(
                                        Arrays.asList("text", "integer", "float", "date"));

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
        String error = isDupicateTable(table.getTable_name(), workspace_folder);
        if(Utility.is_not_null_empty(error)){
            return error;
        }

        //validate the values part
        return validate_values(query, table);

    }


    private String isDupicateTable(String table_name, String workspace_folder) {
        String path = ".//workspace//"+workspace_folder+"//metadata//table_info.txt";
        try {
            String file_content_str = Utility.fetch_file_content(path);
            if(Utility.is_not_null_empty(file_content_str)){
                JSONObject file_content = new JSONObject(file_content_str);
                if(file_content.keySet().contains(table_name.toLowerCase())){
                    return "Table already exists in database";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String validate_values(String query, Table table) {
        
        try{
            Pattern pattern = Pattern.compile("\\(.*?\\)");
            Matcher m = pattern.matcher(query);
            while (m.find()) {
                String str = m.group();
                String str_between_brackets = (String) str.subSequence(1, str.length()-1);
                String[] elements = str_between_brackets.split(",");
                for(String ele : elements){
                    String[] column_and_datatype = ele.trim().split(" ");
                    String datatype = column_and_datatype[1];
                    if(!valid_data_type.contains(datatype.toLowerCase())){
                        return "Incorrect datatype "+datatype+". Valid data types are "+String.join(", ", valid_data_type);
                    }
                    table.add_to_column_to_datatype(column_and_datatype[0], datatype);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return "Syntax error";
        }
        
        return null;
    }
    
}
