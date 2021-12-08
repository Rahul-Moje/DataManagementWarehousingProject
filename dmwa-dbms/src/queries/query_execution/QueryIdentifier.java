package queries.query_execution;

import common.DatabaseOperation;
import common.Utility;
import log_management.Log;
import login.User;
import queries.query_validation.*;
import transaction.Transaction;

public class QueryIdentifier {
	
    String query;
    String workspace_folder;
    String selected_database;
    User user;

    public QueryIdentifier(String query, User user){//
        this.query = query.replaceAll("\\s", " ");
        this.user = user;
        this.workspace_folder = user.getUsername_encrypted();
    }

    
    /** 
     * @param query
     */
    public void setQuery(String query){
        this.query = query;
    }

    
    /** 
     * identify the query and delegate 
     * the execution of query to appropriate class
     * @param commitFlag
     * @param tx
     */
    public void run(boolean commitFlag, Transaction tx){
    	
        try {
            String error= "";
            String result= "";
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
                        result = "Database created successfully";
                        System.out.println(result);
                    }
                }
                new Log(user, DatabaseOperation.CREATE_DATABASE, "", "", query, result, error);
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
                    result = "Using database "+selected_database;
                    System.out.println(result);
                }
                new Log(user, DatabaseOperation.USE_DATABASE, "", "", query, result, error);
            }
            else {
                String workfolder_in_db = workspace_folder+"//"+selected_database;
                if(!Utility.is_not_null_empty(selected_database)){
                    error = "Database not selected. Please select a database using \"use database\" query.";
                }
                else if(query_for_condition.startsWith("create table")){
                    //validation
                    Table table = new Table();
                    CreateTableValidation validator = new CreateTableValidation();
                    error = validator.validate(query, workfolder_in_db, table);
                    
                    //execute
                    if(!Utility.is_not_null_empty(error)){ 
                        CreateTable executor = new CreateTable();
                        Boolean isSuccess= executor.execute(table, workfolder_in_db);  
                        if(isSuccess){
                            result = "Table created successfully";
                            System.out.println(result);
                        }  
                    }
                    new Log(user, DatabaseOperation.CREATE_TABLE, selected_database, table.getTable_name(), query, result, error);
                }
                else if(query_for_condition.startsWith("insert into")){

                    Table table = new Table();
                    InsertValidation validator = new InsertValidation();
                    error = validator.validate(query, workfolder_in_db, table);

                    if(!Utility.is_not_null_empty(error)){ 
                        Insert executor = new Insert();
                        Boolean isSuccess = executor.execute(table, workfolder_in_db, commitFlag, tx);
                        if(isSuccess){
                            result = table.getValues().size()+ " row(s) inserted successfully";
                        }  
                    }
                    new Log(user, DatabaseOperation.INSERT, selected_database, table.getTable_name(), query, result, error);
                }
                else if(query_for_condition.startsWith("select")){

                    Table table = new Table();
                    SelectValidation validator = new SelectValidation();
                    error = validator.validate(query, workfolder_in_db, table);

                    if(!Utility.is_not_null_empty(error)){ 
                        Select executor = new Select();
                        Boolean isSuccess = executor.execute(table);
                        if(isSuccess) {
                            result = table.getValues().size()+ " row(s)";
                            System.out.println(result);
                        }  
                    }
                    new Log(user, DatabaseOperation.SELECT, selected_database, table.getTable_name(), query, result, error);
                }
                else if(query_for_condition.startsWith("update")){
                    Table table = new Table();
                    UpdateTableValidation validator = new UpdateTableValidation();
                    error = validator.validate(query, workfolder_in_db, table);

                    if(!Utility.is_not_null_empty(error)){ 
                        UpdateTable executor = new UpdateTable();
                        Boolean isSuccess = executor.execute(table, workfolder_in_db, commitFlag, tx);
                        if(isSuccess) {
                            result = "Rows updated successfully";
                            System.out.println(result);
                        }  
                    }
                    new Log(user, DatabaseOperation.UPDATE, selected_database, table.getTable_name(), query, result, error);
                }
                else if(query_for_condition.startsWith("delete from")){
                    Table table = new Table();
                    DeleteFromTableValidation validator = new DeleteFromTableValidation();
                    error = validator.validate(query, workfolder_in_db, table);
                    if(!Utility.is_not_null_empty(error)){ 
                        int rows_before_deletion = table.getValues().size();
                        DeleteFromTable executor = new DeleteFromTable();
                        Boolean isSuccess = executor.execute(table, workfolder_in_db, commitFlag, tx);
                        if(isSuccess) {
                            int rows_after_deletion = table.getValues().size();
                            result = (rows_before_deletion-rows_after_deletion)+" rows deleted successfully";
                            System.out.println(result);
                        }  
                    }
                    new Log(user, DatabaseOperation.DELETE, selected_database, table.getTable_name(), query, result, error);
                }
                else if(query_for_condition.startsWith("drop table") || query_for_condition.startsWith("drop")){
                    Table table = new Table();
                    DropTableValidation validator = new DropTableValidation();
                    error = validator.validate(query, workfolder_in_db, table);

                    if(!Utility.is_not_null_empty(error)){ 
                        DropTable executor = new DropTable();
                        Boolean isSuccess = executor.execute(table, workfolder_in_db);
                        if(isSuccess) {
                            result = "Table is dropped from the database";
                            System.out.println(result);
                        }  
                    }
                    new Log(user, DatabaseOperation.DROP, selected_database, table.getTable_name(), query, result, error);
                }
                else{
                    error = "Incorrect syntax. Please refer query guide.";
                }
            }
        

            if(Utility.is_not_null_empty(error)){
                System.out.println("\n\n\t\t\t*********** Error Occurred ************\t\t\t\n");
                System.out.println(error);
                System.out.println("\n\n\t\t\t*********** End ************\t\t\t\n");
            }
    	} catch(Exception e) {
    		tx.rollback();
    	}
    }


    
}
