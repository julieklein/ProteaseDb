package com.google.gwt.proteasedb.client;
import com.google.gwt.user.client.rpc.IsSerializable;


public class SearchRequest implements IsSerializable{
public String proteininputsymbol;
public String peptideinputsequence;
public String requestnature;
public String peptideinputnumber;
public String peptideinputuni;
public int peptideinputstart;
public int peptideinputend;
public int peptideinputmismatch;
public int getPeptideinputmismatch() {
	return peptideinputmismatch;
}

public void setPeptideinputmismatch(int peptideinputmismatch) {
	this.peptideinputmismatch = peptideinputmismatch;
}

public String getPeptideinputuni() {
	return peptideinputuni;
}

public void setPeptideinputuni(String peptideinputuni) {
	this.peptideinputuni = peptideinputuni;
}

public int getPeptideinputstart() {
	return peptideinputstart;
}

public void setPeptideinputstart(int peptideinputstart) {
	this.peptideinputstart = peptideinputstart;
}

public int getPeptideinputend() {
	return peptideinputend;
}

public void setPeptideinputend(int peptideinputend) {
	this.peptideinputend = peptideinputend;
}



public String csuniprot;
public int cspepstart;
public int cspepend;
public String CS_proteasespecies;
public String CS_substratespecies;

public String getCS_proteasespecies() {
	return CS_proteasespecies;
}

public void setCS_proteasespecies(String cS_proteasespecies) {
	CS_proteasespecies = cS_proteasespecies;
}

public String getCS_substratespecies() {
	return CS_substratespecies;
}

public void setCS_substratespecies(String cS_substratespecies) {
	CS_substratespecies = cS_substratespecies;
}

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
