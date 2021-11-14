package common;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

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

            //System.out.println("Are you sure? (Y/N)");
            //isSure= console.readLine().equalsIgnoreCase("Y") ? true : false;
            isSure = true;
        }
        return input;
    }

    public static boolean is_not_null_empty(String input_str) {
        return input_str != null && !input_str.trim().isEmpty();
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
    
}
