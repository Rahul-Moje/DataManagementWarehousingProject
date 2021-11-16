package queries;

public class Guide {
    
    public static void print_queries_for_reference(){

        System.out.println("\n\n\t\t\t*********** Database Queries ************\t\t\t\n");


        String database_queries = "Query to: \n\n"
                        +"1. Create database : create database <database_name>;\n\n"
                        +"2. Use database : use database <database_name>;\n\n"
                        +"3. Create table : create table <table_name> (<column1> text, "
                        +"<column2> int, <column3> float, <column3> date);\n\n"
                        +"4. Insert value in table: insert : insert into <table_name> values (\"Rita\", 100, 100, 2021-01-01);\n\n"
                        +"5. Fetch data: select */<coma_separated_column_names> from <table_name> where rank=1;\n\n"
                        +"6. Update a column: update <table_name> set <column1>=\"Seema\" where <column1>=\"Rita\";\n\n"
                        +"7. Delete a row: delete from <table_name> where <column1>=\"Rita\";\n\n"
                        +"8. Drop table: drop table <table_name>;\n\n";
        
        System.out.println(database_queries);

    }

}
