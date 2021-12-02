package queries.query_validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import queries.query_execution.Table;

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
        return validate_values(query, table);

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
