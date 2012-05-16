package com.google.gwt.proteasedb.server;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



import com.google.gwt.proteasedb.client.ProteaseData;
import com.google.gwt.proteasedb.client.Input;

/**
* I extend the DB_Conn abstract class, then I don't have to rewrite code
* 
* @author branflake2267
*
*/

public class DB_Protease extends DB_Conn {
	
	 /**
     * constructor - nothing to do
     */
    public DB_Protease() {
            // nothing to do
    }
    public ProteaseData[] getProteaseInfo(Input input) throws Throwable {
        
        String query = "SELECT * FROM PROTEASE WHERE P_Symbol = ?";
        String symbol = input.getInput();
        System.out.println(symbol);
        Connection connection = getConn();
        PreparedStatement ps = connection.prepareStatement(query);
        
        // prepare for rpc transport
        ProteaseData[] proteaseData = null;
        
try {
	
    ps.setString(1, symbol);
    
//    Statement select = connection.createStatement();
    ResultSet result = ps.executeQuery();
    
 // init object into the size we need, like a recordset
    int rsSize = getResultSetSize(result); //size the array
    proteaseData = new ProteaseData[rsSize];

    	int i = 0;

        while (result.next()) { 
        proteaseData[i] = new ProteaseData();
        proteaseData[i].P_NL_Name = result.getString("P_NL_Name");
        proteaseData[i].P_Symbol = result.getString("P_Symbol");
        proteaseData[i].P_Uniprotid = result.getString("P_UniprotID");
        proteaseData[i].P_Ecnumber = result.getString("P_EC_Number");
        
        i++;

    }
    
    // clean up
    result.close();
    ps.close();
    connection.close();
    
    
} catch(Throwable ignore) {
        
        System.err.println("Mysql Statement Error: " + query);
        ignore.printStackTrace();
        
}
        
// return the array
        return proteaseData;
}



}
