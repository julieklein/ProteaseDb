package com.google.gwt.proteasedb.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.proteasedb.client.CsJava_1;
import com.google.gwt.proteasedb.client.DisPepJava_1;

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
				int kInt = 0;
				int kLast = 0;

				String csvalidity = null;
				String dispepvalidity = null;

				String fullsequence = null;
				String pepsequence = null;
				String nTerm = null;
				String cTerm = null;

				String queryPeptide = "SELECT * FROM PEPTIDE WHERE S_Uniprotid = ? AND Pd_Start = ? AND Pd_end = ?";

				String pepSubstrateId = searchReq.getPeptideinputuni();
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
						kFirst = 1;

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

				String csNquery0 = null;

				String csNquery1_1 = null;
				String csNquery1_2 = null;
				String csNquery1_3 = null;
				String csNquery1_4 = null;
				String csNquery1_5 = null;
				String csNquery1_6 = null;

				String csNquery2_1 = null;
				String csNquery2_2 = null;
				String csNquery2_3 = null;
				String csNquery2_4 = null;
				String csNquery2_5 = null;
				String csNquery2_6 = null;
				String csNquery2_7 = null;
				String csNquery2_8 = null;
				String csNquery2_9 = null;
				String csNquery2_10 = null;
				String csNquery2_11 = null;
				String csNquery2_12 = null;
				String csNquery2_13 = null;
				String csNquery2_14 = null;
				String csNquery2_15 = null;
				
				String csNquery3_1 = null;
				String csNquery3_2 = null;
				String csNquery3_3 = null;
				String csNquery3_4 = null;
				String csNquery3_5 = null;
				String csNquery3_6 = null;
				String csNquery3_7 = null;
				String csNquery3_8 = null;
				String csNquery3_9 = null;
				String csNquery3_10 = null;
				String csNquery3_11 = null;
				String csNquery3_12 = null;
				String csNquery3_13 = null;
				String csNquery3_14 = null;
				String csNquery3_15 = null;
				String csNquery3_16 = null;
				String csNquery3_17 = null;
				String csNquery3_18 = null;
				String csNquery3_19 = null;
				String csNquery3_20 = null;
				

				String csCquery0 = null;

				String csCquery1_1 = null;
				String csCquery1_2 = null;
				String csCquery1_3 = null;
				String csCquery1_4 = null;
				String csCquery1_5 = null;
				String csCquery1_6 = null;

				String csCquery2_1 = null;
				String csCquery2_2 = null;
				String csCquery2_3 = null;
				String csCquery2_4 = null;
				String csCquery2_5 = null;
				String csCquery2_6 = null;
				String csCquery2_7 = null;
				String csCquery2_8 = null;
				String csCquery2_9 = null;
				String csCquery2_10 = null;
				String csCquery2_11 = null;
				String csCquery2_12 = null;
				String csCquery2_13 = null;
				String csCquery2_14 = null;
				String csCquery2_15 = null;

				String csCquery3_1 = null;
				String csCquery3_2 = null;
				String csCquery3_3 = null;
				String csCquery3_4 = null;
				String csCquery3_5 = null;
				String csCquery3_6 = null;
				String csCquery3_7 = null;
				String csCquery3_8 = null;
				String csCquery3_9 = null;
				String csCquery3_10 = null;
				String csCquery3_11 = null;
				String csCquery3_12 = null;
				String csCquery3_13 = null;
				String csCquery3_14 = null;
				String csCquery3_15 = null;
				String csCquery3_16 = null;
				String csCquery3_17 = null;
				String csCquery3_18 = null;
				String csCquery3_19 = null;
				String csCquery3_20 = null;

				csNquery0 = n1 + n2 + n3 + n4 + n5 + n6;
				csCquery0 = c1 + c2 + c3 + c4 + c5 + c6;

				csNquery1_1 = "_" + n2 + n3 + n4 + n5 + n6;
				csNquery1_2 = n1 + "_" + n3 + n4 + n5 + n6;
				csNquery1_3 = n1 + n2 + "_" + n4 + n5 + n6;
				csNquery1_4 = n1 + n2 + n3 + "_" + n5 + n6;
				csNquery1_5 = n1 + n2 + n3 + n4 + "_" + n6;
				csNquery1_6 = n1 + n2 + n3 + n4 + n5 + "_";

				csCquery1_1 = "_" + c2 + c3 + c4 + c5 + c6;
				csCquery1_2 = c1 + "_" + c3 + c4 + c5 + c6;
				csCquery1_3 = c1 + c2 + "_" + c4 + c5 + c6;
				csCquery1_4 = c1 + c2 + c3 + "_" + c5 + c6;
				csCquery1_5 = c1 + c2 + c3 + c4 + "_" + c6;
				csCquery1_6 = c1 + c2 + c3 + c4 + c5 + "_";

				csNquery2_1 = "_" + "_" + n3 + n4 + n5 + n6;
				csNquery2_2 = "_" + n2 + "_" + n4 + n5 + n6;
				csNquery2_3 = "_" + n2 + n3 + "_" + n5 + n6;
				csNquery2_4 = "_" + n2 + n3 + n4 + "_" + n6;
				csNquery2_5 = "_" + n2 + n3 + n4 + n5 + "_";
				csNquery2_6 = n1 + "_" + "_" + n4 + n5 + n6;
				csNquery2_7 = n1 + "_" + n3 + "_" + n5 + n6;
				csNquery2_8 = n1 + "_" + n3 + n4 + "_" + n6;
				csNquery2_9 = n1 + "_" + n3 + n4 + n5 + "_";
				csNquery2_10 = n1 + n2 + "_" + "_" + n5 + n6;
				csNquery2_11 = n1 + n2 + "_" + n4 + "_" + n6;
				csNquery2_12 = n1 + n2 + "_" + n4 + n5 + "_";
				csNquery2_13 = n1 + n2 + n3 + "_" + "_" + n6;
				csNquery2_14 = n1 + n2 + n3 + "_" + n5 + "_";
				csNquery2_15 = n1 + n2 + n3 + n4 + "_" + "_";

				csCquery2_1 = "_" + "_" + c3 + c4 + c5 + c6;
				csCquery2_2 = "_" + c2 + "_" + c4 + c5 + c6;
				csCquery2_3 = "_" + c2 + c3 + "_" + c5 + c6;
				csCquery2_4 = "_" + c2 + c3 + c4 + "_" + c6;
				csCquery2_5 = "_" + c2 + c3 + c4 + c5 + "_";
				csCquery2_6 = c1 + "_" + "_" + c4 + c5 + c6;
				csCquery2_7 = c1 + "_" + c3 + "_" + c5 + c6;
				csCquery2_8 = c1 + "_" + c3 + c4 + "_" + c6;
				csCquery2_9 = c1 + "_" + c3 + c4 + c5 + "_";
				csCquery2_10 = c1 + c2 + "_" + "_" + c5 + c6;
				csCquery2_11 = c1 + c2 + "_" + c4 + "_" + c6;
				csCquery2_12 = c1 + c2 + "_" + c4 + c5 + "_";
				csCquery2_13 = c1 + c2 + c3 + "_" + "_" + c6;
				csCquery2_14 = c1 + c2 + c3 + "_" + c5 + "_";
				csCquery2_15 = c1 + c2 + c3 + c4 + "_" + "_";
				
				csNquery3_1 = "_" + "_" + "_" + n4 + n5 + n6;				
				csNquery3_2 = "_" + "_" + n3 + "_" + n5 + n6;
				csNquery3_3 = "_" + "_" + n3 + n4 + "_" + n6;
				csNquery3_4 = "_" + "_" + n3 + n4 + n5 + "_";			
				csNquery3_5 = "_" + n2 + "_" + "_" + n5 + n6;
				csNquery3_6 = "_" + n2 + "_" + n4 + "_" + n6;
				csNquery3_7 = "_" + n2 + "_" + n4 + n5 + "_";
				csNquery3_8 = "_" + n2 + n3 + "_" + "_" + n6;
				csNquery3_9 = "_" + n2 + n3 + "_" + n5 + "_";			
				csNquery3_10 = "_" + n2 + n3 + n4 + "_" + "_";
				csNquery3_11 = n1 + "_" + "_" + "_" + n5 + n6;
				csNquery3_12 = n1 + "_" + "_" + n4 + "_" + n6;
				csNquery3_13 = n1 + "_" + "_" + n4 + n5 + "_";
				csNquery3_14 = n1 + "_" + n3 + "_" + "_" + n6;
				csNquery3_15 = n1 + "_" + n3 + "_" + n5 + "_";
				csNquery3_16 = n1 + "_" + n3 + n4 + "_" + "_";			
				csNquery3_17 = n1 + n2 + "_" + "_" + "_" + n6;
				csNquery3_18 = n1 + n2 + "_" + "_" + n5 + "_";
				csNquery3_19 = n1 + n2 + "_" + n4 + "_" + "_";		
				csNquery3_20 = n1 + n2 + n3 + "_" + "_" + "_";
				
				csCquery3_1 = "_" + "_" + "_" + c4 + c5 + c6;				
				csCquery3_2 = "_" + "_" + c3 + "_" + c5 + c6;
				csCquery3_3 = "_" + "_" + c3 + c4 + "_" + c6;
				csCquery3_4 = "_" + "_" + c3 + c4 + c5 + "_";			
				csCquery3_5 = "_" + c2 + "_" + "_" + c5 + c6;
				csCquery3_6 = "_" + c2 + "_" + c4 + "_" + c6;
				csCquery3_7 = "_" + c2 + "_" + c4 + c5 + "_";
				csCquery3_8 = "_" + c2 + c3 + "_" + "_" + c6;
				csCquery3_9 = "_" + c2 + c3 + "_" + c5 + "_";			
				csCquery3_10 = "_" + c2 + c3 + c4 + "_" + "_";
				csCquery3_11 = c1 + "_" + "_" + "_" + c5 + c6;
				csCquery3_12 = c1 + "_" + "_" + c4 + "_" + c6;
				csCquery3_13 = c1 + "_" + "_" + c4 + c5 + "_";
				csCquery3_14 = c1 + "_" + c3 + "_" + "_" + c6;
				csCquery3_15 = c1 + "_" + c3 + "_" + c5 + "_";
				csCquery3_16 = c1 + "_" + c3 + c4 + "_" + "_";			
				csCquery3_17 = c1 + c2 + "_" + "_" + "_" + c6;
				csCquery3_18 = c1 + c2 + "_" + "_" + c5 + "_";
				csCquery3_19 = c1 + c2 + "_" + c4 + "_" + "_";		
				csCquery3_20 = c1 + c2 + c3 + "_" + "_" + "_";
				
				String output = null;
				
				
				if (mismatch == 0) {
					output = "CleavageSite_Sequence = '" + csNquery0 + "' OR CleavageSite_Sequence = '" + csCquery0 + "'";
				} else if (mismatch == 1) {
					output = "CleavageSite_Sequence = '" + csNquery0 + "' OR CleavageSite_Sequence = '" + csCquery0
							+ "CleavageSite_Sequence = '" + csNquery1_1 + "' OR CleavageSite_Sequence = '" + csNquery1_2 + "' OR CleavageSite_Sequence = '" + csNquery1_3
							+ "CleavageSite_Sequence = '" + csNquery1_4 + "' OR CleavageSite_Sequence = '" + csNquery1_5 + "' OR CleavageSite_Sequence = '" + csNquery1_6
							+ "CleavageSite_Sequence = '" + csCquery1_1 + "' OR CleavageSite_Sequence = '" + csCquery1_2 + "' OR CleavageSite_Sequence = '" + csCquery1_3
							+ "CleavageSite_Sequence = '" + csCquery1_4 + "' OR CleavageSite_Sequence = '" + csCquery1_5 + "' OR CleavageSite_Sequence = '" + csCquery1_6;
				} else if (mismatch == 2) {
					output = "CleavageSite_Sequence = '" + csNquery0 + "' OR CleavageSite_Sequence = '" + csCquery0
							+ "CleavageSite_Sequence = '" + csNquery1_1 + "' OR CleavageSite_Sequence = '" + csNquery1_2 + "' OR CleavageSite_Sequence = '" + csNquery1_3
							+ "CleavageSite_Sequence = '" + csNquery1_4 + "' OR CleavageSite_Sequence = '" + csNquery1_5 + "' OR CleavageSite_Sequence = '" + csNquery1_6
							+ "CleavageSite_Sequence = '" + csCquery1_1 + "' OR CleavageSite_Sequence = '" + csCquery1_2 + "' OR CleavageSite_Sequence = '" + csCquery1_3
							+ "CleavageSite_Sequence = '" + csCquery1_4 + "' OR CleavageSite_Sequence = '" + csCquery1_5 + "' OR CleavageSite_Sequence = '" + csCquery1_6
							+ "CleavageSite_Sequence = '" + csNquery2_1 + "' OR CleavageSite_Sequence = '" + csNquery2_2 + "' OR CleavageSite_Sequence = '" + csNquery2_3
							+ "CleavageSite_Sequence = '" + csNquery2_4 + "' OR CleavageSite_Sequence = '" + csNquery2_5 + "' OR CleavageSite_Sequence = '" + csNquery2_6
							+ "CleavageSite_Sequence = '" + csNquery2_7 + "' OR CleavageSite_Sequence = '" + csNquery2_8 + "' OR CleavageSite_Sequence = '" + csNquery2_9
							+ "CleavageSite_Sequence = '" + csNquery2_10 + "' OR CleavageSite_Sequence = '" + csNquery2_11 + "' OR CleavageSite_Sequence = '" + csNquery2_12
							+ "CleavageSite_Sequence = '" + csNquery2_13 + "' OR CleavageSite_Sequence = '" + csNquery2_14 + "' OR CleavageSite_Sequence = '" + csNquery2_15
							+ "CleavageSite_Sequence = '" + csCquery2_1 + "' OR CleavageSite_Sequence = '" + csCquery2_2 + "' OR CleavageSite_Sequence = '" + csCquery2_3
							+ "CleavageSite_Sequence = '" + csCquery2_4 + "' OR CleavageSite_Sequence = '" + csCquery2_5 + "' OR CleavageSite_Sequence = '" + csCquery2_6
							+ "CleavageSite_Sequence = '" + csCquery2_7 + "' OR CleavageSite_Sequence = '" + csCquery2_8 + "' OR CleavageSite_Sequence = '" + csCquery2_9
							+ "CleavageSite_Sequence = '" + csCquery2_10 + "' OR CleavageSite_Sequence = '" + csCquery2_11 + "' OR CleavageSite_Sequence = '" + csCquery2_12
							+ "CleavageSite_Sequence = '" + csCquery2_13 + "' OR CleavageSite_Sequence = '" + csCquery2_14 + "' OR CleavageSite_Sequence = '" + csCquery2_15;
				} else if (mismatch == 3) {
					output = "CleavageSite_Sequence = '" + csNquery0 + "' OR CleavageSite_Sequence = '" + csCquery0
							+ "CleavageSite_Sequence = '" + csNquery1_1 + "' OR CleavageSite_Sequence = '" + csNquery1_2 + "' OR CleavageSite_Sequence = '" + csNquery1_3
							+ "CleavageSite_Sequence = '" + csNquery1_4 + "' OR CleavageSite_Sequence = '" + csNquery1_5 + "' OR CleavageSite_Sequence = '" + csNquery1_6
							+ "CleavageSite_Sequence = '" + csCquery1_1 + "' OR CleavageSite_Sequence = '" + csCquery1_2 + "' OR CleavageSite_Sequence = '" + csCquery1_3
							+ "CleavageSite_Sequence = '" + csCquery1_4 + "' OR CleavageSite_Sequence = '" + csCquery1_5 + "' OR CleavageSite_Sequence = '" + csCquery1_6
							+ "CleavageSite_Sequence = '" + csNquery2_1 + "' OR CleavageSite_Sequence = '" + csNquery2_2 + "' OR CleavageSite_Sequence = '" + csNquery2_3
							+ "CleavageSite_Sequence = '" + csNquery2_4 + "' OR CleavageSite_Sequence = '" + csNquery2_5 + "' OR CleavageSite_Sequence = '" + csNquery2_6
							+ "CleavageSite_Sequence = '" + csNquery2_7 + "' OR CleavageSite_Sequence = '" + csNquery2_8 + "' OR CleavageSite_Sequence = '" + csNquery2_9
							+ "CleavageSite_Sequence = '" + csNquery2_10 + "' OR CleavageSite_Sequence = '" + csNquery2_11 + "' OR CleavageSite_Sequence = '" + csNquery2_12
							+ "CleavageSite_Sequence = '" + csNquery2_13 + "' OR CleavageSite_Sequence = '" + csNquery2_14 + "' OR CleavageSite_Sequence = '" + csNquery2_15
							+ "CleavageSite_Sequence = '" + csCquery2_1 + "' OR CleavageSite_Sequence = '" + csCquery2_2 + "' OR CleavageSite_Sequence = '" + csCquery2_3
							+ "CleavageSite_Sequence = '" + csCquery2_4 + "' OR CleavageSite_Sequence = '" + csCquery2_5 + "' OR CleavageSite_Sequence = '" + csCquery2_6
							+ "CleavageSite_Sequence = '" + csCquery2_7 + "' OR CleavageSite_Sequence = '" + csCquery2_8 + "' OR CleavageSite_Sequence = '" + csCquery2_9
							+ "CleavageSite_Sequence = '" + csCquery2_10 + "' OR CleavageSite_Sequence = '" + csCquery2_11 + "' OR CleavageSite_Sequence = '" + csCquery2_12
							+ "CleavageSite_Sequence = '" + csCquery2_13 + "' OR CleavageSite_Sequence = '" + csCquery2_14 + "' OR CleavageSite_Sequence = '" + csCquery2_15
					+ "CleavageSite_Sequence = '" + csNquery3_1 + "' OR CleavageSite_Sequence = '" + csNquery3_2 + "' OR CleavageSite_Sequence = '" + csNquery3_3
					+ "CleavageSite_Sequence = '" + csNquery3_4 + "' OR CleavageSite_Sequence = '" + csNquery3_5 + "' OR CleavageSite_Sequence = '" + csNquery3_6
					+ "CleavageSite_Sequence = '" + csNquery3_7 + "' OR CleavageSite_Sequence = '" + csNquery3_8 + "' OR CleavageSite_Sequence = '" + csNquery3_9
					+ "CleavageSite_Sequence = '" + csNquery3_10 + "' OR CleavageSite_Sequence = '" + csNquery3_11 + "' OR CleavageSite_Sequence = '" + csNquery3_12
					+ "CleavageSite_Sequence = '" + csNquery3_13 + "' OR CleavageSite_Sequence = '" + csNquery3_14 + "' OR CleavageSite_Sequence = '" + csNquery3_15
					+ "CleavageSite_Sequence = '" + csNquery3_16 + "' OR CleavageSite_Sequence = '" + csNquery3_17 + "' OR CleavageSite_Sequence = '" + csNquery3_18
					+ "CleavageSite_Sequence = '" + csNquery3_19 + "' OR CleavageSite_Sequence = '" + csNquery3_20
					+ "CleavageSite_Sequence = '" + csCquery3_1 + "' OR CleavageSite_Sequence = '" + csCquery3_2 + "' OR CleavageSite_Sequence = '" + csCquery3_3
					+ "CleavageSite_Sequence = '" + csCquery3_4 + "' OR CleavageSite_Sequence = '" + csCquery3_5 + "' OR CleavageSite_Sequence = '" + csCquery3_6
					+ "CleavageSite_Sequence = '" + csCquery3_7 + "' OR CleavageSite_Sequence = '" + csCquery3_8 + "' OR CleavageSite_Sequence = '" + csCquery3_9
					+ "CleavageSite_Sequence = '" + csCquery3_10 + "' OR CleavageSite_Sequence = '" + csCquery3_11 + "' OR CleavageSite_Sequence = '" + csCquery3_12
					+ "CleavageSite_Sequence = '" + csCquery3_13 + "' OR CleavageSite_Sequence = '" + csCquery3_14 + "' OR CleavageSite_Sequence = '" + csCquery3_15
					+ "CleavageSite_Sequence = '" + csCquery3_16 + "' OR CleavageSite_Sequence = '" + csCquery3_17 + "' OR CleavageSite_Sequence = '" + csCquery3_18
					+ "CleavageSite_Sequence = '" + csCquery3_19 + "' OR CleavageSite_Sequence = '" + csCquery3_20;
					
				}
				
				String queryCleavage = "SELECT * FROM CLEAVAGESITE WHERE " +  output;
				System.out.println(queryCleavage);
				
				
			} else if (searchReq.getRequestnature().equalsIgnoreCase(
					"csRequest")) {
				String peptideuni = searchReq.getCsuniprot();
				int pepstart = searchReq.getCspepstart();
				int pepend = searchReq.getCspepend();
				System.out.println(peptideuni + "aaaaa");
				System.out.println(pepstart + "aaaa");
				System.out.println(pepend + "aaaaa");
				String fullsequence = null;
				String pepsequence = null;

				// Retrieve PEPTIDE SEQUENCE IN UNIPROT
				Document xml = checkSubUniprot(peptideuni);

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
					}
					System.out.println(fullsequence);
					pepsequence = fullsequence.substring(pepstart - 1, pepend);
					System.out.println(pepsequence);
				}
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