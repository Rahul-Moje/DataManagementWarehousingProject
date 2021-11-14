package log_management;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Utility;
import login.User;

public class LogWriter {

    Log log;
    String file_name = ".//logs//%s//system.logs";

    public LogWriter(Log log) {
        this.log = log;
        setFile_name();
        
    }

    public void setFile_name(){
        User user = this.log.getUser();
        String work_folder= user.getUsername_encrypted();
        file_name = String.format(file_name, work_folder);
    }

    public void write_log() {
        JSONArray file_content;
        try {
            String file_content_str= Utility.fetch_file_content(file_name);
            file_content = file_content_str != null ? new JSONArray(file_content_str) : new JSONArray();

            File file = new File(file_name);
            file.getParentFile().mkdirs();

            FileWriter fileWriter = new FileWriter(file_name, false);
            JSONObject log_entry = new JSONObject();
            log_entry.put("timestamp", log.getTimestamp());
            log_entry.put("user", log.getUser().getUsername_plain());
            log_entry.put("quey_type", log.getQuery_type());
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

    



    
}
