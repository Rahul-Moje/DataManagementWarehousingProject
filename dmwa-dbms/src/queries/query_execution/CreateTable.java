package queries.query_execution;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import common.Constants;
import common.Utility;

public class CreateTable {

    String file_path = Constants.TABLE_INFO_FILE_PATH;
    final String header = Constants.TABLE_INFO_HEADERS;
    
    /** 
     * @param table
     * @param workspace_folder
     * @return boolean
     */
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

    
    /** 
     * create an entry of the table and its information
     * write the entry to table_info.tsv
     * @param table
     * @throws IOException
     */
    private void write_in_file(Table table) throws IOException {
        
        create_empty_file_if_not_exists();

        String file_content_str = Utility.fetch_file_content(file_path);
        if(Utility.is_not_null_empty(file_content_str)){
            String string_to_append = Constants.LINE_SEPARATOR+
                                    table.getTable_name()+Constants.DELIMITER
                                    +String.join(";", table.getColumn_to_datatype().keySet())+Constants.DELIMITER
                                    +String.join(";", table.getColumn_to_datatype().values())+Constants.DELIMITER
                                    +String.join(";", table.getPrimary_keys())+Constants.DELIMITER
                                    +String.join(";", table.getUnique_columns())+Constants.DELIMITER
                                    +String.join(";", table.getNot_null_columns())+Constants.DELIMITER;
            String foreign = "";
            HashMap<String,HashMap<String,String>> map_temp = table.getColumn_to_referencetable_to_column();
            if(map_temp!=null && map_temp.size()>0){
                for(String fk: map_temp.keySet()){
                    foreign+=fk+"#";
                    for(String r_table: map_temp.get(fk).keySet()){
                        String r_col = map_temp.get(fk).get(r_table);
                        foreign+=r_table+"#"+r_col+";";
                    }
                }
                foreign = foreign.substring(0, foreign.length()-1);
            }
            string_to_append+=foreign;
            
            file_content_str+=string_to_append;
            Utility.write(file_path, file_content_str);
        }
    }

    
    /** 
     * create empty file if table_info.tsv does not exist
     * @throws IOException
     */
    private void create_empty_file_if_not_exists() throws IOException {
        File file = new File(file_path);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            Utility.write(file_path, header);
        }
    }

    
    /** 
     * set workspace value in the table_info file path
     * @param workspace_folder
     */
    public void setFile_path(String workspace_folder) {
        this.file_path = String.format(file_path, workspace_folder);
    }
    
}
