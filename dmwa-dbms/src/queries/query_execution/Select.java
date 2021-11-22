package queries.query_execution;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Utility;

public class Select {

    public Boolean execute(Table table) {
        
        try{
            JSONArray filterted_rows = new JSONArray();
            JSONArray rows = table.getValues();
            Formatter fmt=new Formatter();
            JSONObject row_for_header = rows.getJSONObject(0);
            String format  = "%30s ".repeat(row_for_header.keySet().size());
            System.out.println("\n---------------------------------------------------------------------------------------------");  
            fmt.format(format, row_for_header.keySet().toArray());
            System.out.println(fmt);  
            System.out.println("---------------------------------------------------------------------------------------------");

            for(int i = 0; i < rows.length(); ++i) {
                JSONObject row = rows.getJSONObject(i);

                if(check_where_condition(row, table)){
                    List<String> values = new ArrayList<String>(); 
                    for(String col : row.keySet()){
                        values.add(row.getString(col));
                    }
                    //System.out.println("----values----- "+values);
                    
                    fmt = new Formatter();
                    fmt.format(format, values.toArray());
                    System.out.println(fmt); 
                    filterted_rows.put(row);
                }
            }
            table.setValues(filterted_rows);
            fmt.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }

    private boolean check_where_condition(JSONObject row, Table table) {

        try{
            String lhs_colname = table.getLhs_column();
            if(!Utility.is_not_null_empty(lhs_colname)){
                return true;
            }
            String rhs_value = table.getRhs_value();
            String operator = table.getOperator();

            if(!row.has(lhs_colname)) {
                return false;
            }
            
            String datatype = table.getColumn_to_datatype().getString(lhs_colname);
            switch (datatype) {
                case "nvarchar":
                    String rhs_str = row.getString(lhs_colname);
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
                    Float rhs_float = Utility.is_not_null_empty(row.getString(lhs_colname)) ? row.getFloat(lhs_colname) : 0.0f;
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

                    if(!Utility.is_not_null_empty(row.getString(lhs_colname)) ) return false;
                    if(!Utility.is_not_null_empty(rhs_value) ) return false;

                    Integer rhs_integer = row.getInt(lhs_colname);
                    Integer rhs_from_condition_integer = Integer.valueOf(rhs_value);
                    System.out.println("**** rhs_integer **** "+rhs_integer);
                    System.out.println("**** rhs_from_condition_integer **** "+rhs_from_condition_integer);
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

                        if(!Utility.is_not_null_empty(row.getString(lhs_colname)) ) return false;
                        if(!Utility.is_not_null_empty(rhs_value) ) return false;

                        Date rhs_date = Date.valueOf(row.getString(lhs_colname));
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
