package com.google.gwt.proteasedb.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.proteasedb.client.CsJava_1;
import com.google.gwt.proteasedb.client.DisPepJava_1;

import com.google.gwt.proteasedb.client.Loop;
import com.google.gwt.proteasedb.client.Mismatch;
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

	public String getSubstrateSQL(String querySubstrate, String proteinrequest)
			throws Throwable {
		String substratesymbol = null;
		String substrateuni = null;
		String substratename = null;
		String output = null;

		Connection connection = getConn();
		PreparedStatement ps = connection.prepareStatement(querySubstrate);
		try {
			ps.setString(1, proteinrequest);
			ps.setString(2, proteinrequest);
			System.out.println(ps);
			ResultSet result = ps.executeQuery();
			int rsSize = getResultSetSize(result);

			while (result.next()) {
				substratesymbol = result.getString("S_Symbol");
				substrateuni = result.getString("S_UniprotId");
				substratename = result.getString("S_NL_Name");
			}

			output = Integer.toString(rsSize) + "\n" + substratesymbol + "\n"
					+ substratename + "\n" + substrateuni;
			System.out.println(output);
			// clean up
			result.close();
			ps.clearParameters();
			ps.close();
			connection.close();

		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + querySubstrate);
			ignore.printStackTrace();
		}
		return output;
	}

	public CsJava_1[] getCsinSql(String queryCleavagesite, String substrateuni)
			throws Throwable {
		Connection connection2 = getConn();
		PreparedStatement ps2 = connection2.prepareStatement(queryCleavagesite);
		CsJava_1[] csjava = null;
		try {
			ps2.setString(1, substrateuni);
			System.out.println(ps2);
			ResultSet result2 = ps2.executeQuery();
			// init object into the size we need, like a recordset
			int rsSize2 = getResultSetSize(result2);
			System.out.println(rsSize2 + "avant");

			int i = 0;

			csjava = new CsJava_1[rsSize2];
			while (result2.next()) {
				csjava[i] = new CsJava_1();
				csjava[i].CleavageSite_Sequence = result2
						.getString("CleavageSite_Sequence");
				csjava[i].P1 = result2.getInt("P1");
				csjava[i].P1prime = result2.getInt("P1prime");
				csjava[i].External_link = result2.getString("External_link");
				csjava[i].PMID = result2.getString("PMID");
				csjava[i].P_UniprotId = result2.getString("P_UniprotId");
				i++;
			}

			// clean up
			result2.close();
			ps2.clearParameters();
			ps2.close();
			connection2.close();
		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + queryCleavagesite);
			ignore.printStackTrace();
		}
		return csjava;
	}


	public CsJava_1[] getCsMISMATCHinSql(String queryCleavagesite, String substrateuni, int pepstart, int pepend, String searchnumber, String terminus, String nOrC)
			throws Throwable {
		Connection connection2 = getConn();
		Statement s2 = connection2.createStatement();
		CsJava_1[] csjava = null;
		try {
			
			System.out.println(s2);
			ResultSet result2 = s2.executeQuery(queryCleavagesite);
			// init object into the size we need, like a recordset
			int rsSize2 = getResultSetSize(result2);
			System.out.println(rsSize2 + "avant");

			int i = 0;

			csjava = new CsJava_1[rsSize2];
			while (result2.next()) {
				csjava[i] = new CsJava_1();
				csjava[i].CleavageSite_Sequence = result2
						.getString("CleavageSite_Sequence");
				csjava[i].P1 = result2.getInt("P1");
				csjava[i].P1prime = result2.getInt("P1prime");
				csjava[i].substratesymbol = result2.getString("S_Symbol");
				csjava[i].External_link = result2.getString("External_link");
				csjava[i].PMID = result2.getString("PMID");
				csjava[i].P_UniprotId = result2.getString("P_UniprotId");
				csjava[i].CleavageSite_onPeptide = terminus;
				csjava[i].CleavageSite_NorC = nOrC;
				csjava[i].searchnumber = searchnumber;				
				i++;
			}

			// clean up
			result2.close();
			s2.close();
			connection2.close();
		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + queryCleavagesite);
			ignore.printStackTrace();
		}
		return csjava;
	}

	public String getProteaseSql(String queryProtease, String puni)
			throws Throwable {
		String output = null;
		String proteasename = null;
		String proteasesymbol = null;
		String proteaseEc = null;

		Connection connection3 = getConn();
		PreparedStatement ps3 = connection3.prepareStatement(queryProtease);
		try {
			ps3.setString(1, puni);
			System.out.println(ps3);
			ResultSet result3 = ps3.executeQuery();
			while (result3.next()) {
				proteasename = result3.getString("P_NL_Name");
				proteasesymbol = result3.getString("P_Symbol");
				proteaseEc = result3.getString("P_EC_Number");
			}
			output = proteasename + "\n" + proteasesymbol + "\n" + proteaseEc;
			result3.close();
			ps3.clearParameters();
			ps3.close();
			connection3.close();

		} catch (Throwable ignore) {

			System.err.println("Mysql Statement Error: " + queryProtease);
			ignore.printStackTrace();

		}
		return output;
	}

	public String getSubstrateSql(String querySubstrate, String suni)
			throws Throwable {

		String substratename = null;
		String substratesymbol = null;
		String output = null;

		Connection connection3 = getConn();
		PreparedStatement ps3 = connection3.prepareStatement(querySubstrate);
		try {
			ps3.setString(1, suni);
			System.out.println(ps3);
			ResultSet result3 = ps3.executeQuery();
			while (result3.next()) {
				substratename = result3.getString("S_NL_Name");
				substratesymbol = result3.getString("S_Symbol");

			}
			output = substratename + "\n" + substratesymbol;
			result3.close();
			ps3.clearParameters();
			ps3.close();
			connection3.close();

		} catch (Throwable ignore) {

			System.err.println("Mysql Statement Error: " + querySubstrate);
			ignore.printStackTrace();

		}
		return output;
	}

	public DisPepJava_1[] getallDisPepSql(String queryAllPeptide,
			String substrateuni) throws Throwable {
		Connection connection5 = getConn();
		PreparedStatement ps5 = connection5.prepareStatement(queryAllPeptide);
		DisPepJava_1[] dispepjava = null;
		try {
			ps5.setString(1, substrateuni);
			System.out.println(ps5);
			ResultSet result5 = ps5.executeQuery();

			// init object into the size we need, like a
			// recordset
			int rsSize5 = getResultSetSize(result5);
			System.out.println(rsSize5 + "quiquiquismaisqui");
			int i = 0;
			dispepjava = new DisPepJava_1[rsSize5];
			while (result5.next()) {
				dispepjava[i] = new DisPepJava_1();
				dispepjava[i].PMID = result5.getString("PMID");
				dispepjava[i].Pd_Disease = result5.getString("Pd_Disease");
				dispepjava[i].Pd_Sequence = result5.getString("Pd_Sequence");
				dispepjava[i].Pd_Regulation = result5
						.getString("Pd_Regulation");
				dispepjava[i].Pd_Start = result5.getInt("Pd_Start");
				dispepjava[i].Pd_End = result5.getInt("Pd_End");
				i++;
			}

			result5.close();
			ps5.clearParameters();
			ps5.close();
			connection5.close();

		} catch (Throwable ignore) {
			System.err.println("Mysql Statement Error: " + queryAllPeptide);
			ignore.printStackTrace();
		}
		return dispepjava;
	}

	public ResultbySubstrateData[] getResultbySubstrateInfo(
			SearchRequest[] searchRequest) throws Throwable {

		String substratesymbol = null;
		String substrateuni = null;
		String substratename = null;

		ResultbySubstrateData[] firstcapacityarray = null;
		ResultbySubstrateData[] intermediatecapacityarray = null;

		int k = 0;
		ResultbySubstrateData[] lastcapacityarray = new ResultbySubstrateData[k];

		for (SearchRequest searchReq : searchRequest) {

			if (searchReq.getRequestnature().equalsIgnoreCase("proteinRequest")) {
				int kFirst = 0;
				int kInter = 0;
				int kLast = 0;

				String subvalidity = null;
				String csvalidity = null;
				String dispepvalidity = null;
				String strupepvalidity = null;

				// CHECK SUBSTRATE IN SQL
				String querySubstrate = "SELECT * FROM SUBSTRATE WHERE S_Symbol = ? OR S_UniprotId = ?";
				String proteinrequest = searchReq.getProteininputsymbol();

				String output = getSubstrateSQL(querySubstrate, proteinrequest);

				String splitoutput[] = output.split("\n");
				int rsSize = Integer.parseInt(splitoutput[0]);
				System.out.println(rsSize);
				substratesymbol = splitoutput[1];
				substratename = splitoutput[2];
				substrateuni = splitoutput[3];

				SubstrateData substrate = new SubstrateData();

				if (rsSize == 0) {
					subvalidity = "no";
					csvalidity = "no";
					dispepvalidity = "no";
					strupepvalidity = "no";
					System.out.println(subvalidity);

					kFirst = k + 1;
					kLast = kFirst;

					firstcapacityarray = new ResultbySubstrateData[kFirst];
					System.arraycopy(lastcapacityarray, 0, firstcapacityarray,
							0, k);
					firstcapacityarray[k] = new ResultbySubstrateData();
					firstcapacityarray[k].setInput(proteinrequest);
					firstcapacityarray[k].setStrupepvalidity_1(strupepvalidity);
					firstcapacityarray[k].setCsvalidity_1(csvalidity);
					firstcapacityarray[k].setSubvalidity_1(subvalidity);
					firstcapacityarray[k].setDispepvalidity_1(dispepvalidity);
					firstcapacityarray[k].setNature("");
					firstcapacityarray[k].input = searchReq.proteininputsymbol;

					lastcapacityarray = new ResultbySubstrateData[kLast];
					lastcapacityarray = firstcapacityarray;

					System.out.println(strupepvalidity + "strupepvalidity");
					System.out.println(csvalidity + "csvalidity");
					System.out.println(subvalidity + "subvalidity");
					System.out.println(dispepvalidity + "dispepvalidity");
					System.out.println(searchReq.proteininputsymbol
							+ "searchReq.proteininputsymbol");
					k = kLast;
					System.out.println(kLast);

				} else {
					subvalidity = "yes";
					substrate.S_NL_Name = substratename;
					substrate.S_Symbol = substratesymbol;
					substrate.S_Uniprotid = substrateuni;
					System.out.println(substrateuni);

					// CHECK CS IN SQL
					String queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE S_UniprotId = ? ORDER BY P1, P_Symbol";
					CsJava_1[] csjava = getCsinSql(queryCleavagesite,
							substrateuni);
					int rsSize2 = csjava.length;
					System.out.println(rsSize2 + "apres");

					if (rsSize2 > 0) {
						csvalidity = "yes";
						kFirst = rsSize2 + k;
						System.out.println(kFirst + "pas les schtroumfs");
					} else {
						csvalidity = "no";
						kFirst = k;
						System.out.println(kFirst + "pas les schtroumfs");
					}

					// size the
					// array
					firstcapacityarray = new ResultbySubstrateData[kFirst];
					System.arraycopy(lastcapacityarray, 0, firstcapacityarray,
							0, k);
					System.out.println(kFirst + "FirstCapArray");

					if (rsSize2 > 0) {
						int i = k;

						for (int j = 0; j < csjava.length; j++) {
							populateCsSql(firstcapacityarray, substrate,
									csjava[j], i, j, proteinrequest);
							i++;
						}
						// CHECK DISPEP IN SQL
						String queryAllPeptide = "SELECT * FROM PEPTIDE WHERE S_UniprotId = ? ORDER BY Pd_Start, Pd_Regulation";

						DisPepJava_1[] disepjava = getallDisPepSql(
								queryAllPeptide, substrateuni);
						int rsSize5 = disepjava.length;
						System.out.println(rsSize5 + "apres");

						if (rsSize5 > 0) {
							dispepvalidity = "yes";
							kInter = kFirst + rsSize5;
							System.out.println(kInter + "pas les schtroumfs");
						} else {
							dispepvalidity = "no";
							kInter = kFirst;
							System.out.println(kInter + "pas les schtroumfs");
						}

						// size
						// the
						// array
						System.out.println(kInter + "InterCapArry");
						intermediatecapacityarray = new ResultbySubstrateData[kInter];

						System.arraycopy(firstcapacityarray, 0,
								intermediatecapacityarray, 0, kFirst);

						if (rsSize5 > 0) {
							i = kFirst;

							for (int j = 0; j < disepjava.length; j++) {
								populateDisPepSql(substratesymbol,
										substrateuni,
										intermediatecapacityarray, i, j,
										disepjava[j]);
								i++;
							}

							// CHECK STRUPEP IN UNIPROT
							Document xml = checkSubUniprot(substrateuni);

							if (xml == null) {
								subvalidity = "no";
								kLast = kInter;
							} else {
								subvalidity = "yes";
								// RETRIEVE ENTRIES THAT ARE CHAIN OR PROPEPTIDE
								// INSIDE XML
								XPathUniprotPep XPather = new XPathUniprotPep();
								String xpathQuery = "/uniprot/entry[./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']|./feature[@type='domain']|./feature[@type='region of interest']|./feature[@type='short sequence motif']]";
								// GET ENTRIES THAT ARE CHAIN OR PROPEPTIDE
								// NODELIST
								NodeList getNodeListbyXPath = XPather
										.getNodeListByXPath(xpathQuery, xml);
								if (getNodeListbyXPath.getLength() == 0) {
									strupepvalidity = "no";
									kLast = kInter;
								} else if (getNodeListbyXPath.getLength() > 0) {
									strupepvalidity = "yes";
									// RETRIEVE ALL PEPTIDES IN SELECTED ENTRIES
									lastcapacityarray = populateStruPepUni(
											substratesymbol, substrateuni,
											substratename,
											intermediatecapacityarray,
											lastcapacityarray, kInter,
											getNodeListbyXPath);
									kLast = lastcapacityarray.length;
								}
							}
						} else if (rsSize5 == 0) {

							// CHECK STRUPEP IN UNIPROT
							Document xml = checkSubUniprot(substrateuni);

							if (xml == null) {
								subvalidity = "no";
								kLast = kInter;
							} else {
								subvalidity = "yes";
								// RETRIEVE ENTRIES THAT ARE CHAIN OR PROPEPTIDE
								// INSIDE XML
								XPathUniprotPep XPather = new XPathUniprotPep();
								String xpathQuery = "/uniprot/entry[./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']|./feature[@type='domain']|./feature[@type='region of interest']|./feature[@type='short sequence motif']]";
								// GET ENTRIES THAT ARE CHAIN OR PROPEPTIDE
								// NODELIST
								NodeList getNodeListbyXPath = XPather
										.getNodeListByXPath(xpathQuery, xml);
								if (getNodeListbyXPath.getLength() == 0) {
									strupepvalidity = "no";
									kLast = kInter;
								} else if (getNodeListbyXPath.getLength() > 0) {
									strupepvalidity = "yes";
									// RETRIEVE ALL PEPTIDES IN SELECTED ENTRIES
									lastcapacityarray = populateStruPepUni(
											substratesymbol, substrateuni,
											substratename,
											intermediatecapacityarray,
											lastcapacityarray, kInter,
											getNodeListbyXPath);
									kLast = lastcapacityarray.length;
								}
							}

						}
					} else if (rsSize2 == 0) {
						// CHECK DISPEP IN SQL
						String queryAllPeptide = "SELECT * FROM PEPTIDE WHERE S_UniprotId = ? ORDER BY Pd_Start, Pd_Regulation";

						DisPepJava_1[] disepjava = getallDisPepSql(
								queryAllPeptide, substrateuni);
						int rsSize5 = disepjava.length;
						System.out.println(rsSize5 + "apres");

						if (rsSize5 > 0) {
							dispepvalidity = "yes";
							kInter = kFirst + rsSize5;
							System.out.println(kInter + "pas les schtroumfs");
						} else {
							dispepvalidity = "no";
							kInter = kFirst;
							System.out.println(kInter + "pas les schtroumfs");
						}

						// size
						// the
						// array
						System.out.println(kInter + "InterCapArry");
						intermediatecapacityarray = new ResultbySubstrateData[kInter];

						System.arraycopy(firstcapacityarray, 0,
								intermediatecapacityarray, 0, kFirst);

						if (rsSize5 > 0) {
							int i = kFirst;
							for (int j = 0; j < disepjava.length; j++) {
								populateDisPepSql(substratesymbol,
										substrateuni,
										intermediatecapacityarray, i, j,
										disepjava[j]);
								i++;
							}
							// CHECK STRUPEP IN UNIPROT
							Document xml = checkSubUniprot(substrateuni);

							if (xml == null) {
								subvalidity = "no";
								kLast = kInter;
							} else {
								subvalidity = "yes";
								// RETRIEVE ENTRIES THAT ARE CHAIN OR
								// PROPEPTIDE
								// INSIDE XML
								XPathUniprotPep XPather = new XPathUniprotPep();
								String xpathQuery = "/uniprot/entry[./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']|./feature[@type='domain']|./feature[@type='region of interest']|./feature[@type='short sequence motif']]";
								// GET ENTRIES THAT ARE CHAIN OR PROPEPTIDE
								// NODELIST
								NodeList getNodeListbyXPath = XPather
										.getNodeListByXPath(xpathQuery, xml);
								if (getNodeListbyXPath.getLength() == 0) {
									strupepvalidity = "no";
									kLast = kInter;
								} else if (getNodeListbyXPath.getLength() > 0) {
									strupepvalidity = "yes";
									// RETRIEVE ALL PEPTIDES IN SELECTED
									// ENTRIES
									lastcapacityarray = populateStruPepUni(
											substratesymbol, substrateuni,
											substratename,
											intermediatecapacityarray,
											lastcapacityarray, kInter,
											getNodeListbyXPath);
									kLast = lastcapacityarray.length;
								}
							}
						} else if (rsSize5 == 0) {

							// CHECK STRUPEP IN UNIPROT
							Document xml = checkSubUniprot(substrateuni);

							if (xml == null) {
								subvalidity = "no";
								kLast = kInter;
							} else {
								subvalidity = "yes";
								// RETRIEVE ENTRIES THAT ARE CHAIN OR
								// PROPEPTIDE
								// INSIDE XML
								XPathUniprotPep XPather = new XPathUniprotPep();
								String xpathQuery = "/uniprot/entry[./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']|./feature[@type='domain']|./feature[@type='region of interest']|./feature[@type='short sequence motif']]";
								// GET ENTRIES THAT ARE CHAIN OR PROPEPTIDE
								// NODELIST
								NodeList getNodeListbyXPath = XPather
										.getNodeListByXPath(xpathQuery, xml);
								if (getNodeListbyXPath.getLength() == 0) {
									strupepvalidity = "no";
									kLast = kInter;
								} else if (getNodeListbyXPath.getLength() > 0) {
									strupepvalidity = "yes";
									// RETRIEVE ALL PEPTIDES IN SELECTED
									// ENTRIES
									lastcapacityarray = populateStruPepUni(
											substratesymbol, substrateuni,
											substratename,
											intermediatecapacityarray,
											lastcapacityarray, kInter,
											getNodeListbyXPath);
									kLast = lastcapacityarray.length;
								}
							}
						}

					}
					lastcapacityarray[k].setStrupepvalidity_1(strupepvalidity);
					lastcapacityarray[k].setCsvalidity_1(csvalidity);
					lastcapacityarray[k].setSubvalidity_1(subvalidity);
					lastcapacityarray[k].setDispepvalidity_1(dispepvalidity);
					lastcapacityarray[k].input = searchReq.proteininputsymbol;

					System.out.println(strupepvalidity + "strupepvalidity");
					System.out.println(csvalidity + "csvalidity");
					System.out.println(subvalidity + "subvalidity");
					System.out.println(dispepvalidity + "dispepvalidity");
					System.out.println(searchReq.proteininputsymbol
							+ "searchReq.proteininputsymbol");
					k = kLast;
					System.out.println(kLast);
				}

			} else if (searchReq.getRequestnature().equalsIgnoreCase(
					"peptideRequest")) {

				int kFirst = 0;
				int kIntN = 0;
				int kIntC = 0;
				int kLast = 0;
				
				ResultbySubstrateData[] intermediatecapacityarrayNterm = null;
				ResultbySubstrateData[] intermediatecapacityarrayCterm = null;
				
							
				String csvalidity = null;
				String dispepvalidity = null;

				String fullsequence = null;
				String pepsequence = null;
				String nTerm = null;
				String cTerm = null;

				String queryPeptide = "SELECT * FROM PEPTIDE WHERE S_Uniprotid = ? AND Pd_Start = ? AND Pd_end = ?";

				String pepSubstrateId = searchReq.getPeptideinputuni();
				String pepNumber = searchReq.getPeptideinputnumber();
				int pepStart = searchReq.getPeptideinputstart();
				int pepEnd = searchReq.getPeptideinputend();

				int mismatch = searchReq.getPeptideinputmismatch();

				Connection connection = getConn();
				PreparedStatement ps = connection
						.prepareStatement(queryPeptide);
				SubstrateData substrate1 = new SubstrateData();
				String searchnumber = searchReq.getPeptideinputnumber();
				System.out.println(searchnumber);

				try {
					ps.setString(1, pepSubstrateId);
					ps.setInt(2, pepStart);
					ps.setInt(3, pepEnd);

					System.out.println(ps);
					ResultSet result1 = ps.executeQuery();
					int rsSize1 = getResultSetSize(result1);

					// size the
					// array
					if (rsSize1 == 0) {
						dispepvalidity = "no";
						kFirst = k;

					} else {
						dispepvalidity = "yes";
						kFirst = rsSize1 + k;
					}

					firstcapacityarray = new ResultbySubstrateData[kFirst];
					System.arraycopy(lastcapacityarray, 0, firstcapacityarray,
							0, k);
					System.out.println(kFirst + "FirstCapArray");

					if (rsSize1 > 0) {
						int i = k;
						// GET PEPTIDES
						while (result1.next()) {
							substrateuni = result1.getString("S_UniprotId");
							substratesymbol = result1.getString("S_Symbol");
							// CHECK Substrate IN SQL
							String querySubstrate = "SELECT * FROM SUBSTRATE WHERE S_UniprotID = ?";
							String outputsubstrate = getSubstrateSql(
									querySubstrate, substrateuni);
							String outputsubstratesplit[] = outputsubstrate
									.split("\n");
							substrate1.S_NL_Name = outputsubstratesplit[0];
							substrate1.S_Uniprotid = substrateuni;
							substrate1.S_Symbol = substratesymbol;
							firstcapacityarray[i] = new ResultbySubstrateData();
							firstcapacityarray[i].setSubstrate(substrate1);
							PeptideData peptide = new PeptideData();
							peptide.searchnumber = searchnumber;
							peptide.sequence = result1.getString("Pd_Sequence");
							String disease = result1.getString("Pd_Disease");
							peptide.disease = disease;
							System.out.println(disease);
							peptide.regulation = result1
									.getString("Pd_Regulation");
							// peptide.structure = "";
							peptide.start = result1.getInt("Pd_Start");
							peptide.end = result1.getInt("Pd_End");
							firstcapacityarray[i].setPeptide(peptide);
							firstcapacityarray[i].setNature("peptide");
							firstcapacityarray[i].setEntryValidity("xxxxx");
							firstcapacityarray[i].setCSInput_substrate(pepSubstrateId);
							firstcapacityarray[i].setCSInput_start(pepStart);
							firstcapacityarray[i].setCSInput_end(pepEnd);
							firstcapacityarray[i].setCSInput_number(pepNumber);

							i++;
						}
					} else if (rsSize1 == 0) {

						// Retrieve PEPTIDE SEQUENCE IN UNIPROT
						Document xml = checkSubUniprot(pepSubstrateId);

						// RETRIEVE ENTTRIES WITH SEQUENCE
						// INSIDE XML
						XPathUniprotPep XPather = new XPathUniprotPep();
						String xpathQuery = "/uniprot/entry/sequence/text()";
						// GET ENTRY THAT HAVE SEQUENCE NODELIST
						NodeList getNodeListbyXPath = XPather
								.getNodeListByXPath(xpathQuery, xml);

						if (getNodeListbyXPath.getLength() > 0) {
							System.out.println("OK");
							// RETRIEVE SEQUENCE IN SELECTED ENTRIES
							XPathNodeUniprot XPathNoder2 = new XPathNodeUniprot();
							String xpathQueryNode2 = "/uniprot/entry/sequence/text()";

							Loop l1 = new Loop();
							int i = k;

							// FOR EACH SELECTED ENTRIE (THAT WILL BE ONLY
							// ONE HERE BUT ANYWAY...)
							for (int j1 = 0; j1 < getNodeListbyXPath
									.getLength(); j1++) {
								// CHECK Substrate IN SQL
								String querySubstrate = "SELECT * FROM SUBSTRATE WHERE S_UniprotID = ?";
								String outputsubstrate = getSubstrateSql(
										querySubstrate, pepSubstrateId);
								String outputsubstratesplit[] = outputsubstrate
										.split("\n");
								substrate1.S_NL_Name = outputsubstratesplit[0];
								substrate1.S_Uniprotid = searchReq.peptideinputuni;
								substrate1.S_Symbol = outputsubstratesplit[1];
								firstcapacityarray[i] = new ResultbySubstrateData();
								firstcapacityarray[i].setSubstrate(substrate1);
								PeptideData peptide = new PeptideData();
								peptide.searchnumber = searchnumber;

								// GET SEQUENCE
								NodeList getNodeListByXPathNoder2 = XPathNoder2
										.getNodeListByXPath(xpathQueryNode2,
												getNodeListbyXPath.item(j1));
								LinkedList<String> stringfromNodelist2 = l1
										.getStringfromNodelist(getNodeListByXPathNoder2);
								fullsequence = stringfromNodelist2.getFirst();
								fullsequence = fullsequence
										.replaceAll("\n", "");
								System.out.println(fullsequence);
								pepsequence = fullsequence.substring(
										pepStart - 1, pepEnd);
								peptide.sequence = pepsequence;
								peptide.start = pepStart;
								peptide.end = pepEnd;
								firstcapacityarray[i].setNature("peptide");
								firstcapacityarray[i].setPeptide(peptide);
								System.out.println(pepsequence);
							}
						}

					}

					// clean up
					result1.close();
					ps.clearParameters();
					ps.close();
					connection.close();

				} catch (Throwable ignore) {
					System.err
							.println("Mysql Statement Error: " + queryPeptide);
					ignore.printStackTrace();

				}

				// Retrieve PEPTIDE SEQUENCE IN UNIPROT
				Document xml = checkSubUniprot(pepSubstrateId);

				// RETRIEVE ENTTRIES WITH SEQUENCE
				// INSIDE XML
				XPathUniprotPep XPather = new XPathUniprotPep();
				String xpathQuery = "/uniprot/entry/sequence/text()";
				// GET ENTRY THAT HAVE SEQUENCE NODELIST
				NodeList getNodeListbyXPath = XPather.getNodeListByXPath(
						xpathQuery, xml);

				if (getNodeListbyXPath.getLength() > 0) {
					System.out.println("OK");
					// RETRIEVE SEQUENCE IN SELECTED ENTRIES
					XPathNodeUniprot XPathNoder2 = new XPathNodeUniprot();
					String xpathQueryNode2 = "/uniprot/entry/sequence/text()";

					Loop l1 = new Loop();

					// FOR EACH SELECTED ENTRIE (THAT WILL BE ONLY
					// ONE HERE BUT ANYWAY...)
					for (int j1 = 0; j1 < getNodeListbyXPath.getLength(); j1++) {

						// GET SEQUENCE
						NodeList getNodeListByXPathNoder2 = XPathNoder2
								.getNodeListByXPath(xpathQueryNode2,
										getNodeListbyXPath.item(j1));
						LinkedList<String> stringfromNodelist2 = l1
								.getStringfromNodelist(getNodeListByXPathNoder2);
						fullsequence = stringfromNodelist2.getFirst();
						fullsequence = fullsequence.replaceAll("\n", "");
						System.out.println(fullsequence);
						nTerm = fullsequence.substring(pepStart - 4,
								pepStart + 2);
						cTerm = fullsequence.substring(pepEnd - 3, pepEnd + 3);
						System.out.println(nTerm);
						System.out.println(cTerm);
					}
				}
				String n1 = nTerm.substring(0, 1);
				String n2 = nTerm.substring(1, 2);
				String n3 = nTerm.substring(2, 3);
				String n4 = nTerm.substring(3, 4);
				String n5 = nTerm.substring(4, 5);
				String n6 = nTerm.substring(5, 6);

				String c1 = cTerm.substring(0, 1);
				String c2 = cTerm.substring(1, 2);
				String c3 = cTerm.substring(2, 3);
				String c4 = cTerm.substring(3, 4);
				String c5 = cTerm.substring(4, 5);
				String c6 = cTerm.substring(5, 6);
				
				Mismatch mismN0[] = new Mismatch [1];
				int numismN0 = 1;
				for (int i = 0; i<numismN0; i++) {
				mismN0[i] = new Mismatch();
				mismN0[i].setSubstratesymbol(substratesymbol);
				mismN0[i].setSearchnumber(searchnumber);
				}
				
				Mismatch mismC0[] = new Mismatch [1];
				int numismC0 = 1;
				for (int i = 0; i<numismC0; i++) {
				mismC0[i] = new Mismatch();
				mismC0[i].setSubstratesymbol(substratesymbol);
				mismC0[i].setSearchnumber(searchnumber);
				}
				
				Mismatch mismN1[] = new Mismatch [6];
				int numismN1 = 6;
				for (int i = 0; i<numismN1; i++) {
				mismN1[i] = new Mismatch();
				mismN1[i].setSubstratesymbol(substratesymbol);
				mismN1[i].setSearchnumber(searchnumber);
				}
				
				Mismatch mismC1[] = new Mismatch [6];
				int numismC1 = 6;
				for (int i = 0; i<numismC1; i++) {
				mismC1[i] = new Mismatch();
				mismC1[i].setSubstratesymbol(substratesymbol);
				mismC1[i].setSearchnumber(searchnumber);
				}
				
				Mismatch mismN2[] = new Mismatch [15];
				int numismN2 = 15;
				for (int i = 0; i<numismN2; i++) {
				mismN2[i] = new Mismatch();
				mismN2[i].setSubstratesymbol(substratesymbol);
				mismN2[i].setSearchnumber(searchnumber);
				}
				
				Mismatch mismC2[] = new Mismatch [15];
				int numismC2 = 15;
				for (int i = 0; i<numismC2; i++) {
				mismC2[i] = new Mismatch();
				mismC2[i].setSubstratesymbol(substratesymbol);
				mismC2[i].setSearchnumber(searchnumber);
				}
				
				Mismatch mismN3[] = new Mismatch [20];
				int numismN3 = 20;
				for (int i = 0; i<numismN3; i++) {
				mismN3[i] = new Mismatch();
				mismN3[i].setSubstratesymbol(substratesymbol);
				mismN3[i].setSearchnumber(searchnumber);
				}
				
				Mismatch mismC3[] = new Mismatch [20];
				int numismC3 = 20;
				for (int i = 0; i<numismC3; i++) {
				mismC3[i] = new Mismatch();
				mismC3[i].setSubstratesymbol(substratesymbol);
				mismC3[i].setSearchnumber(searchnumber);
				}
				
				mismN0[0].setCs_pattern(n1 + n2 + n3 + n4 + n5 + n6);
				mismN0[0].setTerminus("NTerm");
				mismN0[0].setCs(nTerm);
				mismC0[0].setCs_pattern(c1 + c2 + c3 + c4 + c5 + c6);
				mismC0[0].setTerminus("CTerm");
				mismC0[0].setCs(cTerm);

				mismN1[0].setCs_pattern("_" + n2 + n3 + n4 + n5 + n6);
				mismN1[1].setCs_pattern(n1 + "_" + n3 + n4 + n5 + n6);
				mismN1[2].setCs_pattern(n1 + n2 + "_" + n4 + n5 + n6);
				mismN1[3].setCs_pattern(n1 + n2 + n3 + "_" + n5 + n6);
				mismN1[4].setCs_pattern(n1 + n2 + n3 + n4 + "_" + n6);
				mismN1[5].setCs_pattern(n1 + n2 + n3 + n4 + n5 + "_");
				for (int i=0; i<6; i++) {
					mismN1[i].setTerminus("NTerm");
					mismN1[i].setCs(nTerm);
				}
				

				mismC1[0].setCs_pattern("_" + c2 + c3 + c4 + c5 + c6);
				mismC1[1].setCs_pattern(c1 + "_" + c3 + c4 + c5 + c6);
				mismC1[2].setCs_pattern(c1 + c2 + "_" + c4 + c5 + c6);
				mismC1[3].setCs_pattern(c1 + c2 + c3 + "_" + c5 + c6);
				mismC1[4].setCs_pattern(c1 + c2 + c3 + c4 + "_" + c6);
				mismC1[5].setCs_pattern(c1 + c2 + c3 + c4 + c5 + "_");
				for (int i=0; i<6; i++) {
					mismC1[i].setTerminus("CTerm");
					mismC1[i].setCs(cTerm);
				}

				mismN2[0].setCs_pattern("_" + "_" + n3 + n4 + n5 + n6);
				mismN2[1].setCs_pattern("_" + n2 + "_" + n4 + n5 + n6);
				mismN2[2].setCs_pattern("_" + n2 + n3 + "_" + n5 + n6);
				mismN2[3].setCs_pattern("_" + n2 + n3 + n4 + "_" + n6);
				mismN2[4].setCs_pattern("_" + n2 + n3 + n4 + n5 + "_");
				mismN2[5].setCs_pattern(n1 + "_" + "_" + n4 + n5 + n6);
				mismN2[6].setCs_pattern(n1 + "_" + n3 + "_" + n5 + n6);
				mismN2[7].setCs_pattern(n1 + "_" + n3 + n4 + "_" + n6);
				mismN2[8].setCs_pattern(n1 + "_" + n3 + n4 + n5 + "_");
				mismN2[9].setCs_pattern(n1 + n2 + "_" + "_" + n5 + n6);
				mismN2[10].setCs_pattern(n1 + n2 + "_" + n4 + "_" + n6);
				mismN2[11].setCs_pattern(n1 + n2 + "_" + n4 + n5 + "_");
				mismN2[12].setCs_pattern(n1 + n2 + n3 + "_" + "_" + n6);
				mismN2[13].setCs_pattern(n1 + n2 + n3 + "_" + n5 + "_");
				mismN2[14].setCs_pattern(n1 + n2 + n3 + n4 + "_" + "_");
				for (int i=0; i<15; i++) {
					mismN2[i].setTerminus("NTerm");
					mismN2[i].setCs(nTerm);
				}

				mismC2[0].setCs_pattern("_" + "_" + c3 + c4 + c5 + c6);
				mismC2[1].setCs_pattern("_" + c2 + "_" + c4 + c5 + c6);
				mismC2[2].setCs_pattern("_" + c2 + c3 + "_" + c5 + c6);
				mismC2[3].setCs_pattern("_" + c2 + c3 + c4 + "_" + c6);
				mismC2[4].setCs_pattern("_" + c2 + c3 + c4 + c5 + "_");
				mismC2[5].setCs_pattern(c1 + "_" + "_" + c4 + c5 + c6);
				mismC2[6].setCs_pattern(c1 + "_" + c3 + "_" + c5 + c6);
				mismC2[7].setCs_pattern(c1 + "_" + c3 + c4 + "_" + c6);
				mismC2[8].setCs_pattern(c1 + "_" + c3 + c4 + c5 + "_");
				mismC2[9].setCs_pattern(c1 + c2 + "_" + "_" + c5 + c6);
				mismC2[10].setCs_pattern(c1 + c2 + "_" + c4 + "_" + c6);
				mismC2[11].setCs_pattern(c1 + c2 + "_" + c4 + c5 + "_");
				mismC2[12].setCs_pattern(c1 + c2 + c3 + "_" + "_" + c6);
				mismC2[13].setCs_pattern(c1 + c2 + c3 + "_" + c5 + "_");
				mismC2[14].setCs_pattern(c1 + c2 + c3 + c4 + "_" + "_");
				for (int i=0; i<15; i++) {
					mismC2[i].setTerminus("CTerm");
					mismC2[i].setCs(cTerm);
				}
				
				mismN3[0].setCs_pattern("_" + "_" + "_" + n4 + n5 + n6);				
				mismN3[1].setCs_pattern("_" + "_" + n3 + "_" + n5 + n6);
				mismN3[2].setCs_pattern("_" + "_" + n3 + n4 + "_" + n6);
				mismN3[3].setCs_pattern("_" + "_" + n3 + n4 + n5 + "_");			
				mismN3[4].setCs_pattern("_" + n2 + "_" + "_" + n5 + n6);
				mismN3[5].setCs_pattern("_" + n2 + "_" + n4 + "_" + n6);
				mismN3[6].setCs_pattern("_" + n2 + "_" + n4 + n5 + "_");
				mismN3[7].setCs_pattern("_" + n2 + n3 + "_" + "_" + n6);
				mismN3[8].setCs_pattern("_" + n2 + n3 + "_" + n5 + "_");			
				mismN3[9].setCs_pattern("_" + n2 + n3 + n4 + "_" + "_");
				mismN3[10].setCs_pattern(n1 + "_" + "_" + "_" + n5 + n6);
				mismN3[11].setCs_pattern(n1 + "_" + "_" + n4 + "_" + n6);
				mismN3[12].setCs_pattern(n1 + "_" + "_" + n4 + n5 + "_");
				mismN3[13].setCs_pattern(n1 + "_" + n3 + "_" + "_" + n6);
				mismN3[14].setCs_pattern(n1 + "_" + n3 + "_" + n5 + "_");
				mismN3[15].setCs_pattern(n1 + "_" + n3 + n4 + "_" + "_");			
				mismN3[16].setCs_pattern(n1 + n2 + "_" + "_" + "_" + n6);
				mismN3[17].setCs_pattern(n1 + n2 + "_" + "_" + n5 + "_");
				mismN3[18].setCs_pattern(n1 + n2 + "_" + n4 + "_" + "_");		
				mismN3[19].setCs_pattern(n1 + n2 + n3 + "_" + "_" + "_");
				for (int i=0; i<20; i++) {
					mismN3[i].setTerminus("NTerm");
					mismN3[i].setCs(nTerm);
				}
				
				mismC3[0].setCs_pattern("_" + "_" + "_" + c4 + c5 + c6);				
				mismC3[1].setCs_pattern("_" + "_" + c3 + "_" + c5 + c6);
				mismC3[2].setCs_pattern("_" + "_" + c3 + c4 + "_" + c6);
				mismC3[3].setCs_pattern("_" + "_" + c3 + c4 + c5 + "_");			
				mismC3[4].setCs_pattern("_" + c2 + "_" + "_" + c5 + c6);
				mismC3[5].setCs_pattern("_" + c2 + "_" + c4 + "_" + c6);
				mismC3[6].setCs_pattern("_" + c2 + "_" + c4 + c5 + "_");
				mismC3[7].setCs_pattern("_" + c2 + c3 + "_" + "_" + c6);
				mismC3[8].setCs_pattern("_" + c2 + c3 + "_" + c5 + "_");			
				mismC3[9].setCs_pattern("_" + c2 + c3 + c4 + "_" + "_");
				mismC3[10].setCs_pattern(c1 + "_" + "_" + "_" + c5 + c6);
				mismC3[11].setCs_pattern(c1 + "_" + "_" + c4 + "_" + c6);
				mismC3[12].setCs_pattern(c1 + "_" + "_" + c4 + c5 + "_");
				mismC3[13].setCs_pattern(c1 + "_" + c3 + "_" + "_" + c6);
				mismC3[14].setCs_pattern(c1 + "_" + c3 + "_" + c5 + "_");
				mismC3[15].setCs_pattern(c1 + "_" + c3 + c4 + "_" + "_");			
				mismC3[16].setCs_pattern(c1 + c2 + "_" + "_" + "_" + c6);
				mismC3[17].setCs_pattern(c1 + c2 + "_" + "_" + c5 + "_");
				mismC3[18].setCs_pattern(c1 + c2 + "_" + c4 + "_" + "_");		
				mismC3[19].setCs_pattern(c1 + c2 + c3 + "_" + "_" + "_");
				for (int i=0; i<20; i++) {
					mismC3[i].setTerminus("CTerm");
					mismC3[i].setCs(cTerm);
				}
				
				String prequery = "";
				
				if (mismatch == 0) {
					
					int gaps = 0;
					
					//check CS in SQL 0 MM N term
					for (int j=0; j<numismN0; j++) {
					String cs = mismN0[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence = '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					String queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					String terminus = nTerm;
					String nOrC = "NTerm";
					
									
					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSize = csjava.length;
					System.out.println(rsSize + "apres");
//
					if (rsSize > 0) {
						csvalidity = "yes";
						kIntN = rsSize + kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					} else {
						kIntN = kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					}
					// size the
					// array
					intermediatecapacityarrayNterm = new ResultbySubstrateData[kIntN];
					System.arraycopy(firstcapacityarray, 0, intermediatecapacityarrayNterm,
							0, kFirst);

					if (rsSize > 0) {
						int i = kFirst;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayNterm, substrate1,
									csjava[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					//check CS in SQL 0 MM C term
					prequery = "";
					for (int j=0; j<numismC0; j++) {
					String cs = mismC0[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence = '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					terminus = cTerm;
					nOrC = "CTerm";
					
									
					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
//
					if (rsSizeC > 0) {
						csvalidity = "yes";
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;
						
					}
					// size the
					// array
					intermediatecapacityarrayCterm = new ResultbySubstrateData[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0, intermediatecapacityarrayCterm,
							0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayCterm, substrate1,
									csjavaC[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					lastcapacityarray = intermediatecapacityarrayCterm;
					kLast = lastcapacityarray.length;
					System.out.println(lastcapacityarray.length + "ROULEMENT DE TAMBOUR");
					
				} else if (mismatch == 1) {
					int gaps = 1;
					
					//check CS in SQL 1 MM N term
					for (int j=0; j<numismN1; j++) {
					String cs = mismN1[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					String queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					String terminus = nTerm;
					String nOrC = "NTerm";
					
									
					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSize = csjava.length;
					System.out.println(rsSize + "apres");
//
					if (rsSize > 0) {
						csvalidity = "yes";
						kIntN = rsSize + kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					} else {
						kIntN = kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					}
					// size the
					// array
					intermediatecapacityarrayNterm = new ResultbySubstrateData[kIntN];
					System.arraycopy(firstcapacityarray, 0, intermediatecapacityarrayNterm,
							0, kFirst);

					if (rsSize > 0) {
						int i = kFirst;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayNterm, substrate1,
									csjava[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					//check CS in SQL 1 MM C term
					prequery = "";
					for (int j=0; j<numismC1; j++) {
					String cs = mismC1[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					terminus = cTerm;
					nOrC = "CTerm";
					
									
					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
//
					if (rsSizeC > 0) {
						csvalidity = "yes";
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;
						
					}
					// size the
					// array
					intermediatecapacityarrayCterm = new ResultbySubstrateData[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0, intermediatecapacityarrayCterm,
							0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayCterm, substrate1,
									csjavaC[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					lastcapacityarray = intermediatecapacityarrayCterm;
					kLast = lastcapacityarray.length;
					System.out.println(lastcapacityarray.length + "ROULEMENT DE TAMBOUR");
					
					
				} else if (mismatch == 2) {
					int gaps = 2;
					
					//check CS in SQL 2 MM N term
					for (int j=0; j<numismN2; j++) {
					String cs = mismN2[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					String queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					String terminus = nTerm;
					String nOrC = "NTerm";
					
									
					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSize = csjava.length;
					System.out.println(rsSize + "apres");
//
					if (rsSize > 0) {
						csvalidity = "yes";
						kIntN = rsSize + kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					} else {
						kIntN = kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					}
					// size the
					// array
					intermediatecapacityarrayNterm = new ResultbySubstrateData[kIntN];
					System.arraycopy(firstcapacityarray, 0, intermediatecapacityarrayNterm,
							0, kFirst);

					if (rsSize > 0) {
						int i = kFirst;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayNterm, substrate1,
									csjava[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					//check CS in SQL 2 MM C term
					prequery = "";
					for (int j=0; j<numismC2; j++) {
					String cs = mismC2[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					terminus = cTerm;
					nOrC = "CTerm";
					
									
					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
//
					if (rsSizeC > 0) {
						csvalidity = "yes";
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;
						
					}
					// size the
					// array
					intermediatecapacityarrayCterm = new ResultbySubstrateData[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0, intermediatecapacityarrayCterm,
							0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayCterm, substrate1,
									csjavaC[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					lastcapacityarray = intermediatecapacityarrayCterm;
					kLast = lastcapacityarray.length;
					System.out.println(lastcapacityarray.length + "ROULEMENT DE TAMBOUR");
				
				} else if (mismatch == 3) {
					int gaps = 3;
					
					//check CS in SQL 2 MM N term
					for (int j=0; j<numismN3; j++) {
					String cs = mismN3[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					String queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					String terminus = nTerm;
					String nOrC = "NTerm";
					
									
					CsJava_1[] csjava = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSize = csjava.length;
					System.out.println(rsSize + "apres");
//
					if (rsSize > 0) {
						csvalidity = "yes";
						kIntN = rsSize + kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					} else {
						kIntN = kFirst;
						System.out.println(kFirst + "kFirst");
						System.out.println(kIntN + "kInt");
					}
					// size the
					// array
					intermediatecapacityarrayNterm = new ResultbySubstrateData[kIntN];
					System.arraycopy(firstcapacityarray, 0, intermediatecapacityarrayNterm,
							0, kFirst);

					if (rsSize > 0) {
						int i = kFirst;
						for (int l = 0; l < csjava.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayNterm, substrate1,
									csjava[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					//check CS in SQL 3 MM C term
					prequery = "";
					for (int j=0; j<numismC3; j++) {
					String cs = mismC3[j].getCs_pattern();
					prequery = prequery + " OR CleavageSite_Sequence LIKE '" + cs + "'";
					}
					prequery = prequery.replaceFirst(" OR", "");
					queryCleavagesite = "SELECT * FROM CLEAVAGESITE WHERE " + prequery +"  ORDER BY P_Symbol";
					System.out.println(queryCleavagesite);
					
					terminus = cTerm;
					nOrC = "CTerm";
					
									
					CsJava_1[] csjavaC = getCsMISMATCHinSql(queryCleavagesite, pepSubstrateId, pepStart, pepEnd, searchnumber, terminus, nOrC );
					int rsSizeC = csjavaC.length;
					System.out.println(rsSizeC + "apres");
//
					if (rsSizeC > 0) {
						csvalidity = "yes";
						kIntC = rsSizeC + kIntN;
					} else {
						kIntC = kIntN;
						
					}
					// size the
					// array
					intermediatecapacityarrayCterm = new ResultbySubstrateData[kIntC];
					System.arraycopy(intermediatecapacityarrayNterm, 0, intermediatecapacityarrayCterm,
							0, kIntN);

					if (rsSizeC > 0) {
						int i = kIntN;
						for (int l = 0; l < csjavaC.length; l++) {
							populateCsPERFECTSql(intermediatecapacityarrayCterm, substrate1,
									csjavaC[l], i, l, pepSubstrateId, pepStart, pepEnd, pepNumber, gaps);
							i++;
						}
					}
					
					lastcapacityarray = intermediatecapacityarrayCterm;
					kLast = lastcapacityarray.length;
					System.out.println(lastcapacityarray.length + "ROULEMENT DE TAMBOUR");

					
				}
				k = kLast;
				
				
//			} else if (searchReq.getRequestnature().equalsIgnoreCase(
//					"csRequest")) {
//				String peptideuni = searchReq.getCsuniprot();
//				int pepstart = searchReq.getCspepstart();
//				int pepend = searchReq.getCspepend();
//				System.out.println(peptideuni + "aaaaa");
//				System.out.println(pepstart + "aaaa");
//				System.out.println(pepend + "aaaaa");
//				String fullsequence = null;
//				String pepsequence = null;
//
//				// Retrieve PEPTIDE SEQUENCE IN UNIPROT
//				Document xml = checkSubUniprot(peptideuni);
//
//				// RETRIEVE ENTTRIES WITH SEQUENCE
//				// INSIDE XML
//				XPathUniprotPep XPather = new XPathUniprotPep();
//				String xpathQuery = "/uniprot/entry/sequence/text()";
//				// GET ENTRY THAT HAVE SEQUENCE NODELIST
//				NodeList getNodeListbyXPath = XPather.getNodeListByXPath(
//						xpathQuery, xml);
//
//				if (getNodeListbyXPath.getLength() > 0) {
//					System.out.println("OK");
//					// RETRIEVE SEQUENCE IN SELECTED ENTRIES
//					XPathNodeUniprot XPathNoder2 = new XPathNodeUniprot();
//					String xpathQueryNode2 = "/uniprot/entry/sequence/text()";
//
//					Loop l1 = new Loop();
//
//					// FOR EACH SELECTED ENTRIE (THAT WILL BE ONLY
//					// ONE HERE BUT ANYWAY...)
//					for (int j1 = 0; j1 < getNodeListbyXPath.getLength(); j1++) {
//
//						// GET SEQUENCE
//						NodeList getNodeListByXPathNoder2 = XPathNoder2
//								.getNodeListByXPath(xpathQueryNode2,
//										getNodeListbyXPath.item(j1));
//						LinkedList<String> stringfromNodelist2 = l1
//								.getStringfromNodelist(getNodeListByXPathNoder2);
//						fullsequence = stringfromNodelist2.getFirst();
//						fullsequence = fullsequence.replaceAll("\n", "");
//					}
//					System.out.println(fullsequence);
//					pepsequence = fullsequence.substring(pepstart - 1, pepend);
//					System.out.println(pepsequence);
//				}
			}

		}

		// return the array
		return lastcapacityarray;

	}

	private ResultbySubstrateData[] populateStruPepUni(String substratesymbol,
			String substrateuni, String substratename,
			ResultbySubstrateData[] intermediatecapacityarray,
			ResultbySubstrateData[] lastcapacityarray, int kInter,
			NodeList getNodeListbyXPath) {
		int kLast;
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
		for (int j = 0; j < getNodeListbyXPath.getLength(); j++) {

			// GET ALL PEPTIDES
			NodeList getNodeListByXPathNoder2 = XPathNoder2.getNodeListByXPath(
					xpathQueryNode2, getNodeListbyXPath.item(j));

			// create new array with larger capacity
			System.out.println(getNodeListByXPathNoder2.getLength()
					+ "number of peptide");

			kLast = kInter + getNodeListByXPathNoder2.getLength();
			System.out.println(kLast + "LastCapArray");

			lastcapacityarray = new ResultbySubstrateData[kLast];
			System.arraycopy(intermediatecapacityarray, 0, lastcapacityarray,
					0, kInter);

			int i = kInter;

			// FOR EACH PEPTIDE FEATURE OF SELECTED
			// ENTRIES
			for (int l = 0; l < getNodeListByXPathNoder2.getLength(); l++) {

				Node n = getNodeListByXPathNoder2.item(l);

				lastcapacityarray[i] = new ResultbySubstrateData();
				PeptideData peptide = new PeptideData();
				SubstrateData substrate2 = new SubstrateData();
				substrate2.S_NL_Name = substratename;
				substrate2.S_Symbol = substratesymbol;
				substrate2.S_Uniprotid = substrateuni;
				lastcapacityarray[i].setSubstrate(substrate2);
				lastcapacityarray[i].pmid = "";
				lastcapacityarray[i].externallink = "http://www.uniprot.org/uniprot/"
						+ substrateuni;
				lastcapacityarray[i].setEntryValidity("zzzzzzz");

				// PEPTIDE TYPE (eg Chain)
				NodeList getNodeListByXPathNoder20 = XPathNoder20
						.getNodeListByXPath(xpathQueryNode20, n);
				// PEPTIDE DESCRIPTION (eg C2 domain)
				NodeList getNodeListByXPathNoder21 = XPathNoder21
						.getNodeListByXPath(xpathQueryNode21, n);

				LinkedList<String> stringfromNodelist20 = l1
						.getStringfromNodelist(getNodeListByXPathNoder20);
				String string20 = stringfromNodelist20.getFirst();
				String stringx = string20.substring(0, 1).toUpperCase()
						+ string20.substring(1);
				if (string20.equalsIgnoreCase("signal peptide")) {
					peptide.structure = stringx;

				} else if (string20.equalsIgnoreCase("transit peptide")) {
					LinkedList<String> stringfromNodelist21 = l1
							.getStringfromNodelist(getNodeListByXPathNoder21);
					String string21 = stringfromNodelist21.getFirst();
					String stringy = "Transit peptide, " + string21;
					peptide.structure = stringy;

				} else {
					LinkedList<String> stringfromNodelist21 = l1
							.getStringfromNodelist(getNodeListByXPathNoder21);
					if (stringfromNodelist21.isEmpty()) {

						peptide.structure = stringx;

					} else {
						String string21 = stringfromNodelist21.getFirst();
						String stringz = string21 + " (" + stringx + ")";
						peptide.structure = stringz;

					}
				}

				// 2 GET PEPTIDE START
				NodeList getNodeListbyXPathNoder30 = XPathNoder30
						.getNodeListByXPath(xpathQueryNode30, n);
				// 2'
				NodeList getNodeListbyXPathNoder3 = XPathNoder3
						.getNodeListByXPath(xpathQueryNode3, n);

				// 3 GET PEPTIDE END
				NodeList getNodeListbyXPathNoder40 = XPathNoder40
						.getNodeListByXPath(xpathQueryNode40, n);
				// 3'
				NodeList getNodeListbyXPathNoder4 = XPathNoder4
						.getNodeListByXPath(xpathQueryNode4, n);

				LinkedList<String> stringfromNodelist30 = l1
						.getStringfromNodelist(getNodeListbyXPathNoder30);
				String P1 = null;
				String string3 = stringfromNodelist30.getFirst();

				LinkedList<String> stringfromNodelist40 = l1
						.getStringfromNodelist(getNodeListbyXPathNoder40);
				String P1prime = null;
				String string4 = stringfromNodelist40.getFirst();

				if (string3.equalsIgnoreCase("unknown")
						&& string4.equalsIgnoreCase("unknown")) {
					peptide.start = 0;
					peptide.end = 0;
				} else if (string3.equalsIgnoreCase("unknown")
						&& !string4.equalsIgnoreCase("unknown")) {
					peptide.start = 0;

					LinkedList<String> stringfromNodelist4 = l1
							.getStringfromNodelist(getNodeListbyXPathNoder4);
					String stringy = stringfromNodelist4.getFirst();
					P1prime = stringy;
					System.out.println(stringy);
					int intP1prime = Integer.parseInt(P1prime);
					peptide.end = intP1prime;

				} else if (!string3.equalsIgnoreCase("unknown")
						&& string4.equalsIgnoreCase("unknown")) {
					LinkedList<String> stringfromNodelist3 = l1
							.getStringfromNodelist(getNodeListbyXPathNoder3);
					P1 = stringfromNodelist3.getFirst();
					System.out.println(P1);
					int intP1 = Integer.parseInt(P1);
					peptide.start = intP1;

					peptide.end = 0;

				} else if (!string3.equalsIgnoreCase("unknown")
						&& !string4.equalsIgnoreCase("unknown")) {
					LinkedList<String> stringfromNodelist3 = l1
							.getStringfromNodelist(getNodeListbyXPathNoder3);
					P1 = stringfromNodelist3.getFirst();
					System.out.println(P1);
					int intP1 = Integer.parseInt(P1);
					peptide.start = intP1;

					LinkedList<String> stringfromNodelist4 = l1
							.getStringfromNodelist(getNodeListbyXPathNoder4);
					String stringy = stringfromNodelist4.getFirst();
					P1prime = stringy;
					System.out.println(stringy);
					int intP1prime = Integer.parseInt(P1prime);
					peptide.end = intP1prime;
				}

				// peptide.disease = "";
				peptide.regulation = "";
				lastcapacityarray[i].nature = "peptide";
				lastcapacityarray[i].setPeptide(peptide);

				i++;
			}
		}
		return lastcapacityarray;
	}

	private Document checkSubUniprot(String substrateuni) {
		String UniprotURL = "http://www.uniprot.org/uniprot/" + substrateuni
				+ ".xml";
		System.out.println(UniprotURL + "bbbbb");
		ParseUniprotPep parser = new ParseUniprotPep();
		Document xml = parser.getXML(UniprotURL);
		xml.getXmlVersion();
		System.out.println(xml.getXmlVersion());
		System.out.println(xml.toString());
		String xmlstring = parser.getXMLasstring(UniprotURL);
		return xml;
	}

	private void populateCsSql(ResultbySubstrateData[] firstcapacityarray,
			SubstrateData substrate, CsJava_1 csJava_1, int i, int j,
			String proteinrequest) throws SQLException, Throwable {
		ProteaseData protease = new ProteaseData();
		firstcapacityarray[i] = new ResultbySubstrateData();
		firstcapacityarray[i].setSubstrate(substrate);
		firstcapacityarray[i].setInput(proteinrequest);
		firstcapacityarray[i].cleavageSite = csJava_1.CleavageSite_Sequence;
		firstcapacityarray[i].p1 = csJava_1.P1;
		firstcapacityarray[i].p1prime = csJava_1.P1prime;
		firstcapacityarray[i].externallink = csJava_1.External_link;
		firstcapacityarray[i].pmid = csJava_1.PMID;
		firstcapacityarray[i].setEntryValidity("xxxxx");
		firstcapacityarray[i].setNature("cleavagesite");
		System.out.print(firstcapacityarray[i].getEntryValidity());
		String puni = csJava_1.P_UniprotId;
		protease.P_Uniprotid = puni;

		// CHECK PROTEASE IN SQL
		String queryProtease = "SELECT * FROM PROTEASE WHERE P_UniprotID = ?";
		String outputprotease = getProteaseSql(queryProtease, puni);
		String splitouputprotease[] = outputprotease.split("\n");
		protease.P_NL_Name = splitouputprotease[0];
		protease.P_Symbol = splitouputprotease[1];
		protease.P_Ecnumber = splitouputprotease[2];
		firstcapacityarray[i].setProtease(protease);

	}
	
	private void populateCsPERFECTSql(ResultbySubstrateData[] intermediatecapacityarray,
			SubstrateData substrate, CsJava_1 csJava_1, int i, int j,
			String substrateUni, int pepStart, int pepEnd, String pepNumber, int gaps) throws SQLException, Throwable {
		ProteaseData protease = new ProteaseData();
		intermediatecapacityarray[i] = new ResultbySubstrateData();
		intermediatecapacityarray[i].setSubstrate(substrate);
		intermediatecapacityarray[i].setCSInput_substrate(substrateUni);
		intermediatecapacityarray[i].setCSInput_start(pepStart);
		intermediatecapacityarray[i].setCSInput_end(pepEnd);
		intermediatecapacityarray[i].setCSInput_number(pepNumber);
		intermediatecapacityarray[i].CS_database = csJava_1.CleavageSite_Sequence;
		
		if (csJava_1.getCleavageSite_NorC().contains("CTerm")) {
			intermediatecapacityarray[i].CS_terminus = csJava_1.CleavageSite_onPeptide;
			intermediatecapacityarray[i].CS_NorCterm = "CTerm";
		} else if (csJava_1.getCleavageSite_NorC().contains("NTerm")) {
			intermediatecapacityarray[i].CS_terminus = csJava_1.CleavageSite_onPeptide;
			intermediatecapacityarray[i].CS_NorCterm = "NTerm";

		}
		intermediatecapacityarray[i].p1 = csJava_1.P1;
		intermediatecapacityarray[i].p1prime = csJava_1.P1prime;
		intermediatecapacityarray[i].externallink = csJava_1.External_link;
		intermediatecapacityarray[i].pmid = csJava_1.PMID;
		intermediatecapacityarray[i].setEntryValidity("xxxxx");
		intermediatecapacityarray[i].setNature("cleavagesite");
		intermediatecapacityarray[i].setMismatch(gaps);
		System.out.print(intermediatecapacityarray[i].getEntryValidity());
		String puni = csJava_1.P_UniprotId;
		protease.P_Uniprotid = puni;

		// CHECK PROTEASE IN SQL
		String queryProtease = "SELECT * FROM PROTEASE WHERE P_UniprotID = ?";
		String outputprotease = getProteaseSql(queryProtease, puni);
		String splitouputprotease[] = outputprotease.split("\n");
		protease.P_NL_Name = splitouputprotease[0];
		protease.P_Symbol = splitouputprotease[1];
		protease.P_Ecnumber = splitouputprotease[2];
		intermediatecapacityarray[i].setProtease(protease);

	}

	private void populateDisPepSql(String substratesymbol, String substrateuni,
			ResultbySubstrateData[] intermediatecapacityarray, int i, int j,
			DisPepJava_1 dispepjava) throws SQLException {
		intermediatecapacityarray[i] = new ResultbySubstrateData();
		SubstrateData substrate2 = new SubstrateData();
		substrate2.S_Symbol = substratesymbol;
		substrate2.S_Uniprotid = substrateuni;
		intermediatecapacityarray[i].setSubstrate(substrate2);
		intermediatecapacityarray[i].setDispepvalidity_1("yes");
		intermediatecapacityarray[i].pmid = dispepjava.PMID;

		intermediatecapacityarray[i].externallink = "";
		PeptideData peptide = new PeptideData();
		String disease = dispepjava.Pd_Disease;
		peptide.disease = disease;
		peptide.sequence = dispepjava.Pd_Sequence;
		System.out.println(disease);
		peptide.regulation = dispepjava.Pd_Regulation;
		// peptide.structure = "";
		peptide.start = dispepjava.Pd_Start;
		peptide.end = dispepjava.Pd_End;
		intermediatecapacityarray[i].setPeptide(peptide);
		intermediatecapacityarray[i].setEntryValidity("xxxxx");
		intermediatecapacityarray[i].setNature("peptide");

	}
}