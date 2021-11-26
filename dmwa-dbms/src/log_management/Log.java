package log_management;

import java.time.LocalDateTime;

import common.DatabaseOperation;
import login.User;

public class Log {

    User user;
    LocalDateTime timestamp;
    DatabaseOperation database_operation;
    String database;
    String table_name;
    String query;
    String result;


    public Log(User user, DatabaseOperation database_operation, String database, String table_name, String query, String result) {
        this.user = user;
        this.timestamp = LocalDateTime.now();
        this.database_operation = database_operation;
        this.database = database;
        this.table_name = table_name;
        this.query = query;
        this.result = result;

        LogWriter.write(this);
    }

    

    public void setDatabase_operation(DatabaseOperation database_operation) {
        this.database_operation = database_operation;
    }



    public String getDatabase() {
        return database;
    }



    public void setDatabase(String database) {
        this.database = database;
    }



    public String getTable_name() {
        return table_name;
    }



    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public DatabaseOperation getDatabase_operation() {
        return database_operation;
    }

    public void setQuery_type(DatabaseOperation database_operation) {
        this.database_operation = database_operation;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
