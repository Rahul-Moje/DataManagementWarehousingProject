package queries.query_execution;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateTable {

    public Boolean execute(Table table, String workfolder_in_db) {

        try{
            List<HashMap<String,String>> filterted_rows = new ArrayList<>();
            List<HashMap<String,String>> rows = table.getValues();
            Select select = new Select();
            for(int i = 0; i < rows.size(); ++i) {
                HashMap<String,String> row = rows.get(i);
                System.out.println("--row----2h "+row);
                if(select.check_where_condition(row, table)){
                    row.remove(table.getSet_lhs_column());
                    System.out.println("--row----after removing "+row);
                    row.put(table.getSet_lhs_column(), table.getSet_rhs_value());
                    
                    //System.out.println("----values----- "+values);
                }
                filterted_rows.add(row);
            }
            System.out.println("---filterted_rows---- "+filterted_rows);
            System.out.println("----1----------"+table.getValues());
            table.setValues(filterted_rows);

            System.out.println("----1----------"+table.getValues());

            Insert insert = new Insert();
            insert.setRewrite(true);
            insert.execute(table, workfolder_in_db);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
