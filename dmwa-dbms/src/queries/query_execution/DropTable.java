package queries.query_execution;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

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
        String path = ".//workspace//"+workspace_folder+"//metadata//table_info.txt";
        try {
            String file_content_str = Utility.fetch_file_content(path);
            if(Utility.is_not_null_empty(file_content_str)){
                JSONObject file_content = new JSONObject(file_content_str);

                // System.out.println("---file_content---"+file_content);
                file_content.remove(table_name);
                CreateTable createTable = new CreateTable();
                createTable.setFile_path(workspace_folder);
                createTable.write(file_content.toString());
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
