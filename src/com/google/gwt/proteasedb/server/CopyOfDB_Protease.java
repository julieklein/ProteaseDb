package com.google.gwt.proteasedb.server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gwt.proteasedb.client.ProteaseData;

/**
* I extend the DB_Conn abstract class, then I don't have to rewrite code
* 
* @author branflake2267
*
*/

public class CopyOfDB_Protease extends DB_Conn {
	
	 /**
     * constructor - nothing to do
     */
    public CopyOfDB_Protease() {
            // nothing to do
    }
    public ProteaseData[] getProteaseInfo() {
        
        String query = "SELECT * FROM PROTEASE WHERE P_Symbol = 'F12'";
        
        // prepare for rpc transport
        ProteaseData[] proteaseData = null;
        
try {
    Connection connection = getConn();
    Statement select = connection.createStatement();
    ResultSet result = select.executeQuery(query);
    
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
    connection.close();
    
} catch(Exception e) {
        
        System.err.println("Mysql Statement Error: " + query);
        e.printStackTrace();
        
}
        
// return the array
        return proteaseData;
}



}
