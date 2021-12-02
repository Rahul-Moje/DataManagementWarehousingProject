package queries.query_execution;


import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import common.Constants;
import common.Utility;

public class Insert {

    private boolean rewrite = false;

    public void setRewrite(boolean rewrite) {
        this.rewrite = rewrite;
    }


    public Boolean execute(Table table, String workfolder_in_db) {
        System.out.println(table.toString());
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

        try (FileWriter file_writer = new FileWriter(file_path, false)) {
            file_writer.write(data);
            file_writer.flush();
            file_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
