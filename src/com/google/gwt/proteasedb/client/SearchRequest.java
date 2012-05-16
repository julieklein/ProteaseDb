package com.google.gwt.proteasedb.client;
import com.google.gwt.user.client.rpc.IsSerializable;


public class SearchRequest implements IsSerializable{
public String input;

public String getInput() {
	return input;
}

public void setInput(String input) {
	this.input = input;
}

public SearchRequest() {
	
}

}
