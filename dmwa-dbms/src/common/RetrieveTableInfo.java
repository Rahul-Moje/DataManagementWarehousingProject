package common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import queries.query_execution.TableMetaData;

public class RetrieveTableInfo {

    public static List<TableMetaData> getTables(String workspace_folder){
        List<TableMetaData> tables = new ArrayList<>();

        String file_path = ".//workspace//%s//metadata//table_info"+Constants.DATA_FILE_EXTENSION;
        file_path = String.format(file_path, workspace_folder);
        // System.out.println("---file_path---- "+file_path);
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
                        String[] cell_data = row.split(Constants.DELIMITER,-1);
                        HashMap<String,String> col_dtype = new HashMap<>();

                        TableMetaData table = new TableMetaData();
                        table.setTable_name(cell_data[0]);
                        
                        String[] cols = cell_data[1].split(";");
                        String[] data_types = cell_data[2].split(";");
                        for(int i =0; i<cols.length; ++i){
                            col_dtype.put(cols[i], data_types[i]);
                        } 
                        table.setCol_datatype(col_dtype);

                        if(cell_data[3]!=null && !cell_data[3].trim().isEmpty()){
                            String[] pkeys = cell_data[3].split(";");
                            table.setPrimary_keys(Arrays.asList(pkeys));
                        }

                        if(cell_data[4]!=null && !cell_data[4].trim().isEmpty()){
                            String[] ukeys = cell_data[4].split(";");
                            table.setUnique_columns(Arrays.asList(ukeys));
                        }
                        
                        if(cell_data[5]!=null && !cell_data[5].trim().isEmpty()){
                            String[] nnkeys = cell_data[5].split(";");
                            table.setNot_null_columns(Arrays.asList(nnkeys));
                        }

                        if(cell_data[6]!=null && !cell_data[6].trim().isEmpty()){
                            HashMap<String,HashMap<String,String>> column_to_referencetable_to_column = new HashMap<>();
                            String[] fk_infos = cell_data[6].split(";");
                            for(String fk_info: fk_infos){
                                String[] parts = fk_info.split("#");
                                column_to_referencetable_to_column.put(parts[0], new HashMap<>());
                                column_to_referencetable_to_column.get(parts[0]).put(parts[1], parts[2]);
                            }
                            table.setColumn_to_referencetable_to_column(column_to_referencetable_to_column);
                        }

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

    public static HashMap<String,TableMetaData> getMapOfTableNameToInfo(String workspace_folder){

        List<TableMetaData> tables_info= getTables(workspace_folder);
        HashMap<String,TableMetaData> table_to_tableinfo= new HashMap<>();
        for(TableMetaData tmd: tables_info){
            table_to_tableinfo.put(tmd.getTable_name(), tmd);
        }
        return table_to_tableinfo;
    }
    
}
