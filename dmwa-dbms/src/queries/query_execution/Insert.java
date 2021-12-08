package queries.query_execution;

import transaction.Transaction;

public class Insert {

    QueryExecutionUtility qUtil;

    public Insert(){
        qUtil = new QueryExecutionUtility();
    }

    
    /** 
     * insert data into the table file
     * @param table
     * @param workfolder_in_db
     * @param commitFlag
     * @param tx
     * @return Boolean
     */
    public Boolean execute(Table table, String workfolder_in_db, boolean commitFlag, Transaction tx) {
        
        return qUtil.insertData(table, workfolder_in_db, false , commitFlag, tx);
    }

}
