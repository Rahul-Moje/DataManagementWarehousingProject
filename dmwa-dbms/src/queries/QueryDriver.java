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
        +"1. Query\n"
        +"2. See Query Guide", console);

        if(single_or_transaction.equals("1")){
            query_execution();
        }
        // else if(single_or_transaction.equals("2")){
        //     transaction_execution();
        // }
        else if(single_or_transaction.equals("2")){
            Guide.print_queries_for_reference();
            run();
        }
    }

    private void query_execution() {

        String query="";
        QueryIdentifier queryIdentifier= new QueryIdentifier(query, user);;

        do{
            query= Utility.enter_in_console("Enter query or press 1 to go back to previous menu and 2 to go back to main menu:", console);
            if(query.equals("1")){
                run();
            }
            if(query.equals("2")){
                break;
            }
            if(!query.toLowerCase().startsWith("start trasaction")){
                queryIdentifier.setQuery(query);
                queryIdentifier.run();
                continue;
            }
            else{
                //transaction_execution
            }
        }
        while(!query.equals("1") && !query.equals("2"));
        
    }

    private void transaction_execution() {
        
    }
    
}
