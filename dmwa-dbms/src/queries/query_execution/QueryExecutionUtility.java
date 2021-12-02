package queries.query_execution;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import common.Constants;
import common.Utility;

public class QueryExecutionUtility {
    
    public Boolean insertData(Table table, String workfolder_in_db, Boolean rewrite) {
        HashMap<String,String> col_datatype = table.getColumn_to_datatype();
        List<HashMap<String,String>> rows = table.getValues();
        String line_separator = System.getProperty("line.separator");

        String file_path = ".//workspace//"+workfolder_in_db+"//"+table.getTable_name()+Constants.DATA_FILE_EXTENSION;
        Utility.check_create_file_path(file_path);

        String file_content_str;
        String data="";
        try {
            file_content_str = Utility.fetch_file_content(file_path);
            
            if(!Utility.is_not_null_empty(file_content_str) || rewrite == true){
                data = String.join(Constants.DELIMITER, col_datatype.keySet());
                data+=line_separator;
            }
            else{
                data = file_content_str;
            } 
            for(int i= 0; i<rows.size(); ++i){
                HashMap<String,String> row = rows.get(i);
                for(String column_name: col_datatype.keySet()){
                    switch(String.valueOf(col_datatype.get(column_name))){
                        case "nvarchar":
                                data += row.get(column_name);
                                break;
                        case "integer":
                                data += Integer.valueOf(row.get(column_name));
                                break;
                        case "float":
                                data += Float.valueOf(row.get(column_name));
                                break;
                        case "date":
                                data += row.get(column_name);
                                break;
                        default:
                            throw new Exception("Invalid data type: "+col_datatype.get(column_name));
                    }
                    data += Constants.DELIMITER;
                }
                data = data.substring(0, data.length()-1)+line_separator;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            Utility.write(file_path, data);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean check_where_condition(HashMap<String,String> row, Table table) {

        try{
            String lhs_colname = table.getLhs_column();
            if(!Utility.is_not_null_empty(lhs_colname)){
                return true;
            }
            String rhs_value = table.getRhs_value();
            String operator = table.getOperator();

            if(!row.containsKey(lhs_colname)) {
                return false;
            }
            
            String datatype = table.getColumn_to_datatype().get(lhs_colname);
            switch (datatype) {
                case "nvarchar":
                    String rhs_str = row.get(lhs_colname);
                    switch (operator) {
                        case "=":
                            return rhs_str.equalsIgnoreCase(rhs_value);
                        case "!=":
                            return !rhs_str.equalsIgnoreCase(rhs_value);
                        case "like":
                            return rhs_str.toLowerCase().contains(rhs_value.toLowerCase());   
                        case "is null":
                            return Utility.is_not_null_empty(rhs_str);  
                        case "is not null":
                            return !Utility.is_not_null_empty(rhs_str);  
                        default:
                            break;
                    }
                    break;
                case "float":
                    Float rhs_float = Utility.is_not_null_empty(row.get(lhs_colname)) ? Float.valueOf(row.get(lhs_colname)) : 0.0f;
                    if(operator.equalsIgnoreCase("is null")) return !Utility.is_not_null_empty(String.valueOf(rhs_float));
                    if(operator.equalsIgnoreCase("is not null")) return Utility.is_not_null_empty(String.valueOf(rhs_float));
                    Float rhs_from_condition_float = Utility.is_not_null_empty(rhs_value) ? Float.valueOf(rhs_value) : 0.0f;
                    switch (operator) {
                        case "=":
                            return rhs_from_condition_float == rhs_float;
                        case "!=":
                            return rhs_float != rhs_from_condition_float;
                        case ">=":
                            return rhs_float >= rhs_from_condition_float;
                        case ">":
                            return rhs_float > rhs_from_condition_float;
                        case "<=":
                            return rhs_float <= rhs_from_condition_float;
                        case "<":
                            return rhs_float < rhs_from_condition_float;
                        default:
                            break;
                    }
                    break;
                case "integer":
                    
                    if(operator.equalsIgnoreCase("is null")) return !Utility.is_not_null_empty(String.valueOf(row.get(lhs_colname)));
                    if(operator.equalsIgnoreCase("is not null")) return Utility.is_not_null_empty(String.valueOf(row.get(lhs_colname)));

                    if(!Utility.is_not_null_empty(row.get(lhs_colname)) ) return false;
                    if(!Utility.is_not_null_empty(rhs_value) ) return false;

                    Integer rhs_integer = Integer.valueOf(row.get(lhs_colname));
                    Integer rhs_from_condition_integer = Integer.valueOf(rhs_value);
                    switch (operator) {
                        case "=":
                            return rhs_integer == rhs_from_condition_integer;
                        case "!=":
                            return rhs_integer != rhs_from_condition_integer;
                        case ">=":
                            return rhs_integer >= rhs_from_condition_integer;
                        case ">":
                            return rhs_integer > rhs_from_condition_integer;
                        case "<=":
                            return rhs_integer <= rhs_from_condition_integer;
                        case "<":
                            return rhs_integer < rhs_from_condition_integer ;
                        default:
                            break;
                    }
                    break;
                case "date":
                    if(operator.equalsIgnoreCase("is null")) return !Utility.is_not_null_empty(String.valueOf(row.get(lhs_colname)));
                        if(operator.equalsIgnoreCase("is not null")) return Utility.is_not_null_empty(String.valueOf(row.get(lhs_colname)));

                        if(!Utility.is_not_null_empty(row.get(lhs_colname)) ) return false;
                        if(!Utility.is_not_null_empty(rhs_value) ) return false;

                        Date rhs_date = Date.valueOf(row.get(lhs_colname));
                        Date rhs_from_condition_date = Date.valueOf(rhs_value);
                        switch (operator) {
                            case "=":
                                return rhs_date == rhs_from_condition_date ;
                            case "!=":
                                return rhs_date != rhs_from_condition_date;
                            case ">=":
                                return rhs_date == rhs_from_condition_date || rhs_date.after(rhs_from_condition_date) ;
                            case ">":
                                return rhs_date.after(rhs_from_condition_date) ;
                            case "<=":
                                return rhs_date == rhs_from_condition_date || rhs_date.before(rhs_from_condition_date) ;
                            case "<":
                                return rhs_date.before(rhs_from_condition_date) ;
                            default:
                                break;
                        }
                        break;
                default:
                    break;
            }
        }
        catch(Exception e){
            e.printStackTrace();;
            return false;
        }
        return false;
    }

    
}
