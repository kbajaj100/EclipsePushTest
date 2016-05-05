
import java.sql.*;

public class MainClass {
		
	// In the main function, create a database connection to get the number of rules.
	// This can be done by creating a static function 
	// Create a for loop for the where i<= number of rules
	// In the for loop call a function, pass the rule number
	// the function will receive the rule number
	// Get the rule type for the rule
	// Create a method in the DBConn class to execute for that particular rule type
	
	public static void main(String[] args){
		
	
		DBConn myconn = new DBConn();
			
		int rulecount = 4;
		int dummy; 
		
		//myconn.getRuleCount();
		System.out.println(rulecount);
		
		for (int i = 1; i<= rulecount; ++i) //i is the RuleID
			dummy = ExecuteRule(i, myconn); //Will execute each rule by sending the Rule ID
	}

	private static int ExecuteRule(int RuleID, DBConn myconn) {
		// TODO Auto-generated method stub
		
		//int RuleType;
		int RuleTypeCount; // Number of Rule types in the rule
		
		RuleTypeCount = myconn.getRuleTypeNums(RuleID);
		System.out.println("Rule ID is: " + RuleID);
		System.out.println("Rule Type Count is: " + RuleTypeCount);
		
		for (int i = 1; i<= RuleTypeCount; ++i)
		{	
			myconn.ExecRule_RuleTypeNum(RuleID, i);	//i represents the Right Rule Type Number
		}
		
		return 1;
	}
}

