package com.google.gwt.proteasedb.client;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ResultbySubstrateData implements IsSerializable{

	public ProteaseData getProtease() {
		return protease;
	}


	public void setProtease(ProteaseData protease) {
		this.protease = protease;
	}


	public ProteaseData protease;
	

	public SubstrateData substrate;
	public String cleavageSite;
	public int p1;
	public int p1prime;
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
