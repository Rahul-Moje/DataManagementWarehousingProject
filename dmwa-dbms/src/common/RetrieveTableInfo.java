package common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import queries.query_execution.TableMetaData;

public class RetrieveTableInfo {

    // List<Table> tables;

    // public RetrieveTableInfo(){
    //     tables = new ArrayList<>();
    // }

    public static List<TableMetaData> getTables(String workspace_folder){
        List<TableMetaData> tables = new ArrayList<>();

        String file_path = ".//workspace//%s//metadata//table_info"+Constants.DATA_FILE_EXTENSION;
        file_path = String.format(file_path, workspace_folder);

        String content;
        try {
            content = Utility.fetch_file_content(file_path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // System.out.println("---content---- "+content);
        try{
            if(Utility.is_not_null_empty(content)){
                int count = 0;
                String[] rows = content.split(Constants.LINE_SEPARATOR);
                for(String row: rows){
                    if(count > 0){
                        String[] cell_data = row.split(Constants.DELIMITER);
                        HashMap<String,String> col_dtype = new HashMap<>();

                        TableMetaData table = new TableMetaData();
                        table.setTable_name(cell_data[0]);
                        
                        String[] cols = cell_data[1].split(";");
                        String[] data_types = cell_data[2].split(";");
                        for(int i =0; i<cols.length; ++i){
                            col_dtype.put(cols[i], data_types[i]);
                        } 
                        table.setCol_datatype(col_dtype);
                    
                        tables.add(table);
                    }
                    count++;      
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return tables;
    }
    
}
