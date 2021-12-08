package transaction;
//author ruhityagi b00872269
//author janhavisonawane b00881787
public class DefaultTransactionManager implements TransactionManager {

	public static Transaction getTransaction() {
		return new Transaction();
	}

	

}
