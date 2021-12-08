package login;

import analytics.AnalyticsDriver;
import common.DatabaseOperation;
import data_model.DataModelDriver;
import export.ExportDriver;
import log_management.Log;
import log_management.LogWriter;
import queries.QueryDriver;

public class LoginDriver {

    static UserInput userInput;
    static LogWriter log_writer;

    static{
        userInput = new UserInput();
        log_writer = new LogWriter();
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        form();
    }

    /** 
     * form for login and registeration
     */
    private static void form() {

        String input_for_login_registration = userInput.input_for_login_registration();

        if(input_for_login_registration.equals("1")){
            login_workflow();
        }
        else if(input_for_login_registration.equals("2")){
            if(userInput.register()){
                login_workflow();
            }
        }

    }

    /** 
     * method for login opeartions
     */
    private static void login_workflow() {

        if(userInput.login()){
            System.out.println("Login successful!");
            User user = userInput.getUser();
            new Log(user,
                    DatabaseOperation.LOGIN, 
                    "login", "", "",
                    user.getUsername_plain() + " logged in successfully"
            );

            successful_login();
        }
        else{
            form();
        }

    }

    /** 
     * method for after-successful-login operations
     */
    public static void successful_login(){
        String dbms_option = userInput.select_dbms_operation();
        if(dbms_option.equals("1")){
            new QueryDriver(userInput.getUser()).run();
        }
        else if(dbms_option.equals("2")){
            new ExportDriver(userInput.getUser()).run();
        }
        else if(dbms_option.equals("3")){
            new DataModelDriver(userInput.getUser()).run();
        }
        else if(dbms_option.equals("4")){
            new AnalyticsDriver(userInput.getUser()).run();
        }

        successful_login();
    }
    
}
