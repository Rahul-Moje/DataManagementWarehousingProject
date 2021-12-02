package queries.query_execution;

public class Insert {

    QueryExecutionUtility qUtil;

    public Insert(){
        qUtil = new QueryExecutionUtility();
    }

    public Boolean execute(Table table, String workfolder_in_db) {
        
        return qUtil.insertData(table, workfolder_in_db, false);
    }

}
