package queries.query_execution;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Constants;
import transaction.Transaction;

public class UpdateTable {

    QueryExecutionUtility qUtil;

    public UpdateTable(){
        qUtil = new QueryExecutionUtility();
    };
    
    public List<HashMap<String,String>> getRecentData(Transaction txn, Table table, String file_path) {
		String txnData = txn.getTempData().get(file_path);
		if(txnData == null) return table.getValues();

		String[] rows = txnData.split(Constants.LINE_SEPARATOR);
		
		String[] headers = null;
		if(rows.length > 0) {
			headers = rows[0].split(Constants.DELIMITER);
			System.out.println(Arrays.toString(headers));
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
			// System.out.println(data);
			return data;
	}
    

    public Boolean execute(Table table , String workfolder_in_db, boolean commitFlag, Transaction tx) {

        try{
            List<HashMap<String,String>> filtered_rows = new ArrayList<>();
            String file_path = ".//workspace//"+workfolder_in_db+"//"+table.getTable_name()+Constants.DATA_FILE_EXTENSION;
            List<HashMap<String,String>> rows = getRecentData(tx, table, file_path);
            //Map<String,String> updatedRows = new HashMap<>();
            //for primary key validation
            List<String> pk_values = new ArrayList<>();

            //for unique constraint
            Map<String,List<String>> column_unique_values = new HashMap<>();

            for(int i = 0; i < rows.size(); ++i) {
                HashMap<String,String> row = rows.get(i);

                String pk_val = "";

                if(qUtil.check_where_condition(row, table)){
                    row.remove(table.getSet_lhs_column(), table.getSet_rhs_value());
                    row.put(table.getSet_lhs_column(), table.getSet_rhs_value());
                }

                List<String> primary_keys = table.getPrimary_keys();
                if(primary_keys!=null && primary_keys.size()>0){
                    for(String pk: primary_keys){
                        String val = row.get(pk);

                        //check for not null
                        if(val==null || val.trim().isEmpty()){
                            System.out.println("Primary key cannot be null");
                            return false;
                        }
                        pk_val+=row.get(val);
                    }
                }
                
                if(pk_values.contains(pk_val) && pk_val.length() > 1){
                    System.out.println("Primary key constraint. Unable to update with duplicate records.");
                    return false;
                }
                pk_values.add(pk_val);
                //end

                //check for unique
                List<String> unique = table.getUnique_columns();
                if(unique!=null && unique.size()>0){
                    for(String u: unique){
                        String val = row.get(u);

                        if(!column_unique_values.containsKey(u)){
                            column_unique_values.put(u, new ArrayList<>());
                        }
                        else{
                            if(column_unique_values.get(u).contains(val)){
                                System.out.println("unique key constraint");
                                return false;
                            }
                            column_unique_values.get(u).add(val);
                        }
                    }
                }
                //end

                //not null
                List<String> not_null_cols = table.getNot_null_columns();
                if(not_null_cols!=null && not_null_cols.size()>0){
                    for(String nn_col: not_null_cols){
                        String val = row.get(nn_col);
                        if(val==null || val.trim().isEmpty()){
                            System.out.println("not null constraint");
                            return false;
                        }
                    }
                }

                filtered_rows.add(row);
            }
            table.setValues(filtered_rows);
            return qUtil.insertData(table, workfolder_in_db, true, commitFlag, tx);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
