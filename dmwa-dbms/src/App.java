import login.LoginDriver;

public class App{
    public static void main(String[] args) {
        LoginDriver.main(args);

        // LogWriter l = new LogWriter(new Log(new User("username_encrypted", 
        //                                             "username_plain", 
        //                                             "password", 
        //                                             "security_question", 
        //                                             "security_answer"
        //                                             )
        //                                     , LocalDateTime.now()
        //                                     , QueryType.CREATE_DATABASE
        //                                     , "query"
        //                                     , "2 rows updated")
        //                             );
        // l.write_log();
    }
}