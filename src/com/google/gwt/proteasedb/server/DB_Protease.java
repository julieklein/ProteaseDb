package com.google.gwt.proteasedb.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.proteasedb.client.Loop;
import com.google.gwt.proteasedb.client.XPathNodeUniprot;
import com.google.gwt.proteasedb.client.XPathUniprotPep;
import com.google.gwt.proteasedb.client.ParseUniprotPep;
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

	public ResultbySubstrateData[] getResultbySubstrateInfo(
			SearchRequest[] searchRequest) throws Throwable {

		String querySubstrate = "SELECT * FROM SUBSTRATE WHERE S_Symbol = ?";

		ResultbySubstrateData[] firstcapacityarray = null;
		ResultbySubstrateData[] intermediatecapacityarray = null;

		int k = 0;
		ResultbySubstrateData[] lastcapacityarray = new ResultbySubstrateData[k];

		for (SearchRequest searchReq : searchRequest) {
			String request = searchReq.getInput();

			Connection connection = getConn();
			PreparedStatement ps = connection.prepareStatement(querySubstrate);
			// ResultbySubstrateData[] resultbySubstrateDataintermediate = null;
			SubstrateData substrate = new SubstrateData();

			int kFirst = 0;
			int kInter = 0;
			int kLast = 0;

			String substratesymbol = null;
			String substrateuni = null;
			String substratename = null;
			
			try {
				ps.setString(1, request);
				// Statement select = connection.createStatement();
				ResultSet result = ps.executeQuery();
				int rsSize = getResultSetSize(result);

				if (rsSize == 0) {
					firstcapacityarray = new ResultbySubstrateData[1];
					firstcapacityarray[0] = new ResultbySubstrateData();
					firstcapacityarray[0].setSubstrate(substrate);
					ProteaseData protease = new ProteaseData();
					firstcapacityarray[0].setProtease(protease);
					firstcapacityarray[0]
							.setEntryValidity("Sorry, there is no information about "
									+ request + " in the database");
					System.out
							.println(firstcapacityarray[0].getEntryValidity());

				} else {
					while (result.next()) {
						substrateuni = result.getString("S_UniprotId");
						substratename = result.getString("S_NL_Name");
						substratesymbol = result.getString("S_Symbol");
						substrate.S_NL_Name = substratename;
						substrate.S_Symbol = substratesymbol;
						substrate.S_Uniprotid = substrateuni;
						System.out.println(substrateuni);

					}
					Connection connection2 = getConn();
					String queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE S_UniprotId = ? ORDER BY P1, P_Symbol";
					PreparedStatement ps2 = connection2
							.prepareStatement(queryCleavagesite);

					try {
						ps2.setString(1, substrateuni);
						System.out.println(ps2);
						// Statement select = connection.createStatement();
						ResultSet result2 = ps2.executeQuery();

						// init object into the size we need, like a recordset
						int rsSize2 = getResultSetSize(result2); // size the
																	// array
						kFirst = rsSize2 + k;
						firstcapacityarray = new ResultbySubstrateData[kFirst];
						System.arraycopy(lastcapacityarray, 0,
								firstcapacityarray, 0, k);
						System.out.println(kFirst + "FirstCapArray");

						if (rsSize2 > 0) {

							int i = k;

							while (result2.next()) {
								ProteaseData protease = new ProteaseData();
								firstcapacityarray[i] = new ResultbySubstrateData();
								firstcapacityarray[i].setSubstrate(substrate);
								firstcapacityarray[i].cleavageSite = result2
										.getString("CleavageSite_Sequence");
								firstcapacityarray[i].p1 = result2.getInt("P1");
								firstcapacityarray[i].p1prime = result2
										.getInt("P1prime");
								firstcapacityarray[i].setEntryValidity("xxxxx");
								firstcapacityarray[i].setNature("cleavagesite");
								System.out.print(firstcapacityarray[i]
										.getEntryValidity());
								String puni = result2.getString("P_UniprotId");
								protease.P_Uniprotid = puni;

								Connection connection3 = getConn();
								String queryProtease = "SELECT * FROM PROTEASE WHERE P_UniprotID = ?";
								PreparedStatement ps3 = connection3
										.prepareStatement(queryProtease);

								try {
									ps3.setString(1, puni);
									System.out.println(ps3);
									// Statement select =
									// connection.createStatement();
									ResultSet result3 = ps3.executeQuery();
									while (result3.next()) {
										protease.P_NL_Name = result3
												.getString("P_NL_Name");
										protease.P_Symbol = result3
												.getString("P_Symbol");
										protease.P_Ecnumber = result3
												.getString("P_EC_Number");
										firstcapacityarray[i]
												.setProtease(protease);
									}
									result3.close();
									ps3.clearParameters();
									ps3.close();
									connection3.close();
								} catch (Throwable ignore) {

									System.err
											.println("Mysql Statement Error: "
													+ queryProtease);
									ignore.printStackTrace();

								}

								i++;
							}
							// clean up
							// i = i+rsSize2;

							result2.close();
							ps2.clearParameters();
							ps2.close();
							connection2.close();

							Connection connection5 = getConn();
							String queryPeptide = "SELECT * FROM PEPTIDE WHERE S_UniprotId = ? ORDER BY Pd_Start, Pd_Regulation";
							PreparedStatement ps5 = connection5
									.prepareStatement(queryPeptide);

							try {
								ps5.setString(1, substrateuni);
								System.out.println(ps5);
								// Statement select =
								// connection.createStatement();
								ResultSet result5 = ps5.executeQuery();

								// init object into the size we need, like a
								// recordset
								int rsSize5 = getResultSetSize(result5);
								// resultbySubstrateData = new
								// ResultbySubstrateData[rsSize2];
								System.out.println(rsSize5
										+ "quiquiquismaisqui");

								if (rsSize5 > 0) {
									kInter = kFirst + rsSize5;
									System.out.println(kInter
											+ "pas les schtroumfs");
								} 
								else {
									kInter = kFirst;
//									kInter = kFirst + 1;
									System.out.println(kInter
											+ "pas les schtroumfs");
								}

								// size
								// the
								// array
								System.out.println(kInter + "InterCapArry");
								intermediatecapacityarray = new ResultbySubstrateData[kInter];

								System.arraycopy(firstcapacityarray, 0,
										intermediatecapacityarray, 0, kFirst);

								i = kFirst;

								if (rsSize5 > 0) {

									while (result5.next() && i < kInter) {
										intermediatecapacityarray[i] = new ResultbySubstrateData();
										SubstrateData substrate2 = new SubstrateData();
										substrate2.S_Symbol = substratesymbol;
										intermediatecapacityarray[i]
												.setSubstrate(substrate2);
										PeptideData peptide = new PeptideData();
										ProteaseData protease = new ProteaseData();
										intermediatecapacityarray[i]
												.setProtease(protease);
										String disease = result5
												.getString("Pd_Disease");
										peptide.disease = disease;
										System.out.println(disease);
										peptide.regulation = result5
												.getString("Pd_Regulation");
										peptide.structure = "";
										peptide.start = result5
												.getInt("Pd_Start");
										peptide.end = result5.getInt("Pd_End");
										intermediatecapacityarray[i]
												.setPeptide(peptide);
										intermediatecapacityarray[i]
												.setEntryValidity("xxxxx");
										intermediatecapacityarray[i]
												.setNature("peptide");
										i++;

									}
								}
//								else {
//continue;
////									intermediatecapacityarray[i] = new ResultbySubstrateData();
//									intermediatecapacityarray[i]
//											.setNature("peptide");
//									intermediatecapacityarray[i]
//											.setEntryValidity("Sorry, there is no peptide for this substrate in the database");
//									System.out.print("ttttt");

//								}

								result5.close();
								ps5.clearParameters();
								ps5.close();
								connection5.close();

							} catch (Throwable ignore) {
								System.err.println("Mysql Statement Error: "
										+ queryPeptide);
								ignore.printStackTrace();

							}

							// Retrieve UNIPROT STRUCTURE PEPTIDES
							String UniprotURL = "http://www.uniprot.org/uniprot/"
									+ substrateuni + ".xml";
							ParseUniprotPep parser = new ParseUniprotPep();
							Document xml = parser.getXML(UniprotURL);
							xml.getXmlVersion();
							System.out.println(xml.getXmlVersion());
							System.out.println(xml.toString());
							String xmlstring = parser
									.getXMLasstring(UniprotURL);

							// RETRIEVE ENTRIES THAT ARE CHAIN OR PROPEPTIDE
							// INSIDE XML
							XPathUniprotPep XPather = new XPathUniprotPep();
							String xpathQuery = "/uniprot/entry[./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']|./feature[@type='domain']|./feature[@type='region of interest']|./feature[@type='short sequence motif']]";
							// GET ENTRIES THAT ARE CHAIN OR PROPEPTIDE NODELIST
							NodeList getNodeListbyXPath = XPather
									.getNodeListByXPath(xpathQuery, xml);

							if (getNodeListbyXPath.getLength() > 0) {

								// RETRIEVE ALL PEPTIDES IN SELECTED ENTRIES
								XPathNodeUniprot XPathNoder2 = new XPathNodeUniprot();
								String xpathQueryNode2 = "./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']|./feature[@type='domain']|./feature[@type='region of interest']|./feature[@type='short sequence motif']|./feature[@type='topological domain']|./feature[@type='transmembrane region']";
								// FOREACH RETRIEVE PEPTIDE TYPE AND DESCRIPTION
								// IN SELECTED PEPTIDES FEATURES (eg Chain,
								// C-domain 2)
								XPathNodeUniprot XPathNoder20 = new XPathNodeUniprot();
								String xpathQueryNode20 = "./@type";
								XPathNodeUniprot XPathNoder21 = new XPathNodeUniprot();
								String xpathQueryNode21 = "./@description";
								// FOREACH RETRIEVE START AA NUMBER IN SELECTED
								// PEPTIDE FEATURES
								XPathNodeUniprot XPathNoder30 = new XPathNodeUniprot();
								String xpathQueryNode30 = "./location/begin/@status|./location/begin/@position";
								XPathNodeUniprot XPathNoder3 = new XPathNodeUniprot();
								String xpathQueryNode3 = "./location/begin/@position";
								// FOREACH RETRIEVE END AA NUMBER IN SELECTED
								// PEPTIDE FEATURES
								XPathNodeUniprot XPathNoder40 = new XPathNodeUniprot();
								String xpathQueryNode40 = "./location/end/@status|./location/end/@position";
								XPathNodeUniprot XPathNoder4 = new XPathNodeUniprot();
								String xpathQueryNode4 = "./location/end/@position";
								Loop l1 = new Loop();

								// FOR EACH SELECTED ENTRIE (THAT WILL BE ONLY
								// ONE HERE BUT ANYWAY...)
								for (int j = 0; j < getNodeListbyXPath
										.getLength(); j++) {

									// GET ALL PEPTIDES
									NodeList getNodeListByXPathNoder2 = XPathNoder2
											.getNodeListByXPath(
													xpathQueryNode2,
													getNodeListbyXPath.item(j));

									// create new array with larger capacity
									System.out.println(getNodeListByXPathNoder2.getLength() + "number of peptide");
									
									kLast = kInter
											+ getNodeListByXPathNoder2
													.getLength();
									System.out.println(kLast + "LastCapArray");

									lastcapacityarray = new ResultbySubstrateData[kLast];
									System.arraycopy(intermediatecapacityarray,
											0, lastcapacityarray, 0, kInter);

									i = kInter;

									// FOR EACH PEPTIDE FEATURE OF SELECTED
									// ENTRIES
									for (int l = 0; l < getNodeListByXPathNoder2
											.getLength(); l++) {

										Node n = getNodeListByXPathNoder2
												.item(l);

										lastcapacityarray[i] = new ResultbySubstrateData();
										PeptideData peptide = new PeptideData();
										SubstrateData substrate2 = new SubstrateData();
										substrate2.S_NL_Name = substratename;
										substrate2.S_Symbol = substratesymbol;
										substrate2.S_Uniprotid = substrateuni;
										lastcapacityarray[i].setSubstrate(substrate2);
										lastcapacityarray[i].setEntryValidity("zzzzzzz");
										
										// PEPTIDE TYPE (eg Chain)
										NodeList getNodeListByXPathNoder20 = XPathNoder20
												.getNodeListByXPath(
														xpathQueryNode20, n);
										// PEPTIDE DESCRIPTION (eg C2 domain)
										NodeList getNodeListByXPathNoder21 = XPathNoder21
												.getNodeListByXPath(
														xpathQueryNode21, n);

										LinkedList<String> stringfromNodelist20 = l1
												.getStringfromNodelist(getNodeListByXPathNoder20);
										String string20 = stringfromNodelist20
												.getFirst();
										String stringx = string20
												.substring(0, 1)
												.toUpperCase()
												+ string20.substring(1);
										if (string20
												.equalsIgnoreCase("signal peptide")) {	
											peptide.structure = stringx;
											

										} else if (string20
												.equalsIgnoreCase("transit peptide")) {
											LinkedList<String> stringfromNodelist21 = l1
													.getStringfromNodelist(getNodeListByXPathNoder21);
											String string21 = stringfromNodelist21
													.getFirst();
											String stringy = "Transit peptide, "
													+ string21;
											peptide.structure = stringy;
											
										} else {
											LinkedList<String> stringfromNodelist21 = l1
													.getStringfromNodelist(getNodeListByXPathNoder21);
											if (stringfromNodelist21.isEmpty()) {
												
												peptide.structure = stringx;
												

											} else {
												String string21 = stringfromNodelist21
														.getFirst();
												String stringz = string21 +" ("+stringx+")";
												peptide.structure = stringz;
												
											}
										}
										
										//2 GET PEPTIDE START
					                    NodeList getNodeListbyXPathNoder30 = XPathNoder30.getNodeListByXPath(xpathQueryNode30, n);
					                    //2' 
					                    NodeList getNodeListbyXPathNoder3 = XPathNoder3.getNodeListByXPath(xpathQueryNode3, n);
										
										
										 //3 GET PEPTIDE END
					                    NodeList getNodeListbyXPathNoder40 = XPathNoder40.getNodeListByXPath(xpathQueryNode40, n);
					                    //3' 
					                    NodeList getNodeListbyXPathNoder4 = XPathNoder4.getNodeListByXPath(xpathQueryNode4, n);

										
					                    LinkedList<String> stringfromNodelist30 = l1.getStringfromNodelist(getNodeListbyXPathNoder30);
					                    String P1 = null;
					                    String string3 = stringfromNodelist30.getFirst();

					                    LinkedList<String> stringfromNodelist40 = l1.getStringfromNodelist(getNodeListbyXPathNoder40);
				                        String P1prime = null;
				                        String string4 = stringfromNodelist40.getFirst();
				                        
					                    if (string3.equalsIgnoreCase("unknown") && string4.equalsIgnoreCase("unknown")) {
					                        peptide.start = 0;
					                        peptide.end = 0;
					                    } else if (string3.equalsIgnoreCase("unknown") && !string4.equalsIgnoreCase("unknown")) {
					                    	peptide.start = 0;
					                    	
					                    	LinkedList<String> stringfromNodelist4 = l1.getStringfromNodelist(getNodeListbyXPathNoder4);
				                            String stringy = stringfromNodelist4.getFirst();
				                            P1prime = stringy;
				                            System.out.println(stringy);
				                            int intP1prime = Integer.parseInt(P1prime);
				                            peptide.end = intP1prime;
					                        	
					                        } else if (!string3.equalsIgnoreCase("unknown") && string4.equalsIgnoreCase("unknown")) {
					                        	LinkedList<String> stringfromNodelist3 = l1.getStringfromNodelist(getNodeListbyXPathNoder3);
					                            P1 = stringfromNodelist3.getFirst();			                            
					                            System.out.println(P1);
					                            int intP1 = Integer.parseInt(P1);
					                            peptide.start = intP1;
					                            
					                        peptide.end = 0;	
					                        	
					                        } else if (!string3.equalsIgnoreCase("unknown") && !string4.equalsIgnoreCase("unknown")) {
					                        	LinkedList<String> stringfromNodelist3 = l1.getStringfromNodelist(getNodeListbyXPathNoder3);
					                            P1 = stringfromNodelist3.getFirst();					                            
					                            System.out.println(P1);
					                            int intP1 = Integer.parseInt(P1);
					                            peptide.start = intP1;
					                            
					                        	LinkedList<String> stringfromNodelist4 = l1.getStringfromNodelist(getNodeListbyXPathNoder4);
					                            String stringy = stringfromNodelist4.getFirst();
					                            P1prime = stringy;
					                            System.out.println(stringy);
					                            int intP1prime = Integer.parseInt(P1prime);
					                            peptide.end = intP1prime;
					                        }
					                        	
					                    
					                    peptide.disease = "";
										peptide.regulation = "";
										lastcapacityarray[i].nature = "peptide";
										lastcapacityarray[i]
												.setPeptide(peptide);
										
										i++;
									}

								}

							} else {
								continue;
							}
							
						} else {

							Connection connection4 = getConn();
							PreparedStatement ps4 = connection4
									.prepareStatement(querySubstrate);

							SubstrateData substrate2 = new SubstrateData();
							ProteaseData protease2 = new ProteaseData();

							try {
								ps4.setString(1, request);
								System.out.println(ps4);
								// Statement select =
								// connection.createStatement();
								ResultSet result4 = ps4.executeQuery();
								int rsSize4 = getResultSetSize(result4); // size
																			// the
																			// array
								firstcapacityarray = new ResultbySubstrateData[rsSize4];
								System.out.println(rsSize4);

								while (result4.next()) {
									firstcapacityarray[0] = new ResultbySubstrateData();
									substrate2.S_Uniprotid = result4
											.getString("S_UniprotId");
									substrate2.S_NL_Name = result4
											.getString("S_NL_Name");
									substrate2.S_Symbol = result4
											.getString("S_Symbol");
									firstcapacityarray[0]
											.setSubstrate(substrate2);
									firstcapacityarray[0]
											.setProtease(protease2);
									firstcapacityarray[0]
											.setNature("cleavagesite");
									firstcapacityarray[0]
											.setEntryValidity("Sorry, there is no cleavage site for this substrate in the database");
									System.out.print("wwwww");
								}

								// clean up
								result4.close();
								ps4.clearParameters();
								ps4.close();
								connection4.close();

							} catch (Throwable ignore) {
								System.err.println("Mysql Statement Error: "
										+ querySubstrate);
								ignore.printStackTrace();

							}

						}

					} catch (Throwable ignore) {

						System.err.println("Mysql Statement Error: "
								+ queryCleavagesite);
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
			k = kLast;
		}

		// return the array

		return lastcapacityarray;

	}

}
