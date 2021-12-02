package queries.query_execution;

import java.util.HashMap;
import java.util.List;

public class TableMetaData {

    String table_name;
    HashMap<String,String> col_datatype;
    List<String> foreign_key;
    List<String> primary_key;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public HashMap<String, String> getCol_datatype() {
        return col_datatype;
    }

    public void setCol_datatype(HashMap<String, String> col_datatype) {
        this.col_datatype = col_datatype;
    }

    public List<String> getForeign_key() {
        return foreign_key;
    }

    public void setForeign_key(List<String> foreign_key) {
        this.foreign_key = foreign_key;
    }

    public List<String> getPrimary_key() {
        return primary_key;
    }
    
    public void setPrimary_key(List<String> primary_key) {
        this.primary_key = primary_key;
    }
    
    
}
