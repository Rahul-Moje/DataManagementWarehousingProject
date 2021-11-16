package queries.query_execution;

import common.Utility;
import queries.query_validation.*;

public class QueryIdentifier {

    String query;
    String workspace_folder;
    String selected_database;

    public QueryIdentifier(String query, String workspace_folder){
        this.query = query;
        this.workspace_folder = workspace_folder;
    }

    public void run(){

        String error= "";
        query = query.toLowerCase().replaceAll("\\s", " ");
        if(query.startsWith("create database")){
            //validate
            CreateDatabaseValidation validator = new CreateDatabaseValidation();
            error = validator.validate(query, workspace_folder);

            //execute
            if(!Utility.is_not_null_empty(error)){
                CreateDatabase executor = new CreateDatabase();
                executor.execute(query, workspace_folder);
            }
        }
        else if(query.startsWith("use database")){
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
            }
        }
        else if(query.startsWith("create table")){
            
        }
        else if(query.startsWith("insert into")){
            
        }
        else if(query.startsWith("select")){
            
        }
        else if(query.startsWith("update")){
            
        }
        else if(query.startsWith("delete from")){
            
        }
        else if(query.startsWith("drop table")){
            
        }

        if(Utility.is_not_null_empty(error)){
            System.out.println("\n\n\t\t\t*********** Error Occurred ************\t\t\t\n");
            System.out.println(error);
            System.out.println("\n\n\t\t\t*********** End ************\t\t\t\n");
        }
    }


    
}
