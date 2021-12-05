package common;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import queries.query_execution.Table;
import queries.query_execution.TableMetaData;

public class Utility {

    public static void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void system_abort() {
        System.out.println("Aborting.....");
        System.exit(0);
    }

    public static String enter_in_console(String question, Console console){
        
        boolean isSure = false;
        String input= "";
        while(!isSure){
            System.out.println(question);
            input= console.readLine();
            if(!is_not_null_empty(input)){
                System.out.println("Input cannot be blank. Enter again.");
                return enter_in_console(question, console);
            }

            //System.out.println("Are you sure? (Y/N)");
            //isSure= console.readLine().equalsIgnoreCase("Y") ? true : false;
            isSure = true;
        }
        return input;
    }

    public static boolean is_not_null_empty(String input_str) {
        return input_str != null && !input_str.trim().isEmpty();
    }

    public static void check_create_file_path(String file_path) {
        File file = new File(file_path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
            
    }

    public static void check_create_directory(String directory) {
        File file = new File(directory);
        if(!file.exists()){
            file.mkdirs();
        }
            
    }

    public static String fetch_file_content(String file_name) throws IOException{
        File file = new File(file_name);
        if (file.exists()){
            String info="";
           
            InputStream inputStream = new FileInputStream(file_name);
            info = IOUtils.toString(inputStream, "UTF-8");
            
            return info;
        }
        return null;
    }

    public static void write(String file_path, String content) throws IOException{
        // content = content.trim();
        FileWriter fileWriter = new FileWriter(file_path, false);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();

    }

    public static String return_if_foreign_key(Table table, String workspace_folder) {
        List<TableMetaData> tables_info = RetrieveTableInfo.getTables(workspace_folder);

        for(TableMetaData tmd: tables_info){
            HashMap<String,HashMap<String,String>> foreign_map = tmd.getColumn_to_referencetable_to_column();
            for(String fk: foreign_map.keySet()){
                if(foreign_map.get(fk).containsKey(table.getTable_name())){
                    return "Cannot delete the table since it is refenced by another table- "+tmd.getTable_name();
                }
            }

        }
        return null;
    }
    
}
