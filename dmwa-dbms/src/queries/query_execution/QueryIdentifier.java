package queries.query_execution;

import common.DatabaseOperation;
import common.Utility;
import log_management.Log;
import login.User;
import queries.query_validation.*;

public class QueryIdentifier {

    String query;
    String workspace_folder;
    String selected_database;
    User user;

    public QueryIdentifier(String query, User user){
        this.query = query.replaceAll("\\s", " ");
        this.user = user;
        this.workspace_folder = user.getUsername_encrypted();
    }

    public void setQuery(String query){
        this.query = query;
    }

    public void run(){

        String error= "";
        String query_for_condition = query.toLowerCase();
        if(query_for_condition.startsWith("create database")){
            query = query.toLowerCase();
            //validate
            CreateDatabaseValidation validator = new CreateDatabaseValidation();
            error = validator.validate(query, workspace_folder);

            //execute
            if(!Utility.is_not_null_empty(error)){
                CreateDatabase executor = new CreateDatabase();
                Boolean isSuccess = executor.execute(query, workspace_folder);
                if(isSuccess){
                    String result = "Database created successfully";
                    System.out.println(result);
                    new Log(user, DatabaseOperation.CREATE_DATABASE, "", "", query, result);
                }
            }
        }
        else if(query_for_condition.startsWith("use database")){
            query = query.toLowerCase();
            //validate
            UseDatabaseValidation validator = new UseDatabaseValidation();
            error = validator.validate(query, workspace_folder);

            //execute
            if(!Utility.is_not_null_empty(error)){   
                if(query.endsWith(";")){
                    query = query.substring(0, query.length()-1);
                }
                String[] tokens = query.split(" ");
                selected_database = tokens[tokens.length-1];
                String result = "Using database "+selected_database;
                System.out.println(result);
                new Log(user, DatabaseOperation.USE_DATABASE, selected_database, "", query, result);
            }
        }
        else {
            String workfolder_in_db = workspace_folder+"//"+selected_database;
            if(!Utility.is_not_null_empty(selected_database)){
                error = "Database not selected. Please select a database using \"use database\" query.";
            }
            else if(query_for_condition.startsWith("create table")){
                //validation
                Table table = new Table();
                //System.out.println(table.toString());
                CreateTableValidation validator = new CreateTableValidation();
                error = validator.validate(query, workfolder_in_db, table);

                
                //execute
                if(!Utility.is_not_null_empty(error)){ 
                    //System.out.println(table.toString());
                    CreateTable executor = new CreateTable();
                    Boolean isSuccess= executor.execute(table, workfolder_in_db);  
                    if(isSuccess){
                        String result = "Table created successfully";
                        System.out.println(result);
                        new Log(user, DatabaseOperation.CREATE_TABLE, selected_database, table.getTable_name(), query, result);
                    }  
                }
            }
            else if(query_for_condition.startsWith("insert into")){

                Table table = new Table();
                InsertValidation validator = new InsertValidation();
                error = validator.validate(query, workfolder_in_db, table);

                if(!Utility.is_not_null_empty(error)){ 
                    Insert executor = new Insert();
                    Boolean isSuccess = executor.execute(table, workfolder_in_db);
                    if(isSuccess){
                        String result = table.getValues().length()+ " row(s) inserted successfully";
                        System.out.println(result);
                        new Log(user, DatabaseOperation.INSERT, selected_database, table.getTable_name(), query, result);
                    }  
                }
            }
            else if(query_for_condition.startsWith("select")){

                Table table = new Table();
                SelectValidation validator = new SelectValidation();
                error = validator.validate(query, workfolder_in_db, table);

                if(!Utility.is_not_null_empty(error)){ 
                    Select executor = new Select();
                    Boolean isSuccess = executor.execute(table);
                    if(isSuccess) {
                        String result = table.getValues().length()+ " row(s)";
                        System.out.println(result);
                        new Log(user, DatabaseOperation.INSERT, selected_database, table.getTable_name(), query, result);
                    }  
                }
                
            }
            else if(query_for_condition.startsWith("update")){
                Table table = new Table();
                UpdateTableValidation validator = new UpdateTableValidation();
                error = validator.validate(query, workfolder_in_db, table);

                if(!Utility.is_not_null_empty(error)){ 
                    UpdateTable executor = new UpdateTable();
                    Boolean isSuccess = executor.execute(table, workfolder_in_db);
                    if(isSuccess) {
                        String result = "Table is dropped from the database";
                        System.out.println(result);
                        new Log(user, DatabaseOperation.DROP, selected_database, table.getTable_name(), query, result);
                    }  
                }
            }
            else if(query_for_condition.startsWith("delete from")){
                
            }
            else if(query_for_condition.startsWith("drop table") || query_for_condition.startsWith("drop")){
                Table table = new Table();
                DropTableValidation validator = new DropTableValidation();
                error = validator.validate(query, workfolder_in_db, table);

                if(!Utility.is_not_null_empty(error)){ 
                    DropTable executor = new DropTable();
                    Boolean isSuccess = executor.execute(table, workfolder_in_db);
                    if(isSuccess) {
                        String result = "Table is dropped from the database";
                        System.out.println(result);
                        new Log(user, DatabaseOperation.DROP, selected_database, table.getTable_name(), query, result);
                    }  
                }
            }
        }
        

        if(Utility.is_not_null_empty(error)){
            System.out.println("\n\n\t\t\t*********** Error Occurred ************\t\t\t\n");
            System.out.println(error);
            System.out.println("\n\n\t\t\t*********** End ************\t\t\t\n");
        }
    }


    
}
