package common;

import java.io.Console;
import java.util.concurrent.TimeUnit;

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

    public static String enterInConsole(String question, Console console){
        
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

    public static boolean is_null_empty(String input_str) {
        return input_str != null && !input_str.trim().isEmpty();
    }
    
}
