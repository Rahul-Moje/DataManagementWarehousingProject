package queries.query_execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class Table {

    String database;
    String table_name;
    HashMap<String,String> column_to_datatype;
    List<HashMap<String,String>> values;
    String where_clause;
    String operator;
    String lhs_column;
    String rhs_value;
    String set_lhs_column;
    String set_rhs_value;
    List<String> not_null_columns;
    List<String> unique_columns;
    List<String> primary_keys;
    HashMap<String,HashMap<String,String>> column_to_referencetable_to_column;


    
    public Table() {
        column_to_datatype = new HashMap<>();
        values = new ArrayList<>();
        not_null_columns = new ArrayList<>();
        unique_columns = new ArrayList<>();
        primary_keys = new ArrayList<>();
        column_to_referencetable_to_column = new HashMap<>();
    }

    
    /** 
     * @return String
     */
    public String getDatabase() {
        return database;
    }

    
    /** 
     * @param database
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    
    /** 
     * @param table_name
     */
    public void setTable_name(String table_name){
        this.table_name = table_name.toLowerCase();
    }

    
    /** 
     * @return String
     */
    public String getTable_name(){
        return this.table_name;
    }

    
    /** 
     * @return String
     */
    public String getLhs_column() {
        return lhs_column;
    }

    
    /** 
     * @param lhs_column
     */
    public void setLhs_column(String lhs_column) {
        this.lhs_column = lhs_column;
    }

    
    /** 
     * @return String
     */
    public String getRhs_value() {
        return rhs_value;
    }

    
    /** 
     * @param rhs_value
     */
    public void setRhs_value(String rhs_value) {
        this.rhs_value = rhs_value;
    }

    
    /** 
     * @param where_clause
     */
    public void setWhere_Clause(String where_clause){
        this.where_clause = where_clause;
    }

    
    /** 
     * @return String
     */
    public String getWhere_clause(){
        return this.table_name;
    }

    
    /** 
     * @param column_to_datatype
     */
    public void setColumn_to_datatype(HashMap<String,String> column_to_datatype){
        this.column_to_datatype = column_to_datatype;
    }

    
    /** 
     * @return HashMap<String, String>
     */
    public HashMap<String,String> getColumn_to_datatype(){
        return this.column_to_datatype;
    }

    
    /** 
     * @param key
     * @param value
     */
    public void add_to_column_to_datatype(String key, String value){
        if(this.column_to_datatype == null){
            this.column_to_datatype = new LinkedHashMap<>();
        }
        this.column_to_datatype.put(key.toLowerCase(), value);
    }

    
    /** 
     * @param values
     */
    public void setValues(List<HashMap<String,String>> values){
        this.values = values;
    }

    
    /** 
     * @return List<HashMap<String, String>>
     */
    public List<HashMap<String,String>> getValues(){
        return this.values;
    }

    
    /** 
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    
    /** 
     * @param where_clause
     */
    public void setWhere_clause(String where_clause) {
        this.where_clause = where_clause;
    }

    
    /** 
     * @return String
     */
    public String getOperator() {
        return operator;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "Table [column_to_datatype=" + column_to_datatype + ", lhs_column=" + lhs_column + ", operator="
                + operator + ", rhs_value=" + rhs_value + ", table_name=" + table_name + ", values=" + values
                + ", where_clause=" + where_clause + "]";
    }

    
    /** 
     * @param set_lhs_column
     */
    public void setSet_lhs_column(String set_lhs_column) {
        this.set_lhs_column = set_lhs_column;
    }

    
    /** 
     * @return String
     */
    public String getSet_lhs_column() {
        return set_lhs_column;
    }

    
    /** 
     * @param set_rhs_value
     */
    public void setSet_rhs_value(String set_rhs_value) {
        this.set_rhs_value = set_rhs_value;
    }

    
    /** 
     * @return String
     */
    public String getSet_rhs_value() {
        return this.set_rhs_value;
    }

    
    /** 
     * @return List<String>
     */
    public List<String> getNot_null_columns() {
        if(not_null_columns == null){
            not_null_columns = new ArrayList<>();
        }
        return not_null_columns;
    }

    
    /** 
     * @param not_null_columns
     */
    public void setNot_null_columns(List<String> not_null_columns) {
        this.not_null_columns = not_null_columns;
    }

    
    /** 
     * @return List<String>
     */
    public List<String> getUnique_columns() {
        if(unique_columns == null){
            unique_columns = new ArrayList<>();
        }
        return unique_columns;
    }

    
    /** 
     * @param unique_columns
     */
    public void setUnique_columns(List<String> unique_columns) {
        this.unique_columns = unique_columns;
    }

    
    /** 
     * @param unique_column
     */
    public void addUnique_column(String unique_column) {
        if(unique_columns == null){
            unique_columns = new ArrayList<>();
        }
        this.unique_columns.add(unique_column);
    }

    
    /** 
     * @param primary_keys
     */
    public void setPrimary_keys(List<String> primary_keys) {
        this.primary_keys = primary_keys;
    }

    
    /** 
     * @param not_null_column
     */
    public void addNot_null_column(String not_null_column) {
        if(not_null_columns == null){
            not_null_columns = new ArrayList<>();
        }
        this.not_null_columns.add(not_null_column);
    }

    
    /** 
     * @param key
     */
    public void addPrimary_Key(String key) {
        if(primary_keys == null){
            primary_keys = new ArrayList<>();
        }
        this.primary_keys.add(key);
    }

    
    /** 
     * @return List<String>
     */
    public List<String> getPrimary_keys(){
        return this.primary_keys;
    }

    
    /** 
     * @return HashMap<String, HashMap<String, String>>
     */
    public HashMap<String, HashMap<String, String>> getColumn_to_referencetable_to_column() {
        return column_to_referencetable_to_column;
    }

    
    /** 
     * @param column_to_referencetable_to_column
     */
    public void setColumn_to_referencetable_to_column(
            HashMap<String, HashMap<String, String>> column_to_referencetable_to_column) {
        this.column_to_referencetable_to_column = column_to_referencetable_to_column;
    }

    

    
}
