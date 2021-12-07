package export;
import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.RetrieveTableInfo;
import common.Utility;
import login.User;
import queries.query_execution.TableMetaData;
import queries.query_validation.ExistingDatabseValidation;

public class ExportDriver {
    
    Console console;
    User user;
    String workspace_folder;
    String  dbName;

    public ExportDriver(User user){
        console = System.console();
        this.user = user;
        this.workspace_folder = user.getUsername_encrypted();
    }

    public void run() {
        dbName= Utility.enter_in_console("Enter Database Name", console);
        ExistingDatabseValidation validator = new ExistingDatabseValidation();
        String error = validator.validate(dbName, workspace_folder);

            if(!Utility.is_not_null_empty(error)){
            
                String selection_for_export = Utility.enter_in_console("Select option 1/2: \n"
                +"1. Export Structure\n"
                +"2. Export Structure And Value", console);
        
                if(selection_for_export.equals("1")){
                    export_SQLDump("Structure");
                }
                else if(selection_for_export.equals("2")){
                    export_SQLDump("WithData");
                }

            }
            else
            {
                System.out.println("\n\n\t\t\t*********** Error Occurred ************\t\t\t\n");
                System.out.println(error);
                System.out.println("\n\n\t\t\t*********** End ************\t\t\t\n");
            }
    }

    private void export_SQLDump(String exportType) {
        try {
           String query="CREATE DATABASE "+dbName+";\n";
           query=query+"USE "+dbName+";\n";
            List<TableMetaData> tableMetaList=RetrieveTableInfo.getTables(workspace_folder+"//"+dbName); 
            for(TableMetaData tableMeta:tableMetaList)
            {
                String tableName=tableMeta.getTable_name();
                ArrayList<String> columnTypeList=new ArrayList<String>();
                ArrayList<String> columnNameList=new ArrayList<String>();
                HashMap<String,String> colInfo=tableMeta.getCol_datatype();
                query=query+"CREATE TABLE "+tableName+"(";

                List<String> primary_keys=tableMeta.getPrimary_keys();
                List<String> unique_columns=tableMeta.getUnique_columns();
                List<String> not_null_columns=tableMeta.getNot_null_columns();
                HashMap<String,HashMap<String,String>> column_to_referencetable_to_column=tableMeta.getColumn_to_referencetable_to_column();

                for (Map.Entry<String, String> entry : colInfo.entrySet()) 
                {
                    String columnName = entry.getKey();
                    String columnType = entry.getValue();
                    query=query+columnName+" "+columnType;

                    if(unique_columns.contains(columnName))
                    {
                       query=query+" "+"UNIQUE";
                    }   
                   if(not_null_columns.contains(columnName)|| primary_keys.contains(columnName))
                    {
                        query=query+" "+"NOT NULL";
                    }                    
                    query=query+",";
                    columnTypeList.add(columnType);
                    columnNameList.add(columnName);   
                }  
                if(primary_keys.size()>0)
                {
                    String primaryKey=primary_keys.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "", ""));
                    query=query+"PRIMARY KEY ("+primaryKey+"),";
                }

                for (Map.Entry<String,HashMap<String,String>> foreignEntry : column_to_referencetable_to_column.entrySet()) 
                {
                    String foreignKeyName = foreignEntry.getKey();
                    query=query+"FOREIGN KEY ("+foreignKeyName+") REFERENCES ";
                    HashMap<String,String> foreignKeyValue = foreignEntry.getValue();
                    for (Map.Entry<String, String> foreignTableInfo : foreignKeyValue.entrySet()) 
                    {
                        String parentTableName = foreignTableInfo.getKey();
                        String parentcolumnName = foreignTableInfo.getValue();
                        query=query+parentTableName+"("+parentcolumnName+"),";
                    }
                }
                
                query=query.substring(0,query.length()-1);
                if(exportType=="Structure")
                {
                  query=query+");\n";
                }
                else
                {
                  query=query+");\n"+get_InsertedValues(tableName,columnNameList,columnTypeList);
                }
            }

            String path = ".//workspace//"+workspace_folder+"//"+dbName+"//Export";
            Utility.check_create_directory(path);
            String file_path = ".//workspace//"+workspace_folder+"//"+dbName+"//Export//"+dbName+"_"+System.currentTimeMillis()+".sql";
            Utility.check_create_file_path(file_path);
            FileWriter file_writer = new FileWriter(file_path, false);
            file_writer.write(query);
            file_writer.flush();
            file_writer.close();  
            System.out.println("Export Successfully!! Check your workspace");     
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String get_InsertedValues(String tableName,ArrayList<String> columnNameList,ArrayList<String> columnTypeList)
    {
        String file_name=".//workspace//"+workspace_folder+"//"+dbName+"//"+tableName+".tsv";
        try {
            String file_content_str= Utility.fetch_file_content(file_name);
            if(file_content_str!=null)
            {
                String[] tableData = file_content_str.split(System.getProperty("line.separator"));
                String full_InsertQuery="";
                for(int i=1;i<tableData.length;i++)
                {             
                    String[] rowData=tableData[i].split("~");
                    full_InsertQuery=full_InsertQuery+get_RowDataFormated(tableName,rowData,columnNameList,columnTypeList);
                }   
                return full_InsertQuery;
            }
            else
            {
                return "";
            }

        } catch (IOException e) {
            return e.getMessage();
        }   
    }
    private String get_RowDataFormated(String tableName,String[] rowData,ArrayList<String> columnNameList,ArrayList<String> columnTypeList)
    {
        String inset_Query="INSERT INTO "+tableName+"(";
        String insert_Values="VALUES(";
        for(int i=0;i<rowData.length;i++)
        {
            String val=rowData[i]; 
            String columnName=columnNameList.get(i);
            String dataType=columnTypeList.get(i);
            inset_Query=inset_Query+columnName+",";
            if(dataType.equals("nvarchar") || dataType.equals("date"))
            {               
                insert_Values=insert_Values+"'"+val+"',";
            }
            else
            {
                insert_Values=insert_Values+val+",";
            }      
        }
        inset_Query=inset_Query.substring(0,inset_Query.length()-1);
        inset_Query=inset_Query+") ";
        insert_Values=insert_Values.substring(0,insert_Values.length()-1);
        insert_Values=insert_Values+");";
       return inset_Query=inset_Query+insert_Values+"\n";

    }

}
