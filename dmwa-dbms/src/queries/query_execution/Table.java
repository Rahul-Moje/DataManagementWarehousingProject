package queries.query_execution;

import org.json.JSONObject;

public class Table {

    String table_name;
    JSONObject column_to_datatype;

    public void setTable_name(String table_name){
        this.table_name = table_name.toLowerCase();
    }

    public String getTable_name(){
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

    @Override
    public String toString() {
        return "Table [column_to_datatype=" + column_to_datatype + ", table_name=" + table_name + "]";
    }

    
}
