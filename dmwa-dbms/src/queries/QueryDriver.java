package queries;


import java.io.Console;

import common.Utility;
import login.User;
import queries.query_execution.QueryIdentifier;

public class QueryDriver {

    Console console;
    User user;

    public QueryDriver(User user){
        console = System.console();
        this.user = user;
    }

    public void run() {
        
        String single_or_transaction = Utility.enter_in_console("Select option 1/2/3: \n"
        +"1. Single Query\n"
        +"2. Transaction\n"
        +"3. See Query Guide", console);

        if(single_or_transaction.equals("1")){
            single_query_execution();
        }
        else if(single_or_transaction.equals("2")){
            transaction_execution();
        }
        else if(single_or_transaction.equals("3")){
            Guide.print_queries_for_reference();
            run();
        }
    }

    private void single_query_execution() {

        String query = Utility.enter_in_console("Enter query or press 1 to go back to menu: ", console);
        QueryIdentifier queryIdentifier= new QueryIdentifier(query, user);
        while(!query.equals("1")){
            queryIdentifier.run();
            query = Utility.enter_in_console("Enter query or press 1 to go back to menu: ", console);
            queryIdentifier.setQuery(query);
        }
        
    }

    private void transaction_execution() {
        
    }
    
}
