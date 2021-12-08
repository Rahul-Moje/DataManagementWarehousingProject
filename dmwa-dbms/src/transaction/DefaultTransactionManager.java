package transaction;

//author janhavisonawane b00881787
//author ruhityagi b00872269
public class DefaultTransactionManager implements TransactionManager {

	public static Transaction getTransaction() {
		return new Transaction();
	}

	

}
