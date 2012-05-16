package com.google.gwt.proteasedb.client;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CleavagesiteData implements IsSerializable{

	public ProteaseData protease;
	public SubstrateData substrate;
	public String cleavageSite;
	public int p1;
	public int p1prime;

	
	/**
     * constructor
     */
    public CleavagesiteData() {
            // nothing to do
    }

	
}
