package queries.query_execution;

import org.json.JSONArray;
import org.json.JSONObject;

public class Table {

    String database;
    String table_name;
    JSONObject column_to_datatype;
    JSONArray values;
    String where_clause;
    String operator;
    String lhs_column;
    String rhs_value;
    String set_lhs_column;
    String set_rhs_value;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setTable_name(String table_name){
        this.table_name = table_name.toLowerCase();
    }

    public String getTable_name(){
        return this.table_name;
    }

    public String getLhs_column() {
        return lhs_column;
    }

    public void setLhs_column(String lhs_column) {
        this.lhs_column = lhs_column;
    }

    public String getRhs_value() {
        return rhs_value;
    }

    public void setRhs_value(String rhs_value) {
        this.rhs_value = rhs_value;
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

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setWhere_clause(String where_clause) {
        this.where_clause = where_clause;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "Table [column_to_datatype=" + column_to_datatype + ", lhs_column=" + lhs_column + ", operator="
                + operator + ", rhs_value=" + rhs_value + ", table_name=" + table_name + ", values=" + values
                + ", where_clause=" + where_clause + "]";
    }

    public void setSet_lhs_column(String set_lhs_column) {
        this.set_lhs_column = set_lhs_column;
    }

    public String getSet_lhs_column() {
        return set_lhs_column;
    }

    public void setSet_rhs_value(String set_rhs_value) {
        this.set_rhs_value = set_rhs_value;
    }

    public String getSet_rhs_value() {
        return this.set_rhs_value;
    }

    

    
}
