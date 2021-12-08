package queries;


import java.io.Console;

import common.Utility;
import login.User;
import queries.query_execution.QueryIdentifier;
import transaction.DefaultTransactionManager;
import transaction.Transaction;

public class QueryDriver {

    Console console;
    User user;

    public QueryDriver(User user){
        console = System.console();
        this.user = user;
    }

    public void run() {
        
        String single_or_transaction = Utility.enter_in_console("Select option 1/2: \n"
        +"1. Query\n"
        +"2. See Query Guide", console);

        if(single_or_transaction.equals("1")){
            query_execution();
        }
        else if(single_or_transaction.equals("2")){
            Guide.print_queries_for_reference();
            run();
        }
    }

    private void query_execution() {

        String queryInput="";
        QueryIdentifier queryIdentifier= new QueryIdentifier(queryInput, user);;

        do{
            queryInput= Utility.enter_in_console("Enter query or press 1 to go back to previous menu and 2 to go back to main menu:", console);
            if(queryInput.equals("1")){
                run();
                break;
            }
            if(queryInput.equals("2")){
                break;
            }
            boolean isTxn = isQueryTransactional(queryInput);
            
            String[] queryList =  queryInput.split(";");
            
            Transaction tx = DefaultTransactionManager.getTransaction();
            for(int i =0; i< queryList.length; i++) {
                String query = queryList[i];
                if(query == null || query.length() < 15  ) {
                    continue;
                }
                if(!isTxn){
                    queryIdentifier.setQuery(query);
                    queryIdentifier.run(true, tx);
                    continue;
                }
                else{

                    //transaction_execution
                    //Generate random number as transaction id
                    
                    //Loop over queryList and snd true for last query
                    queryIdentifier.setQuery(query);
                    //give true to last query from query list or else give false
                    if(i != queryList.length -1)
                    	queryIdentifier.run(false, tx);
                    else
                    	queryIdentifier.run(true, tx);
                }

            }
        }
        while(!queryInput.equals("1") && !queryInput.equals("2"));
        
    }

    
    /** 
     * @param query
     * @return boolean
     */
    private boolean isQueryTransactional(String query) {
        String[] queries =query.split(";");
        int dmlCount = 0;
        for(String q: queries) {
        	String tmp = q.toLowerCase();
        	if(tmp.startsWith("insert into")
        			|| tmp.startsWith("update")
        			|| tmp.startsWith("delete from"))
        		dmlCount++;
        	if(dmlCount >=2) return true;
        		
        }
        return false;
    }
    
}
