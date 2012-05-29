package com.google.gwt.proteasedb.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.proteasedb.client.PeptideData;
import com.google.gwt.proteasedb.client.ResultbySubstrateData;
import com.google.gwt.proteasedb.client.SubstrateData;
import com.google.gwt.proteasedb.client.ProteaseData;
import com.google.gwt.proteasedb.client.SearchRequest;
import com.sun.source.tree.NewClassTree;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

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

	public ResultbySubstrateData[] getResultbySubstrateInfo(SearchRequest[] searchRequest)
			throws Throwable {

		String querySubstrate = "SELECT * FROM SUBSTRATE WHERE S_Symbol = ?";
		
		
		ResultbySubstrateData[] firstcapacityarray = null;
		ResultbySubstrateData[] intermediatecapacityarray = null;
		
		int k = 0;
		ResultbySubstrateData[] lastcapacityarray = new ResultbySubstrateData[k];
		
		
			
		for (SearchRequest searchReq : searchRequest) {		
	    String request = searchReq.getInput();
	    
		Connection connection = getConn();
		PreparedStatement ps = connection.prepareStatement(querySubstrate);
//		ResultbySubstrateData[] resultbySubstrateDataintermediate = null;
		SubstrateData substrate = new SubstrateData();

		String suni = null;

		try {
			ps.setString(1, request);
			// Statement select = connection.createStatement();
			ResultSet result = ps.executeQuery();
			int rsSize = getResultSetSize(result); 
			
			if(rsSize==0){
				firstcapacityarray = new ResultbySubstrateData[1];
				firstcapacityarray[0] = new ResultbySubstrateData();			
				firstcapacityarray[0].setSubstrate(substrate);
				ProteaseData protease = new ProteaseData();
				firstcapacityarray[0].setProtease(protease);
				firstcapacityarray[0].setEntryValidity("Sorry, there is no information about "+request+" in the database");	
				System.out.println(firstcapacityarray[0].getEntryValidity());
				
			}else{
			while (result.next()) {
				suni = result.getString("S_UniprotId");
				substrate.S_NL_Name = result.getString("S_NL_Name");
				substrate.S_Symbol = result.getString("S_Symbol");
				substrate.S_Uniprotid = suni;
				System.out.println(suni);

			}
			Connection connection2 = getConn();
			String queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE S_UniprotId = ? ORDER BY P1, P_Symbol";
			PreparedStatement ps2 = connection2.prepareStatement(queryCleavagesite);
			

			try {
				ps2.setString(1, suni);
				System.out.println(ps2);
				// Statement select = connection.createStatement();
				ResultSet result2 = ps2.executeQuery();

				// init object into the size we need, like a recordset
				int rsSize2 = getResultSetSize(result2); // size the array
				int sizefirstarray = rsSize2 + k;
				firstcapacityarray = new ResultbySubstrateData[sizefirstarray];
				System.arraycopy(lastcapacityarray, 0, firstcapacityarray, 0, k);
				System.out.println(sizefirstarray);
				
				if(rsSize2>0){

				int i = 0;

				while (result2.next()) {
					ProteaseData protease = new ProteaseData();
					firstcapacityarray[i] = new ResultbySubstrateData();
					firstcapacityarray[i].setSubstrate(substrate);
					firstcapacityarray[i].cleavageSite = result2
							.getString("CleavageSite_Sequence");
					firstcapacityarray[i].p1 = result2.getInt("P1");
					firstcapacityarray[i].p1prime = result2.getInt("P1prime");
					firstcapacityarray[i].setEntryValidity("xxxxx");
					firstcapacityarray[i].setNature("cleavagesite");
					System.out.print(firstcapacityarray[i].getEntryValidity());
					String puni = result2.getString("P_UniprotId");
					protease.P_Uniprotid = puni;
					
					Connection connection3 = getConn();
					String queryProtease = "SELECT * FROM PROTEASE WHERE P_UniprotID = ?";
					PreparedStatement ps3 = connection3
							.prepareStatement(queryProtease);

					try {
						ps3.setString(1, puni);
						System.out.println(ps3);
						// Statement select = connection.createStatement();
						ResultSet result3 = ps3.executeQuery();
						while (result3.next()) {
							protease.P_NL_Name = result3.getString("P_NL_Name");
							protease.P_Symbol = result3.getString("P_Symbol");
							protease.P_Ecnumber = result3.getString("P_EC_Number");
							firstcapacityarray[i].setProtease(protease);
						}
						result3.close();
						ps3.clearParameters();
						ps3.close();
						connection3.close();
					} catch (Throwable ignore) {

						System.err.println("Mysql Statement Error: "
								+ queryProtease);
						ignore.printStackTrace();

					}

					i++;
				}
				// clean up
					//i = i+rsSize2;
					System.out.println(sizefirstarray +"quiquiquisontlessnorkis");
							result2.close();
							ps2.clearParameters();
							ps2.close();
							connection2.close();
							
							
							
							Connection connection5 = getConn();
							String queryPeptide = "SELECT * FROM PEPTIDE WHERE S_UniprotId = ? ORDER BY Pd_Start, Pd_Regulation";
							PreparedStatement ps5 = connection5.prepareStatement(queryPeptide);
							

							try {
								ps5.setString(1, suni);
								System.out.println(ps5);
								// Statement select = connection.createStatement();
								ResultSet result5 = ps5.executeQuery();

								// init object into the size we need, like a recordset
								int rsSize5 = getResultSetSize(result5); // size the array
								//resultbySubstrateData = new ResultbySubstrateData[rsSize2];
								System.out.println(rsSize5+"quiquiquismaisqui");
								
								if(rsSize5>0) {
								k = sizefirstarray + rsSize5;
								System.out.println(k+"pas les schtroumfs");
								}else {
								k = sizefirstarray + 1;
								System.out.println(k+"pas les schtroumfs");
								}
								lastcapacityarray = new ResultbySubstrateData[k];
								
								System.arraycopy(firstcapacityarray, 0, lastcapacityarray, 0, firstcapacityarray.length);
								
								i=firstcapacityarray.length;
								
								if(rsSize5>0){

								while (result5.next() && i<k) {
									lastcapacityarray[i] = new ResultbySubstrateData();
									SubstrateData substrate2 = new SubstrateData();
									substrate2.S_Symbol = result5.getString("S_Symbol");
									System.out.println(result5.getString("S_Symbol")+"ni bob");
									lastcapacityarray[i].setSubstrate(substrate2);
									PeptideData peptide = new PeptideData();
									ProteaseData protease= new ProteaseData();
									lastcapacityarray[i].setProtease(protease);	
									String disease = result5.getString("Pd_Disease");
									peptide.disease = disease;
									System.out.println(disease);
									peptide.regulation = result5.getString("Pd_Regulation");
									peptide.start = result5.getInt("Pd_Start");
									peptide.end = result5.getInt("Pd_End");
									lastcapacityarray[i].setPeptide(peptide);
									lastcapacityarray[i].setEntryValidity("xxxxx");
									lastcapacityarray[i].setNature("peptide");
									i++;
		
								}
								}else {
									
									lastcapacityarray[i] = new ResultbySubstrateData();
									lastcapacityarray[i].setNature("peptide");
									lastcapacityarray[i].setEntryValidity("Sorry, there is no peptide for this substrate in the database");
									System.out.print("ttttt");
									

								}
							
								result5.close();
								ps5.clearParameters();
								ps5.close();
								connection5.close();
								
							} catch (Throwable ignore) {
								System.err.println("Mysql Statement Error: " + queryPeptide);
								ignore.printStackTrace();

							}
							
							
				}else{
					
					Connection connection4 = getConn();
					PreparedStatement ps4 = connection4.prepareStatement(querySubstrate);
					
					SubstrateData substrate2 = new SubstrateData();
					ProteaseData protease2 = new ProteaseData();
					
					try {
						ps4.setString(1, request);
						System.out.println(ps4);
						// Statement select = connection.createStatement();
						ResultSet result4= ps4.executeQuery();
						int rsSize4 = getResultSetSize(result4); // size the array
						firstcapacityarray = new ResultbySubstrateData[rsSize4];
						System.out.println(rsSize4);

					
						while (result4.next()) {
							firstcapacityarray[0] = new ResultbySubstrateData();
							substrate2.S_Uniprotid = result4.getString("S_UniprotId");							
							substrate2.S_NL_Name = result4.getString("S_NL_Name");
							substrate2.S_Symbol = result4.getString("S_Symbol");
							firstcapacityarray[0].setSubstrate(substrate2);							
							firstcapacityarray[0].setProtease(protease2);
							firstcapacityarray[0].setNature("cleavagesite");
							firstcapacityarray[0].setEntryValidity("Sorry, there is no cleavage site for this substrate in the database");
							System.out.print("wwwww");
						}
						
						// clean up
						result4.close();
						ps4.clearParameters();
						ps4.close();
						connection4.close();

					} catch (Throwable ignore) {
						System.err.println("Mysql Statement Error: " + querySubstrate);
						ignore.printStackTrace();

					}
					
				}			
				
				
			} catch (Throwable ignore) {

				System.err.println("Mysql Statement Error: " + queryCleavagesite);
				ignore.printStackTrace();

			}
			// clean up
			result.close();
			ps.clearParameters();
			ps.close();
			connection.close();
			}

		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + querySubstrate);
			ignore.printStackTrace();

		}

		
		
		
		}	
		
		// return the array
		
		
		return lastcapacityarray;
	
		
	}
		
	}

