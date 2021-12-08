package transaction;

public class DefaultTransactionManager implements TransactionManager {

	
	/** 
	 * @return Transaction
	 */
	public static Transaction getTransaction() {
		return new Transaction();
	}

	

}
