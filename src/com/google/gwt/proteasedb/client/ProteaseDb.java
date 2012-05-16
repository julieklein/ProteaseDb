package com.google.gwt.proteasedb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProteaseDb implements EntryPoint {
	// rpc init Var 
    private DBConnectionAsync callProvider;
    // main widget panel
    private VerticalPanel mainPanel = new VerticalPanel();   
    private VerticalPanel pProteaseTable = new VerticalPanel();
    private HorizontalPanel searchpanel = new HorizontalPanel();
    private TextBox searchBox = new TextBox();
    private Button searchButton = new Button("Search");

    
    
    // table for the bible info
    private Grid grid = null;

	
	public void onModuleLoad() {
		// TODO Auto-generated method stub 
		// init the rpc
		
		searchpanel.add(searchBox);
		searchpanel.add(searchButton);
		searchpanel.addStyleName("pProteaseTable");
		mainPanel.add(searchpanel);
		pProteaseTable.addStyleName("pProteaseTable");
		mainPanel.add(pProteaseTable);
	    
       
		 rpcInit();
//        
//        // start the process
//        getProteaseInfo();

        RootPanel.get("protease").add(mainPanel);
        
     // Move cursor focus to the input box.
	    searchBox.setFocus(true);
	    
	 // Listen for mouse events on the Add button.
	    searchButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	//keep search to setup prepared statement
	    	  getPreparedStatement();
	    	  //start the process
	          getProteaseInfo();
	    	
	      }
	    });
	    
	    // Listen for keyboard events in the input box.
	    searchBox.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	        	getPreparedStatement();
	        	 //start the process
	            getProteaseInfo();
	        	
	        }
	      }
	    });
	   
			
		}
	
	/**
     * Init the RPC provider
     * 
     * NOTE: ./public/MySQLConn.gwt.xml - determines the servlet context
     * read more of my gwtTomcat documentation in http://gwt-examples.googlecode.com
     */
	private void rpcInit() {
    callProvider = (DBConnectionAsync) GWT.create(DBConnection.class);
    ServiceDefTarget target = (ServiceDefTarget) callProvider;
    
    // The path 'MySQLConnService' is determined in ./public/MySQLConn.gwt.xml
    // This path directs Tomcat to listen for this context on the server side, 
    // thus intercepting the rpc requests.
    String moduleRelativeURL = GWT.getModuleBaseURL() + "MySQLConnection";
    target.setServiceEntryPoint(moduleRelativeURL);
}
	 /**
	   * Add protease to proteaseTable. Executed when the user clicks the searchButton or
	   * presses enter in the searchBox.
	   */
	
	private Input getPreparedStatement(){
		String symbol = searchBox.getText().toUpperCase().trim();
        searchBox.setFocus(true);    
        System.out.println(symbol+"1");
        Input input = new Input();
        return input;
       
			}
	
	private void getProteaseInfo() {

//        // draw loading
//        loading.show();
        
        AsyncCallback callback = new AsyncCallback() {
                
                // fail
                public void onFailure(Throwable ex) {
                        RootPanel.get().add(new HTML(ex.toString()));
                }

                // success
                public void onSuccess(Object result) {

                        ProteaseData[] proteaseData = (ProteaseData[]) result; 
                       
                        
                        // draw bible info
                        drawProteaseInfo(proteaseData);
                        
                        
//                        // hide loading
//                        loading.hide();

                }
        };
                        
        // remote procedure call to the server to get the bible info
        callProvider.getProteaseInfo(callback);
}
	 /**
     * draw bible info to screen after rpc callback
     * 
     * @param bibleData
     */
    private void drawProteaseInfo(ProteaseData[] proteaseData) {
            
            // if null nothing to do, then exit
            // this will prevent errors from showing up
            if (proteaseData == null) {
                    return;
            }
            
            int rows = proteaseData.length;
            
            // set up the table the bible info will go into. 
            // I already init the grid var above so I can reference it other methods in this instance.
            grid = new Grid(rows+1, 4);
            pProteaseTable.add(grid);
                    
            Label name = new Label("Name");
            Label symbol = new Label("Symbol");
            Label uniprot = new Label("Uniprot ID");
            Label ecnumber = new Label("EC Number");
            
            // tool-tip hover
            name.setTitle("Protease Name");
            symbol.setTitle("Protease Symbol");
            uniprot.setTitle("Protease Uniprot ID");
            ecnumber.setTitle("Protease EC Number");
            
            // label row - Starts with 0 ordinal
            grid.setWidget(0, 0, name);
            grid.setWidget(0, 1, symbol);
            grid.setWidget(0, 2, uniprot);
            grid.setWidget(0, 3, ecnumber);
            
            // go through the protease database
            for (int i = 0; i < rows; i++) {
                    
                                        
                    grid.setWidget(i+1, 0, new HTML(proteaseData[i].P_NL_Name));
                    grid.setWidget(i+1, 1, new HTML(proteaseData[i].P_Symbol));
                    grid.setWidget(i+1, 2, new HTML(proteaseData[i].P_Uniprotid));
                    grid.setWidget(i+1, 3, new HTML(proteaseData[i].P_Ecnumber));
                    
                    // row style
                    boolean even = i % 2 == 0;
                    String style = "";
                    if (even == true) {
                            style = "rs-even";
                    } else {
                            style = "rs-odd";               
                    }
                    grid.getCellFormatter().setStyleName(i+1, 0, "proteaseNumericColumnBIG");
                    grid.getCellFormatter().setStyleName(i+1, 1, "proteaseNumericColumnSMALL");
                    grid.getCellFormatter().setStyleName(i+1, 2, "proteaseNumericColumnSMALL");
                    grid.getCellFormatter().setStyleName(i+1, 3, "proteaseNumericColumnSMALL");
            }
            
            grid.setStyleName("proteaseTable");
            grid.getRowFormatter().addStyleName(0, "proteaseTableHeader");
            grid.getCellFormatter().addStyleName(0, 0, "proteaseNumericColumnBIG");
            grid.getCellFormatter().addStyleName(0, 1, "proteaseNumericColumnSMALL");
            grid.getCellFormatter().addStyleName(0, 2, "proteaseNumericColumnSMALL");
            grid.getCellFormatter().addStyleName(0, 3, "proteaseNumericColumnSMALL");

            
            grid.setCellPadding(6);
            
//            // observer grid
//            grid.addTableListener(this);
            
            

            
    }
    




}
