package queries;


import java.io.Console;

import common.Utility;

public class QueryDriver {

    Console console;

    public QueryDriver(){
        console = System.console();
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

    private void transaction_execution() {
    }

    private void single_query_execution() {
    }
    
}
