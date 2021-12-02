package queries.query_execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DeleteFromTable {

    QueryExecutionUtility qUtil;

    public DeleteFromTable(){
        qUtil = new QueryExecutionUtility();
    }

    public Boolean execute(Table table, String workfolder_in_db) {
        
        try{
            List<HashMap<String,String>> filterted_rows = new ArrayList<>();
            List<HashMap<String,String>> rows = table.getValues();
            for(int i = 0; i < rows.size(); ++i) {
                HashMap<String,String> row = rows.get(i);

                if(!qUtil.check_where_condition(row, table)){
                    filterted_rows.add(row);
                }
            }
            table.setValues(filterted_rows);
            return qUtil.insertData(table, workfolder_in_db, true);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
