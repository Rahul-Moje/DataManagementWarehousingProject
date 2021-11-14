package login;

import java.io.File;

import analytics.AnalyticsDriver;
import data_model.DataModelDriver;
import export.ExportDriver;
import queries.QueryDriver;

public class LoginDriver {

    static UserInput userInput;

    static{
        userInput = new UserInput();
    }

    public static void main(String[] args) {
        form();
    }

    private static void form(){

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

    private static void login_workflow() {

        if(userInput.login()){
            System.out.println("Login successful!");
            create_user_workspace(userInput.getUser().getUsername_encrypted());
            successful_login();
        }
        else{
            form();
        }

    }

    private static void create_user_workspace(String username_encrypted) {
        File workspace = new File(String.format(".//workspace//%s", username_encrypted));
        workspace.mkdirs();
    }


    private static void successful_login(){
        String dbms_option = userInput.select_dbms_operation();
        if(dbms_option.equals("1")){
            QueryDriver.run();
        }
        else if(dbms_option.equals("2")){
            ExportDriver.run();
        }
        else if(dbms_option.equals("3")){
            DataModelDriver.run();
        }
        else if(dbms_option.equals("4")){
            AnalyticsDriver.run();
        }
    }
    
}
