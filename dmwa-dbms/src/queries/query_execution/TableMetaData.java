package queries.query_execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableMetaData {

    String table_name;
    HashMap<String,String> col_datatype;
    List<String> primary_keys;
    List<String> unique_columns;
    List<String> not_null_columns;
    HashMap<String,HashMap<String,String>> column_to_referencetable_to_column;

    public TableMetaData() {
        col_datatype = new HashMap<>();
        primary_keys = new ArrayList<>();
        unique_columns = new ArrayList<>();
        column_to_referencetable_to_column = new HashMap<>();
    }

    public String getTable_name() {
        return table_name;
    }

    @Override
    public String toString() {
        return "TableMetaData [col_datatype=" + col_datatype + ", column_to_referencetable_to_column="
                + column_to_referencetable_to_column + ", not_null_columns=" + not_null_columns + ", primary_keys="
                + primary_keys + ", table_name=" + table_name + ", unique_columns=" + unique_columns + "]";
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

    public List<String> getPrimary_keys() {
        return primary_keys;
    }
    
    public void setPrimary_keys(List<String> primary_keys) {
        this.primary_keys = primary_keys;
    }

    public List<String> getUnique_columns() {
        return unique_columns;
    }

    public void setUnique_columns(List<String> unique_columns) {
        this.unique_columns = unique_columns;
    }

    public List<String> getNot_null_columns() {
        return not_null_columns;
    }

    public void setNot_null_columns(List<String> not_null_columns) {
        this.not_null_columns = not_null_columns;
    }
    
    public HashMap<String, HashMap<String, String>> getColumn_to_referencetable_to_column() {
        return column_to_referencetable_to_column;
    }

    public void setColumn_to_referencetable_to_column(
            HashMap<String, HashMap<String, String>> column_to_referencetable_to_column) {
        this.column_to_referencetable_to_column = column_to_referencetable_to_column;
    }
}
