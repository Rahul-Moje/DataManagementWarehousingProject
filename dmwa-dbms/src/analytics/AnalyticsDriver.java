package analytics;

import common.Utility;
import login.User;
import org.json.JSONArray;
import queries.query_validation.UseDatabaseValidation;

import java.io.IOException;

public class AnalyticsDriver {

    private User user;
    private UseDatabaseValidation useDatabaseValidation;
    private static String FILE_DIRECTORY = ".//workspace";
    private String REGEX = "\".*\"";

    private static final String MENU = "***Welcome to Analytics**\n" +
            "1. Total number of queries executed on database\n" +
            "2. Total number of create queries executed on database\n" +
            "3. Total number of select queries executed on database\n" +
            "4. Total number of update queries executed on database \n" +
            "5. Total number of insert queries executed on database \n" +
            "6. Total number of delete queries executed on database \n" +
            "7. Total number of drop queries executed on database \n" +
            "8. Total number of tables within database\n" +
            "9. Return to main menu";

    public AnalyticsDriver(User user) {
        this.user = user;
        this.useDatabaseValidation = new UseDatabaseValidation();
    }

    public void run() {
        String input = Utility.enter_in_console("Enter query to select database for analytics", System.console());
        String result = useDatabaseValidation.validate(input, user.getUsername_encrypted());
        if(null == result) {
            runAnalyticsOnDatabase(input.split(" ")[2]);
        } else {
            System.out.println("Wrong database entered. Back to main menu");
        }
    }

    private void runAnalyticsOnDatabase(String databaseName) {
        String input = Utility.enter_in_console(MENU, System.console());
        JSONArray jsonArray = null;
        try {
            jsonArray = readSystemLogs();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred. Displaying menu again");
            runAnalyticsOnDatabase(databaseName);
        }

        switch (input) {
            case "1" :
                findTotalNumberOfQueries(databaseName, jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "2" :
                findTotalNumberOfCreateQueries(databaseName, jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "3":
                findTotalNumberOfSelectQueries(databaseName, jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "4" :
                findTotalNumberOfUpdateQueries(databaseName, jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "5" :
                findTotalNumberOfInsertQueries(databaseName, jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "6" :
                findTotalNumberOfDeleteQueries(databaseName, jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "7" :
                findTotalNumberOfDropQueries(databaseName, jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "8" :
                findTotalNumberOfTables(databaseName,jsonArray);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "9" :
                System.out.println("Option9. Taking back to main menu");
                break;
            default:
                System.out.println("Invalid input.");
                runAnalyticsOnDatabase(databaseName);
                break;
        }
    }

    private JSONArray readSystemLogs() throws IOException {
        String file_content_str = Utility.fetch_file_content(FILE_DIRECTORY + "\\" + user.getUsername_encrypted() + "\\logs\\system.logs");
        if(Utility.is_not_null_empty(file_content_str)) {
            JSONArray jsonArray = new JSONArray(file_content_str);
            return jsonArray;
        }
        return null;
    }

    private void findTotalNumberOfQueries(String databaseName, JSONArray jsonArray) {

    }

    private void findTotalNumberOfCreateQueries(String databaseName, JSONArray jsonArray) {
    }

    private void findTotalNumberOfUpdateQueries(String databaseName, JSONArray jsonArray) {
    }

    private void findTotalNumberOfInsertQueries(String databaseName, JSONArray jsonArray) {
    }

    private void findTotalNumberOfSelectQueries(String databaseName, JSONArray jsonArray) {
    }

    private void findTotalNumberOfDeleteQueries(String databaseName, JSONArray jsonArray) {
    }

    private void findTotalNumberOfDropQueries(String databaseName, JSONArray jsonArray) {
    }

    private void findTotalNumberOfTables(String databaseName, JSONArray jsonArray) {
    }



}
