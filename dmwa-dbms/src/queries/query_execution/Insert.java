package queries.query_execution;

import transaction.Transaction;

public class Insert {

    QueryExecutionUtility qUtil;

    public Insert(){
        qUtil = new QueryExecutionUtility();
    }

    public Boolean execute(Table table, String workfolder_in_db, boolean commitFlag, Transaction tx) {
        
        return qUtil.insertData(table, workfolder_in_db, false , commitFlag, tx);
    }

}
