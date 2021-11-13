package login;
import java.io.Console;

import common.Utility;

public class UserInput {

    static Console console;

    public UserInput(){
        console = System.console();
    }

    public String input_for_login_registration(){
        return Utility.enterInConsole("Choose from following options (1/2):"
                                    +"\n1. Login"
                                    +"\n2. Register", console);
    }

    public boolean login(){
        System.out.println("\n\n\t\t\t*********** Login ************\t\t\t\n"); 

        String username = Utility.enterInConsole("Enter username:", console);
        String password = Utility.enterInConsole("Enter password:", console);
        UserProfileIO userProfileIO = new UserProfileIO();
        UserLoginDetails userLoginDetails = userProfileIO.check_credentials(username, password);
        if(userLoginDetails != null){
            int count = 1;
            while(count<=3){
                String input_answer = Utility.enterInConsole("Security Question : "+userLoginDetails.getSecurity_question(), console);
                if(input_answer.equalsIgnoreCase(userLoginDetails.getSecurity_answer())){
                    System.out.println("Login successful!");
                    return true;
                }
                else{
                    int remaining_attempts = 3- count;
                    System.out.println("Incorrect answer. "+ remaining_attempts+" attempts left.");
                }
                count++;
            }
        }
        else{
            System.out.println("Incorrect username/password. Please try again");
        }
        return false;

    }

    public boolean register(){
        System.out.println("\n\n\t\t\t*********** Register ************\t\t\t\n"); 
        LoginRegisterStatus result = LoginRegisterStatus.USER_ALREADY_EXISTS;
        while(result == LoginRegisterStatus.USER_ALREADY_EXISTS){
            String username = Utility.enterInConsole("Enter username:", console);
            String password = Utility.enterInConsole("Enter password:", console);
            String security_question = Utility.enterInConsole("Enter a security question:", console);
            String security_question_answer = Utility.enterInConsole("Answer of the security question:", console);
            UserLoginDetails userLoginDetails = new UserLoginDetails(null, username, password, security_question, security_question_answer);
            UserProfileIO userProfileIO = new UserProfileIO();
            result= userProfileIO.add_user(userLoginDetails);    
            if(result == LoginRegisterStatus.SUCCESS){
                System.out.println("Registered successfully!");
                System.out.println("Redirecting to login...");
                Utility.sleep(3);
                return true;
            }    
            else if(result == LoginRegisterStatus.USER_ALREADY_EXISTS){
                System.out.println("Username already exists. Please try again.");
                Utility.sleep(3);
                continue;
            }
            else{
                Utility.system_abort();
            }
        }
        return false;
        
    }

    public String select_dbms_operation(){
        return Utility.enterInConsole("Choose from following options (1/2/3/4): "
                                    +"\n1. Write Queries "
                                    +"\n2. Export "
                                    +"\n3. Data Model "
                                    +"\n4. Analytics", console);
    }

}
