package transaction;

public class DefaultTransactionManager implements TransactionManager {

	public static Transaction getTransaction() {
		return new Transaction();
	}

	

}
