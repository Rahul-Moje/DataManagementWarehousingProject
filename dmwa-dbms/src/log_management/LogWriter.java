package log_management;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Constants;
import common.Utility;

public class LogWriter {
    
    static String file_name = Constants.SYSTEM_LOG_FILE_PATH;

    
    /** 
     * create an entry of log and append in log file
     * @param log
     */
    public static void write(Log log) {
        setFile_name(log.getUser().getUsername_encrypted());
        JSONArray file_content;
        try {
            String file_content_str= Utility.fetch_file_content(file_name);
            file_content = file_content_str != null ? new JSONArray(file_content_str) : new JSONArray();

            FileWriter fileWriter = new FileWriter(file_name, false);
            JSONObject log_entry = new JSONObject();
            log_entry.put("timestamp", log.getTimestamp());
            log_entry.put("user", log.getUser().getUsername_plain());
            log_entry.put("database", log.getDatabase());
            log_entry.put("table_name", log.getTable_name());
            log_entry.put("query_type", log.getDatabase_operation());
            log_entry.put("query", log.getQuery());
            log_entry.put("result", log.getResult());

            file_content.put(log_entry);
            fileWriter.write(file_content.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }    
    }

    
    /** 
     * set user's workspace name to the log file path
     * @param username_encrypted
     */
    private static void setFile_name(String username_encrypted){
        file_name = String.format(file_name, username_encrypted);
        Utility.check_create_file_path(file_name);
    }

}
