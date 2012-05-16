package com.google.gwt.proteasedb.server;

import com.google.gwt.proteasedb.client.DBConnection;
import com.google.gwt.proteasedb.client.ProteaseData;
import com.google.gwt.proteasedb.client.ProteaseDb;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.proteasedb.client.Input;
/**
 * server side class for Async calls
 * 
 * 
 * Make sure you add a reference library (external jar in build path) JDBC Connector - 
 * You will see I put it in /opt/classpath/mysql-connector-java-5.1.5/mysql-connector-java-5.1.5-bin.jar
 * 
 * @author branflake2267
 *
 */

public class MySQLConnection extends RemoteServiceServlet implements DBConnection {
	  /**
     * constructor
     */
    public MySQLConnection() {
    	
    }

	@Override
	public ProteaseData[] getProteaseInfo() throws Throwable {
		
		// TODO Auto-generated method stub
		DB_Protease db = new DB_Protease();
		Input input = new Input();

		ProteaseData[] proteaseData = db.getProteaseInfo(input);
		return proteaseData;
	}

}
