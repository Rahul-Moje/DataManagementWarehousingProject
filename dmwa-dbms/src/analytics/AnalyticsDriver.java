package analytics;

import common.Utility;
import login.User;
import queries.query_validation.UseDatabaseValidation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsDriver {

    private User user;
    private UseDatabaseValidation useDatabaseValidation;
    private static String FILE_DIRECTORY = ".//workspace";

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
        switch (input) {
            case "1" :
                findTotalNumberOfQueries(databaseName);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "2" :
                findTotalNumberOfCreateQueries(databaseName);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "3":
                findTotalNumberOfSelectQueries(databaseName);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "4" :
                findTotalNumberOfUpdateQueries(databaseName);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "5" :
                findTotalNumberOfInsertQueries(databaseName);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "6" :
                findTotalNumberOfDeleteQueries(databaseName);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "7" :
                findTotalNumberOfDropQueries(databaseName);
                runAnalyticsOnDatabase(databaseName);
                break;
            case "8" :
                findTotalNumberOfTables(databaseName);
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

    private List<String> findDatabasesForCurrentUser() throws IOException {
        return Files.list(Paths.get(FILE_DIRECTORY + "\\" + user.getUsername_encrypted()))
                .filter(path -> !path.toFile().getName().equals("logs"))
                .map(path -> path.toFile().getName())
                .collect(Collectors.toList());
    }

    private void findTotalNumberOfQueries(String databaseName) {

    }

    private void findTotalNumberOfCreateQueries(String databaseName) {
    }

    private void findTotalNumberOfUpdateQueries(String databaseName) {
    }

    private void findTotalNumberOfInsertQueries(String databaseName) {
    }

    private void findTotalNumberOfSelectQueries(String databaseName) {
    }

    private void findTotalNumberOfDeleteQueries(String databaseName) {
    }

    private void findTotalNumberOfDropQueries(String databaseName) {
    }

    private void findTotalNumberOfTables(String databaseName) {
    }



}
