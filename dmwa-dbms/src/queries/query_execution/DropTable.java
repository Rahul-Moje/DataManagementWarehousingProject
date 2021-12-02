package queries.query_execution;

import java.io.File;
import java.io.IOException;

import common.Constants;
import common.Utility;

public class DropTable {

    public Boolean execute(Table table, String workspace_folder) {
        
        Boolean isSuccess = removeFromMetaData(table.getTable_name(), workspace_folder);
        if(!isSuccess){
            return false;
        }

        return deleteTableFile(table.getTable_name(), workspace_folder);
        
    }

    private Boolean removeFromMetaData(String table_name, String workspace_folder) {
        String path = ".//workspace//"+workspace_folder+"//metadata//table_info"+Constants.DATA_FILE_EXTENSION;
        String final_file_content = "";
        try {
            String file_content_str = Utility.fetch_file_content(path);
            if(Utility.is_not_null_empty(file_content_str)){
                int count = 0;
                String[] rows = file_content_str.split(Constants.LINE_SEPARATOR);
                final_file_content+=rows[0];
                for(String row: rows){
                    if(count > 0){
                        String[] cell_data = row.split(Constants.DELIMITER);
                        String table_name_from_file = cell_data[0];
                        if(!table_name.equalsIgnoreCase(table_name_from_file)){
                            final_file_content+=rows[count];
                        }
                    }
                    count++;      
                }

                CreateTable createTable = new CreateTable();
                createTable.setFile_path(workspace_folder);
                createTable.write(final_file_content);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean deleteTableFile(String table_name, String workspace_folder) {

        try{
            String path = ".//workspace//"+workspace_folder+"//"+table_name+".tsv";
            File f = new File(path);
            f.delete();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

}
