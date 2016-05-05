import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBExecute {

	/*  Purpose of this class is to be the DB Connection and Execution class
	*	The config info will be stored in a config file which this class will be able to read
	*	As a result, the program will be environment independent
	*	Will need to make the program environment aware
	*/
	
	private String dbName = "RevAscent_LIPS";
	private String user = "sa";
	private String pass = "";
	
	private String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";user=" + user + ";password=" + pass;

	private String SQL; 
	private String Claims = "(";
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs= null;
	
	private int count = 0;
	private String code;
}
