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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.cellview.client.CellTable;

public class ProteaseDb implements EntryPoint {
	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel pSubstratePanel = new VerticalPanel();
	private VerticalPanel pCleavagesitePanel = new VerticalPanel();
	private HorizontalPanel searchpanel = new HorizontalPanel();
	private TextBox searchBox = new TextBox();
	private Button searchButton = new Button("Search");

	// table for the bible info
	private Grid grid = null;
	private Grid substrateGrid = null;

	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// init the rpc

		searchpanel.add(searchBox);
		searchpanel.add(searchButton);
		searchpanel.addStyleName("pProteaseTable");
		mainPanel.add(searchpanel);
		searchpanel.setWidth("192px");
		pSubstratePanel.addStyleName("pProteaseTable");

		mainPanel.add(pSubstratePanel);
		pSubstratePanel.setSize("205px", "207px");
		mainPanel.add(pCleavagesitePanel);

		rpcInit();
		//
		// // start the process
		// getProteaseInfo();

		RootPanel rootPanel = RootPanel.get("protease");
		rootPanel.add(mainPanel);

		// Move cursor focus to the input box.
		searchBox.setFocus(true);

		// Listen for mouse events on the Add button.
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				generateSearchRequest();
				// start the process

			}
		});

		// Listen for keyboard events in the input box.
		searchBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					generateSearchRequest();

				}
			}
		});

	}

	/**
	 * Init the RPC provider
	 * 
	 * NOTE: ./public/MySQLConn.gwt.xml - determines the servlet context read
	 * more of my gwtTomcat documentation in http://gwt-examples.googlecode.com
	 */
	private void rpcInit() {
		callProvider = (DBConnectionAsync) GWT.create(DBConnection.class);
		ServiceDefTarget target = (ServiceDefTarget) callProvider;

		// The path 'MySQLConnService' is determined in
		// ./public/MySQLConn.gwt.xml
		// This path directs Tomcat to listen for this context on the server
		// side,
		// thus intercepting the rpc requests.
		String moduleRelativeURL = GWT.getModuleBaseURL() + "MySQLConnection";
		target.setServiceEntryPoint(moduleRelativeURL);
	}

	/**
	 * Add protease to proteaseTable. Executed when the user clicks the
	 * searchButton or presses enter in the searchBox.
	 */

	private void generateSearchRequest() {
		String symbol = searchBox.getText().toUpperCase().trim();
		searchBox.setFocus(true);
		searchBox.setText("");
		System.out.println(symbol + "1");
		SearchRequest input = new SearchRequest();
		input.setInput(symbol);
		getCleavagesiteInfo(input);

	}

	private void getCleavagesiteInfo(SearchRequest input) {

		// // draw loading
		// loading.show();
		pSubstratePanel.clear();
		pCleavagesitePanel.clear();
		AsyncCallback callback = new AsyncCallback() {

			// fail
			public void onFailure(Throwable ex) {
				RootPanel.get().add(new HTML(ex.toString()));
			}

			// success
			public void onSuccess(Object result) {

				CleavagesiteData[] cleavagesiteData = (CleavagesiteData[]) result;

				// draw bible info
				drawCleavagesiteInfo(cleavagesiteData);

				// // hide loading
				// loading.hide();

			}
		};

		// remote procedure call to the server to get the bible info
		callProvider.getCleavagesiteInfo(input, callback);
	}

	/**
	 * draw bible info to screen after rpc callback
	 * 
	 * @param bibleData
	 */
	private void drawCleavagesiteInfo(CleavagesiteData[] cleavagesiteData) {

		// if null nothing to do, then exit
		// this will prevent errors from showing up
		if (cleavagesiteData == null) {
			return;
		}

		int rows = cleavagesiteData.length;

		// set up the table the bible info will go into.
		// I already init the grid var above so I can reference it other methods
		// in this instance.

		substrateGrid = new Grid(rows + 1, 3);
		pSubstratePanel.add(substrateGrid);
		pCleavagesitePanel.addStyleName("pProteaseTable");

		grid = new Grid(rows + 1, 7);
		pCleavagesitePanel.add(grid);
		pCleavagesitePanel.addStyleName("pProteaseTable");
		
		Label cssequence = new Label("CleavageSite sequence");
		Label p1 = new Label("P1");
		Label p1prime = new Label("P1'");
		Label name = new Label("Name");
		Label symbol = new Label("Symbol");
		Label uniprot = new Label("Uniprot ID");
		Label ecnumber = new Label("EC Number");

		// // tool-tip hover
		// name.setTitle("Protease Name");
		// symbol.setTitle("Protease Symbol");
		// uniprot.setTitle("Protease Uniprot ID");
		// ecnumber.setTitle("Protease EC Number");

		// label row - Starts with 0 ordinal
		grid.setWidget(0, 0, cssequence);
		grid.setWidget(0, 1, p1);
		grid.setWidget(0, 2, p1prime);
		grid.setWidget(0, 3, name);
		grid.setWidget(0, 4, symbol);
		grid.setWidget(0, 5, uniprot);
		grid.setWidget(0, 6, ecnumber);
		
		for (int i=0; i<1; i++){
			substrateGrid.setWidget(0, 0, new HTML(
					cleavagesiteData[i].substrate.S_NL_Name));
			substrateGrid.setWidget(0, 1, new HTML(
					cleavagesiteData[i].substrate.S_Symbol));
			substrateGrid.setWidget(0, 2, new HTML(
							cleavagesiteData[i].substrate.S_Uniprotid));
			// row style
						boolean even = i % 2 == 0;
						String style = "";
						if (even == true) {
							style = "rs-even";
						} else {
							style = "rs-odd";
						}
						substrateGrid.getCellFormatter().setStyleName(0, 0,
								"proteaseNumericColumnBIG");
						substrateGrid.getCellFormatter().setStyleName(0, 1,
								"proteaseNumericColumnSMALL");
						substrateGrid.getCellFormatter().setStyleName(0, 2,
								"proteaseNumericColumnSMALL");
						
					}
		

		// go through the protease database
		for (int i = 0; i < rows; i++) {
			grid.setWidget(i + 1, 0, new HTML(
					cleavagesiteData[i].cleavageSite));
			
			String sp1 = Integer.toString(cleavagesiteData[i].p1);
			String sp1prime = Integer.toString(cleavagesiteData[i].p1prime);
			
			grid.setWidget(i + 1, 1, new HTML(
					sp1));
			grid.setWidget(i + 1, 2, new HTML(
					sp1prime));
			grid.setWidget(i + 1, 3, new HTML(
					cleavagesiteData[i].protease.P_NL_Name));
			grid.setWidget(i + 1, 4, new HTML(
					cleavagesiteData[i].protease.P_Symbol));
			grid.setWidget(i + 1, 5, new HTML(
					cleavagesiteData[i].protease.P_Uniprotid));
			grid.setWidget(i + 1, 6, new HTML(
					cleavagesiteData[i].protease.P_Ecnumber));

			// row style
			boolean even = i % 2 == 0;
			String style = "";
			if (even == true) {
				style = "rs-even";
			} else {
				style = "rs-odd";
			}
			grid.getCellFormatter().setStyleName(i + 1, 0,
					"proteaseNumericColumnSMALL");
			grid.getCellFormatter().setStyleName(i + 1, 1,
					"proteaseNumericColumnSMALL");
			grid.getCellFormatter().setStyleName(i + 1, 2,
					"proteaseNumericColumnSMALL");
			grid.getCellFormatter().setStyleName(i + 1, 3,
					"proteaseNumericColumnBIG");
			grid.getCellFormatter().setStyleName(i + 1, 4,
					"proteaseNumericColumnSMALL");
			grid.getCellFormatter().setStyleName(i + 1, 5,
					"proteaseNumericColumnSMALL");
			grid.getCellFormatter().setStyleName(i + 1, 6,
					"proteaseNumericColumnSMALL");
		}

		grid.setStyleName("proteaseTable");
		grid.getRowFormatter().addStyleName(0, "proteaseTableHeader");
		grid.getCellFormatter().addStyleName(0, 0, "proteaseNumericColumnBIG");
		grid.getCellFormatter()
				.addStyleName(0, 1, "proteaseNumericColumnSMALL");
		grid.getCellFormatter()
				.addStyleName(0, 2, "proteaseNumericColumnSMALL");
		grid.getCellFormatter()
				.addStyleName(0, 3, "proteaseNumericColumnSMALL");

		grid.setCellPadding(6);
		
		substrateGrid.setStyleName("proteaseTable");
		substrateGrid.setCellPadding(6);

		// // observer grid
		// grid.addTableListener(this);

	}
}
