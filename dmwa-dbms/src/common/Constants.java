package common;

public class Constants {

    public static final String DELIMITER = "~";
    public static final String DATA_FILE_EXTENSION = ".tsv";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String USER_ROFILE_PATH = ".//metadata//USER_PROFILE.txt";
    public static final String SYSTEM_LOG_FILE_PATH = ".//workspace//%s//logs//system.logs";
    public static final String TABLE_INFO_FILE_PATH = ".//workspace//%s//metadata//table_info"+DATA_FILE_EXTENSION;
    public static final String TABLE_INFO_HEADERS = String.format("table_name%scolumns%sdata_types%sprimary_keys%sunique_keys%snot_null_keys%sforeign_key", 
    Constants.DELIMITER, Constants.DELIMITER,Constants.DELIMITER,Constants.DELIMITER,Constants.DELIMITER,Constants.DELIMITER);

    
    
}
