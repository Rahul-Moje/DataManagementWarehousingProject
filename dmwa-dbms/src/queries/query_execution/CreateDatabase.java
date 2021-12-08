package queries.query_execution;

import common.Utility;

public class CreateDatabase {

    
    /** 
     * create a directory for database
     * @param query
     * @param workspace_folder
     * @return boolean
     */
    public boolean execute(String query, String workspace_folder){
        try{
            
            if(query.endsWith(";")){
                query = query.substring(0, query.length()-1);
            }
            String[] tokens = query.split(" ");
            String db_name = tokens[tokens.length-1];
            String path = ".//workspace//"+workspace_folder+"//"+db_name;
            Utility.check_create_directory(path);

            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
}
