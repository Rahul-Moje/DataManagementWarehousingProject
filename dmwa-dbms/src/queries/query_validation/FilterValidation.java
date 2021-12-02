package queries.query_validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Utility;
import queries.query_execution.Table;

public class FilterValidation {

    public static final List<String> operators = new ArrayList<>(
        Arrays.asList(
            "=",
            "!=",
            "<=",
            "<",
            ">=",
            ">",
            "is not null",
            "is null",
            "like"
    ));

    public static final Map<String, List<String>> datatype_to_operators= new HashMap<String, List<String>> () {{
        put("nvarchar", new ArrayList<>(
            Arrays.asList(
                "=",
                "!=",
                "like",
                "not null",
                "is null"
            )
        ));
        put("integer", new ArrayList<>(
            Arrays.asList(
                "=",
                "!=",
                ">",
                ">=",
                "<",
                "<=",
                "not null",
                "is null"
            )
        ));
        put("float", new ArrayList<>(
            Arrays.asList(
                "=",
                "!=",
                ">",
                ">=",
                "<",
                "<=",
                "not null",
                "is null"
            )
        ));
        put("date", new ArrayList<>(
            Arrays.asList(
                "=",
                "!=",
                ">",
                ">=",
                "<",
                "<=",
                "not null",
                "is null"
            )
        ));
    }};

    public String validateWhereClause(String query, Table table){
        
        String where_clause = query.toLowerCase().contains("where") ? query.substring(query.indexOf("where")+5).trim() : "";
        if(!Utility.is_not_null_empty(where_clause)){
            return null;
        }
        table.setWhere_Clause(where_clause);
        for(String operator: operators){
            if(where_clause.contains(operator)){
                table.setOperator(operator);
                break;
            }
        }
        if(!Utility.is_not_null_empty(table.getOperator())){
            return "Invalid operator in where condition";
        }
        
        String lhs_colname = where_clause.split(table.getOperator())[0].trim();
        String rhs_value = where_clause.split(table.getOperator())[1].trim();

        
        if(table.getColumn_to_datatype().containsKey(lhs_colname)){
            String data_type = table.getColumn_to_datatype().get(lhs_colname);
            if(data_type.equalsIgnoreCase("nvarchar")){
                if(rhs_value.startsWith("\"") || rhs_value.startsWith("\'")){
                    rhs_value = (String) rhs_value.subSequence(1, rhs_value.length()-1);
                }
            }
            if(!datatype_to_operators.get(data_type).contains(table.getOperator())){
                return String.format("Invalid operator for %s datatype. Valid operators are %s", 
                        data_type, String.join(",", datatype_to_operators.get(data_type)) );
            }
            table.setLhs_column(lhs_colname);
            table.setRhs_value(rhs_value);
        }
        else{
            return "Invalid column in where condition";
        }
        return null;

    }
    
}
