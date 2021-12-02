package queries.query_execution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.Constants;
import common.Utility;

public class CreateTable {

    String file_path = ".//workspace//%s//metadata//table_info"+Constants.DATA_FILE_EXTENSION;
    final String header = String.format("table_name%scolumns%sdata_types", Constants.DELIMITER, Constants.DELIMITER);

    public boolean execute(Table table, String workspace_folder) {

        setFile_path(workspace_folder);

        try {
            write_in_file(table);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void write_in_file(Table table) throws IOException {
        
        create_empty_file_if_not_exists();

        String file_content_str = Utility.fetch_file_content(file_path);
        if(Utility.is_not_null_empty(file_content_str)){
            String string_to_append = Constants.LINE_SEPARATOR+
                                    table.getTable_name()+Constants.DELIMITER
                                    +String.join(";", table.getColumn_to_datatype().keySet())+Constants.DELIMITER
                                    +String.join(";", table.getColumn_to_datatype().values());
            file_content_str+=string_to_append;
            write(file_content_str);
        }
        
        
    }

    public void write(String content) throws IOException{
        FileWriter fileWriter = new FileWriter(file_path, false);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();

    }

    private void create_empty_file_if_not_exists() throws IOException {
        File file = new File(file_path);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            write(header);
        }
    }

    public void setFile_path(String workspace_folder) {
        this.file_path = String.format(file_path, workspace_folder);
    }
    
}
