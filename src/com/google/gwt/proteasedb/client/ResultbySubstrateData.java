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
