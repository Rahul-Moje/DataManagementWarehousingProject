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
    String error;


    public Log(User user, DatabaseOperation database_operation, String database, String table_name, String query, String result, String error) {
        this.user = user;
        this.timestamp = LocalDateTime.now();
        this.database_operation = database_operation;
        this.database = database;
        this.table_name = table_name;
        this.query = query;
        this.result = result;
        this.error = error;
        LogWriter.write(this);
    }

    

    
    /** 
     * @param database_operation
     */
    public void setDatabase_operation(DatabaseOperation database_operation) {
        this.database_operation = database_operation;
    }



    
    /** 
     * @return String
     */
    public String getDatabase() {
        return database;
    }



    
    /** 
     * @param database
     */
    public void setDatabase(String database) {
        this.database = database;
    }



    
    /** 
     * @return String
     */
    public String getTable_name() {
        return table_name;
    }



    
    /** 
     * @param table_name
     */
    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }



    
    /** 
     * @return User
     */
    public User getUser() {
        return user;
    }

    
    /** 
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    
    /** 
     * @return LocalDateTime
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    
    /** 
     * @param timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    
    /** 
     * @return DatabaseOperation
     */
    public DatabaseOperation getDatabase_operation() {
        return database_operation;
    }

    
    /** 
     * @param database_operation
     */
    public void setQuery_type(DatabaseOperation database_operation) {
        this.database_operation = database_operation;
    }

    
    /** 
     * @return String
     */
    public String getQuery() {
        return query;
    }

    
    /** 
     * @param query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    
    /** 
     * @return String
     */
    public String getResult() {
        return result;
    }

    
    /** 
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /** 
     * @return String
     */
    public String getError() {
        return error;
    }

    /** 
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }

}
