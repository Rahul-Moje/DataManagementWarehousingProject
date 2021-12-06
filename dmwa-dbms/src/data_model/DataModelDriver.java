package data_model;

import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import common.RetrieveTableInfo;
import common.Utility;
import login.User;
import queries.query_execution.TableMetaData;
import queries.query_validation.ExistingDatabseValidation;

public class DataModelDriver {
    
    Console console;
    User user;
    String workspace_folder;
    String  dbName;

    public DataModelDriver(User user){
        console = System.console();
        this.user = user;
        this.workspace_folder = user.getUsername_encrypted();
    }


    public void run() {
        dbName= Utility.enter_in_console("Enter Database Name", console);
        ExistingDatabseValidation validator = new ExistingDatabseValidation();
        String error = validator.validate(dbName, workspace_folder);

        
        if(!Utility.is_not_null_empty(error)){
            export_DataModel();
        }
        else
        {
            System.out.println("\n\n\t\t\t*********** Error Occurred ************\t\t\t\n");
            System.out.println(error);
            System.out.println("\n\n\t\t\t*********** End ************\t\t\t\n");
        }
    }

    private void export_DataModel() {
        try {
            StringBuffer query=new StringBuffer(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now())+"\n"); 
            query.append("Database Name: "+dbName+"\n\n");
            List<TableMetaData> tableMetaList=RetrieveTableInfo.getTables(workspace_folder+"//"+dbName);
            for(TableMetaData tableMeta:tableMetaList)
            {
                String tableName=tableMeta.getTable_name(); 
                query.append(String.format("%s", "----------------------------------------------------------------------------------------------------------------------------\n"));
                query.append(String.format("%-1s %-20s %-1s %-97s %-1s","|", "Table Name", "|",tableName,"|\n"));
                query.append(String.format("%s", "----------------------------------------------------------------------------------------------------------------------------\n"));
                query.append(String.format("%-1s %-20s %-1s %-15s %-1s %-15s %-1s %-15s %-1s %-15s %-1s %-25s %-1s","|", "ColumnName", "|", "ColumnType", "|", "PrimaryKey","|","NotNull","|","Unique","|","ForeignKey","|\n"));
                query.append(String.format("%s", "----------------------------------------------------------------------------------------------------------------------------\n"));
                List<String> primary_keys=tableMeta.getPrimary_keys();
                List<String> unique_columns=tableMeta.getUnique_columns();
                List<String> not_null_columns=tableMeta.getNot_null_columns();
                HashMap<String,HashMap<String,String>> column_to_referencetable_to_column=tableMeta.getColumn_to_referencetable_to_column();
                HashMap<String,String> colInfo=tableMeta.getCol_datatype();

                System.out.println(colInfo);
               for (Map.Entry<String, String> entry : colInfo.entrySet()) 
                {
                    String columnName = entry.getKey();
                    String columnType = entry.getValue();
                    String isPrimaryKey="NO";
                    String isNotNull="NO";
                    String isUniqueKey="NO";
                    String isForeignKey="";

                    if(unique_columns.contains(columnName))
                    {
                        isUniqueKey="YES";
                    }   
                   if(not_null_columns.contains(columnName))
                    {
                        isNotNull="YES";
                    }
                    if(primary_keys.contains(columnName))
                    {
                        isPrimaryKey="YES";
                        isNotNull="YES";
                    }

                    for (Map.Entry<String,HashMap<String,String>> foreignEntry : column_to_referencetable_to_column.entrySet()) 
                    {
                        String foreignKeyName = foreignEntry.getKey();
                        if(foreignKeyName.equals(columnName))
                        {
                            HashMap<String,String> foreignKeyValue = foreignEntry.getValue();
                            for (Map.Entry<String, String> foreignTableInfo : foreignKeyValue.entrySet()) 
                            {
                                String parentTableName = foreignTableInfo.getKey();
                                String parentcolumnName = foreignTableInfo.getValue();
                                isForeignKey=isForeignKey+parentTableName+"("+parentcolumnName+") ";
                            }
                        }
                    }
                    if(isForeignKey=="")
                    {
                        isForeignKey="NO";
                    }

                    query.append(String.format("%-1s %-20s %-1s %-15s %-1s %-15s %-1s %-15s %-1s %-15s %-1s %-25s %-1s","|",columnName, "|",columnType, "|",isPrimaryKey,"|",isNotNull,"|",isUniqueKey,"|",isForeignKey,"|\n"));             
                }   
                query.append(String.format("%s", "----------------------------------------------------------------------------------------------------------------------------\n\n\n\n"));           
            }

            String path = ".//workspace//"+workspace_folder+"//"+dbName+"//ERExport";
            Utility.check_create_directory(path);
            String file_path = ".//workspace//"+workspace_folder+"//"+dbName+"//ERExport//"+dbName+"_"+System.currentTimeMillis()+".txt";
            Utility.check_create_file_path(file_path);
            FileWriter file_writer = new FileWriter(file_path, false);
            file_writer.write(query.toString());
            file_writer.flush();
            file_writer.close();  
            System.out.println("DataModel Export Successfully!! Check your workspace");     
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
}
