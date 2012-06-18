package com.google.gwt.proteasedb.client;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ResultbySubstrateData implements IsSerializable{

	public ProteaseData getProtease() {
		return protease;
	}


	public void setProtease(ProteaseData protease) {
		this.protease = protease;
	}

	public String nature;
	public String input;

	public String getInput() {
		return input;
	}


	public void setInput(String input) {
		this.input = input;
	}


	public String getNature() {
		return nature;
	}


	public void setNature(String nature) {
		this.nature = nature;
	}

	public ProteaseData protease;
	public PeptideData getPeptide() {
		return peptide;
	}


	public void setPeptide(PeptideData peptide) {
		this.peptide = peptide;
	}


	public PeptideData peptide;



	public SubstrateData substrate;
	public String cleavageSite;
	public int p1;
	public int p1prime;
	public String externallink;
	public String pmid;
	public String csvalidity_1;
	public String dispepvalidity_1;
	public String strupepvalidity_1;
	public String subvalidity_1;
	
	public String CS_pattern;
	public String CS_database;
	public String CS_substrate;
	public int mismatch;
	public int getMismatch() {
		return mismatch;
	}


	public void setMismatch(int mismatch) {
		this.mismatch = mismatch;
	}


	public String getCSInput_substrate() {
		return CSInput_substrate;
	}


	public void setCSInput_substrate(String cSInput_substrate) {
		CSInput_substrate = cSInput_substrate;
	}


	public String getCSInput_number() {
		return CSInput_number;
	}


	public void setCSInput_number(String cSInput_number) {
		CSInput_number = cSInput_number;
	}


	public int getCSInput_start() {
		return CSInput_start;
	}


	public void setCSInput_start(int cSInput_start) {
		CSInput_start = cSInput_start;
	}


	public int getCSInput_end() {
		return CSInput_end;
	}


	public void setCSInput_end(int cSInput_end) {
		CSInput_end = cSInput_end;
	}

	public String CS_terminus;
	public String CS_NorCterm;
	
	public String CSInput_substrate; 
	public String CSInput_number;
	public int CSInput_start;
	public int CSInput_end;
	public String CS_mismatch;
	
	public String getCleavageSite() {
		return cleavageSite;
	}


	public void setCleavageSite(String cleavageSite) {
		this.cleavageSite = cleavageSite;
	}


	public int getP1() {
		return p1;
	}


	public void setP1(int p1) {
		this.p1 = p1;
	}


	public int getP1prime() {
		return p1prime;
	}


	public void setP1prime(int p1prime) {
		this.p1prime = p1prime;
	}


	public String getExternallink() {
		return externallink;
	}


	public void setExternallink(String externallink) {
		this.externallink = externallink;
	}


	public String getPmid() {
		return pmid;
	}


	public void setPmid(String pmid) {
		this.pmid = pmid;
	}


	public String getCS_pattern() {
		return CS_pattern;
	}


	public void setCS_pattern(String cS_pattern) {
		CS_pattern = cS_pattern;
	}


	public String getCS_database() {
		return CS_database;
	}


	public void setCS_database(String cS_database) {
		CS_database = cS_database;
	}


	public String getCS_terminus() {
		return CS_terminus;
	}


	public void setCS_terminus(String cS_terminus) {
		CS_terminus = cS_terminus;
	}


	public String getCS_NorCterm() {
		return CS_NorCterm;
	}


	public void setCS_NorCterm(String cS_NorCterm) {
		CS_NorCterm = cS_NorCterm;
	}


	public String getSubvalidity_1() {
		return subvalidity_1;
	}


	public void setSubvalidity_1(String subvalidity_1) {
		this.subvalidity_1 = subvalidity_1;
	}


	public String getCsvalidity_1() {
		return csvalidity_1;
	}


	public void setCsvalidity_1(String csvalidity_1) {
		this.csvalidity_1 = csvalidity_1;
	}


	public String getDispepvalidity_1() {
		return dispepvalidity_1;
	}


	public void setDispepvalidity_1(String dispepvalidity_1) {
		this.dispepvalidity_1 = dispepvalidity_1;
	}


	public String getStrupepvalidity_1() {
		return strupepvalidity_1;
	}


	public void setStrupepvalidity_1(String strupepvalidity_1) {
		this.strupepvalidity_1 = strupepvalidity_1;
	}


	public String getEntryValidity() {
		return entryValidity;
	}


	public void setEntryValidity(String entryValidity) {
		this.entryValidity = entryValidity;
	}


	public String entryValidity;
	
	/**
     * constructor
     */
    public ResultbySubstrateData() {
            // nothing to do
    }


	public SubstrateData getSubstrate() {
		return substrate;
	}


	public void setSubstrate(SubstrateData substrate) {
		this.substrate = substrate;
	}




	
}
