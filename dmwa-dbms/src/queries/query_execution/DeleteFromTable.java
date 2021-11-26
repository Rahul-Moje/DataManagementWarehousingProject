package queries.query_execution;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeleteFromTable {

    public Boolean execute(Table table, String workfolder_in_db) {
        
        try{
            JSONArray filterted_rows = new JSONArray();
            JSONArray rows = table.getValues();
            Select select = new Select();
            for(int i = 0; i < rows.length(); ++i) {
                JSONObject row = rows.getJSONObject(i);

                if(!select.check_where_condition(row, table)){
                    filterted_rows.put(row);
                    
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
