package analytics;

import common.DatabaseOperation;
import common.Utility;
import login.User;
import org.json.JSONArray;
import org.json.JSONObject;
import queries.query_validation.UseDatabaseValidation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class AnalyticsDriver {

    private User user;
    private UseDatabaseValidation useDatabaseValidation;
    private static String FILE_DIRECTORY = ".//workspace";
    private String REGEX = "\".*\"";
    private StringBuilder stringBuilder;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

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
        stringBuilder = new StringBuilder("Analytics started\n");
        String input = Utility.enter_in_console("Enter query to select database for analytics", System.console());
        String result = useDatabaseValidation.validate(input, user.getUsername_encrypted());
        if(null == result) {
            runAnalyticsOnDatabase(input.split(" ")[2]);
        } else {
            System.out.println("Wrong database entered. Back to main menu");
        }
        stringBuilder.append("Analytics ended\n");
        writeAnalyticsToFile();
    }

    private void writeAnalyticsToFile() {
        Path path = Paths.get(FILE_DIRECTORY + "\\" + user.getUsername_encrypted() + "\\logs\\analytics_" + DATE_FORMAT.format(new Date()));
        try {
            Files.write(path, Collections.singleton(stringBuilder.toString()));
        } catch (IOException e) {
            e.printStackTrace();
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
            return new JSONArray(file_content_str);
        }
        return null;
    }

    private void findTotalNumberOfQueries(String databaseName, JSONArray jsonArray) {
        int count = 0;
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName)) {
                count++;
            }
        }
        System.out.println("Total number of queries executed by " + user.getUsername_plain() + " is " + count);
        stringBuilder.append("Total number of queries executed by " + user.getUsername_plain() + " is " + count + "\n");
    }

    private boolean isLogRelatedToDatabase(JSONObject jsonObject, String databaseName) {
        if(jsonObject.getString("database").equals(databaseName)
                && jsonObject.getString("user").equalsIgnoreCase(user.getUsername_plain())
                && !jsonObject.getString("query_type").equals(DatabaseOperation.LOGIN.name())
                && !jsonObject.getString("query_type").equals(DatabaseOperation.USE_DATABASE.name())) {
            return true;
        }
        if(jsonObject.getString("query_type").equals(DatabaseOperation.CREATE_DATABASE.name())
                && jsonObject.getString("query").split(" ")[2].equals(databaseName)
                && jsonObject.getString("result").contains("success")) {
            return true;
        }
        return false;
    }

    private void findTotalNumberOfCreateQueries(String databaseName, JSONArray jsonArray) {
        Map<String, Integer> countMap = new HashMap<>();
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName) && isQueryOfType(jsonObject, DatabaseOperation.CREATE_TABLE)) {
                String tableName = jsonObject.getString("table_name");
                if(countMap.containsKey(tableName)) {
                    countMap.put(tableName, countMap.get(tableName)+1);
                } else {
                    countMap.put(tableName, 1);
                }
            }
        }
        displayQueryStats(countMap, DatabaseOperation.CREATE_TABLE);
    }

    private void displayQueryStats(Map<String, Integer> countMap, DatabaseOperation operation) {
        if(countMap.size() == 0) {
            System.out.println("No " + operation.name() + " queries were executed by " + user.getUsername_plain());
            stringBuilder.append("No " + operation.name() + " queries were executed by " + user.getUsername_plain() + "\n");
            return;
        }
        for(Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println("Total number of " + operation.name() + " queries executed by " + user.getUsername_plain()
                    + " on table " + entry.getKey() + " is " + entry.getValue());
            stringBuilder.append("Total number of " + operation.name() + " queries executed by " + user.getUsername_plain()
                    + " on table " + entry.getKey() + " is " + entry.getValue() + "\n");
        }
    }

    private boolean isQueryOfType(JSONObject jsonObject, DatabaseOperation operation) {
        return jsonObject.getString("query_type").equals(operation.name());
    }

    private void findTotalNumberOfUpdateQueries(String databaseName, JSONArray jsonArray) {
        Map<String, Integer> countMap = new HashMap<>();
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName) && isQueryOfType(jsonObject, DatabaseOperation.UPDATE)) {
                String tableName = jsonObject.getString("table_name");
                if(countMap.containsKey(tableName)) {
                    countMap.put(tableName, countMap.get(tableName)+1);
                } else {
                    countMap.put(tableName, 1);
                }
            }
        }
        displayQueryStats(countMap, DatabaseOperation.UPDATE);
    }

    private void findTotalNumberOfInsertQueries(String databaseName, JSONArray jsonArray) {
        Map<String, Integer> countMap = new HashMap<>();
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName) && isQueryOfType(jsonObject, DatabaseOperation.INSERT)) {
                String tableName = jsonObject.getString("table_name");
                if(countMap.containsKey(tableName)) {
                    countMap.put(tableName, countMap.get(tableName)+1);
                } else {
                    countMap.put(tableName, 1);
                }
            }
        }
        displayQueryStats(countMap, DatabaseOperation.INSERT);
    }

    private void findTotalNumberOfSelectQueries(String databaseName, JSONArray jsonArray) {
        Map<String, Integer> countMap = new HashMap<>();
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName) && isQueryOfType(jsonObject, DatabaseOperation.SELECT)) {
                String tableName = jsonObject.getString("table_name");
                if(countMap.containsKey(tableName)) {
                    countMap.put(tableName, countMap.get(tableName)+1);
                } else {
                    countMap.put(tableName, 1);
                }
            }
        }
        displayQueryStats(countMap, DatabaseOperation.SELECT);
    }

    private void findTotalNumberOfDeleteQueries(String databaseName, JSONArray jsonArray) {
        Map<String, Integer> countMap = new HashMap<>();
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName) && isQueryOfType(jsonObject, DatabaseOperation.DELETE)) {
                String tableName = jsonObject.getString("table_name");
                if(countMap.containsKey(tableName)) {
                    countMap.put(tableName, countMap.get(tableName)+1);
                } else {
                    countMap.put(tableName, 1);
                }
            }
        }
        displayQueryStats(countMap, DatabaseOperation.DELETE);
    }

    private void findTotalNumberOfDropQueries(String databaseName, JSONArray jsonArray) {
        Map<String, Integer> countMap = new HashMap<>();
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName) && isQueryOfType(jsonObject, DatabaseOperation.DROP)) {
                String tableName = jsonObject.getString("table_name");
                if(countMap.containsKey(tableName)) {
                    countMap.put(tableName, countMap.get(tableName)+1);
                } else {
                    countMap.put(tableName, 1);
                }
            }
        }
        displayQueryStats(countMap, DatabaseOperation.DROP);
    }

    private void findTotalNumberOfTables(String databaseName, JSONArray jsonArray) {
        Set<String> tableNames = new HashSet<>();
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(isLogRelatedToDatabase(jsonObject, databaseName)) {
                String tableName = jsonObject.getString("table_name");
                if(null != tableName && !"".equals(tableName)) {
                    tableNames.add(tableName);
                }
            }
        }
        System.out.println("Total number of tables in database " + databaseName + " is " + tableNames.size());
        stringBuilder.append("Total number of tables in database " + databaseName + " is " + tableNames.size());
        System.out.println("Tables are " + tableNames);
        stringBuilder.append("Tables are " + tableNames);
    }



}
