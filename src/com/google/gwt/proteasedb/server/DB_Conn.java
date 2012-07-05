package com.google.gwt.proteasedb.server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * create a db_conn - this is just an example of one way to do this.
 * NOTE: on class "abstract" - you have to create subclasses from this class to use this class in other classes. 
 * Delete abstract if you want to use this class directly.
 * @author branflake2267
 *
 */

public class DB_Conn {

	 
     /**
      * Constructor
      */
	 public DB_Conn() {
	 }
	 
	 protected Connection getConn() {
		 Connection conn = null;
	 
		 try {
		        String host = "jdbc:mysql://localhost:3306/";
		        String dbName = "Proteasix";
		        String usermame = "root";
		        String pwd = "kschoicesql";
		        conn = DriverManager.getConnection(host + dbName + "?user=" + usermame +"&password=" + pwd);
        
		        System.out.println("Connection Success");
		          
	          } catch (Exception e) {
                
                // error
                System.err.println("Mysql Connection Error: ");
                
                // for debugging error
                e.printStackTrace();
            }
        
            return conn;

	    }
	 
	 protected static int getResultSetSize(ResultSet resultSet) {
         int size = -1;

         try {
             resultSet.last();
             size = resultSet.getRow();
             resultSet.beforeFirst();
         } catch(SQLException e) {
             return size;
         }

         return size;
     }

}

