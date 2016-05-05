
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class DBConn {
	
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
	
	DBExecute myExecute = new DBExecute();

	
	public int execSQL_returnint(String SQL)
	{
		try {
			//Step 1. Connection to the db
			conn = DriverManager.getConnection(dbUrl);
		
			// Create statement object
			stmt = conn.createStatement();
		
			// 3. Execute SQL query
			rs = stmt.executeQuery(SQL);
			
			//4. Process result set
			while (rs.next()){
				
				count = rs.getInt("count");
				//System.out.println(count);
				return count;
			}
		}
		
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			//close(myConn, myStmt, myRS);
			if (rs   != null) try { rs.close();   } catch(Exception e) {}
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}
			if (conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return count;
			
	}
	
	
	public String execSQL_returnString(String SQL)
	{
		try {
			//Step 1. Connection to the db
			conn = DriverManager.getConnection(dbUrl);
		
			// Create statement object
			stmt = conn.createStatement();
		
			// 3. Execute SQL query
			rs = stmt.executeQuery(SQL);
			
			//4. Process result set
			while (rs.next()){
				
				code = rs.getString("code");
				//System.out.println(count);
				return code;
			}
		}
		
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			//close(myConn, myStmt, myRS);
			if (rs   != null) try { rs.close();   } catch(Exception e) {}
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}
			if (conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return code;
		
		
	}
	
	
	public int getRuleCount(){
		//Returns the number of rule IDs in the database
				
		SQL = "select COUNT(distinct Rule_ID) count from rcmods.Rule_sheet";
	
		count = execSQL_returnint(SQL);
		return count;
	}
	
	public String getRuleResult(int RuleID, int RuleTypeCount){
		
		String rs1 = "";
		
		return rs1;
	}

	public int getRuleTypeNums(int ruleID) {
		// Get the number of rule types for a given RuleID
		
		SQL = "select count(distinct Right_Rule_Type_ID) count from rcmods.Rule_Sheet where Rule_ID = " + ruleID;

		count = execSQL_returnint(SQL);
		return count;
		
	}

	public int getRuleType(int ruleNum, int RuleTypeNum) {
		// Gets the Right Rule Type ID 
		int RuleTypeID;
		
		SQL = "select distinct Right_Rule_Type_ID count from rcmods.Rule_Sheet where Rule_ID = " + 
				ruleNum + " and Rule_Type_Number = " + RuleTypeNum;
		
		RuleTypeID = execSQL_returnint(SQL);
		
		return RuleTypeID;

	}
	
	public int getLeftRuleType(int ruleNum) {
		// Gets the Rule Type ID 
		int LeftRuleTypeID;
		
		SQL = "select distinct Left_Rule_Type_ID count from rcmods.Rule_Sheet_Left where Rule_ID = " + ruleNum;
		
		LeftRuleTypeID = execSQL_returnint(SQL);
		
		return LeftRuleTypeID;

	}

	public void ExecRule_RuleTypeNum(int ruleID, int RuleTypeNumber) {
		// TODO Auto-generated method stub
		
		//Get the Rule Type for the given Rule ID and Rule Type Number
		int RightRuleType;
		
		RightRuleType = getRuleType(ruleID, RuleTypeNumber);
		
		// Construct SQL for the Rules
		if ((RightRuleType == 1) || (RightRuleType == 2))// || (RuleType == 3)) 
		{
			ExecRule_123_Left(ruleID, RightRuleType, RuleTypeNumber);
		}
	}
	
	public void ExecRule_123_Left(int ruleID, int RightRuleType, int RuleTypeNumber)
	{
		
		int dummy = 1;
		System.out.println();
		
		Claims = "(";
	
		SQL = getSQL_Left(ruleID, RightRuleType, RuleTypeNumber);
				
					
			//System.out.println(SQL);
			try {
				//Step 1. Connection to the db
				conn = DriverManager.getConnection(dbUrl);
			
				// Create statement object
				stmt = conn.createStatement();
			
				// 3. Execute SQL query
				rs = stmt.executeQuery(SQL);
				
				//4. Process result set
				while (rs.next()){
					
					int clm_id = rs.getInt("CLM_ID");//.getString("count");
					System.out.println(clm_id);
					if (dummy != 1)
						Claims = Claims + ", " + clm_id;
					else Claims = Claims + clm_id;
					++dummy;
				}
				Claims = Claims + ")";
				System.out.println(Claims);
			}
			
			// Handle any errors that may have occurred.
			catch (Exception e) {
				e.printStackTrace();
			}
		
			
			finally {
				//close(myConn, myStmt, myRS);
				if (rs   != null) try { rs.close();   } catch(Exception e) {}
				if (stmt != null) try { stmt.close(); } catch(Exception e) {}
				if (conn != null) try { conn.close(); } catch(Exception e) {}
			}
			
			ExecRule_1_Right(Claims, ruleID, RightRuleType, RuleTypeNumber);
			
		}

	private String getSQL_Left(int ruleID, int ruleType, int ruleTypeNumber) {
		// TODO Auto-generated method stub
		System.out.println("Rule ID is : " + ruleID);
		System.out.println("Right Rule Type is : " + ruleType);
		
		//Get the LeftRuleTypeID
		int lefttype;
		lefttype = getLeftRuleType(ruleID);
		
		if (lefttype == 1) 
		{
			SQL = 	"select distinct CLM_ID CLM_ID " + 
					"from rcmdw.FACT_CLAIM_DETAIL " +
					"where CLM_ID in " +
					"(select CLM_ID " + 
					"from rcmdw.FACT_CLAIM_DETAIL " + 
					"where CPT_CODE = " +  
					"(select distinct Rule_Primary_Code " + 
					"from rcmods.Rule_Sheet_Left " + 
					"where Rule_ID = " + ruleID + 
					//" and Rule_Type_ID = " + ruleType + 
					") " + 
					"group by CLM_ID " + 
					"having COUNT(CPT_CODE_ID) = 2)";
		}
		else if (lefttype == 2)
		{
			SQL = 	"select distinct a11.CLM_ID CLM_ID " +
					"from " + 
					"(select CLM_ID " + 
					"from rcmdw.FACT_CLAIM_DETAIL " + 
					"where CPT_CODE in " + 
					"(select Rule_Primary_Code " + 
					"from rcmods.Rule_Sheet_Left " +
					"where Rule_ID = " + ruleID + " and " +
					"Rule_Left_Line_ID = 1) " +
					"group by CLM_ID " +
					"having COUNT(CPT_CODE) = 1) a11 " +
					"join " +
					"(select CLM_ID " +
					"from rcmdw.FACT_CLAIM_DETAIL " +
					"where CPT_CODE in " +
					"(select Rule_Primary_Code " +
					"from rcmods.Rule_Sheet_Left " + 
					"where Rule_ID = " + ruleID + " and " +
					"Rule_Left_Line_ID = 2) " +
					"group by CLM_ID " +
					"having COUNT(CPT_CODE) = 1) a12 on " +
					"(a11.CLM_ID = a12.CLM_ID)"; 
			System.out.println(SQL);
		}
		
		return SQL;
	}


	private void ExecRule_1_Right(String claims_list, int ruleID, int RightRuleType, int RuleTypeNumber) {
		// TODO Auto-generated method stub

		int Sub_Rule_count;
		String Flagged_Claims;
		
		// Get the number of sub rules for that ruleID and Rule Type
		SQL = "select max(Rule_Sub_ID)  count " + 
			  "from rcmods.Rule_sheet " + 
			  "where Rule_ID = " + ruleID  + " " +
			  "and Rule_Type_number = " + RuleTypeNumber;
		
		Sub_Rule_count = execSQL_returnint(SQL);
		
		System.out.println("Max Subrule is: " + Sub_Rule_count);
		
		// for each sub rule create SQL to flag claims
		for(int i = 1; i <= Sub_Rule_count; ++i)
		{
			// Get the code for which the claim should be checked if missing
			code = "";
			
			SQL = "select Missing_Value code " +
				  "from rcmods.Rule_sheet " +
				  "where Rule_ID = " + ruleID + " " +
				  "and Rule_Type_number = " + RuleTypeNumber + " " +
				  "and Rule_Sub_ID = " + i;

			System.out.println(SQL);
			code = execSQL_returnString(SQL);
			System.out.println("Missing Code is: "+ code);
			System.out.println("RightRuleType is: " + RightRuleType);
			System.out.println("Sub_Rule_ID is: " + i);
			
			// Conduct the check based on the Right Rule Type
			if (RightRuleType ==1)
			{
				SQL = "select a11.CLM_ID CLM_ID " +
					  "from rcmdw.FACT_CLAIM_DETAIL a11 " +
					  "where a11.CPT_Code in ('" +  
					  code + "') " +
					  "and CLM_ID in " +
					  claims_list + " " +
					  "group by a11.CLM_ID " +
					  "having COUNT(a11.CPT_CODE) = 1";
				//System.out.println(SQL);
			}
			else if (RightRuleType ==2)
			{
				
				SQL = "select distinct a11.CLM_ID " +
					  "from rcmdw.FACT_CLAIM_DETAIL a11 "+
					  "join " +
					  "(select CLM_ID " +
					  "from rcmdw.FACT_CLAIM_DETAIL " +
					  "where CPT_CODE in " + 
					  "(select Rule_Sub_Primary_Code1 " + 
					  "from rcmods.Rule_Sheet " +
					  "where Rule_ID = " + ruleID + " " + 
					  "and Rule_Sub_ID = " + i + ")) a12 on " +
					  "(a11.CLM_ID = a12.CLM_ID) " +
					  "where a11.CLM_ID in " +
					  claims_list + " and " + 
					  "a11.CLM_ID not in " +
					  "( " +  
					  "(select distinct CLM_ID " +
					  "from rcmdw.FACT_CLAIM_DETAIL " +
					  "where CPT_CODE in " + 
					  "(select Missing_Value " +
					  "from rcmods.Rule_Sheet " + 
					  "where Rule_ID = " + ruleID + " " + 
					  "and Rule_Sub_ID = " + i + ")))";

			}

			count = execFlagClaimSQL(ruleID, RightRuleType, RuleTypeNumber, code, i);
		}

	}

	private int execFlagClaimSQL(int ruleID, int RuleType, int RuleTypeNumber, String code, int Sub_Rule_Line) {
	// Get the list of claims that are flagged	

		System.out.println(SQL);
		int dummy;

		try {
			//Step 1. Connection to the db
			conn = DriverManager.getConnection(dbUrl);
		
			// Create statement object
			stmt = conn.createStatement();
		
			// 3. Execute SQL query
			rs = stmt.executeQuery(SQL);
			
			//4. Process result set
			while (rs.next()){
				
				int clm_id = rs.getInt("CLM_ID");//.getString("count");
				//System.out.println(clm_id);
				dummy = execInsertFlagClaim(clm_id, code, ruleID, Sub_Rule_Line);
			}

		}

		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}
	
		finally {
			//close(myConn, myStmt, myRS);
			if (rs   != null) try { rs.close();   } catch(Exception e) {}
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}
			if (conn != null) try { conn.close(); } catch(Exception e) {}
		}

		return 0;
	}


	private int execInsertFlagClaim(int clm_id, String code, int ruleID, int Sub_Rule_Line) {
		// Insert each claim into the flagged table
		
		SQL = "INSERT INTO rcmods.FACT_GIC_STG " +
			  "("+
			  "CLM_ID, " +
			  "RULE_ID, " +
			  "SUB_RULE_ID, " +
			  "CPT_CODE " + 
			  //"RUN_DATE," +
			  ") " +
			  "VALUES "+
			  "(" + clm_id + ", " + ruleID + ", " + Sub_Rule_Line + ", " + "'" + code + "')";// + ", select cast(GETDATE() as DATE))";
		
		System.out.println(SQL);
		//return 0;

		try {
			//Step 1. Connection to the db
			conn = DriverManager.getConnection(dbUrl);
		
			// Create statement object
			stmt = conn.createStatement();
		
			// 3. Execute SQL query
			stmt.executeUpdate(SQL);
			
		}
		
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}
	
		
		/*finally {
			//close(myConn, myStmt, myRS);
			if (rs   != null) try { rs.close();   } catch(Exception e) {}
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}
			if (conn != null) try { conn.close(); } catch(Exception e) {}
		}*/

		return 0;
	}

}