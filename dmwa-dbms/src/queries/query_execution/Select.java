package queries.query_execution;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Select {

    public Boolean execute(Table table) {
        
        try{
            JSONArray rows = table.getValues();
            Formatter fmt=null;
            
            for(int i = 0; i < rows.length(); ++i) {
                JSONObject row = rows.getJSONObject(i);
                String format = "";
                List<String> values = new ArrayList<String>(); 
                for(String col : row.keySet()){
                    format+="%30s";
                    values.add(row.getString(col));
                }
                //System.out.println("----values----- "+values);
                if(i == 0) {
                    fmt = new Formatter();
                    System.out.println("\n---------------------------------------------------------------------------------------------");  
                    fmt.format(format, row.keySet().toArray());
                    System.out.println(fmt);  
                    System.out.println("---------------------------------------------------------------------------------------------");
                }
                fmt = new Formatter();
                fmt.format(format, values.toArray());
                System.out.println(fmt); 
            }
            fmt.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }
    
}
