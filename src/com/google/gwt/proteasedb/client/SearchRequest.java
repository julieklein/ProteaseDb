package com.google.gwt.proteasedb.client;
import com.google.gwt.user.client.rpc.IsSerializable;


public class SearchRequest implements IsSerializable{
public String proteininputsymbol;
public String peptideinputsequence;
public String requestnature;
public String peptideinputnumber;
public String csuniprot;
public int cspepstart;
public int cspepend;

public String getCsuniprot() {
	return csuniprot;
}

public void setCsuniprot(String csuniprot) {
	this.csuniprot = csuniprot;
}

public int getCspepstart() {
	return cspepstart;
}

public void setCspepstart(int cspepstart) {
	this.cspepstart = cspepstart;
}

public int getCspepend() {
	return cspepend;
}

public void setCspepend(int cspepend) {
	this.cspepend = cspepend;
}

public String getPeptideinputnumber() {
	return peptideinputnumber;
}

public void setPeptideinputnumber(String peptideinputnumber) {
	this.peptideinputnumber = peptideinputnumber;
}

public String getRequestnature() {
	return requestnature;
}

public void setRequestnature(String requestnature) {
	this.requestnature = requestnature;
}

public String getProteininputsymbol() {
	return proteininputsymbol;
}

public void setProteininputsymbol(String proteininputsymbol) {
	this.proteininputsymbol = proteininputsymbol;
}

public String getPeptideinputsequence() {
	return peptideinputsequence;
}

public void setPeptideinputsequence(String peptideinputsequence) {
	this.peptideinputsequence = peptideinputsequence;
}



public SearchRequest() {
	
}

}
