package transaction;

//author janhavisonawane b00881787
//author ruhityagi b00872269
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import common.Utility;

public final class Transaction {
	
	private final long transactionId;
	private final Map<String, String> tempData;
	
	public Transaction() {
		super();
		this.transactionId = Utility.generateTransaction();
		tempData = new LinkedHashMap<>();
	}
	
	public long getTransactionId() {
		return transactionId;
	}
	public Map<String, String> getTempData() {
		return tempData;
	}
	public void setTempData(String filePath, String data) {
		tempData.put(filePath, data);
	}
	
	public void commit() {
		for(Entry<String, String> e: tempData.entrySet()) {
			try {
				Utility.write(e.getKey(), e.getValue());
				
			} catch (IOException e1) {
				rollback();
				System.out.println("Rolling back transaction");
				e1.printStackTrace();
				break;
			}
		}
		tempData.clear();
	}
	
	public void rollback() {
		tempData.clear();
	}

}
