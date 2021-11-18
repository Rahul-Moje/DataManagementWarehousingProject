package queries.query_execution;

import org.json.JSONArray;
import org.json.JSONObject;

public class Table {

    String table_name;
    JSONObject column_to_datatype;
    JSONArray values;
    String where_clause;

    public void setTable_name(String table_name){
        this.table_name = table_name.toLowerCase();
    }

    public String getTable_name(){
        return this.table_name;
    }

    public void setWhere_Clause(String where_clause){
        this.where_clause = where_clause;
    }

    public String getWhere_clause(){
        return this.table_name;
    }

    public void setColumn_to_datatype(JSONObject column_to_datatype){
        this.column_to_datatype = column_to_datatype;
    }

    public JSONObject getColumn_to_datatype(){
        return this.column_to_datatype;
    }

    public void add_to_column_to_datatype(String key, String value){
        if(this.column_to_datatype == null){
            this.column_to_datatype = new JSONObject();
        }
        this.column_to_datatype.put(key.toLowerCase(), value);
    }

    public void setValues(JSONArray values){
        this.values = values;
    }

    public JSONArray getValues(){
        return this.values;
    }

    @Override
    public String toString() {
        return "Table [column_to_datatype=" + column_to_datatype + ", table_name=" + table_name + ", values=" + values
                + "]";
    }

    
}
