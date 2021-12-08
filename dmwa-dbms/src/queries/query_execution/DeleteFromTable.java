package queries.query_execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.Constants;
import transaction.Transaction;


public class DeleteFromTable {

    QueryExecutionUtility qUtil;

    public DeleteFromTable(){
        qUtil = new QueryExecutionUtility();
    }
    
    
    public List<HashMap<String,String>> getRecentData(Transaction txn, Table table, String file_path) {
		String txnData = txn.getTempData().get(file_path);
		if(txnData == null) return table.getValues();

		String[] rows = txnData.split(Constants.LINE_SEPARATOR);
		
		String[] headers = null;
		if(rows.length > 0) {
			headers = rows[0].split(Constants.DELIMITER);
		}
		List<HashMap<String,String>> data = new ArrayList<>();
			for(int i=1; i< rows.length; i++) {
				HashMap<String, String> map = new HashMap<>();
				
				String[] cells = rows[i].split(Constants.DELIMITER);
				for(int j=0; j< headers.length; j++) {
					map.put(headers[j], cells[j]);
				}
				data.add(map);
			}
			return data;
	}

    public Boolean execute(Table table, String workfolder_in_db, boolean commitFlag, Transaction tx) {
        
        try{
            List<HashMap<String,String>> filtered_rows = new ArrayList<>();
            String file_path = ".//workspace//"+workfolder_in_db+"//"+table.getTable_name()+Constants.DATA_FILE_EXTENSION;

            List<HashMap<String,String>> rows = getRecentData(tx, table, file_path);
            for(int i = 0; i < rows.size(); ++i) {
                HashMap<String,String> row = rows.get(i);

                if(!qUtil.check_where_condition(row, table)){
                    filtered_rows.add(row);
                }
            }
            table.setValues(filtered_rows);
            return qUtil.insertData(table, workfolder_in_db, true,true, tx);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
