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
