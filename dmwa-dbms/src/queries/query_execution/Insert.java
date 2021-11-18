package queries.query_execution;


import java.io.FileWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Constants;
import common.Utility;

public class Insert {

    public Boolean execute(Table table, String workfolder_in_db) {
        System.out.println(table.toString());
        JSONObject col_datatype = table.getColumn_to_datatype();
        JSONArray rows = table.getValues();
        String line_separator = System.getProperty("line.separator");

        String file_path = ".//workspace//"+workfolder_in_db+"//"+table.getTable_name()+".tsv";
        Utility.check_create_file_path(file_path);

        String file_content_str;
        try {
            file_content_str = Utility.fetch_file_content(file_path);
        
            FileWriter file_writer = new FileWriter(file_path, false);
            String data="";
            if(!Utility.is_not_null_empty(file_content_str)){
                data = String.join(Constants.DELIMITER, col_datatype.keySet());
                data+=line_separator;
            }
            else{
                data = file_content_str;
            } 
            for(int i= 0; i<rows.length(); ++i){
                JSONObject row = rows.getJSONObject(i);
                for(String column_name: col_datatype.keySet()){
                    switch(String.valueOf(col_datatype.get(column_name))){
                        case "nvarchar":
                                data += row.getString(column_name);
                                break;
                        case "integer":
                                data += row.getInt(column_name);
                                break;
                        case "float":
                                data += row.getFloat(column_name);
                                break;
                        case "date":
                                data += row.getString(column_name);
                                break;
                        default:
                            throw new Exception("Invalid data type: "+col_datatype.get(column_name));
                    }
                    data += Constants.DELIMITER;
                }
                data = data.substring(0, data.length()-1)+line_separator;
            }
            file_writer.write(data);
            file_writer.flush();
            file_writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
