package queries.query_execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DeleteFromTable {

    public Boolean execute(Table table, String workfolder_in_db) {
        
        try{
            List<HashMap<String,String>> filterted_rows = new ArrayList<>();
            List<HashMap<String,String>> rows = table.getValues();
            Select select = new Select();
            for(int i = 0; i < rows.size(); ++i) {
                HashMap<String,String> row = rows.get(i);

                if(!select.check_where_condition(row, table)){
                    filterted_rows.add(row);
                    
                    //System.out.println("----values----- "+values);
                }
            }
            table.setValues(filterted_rows);

            Insert insert = new Insert();
            insert.execute(table, workfolder_in_db);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
