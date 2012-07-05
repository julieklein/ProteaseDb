package com.google.gwt.proteasedb.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * I use this class to store my mysql recordset in an object that is an array.
 * This will give an example of how I pass data from the server to client in an object, 
 * one of my favourites for its simplicity.
 * 
 * @author branflake2267
 *
 */


public class SubstrateData implements IsSerializable{
	
	//fields to store data
	public String S_NL_Name;
	public String S_Symbol;
	public String S_Uniprotid;
	public String S_Taxon;
	
	
	/**
     * constructor
     */
    public SubstrateData() {
            // nothing to do
    }

	

}