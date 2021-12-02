package queries.query_execution;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateTable {

    QueryExecutionUtility qUtil;

    public UpdateTable(){
        qUtil = new QueryExecutionUtility();
    }

    public Boolean execute(Table table, String workfolder_in_db) {

        try{
            List<HashMap<String,String>> filterted_rows = new ArrayList<>();
            List<HashMap<String,String>> rows = table.getValues();
            for(int i = 0; i < rows.size(); ++i) {
                HashMap<String,String> row = rows.get(i);
                if(qUtil.check_where_condition(row, table)){
                    row.remove(table.getSet_lhs_column());
                    row.put(table.getSet_lhs_column(), table.getSet_rhs_value());
                }
                filterted_rows.add(row);
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
