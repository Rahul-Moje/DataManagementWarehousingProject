package queries.query_execution;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

public class Select {

    QueryExecutionUtility qUtil;

    public Select(){
        qUtil = new QueryExecutionUtility();
    }

    public Boolean execute(Table table) {
        
        try{
            // LinkedList<LinkedHashMap<String,String>> filterted_rows = new LinkedList<>();
            List<HashMap<String,String>> filterted_rows = new ArrayList<>();
            List<HashMap<String,String>> rows = table.getValues();
            Formatter fmt=new Formatter();
            HashMap<String,String> row_for_header;
            if(rows !=null && rows.size()>0){
                row_for_header= rows.get(0);
            }
            else{
                row_for_header= table.getColumn_to_datatype();
            }
            String format  = "%30s ".repeat(row_for_header.keySet().size());
            System.out.println("\n---------------------------------------------------------------------------------------------");  
            fmt.format(format, row_for_header.keySet().toArray());
            System.out.println(fmt);  
            System.out.println("---------------------------------------------------------------------------------------------");

            if(rows !=null && rows.size()>0){
                for(int i = 0; i < rows.size(); ++i) {
                    HashMap<String,String> row = rows.get(i);

                    if(qUtil.check_where_condition(row, table)){
                        List<String> values = new ArrayList<String>(); 
                        for(String col : row.keySet()){
                            values.add(row.get(col));
                        }
                        
                        fmt = new Formatter();
                        fmt.format(format, values.toArray());
                        System.out.println(fmt); 
                        filterted_rows.add(row);
                    }
                }
            }
            
            table.setValues(filterted_rows);
            fmt.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }
    
}
