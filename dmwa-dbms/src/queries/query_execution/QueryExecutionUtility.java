package queries.query_execution;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import common.Constants;
import common.Utility;
import transaction.Transaction;

public class QueryExecutionUtility {

    public Boolean insertData(Table table, String workfolder_in_db, Boolean rewrite, 
    											boolean commitFlag, Transaction txn) {
        HashMap<String,String> col_datatype = table.getColumn_to_datatype();
        String line_separator = System.getProperty("line.separator");
        
        String file_path = ".//workspace//"+workfolder_in_db+"//"+table.getTable_name()+Constants.DATA_FILE_EXTENSION;
        Utility.check_create_file_path(file_path);
        List<HashMap<String,String>> rows = table.getValues();

        String file_content_str;
        String data="";
        try {
        	String tmp_content = txn.getTempData().get(file_path);
            file_content_str = tmp_content == null ? Utility.fetch_file_content(file_path) : tmp_content;
            
            if(!Utility.is_not_null_empty(file_content_str) || rewrite) {
                data = String.join(Constants.DELIMITER, col_datatype.keySet());
                data+=line_separator;
                // System.out.println("---data first---- "+data);
            }
            else {
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
                // System.out.println("---data last---- "+data);
                data = data.substring(0, data.length()-1)+line_separator;
            }
        } catch (Exception e) {
            e.printStackTrace();
            txn.rollback();
            System.out.println("Rolling back transaction");
            return false;
        }

        try {
        	//If commit flag true then call Commit on txn 
        	txn.setTempData(file_path, data);
        	if(commitFlag) {
        		txn.commit();
        		System.out.println(table.getValues().size()+" row(s) affected!");
        	}
        } catch (Exception e) {
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
                case "integer":
                case "float":
                    if(operator.equalsIgnoreCase("is null")) return !Utility.is_not_null_empty(String.valueOf(row.get(lhs_colname)));
                    if(operator.equalsIgnoreCase("is not null")) return Utility.is_not_null_empty(String.valueOf(row.get(lhs_colname)));

                    BigDecimal rhs_float = BigDecimal.ZERO;
                    BigDecimal rhs_from_condition_float = BigDecimal.ZERO;
                    int comparedValue = 0;
                    if (!row.get(lhs_colname).isBlank()) {
                        rhs_float = new BigDecimal(row.get(lhs_colname));
                    }
                    if (rhs_value != null && !rhs_value.isBlank()) {
                        rhs_from_condition_float = new BigDecimal(rhs_value);
                        comparedValue = rhs_float.compareTo(rhs_from_condition_float);
                    }
                    switch (operator) {
                        case "=":
                            return comparedValue==0;
                        case "!=":
                            return comparedValue!=0;
                        case ">=":
                            return comparedValue>=0;
                        case ">":
                            return comparedValue>0;
                        case "<=":
                            return comparedValue<=0;
                        case "<":
                            return comparedValue<0;
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
                                return rhs_date.equals(rhs_from_condition_date) ;
                            case "!=":
                                return !rhs_date.equals(rhs_from_condition_date);
                            case ">=":
                                return rhs_date.equals(rhs_from_condition_date) || rhs_date.after(rhs_from_condition_date) ;
                            case ">":
                                return rhs_date.after(rhs_from_condition_date) ;
                            case "<=":
                                return rhs_date.equals(rhs_from_condition_date) || rhs_date.before(rhs_from_condition_date) ;
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
