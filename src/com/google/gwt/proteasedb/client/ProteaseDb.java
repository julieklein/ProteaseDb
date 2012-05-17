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
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.DecoratedStackPanel;

public class ProteaseDb implements EntryPoint {
	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel pResultPanel = new VerticalPanel();
	// private VerticalPanel pCleavagesitePanel = new VerticalPanel();
	private HorizontalPanel searchpanel = new HorizontalPanel();
	private TextBox searchBox = new TextBox();
	private Button searchButton = new Button("Search");
	private DecoratedStackPanel dStackPanel = new DecoratedStackPanel();
	private Label lSubstrate = new Label("Substrate: ");
	private Label lResult = new Label("Result: ");
	private Label lError = new Label();

	// table for the bible info
	private Grid grid = null;
	private Grid substrateGrid = null;

	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// init the rpc

		searchpanel.add(searchBox);

		searchpanel.add(searchButton);
		searchpanel.addStyleName("pSearchpanel");
		mainPanel.add(searchpanel);

		pResultPanel.addStyleName("pResultPanel");
		mainPanel.add(pResultPanel);
		pResultPanel.setWidth("700px");

		// pCleavagesitePanel.addStyleName("pCleavagesitePanel");
		// mainPanel.add(pCleavagesitePanel);

		rpcInit();
		//
		// // start the process
		// getProteaseInfo();

		RootPanel rootPanel = RootPanel.get("protease");
		rootPanel.add(mainPanel);

		// Move cursor focus to the input box.
		searchBox.setFocus(true);

		// Listen for keyboard events in the input box.
		searchBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					generateSearchRequest();

				}
			}
		});

		// Listen for mouse events on the Add button.
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				generateSearchRequest();
				// start the process

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
		getResultbySubstrateInfo(input);

	}

	private void getResultbySubstrateInfo(SearchRequest input) {

		// // draw loading
		// loading.show();
		pResultPanel.clear();
		dStackPanel.clear();
		// pCleavagesitePanel.clear();
		AsyncCallback callback = new AsyncCallback() {

			// fail
			public void onFailure(Throwable ex) {
				RootPanel.get().add(new HTML(ex.toString()));
			}

			// success
			public void onSuccess(Object result) {

				ResultbySubstrateData[] resultbySubstrate = (ResultbySubstrateData[]) result;

				// draw bible info
				drawResultbySubstrateInfo(resultbySubstrate);

				// // hide loading
				// loading.hide();

			}
		};

		// remote procedure call to the server to get the bible info
		callProvider.getResultbySubstrateInfo(input, callback);
	}

	/**
	 * draw bible info to screen after rpc callback
	 * 
	 * @param bibleData
	 */
	private void drawResultbySubstrateInfo(
			ResultbySubstrateData[] resultbySubstrateData) {

		// if null nothing to do, then exit
		// this will prevent errors from showing up
		if (resultbySubstrateData == null) {
			return;
		}

		String error = resultbySubstrateData[0].getEntryValidity();
		System.out.println(error);
		lError = new Label(error);
		
		
		if (resultbySubstrateData[0].getEntryValidity().contains(
				"Sorry, there is no information about")) {
			pResultPanel.add(lSubstrate);
			lSubstrate.addStyleName("Label");
			pResultPanel.add(lError);
			lError.addStyleName("lError");
		} else {
			if (resultbySubstrateData[0]
					.getEntryValidity()
					.equalsIgnoreCase(
							"Sorry, there is no cleavage site for this substrate in the database")) {
				substrateGrid = new Grid(1, 3);

				pResultPanel.add(lSubstrate);
				pResultPanel.add(substrateGrid);
				pResultPanel.add(lResult);
				lSubstrate.addStyleName("Label");
				lResult.addStyleName("Label");
				pResultPanel.add(dStackPanel);
				dStackPanel.addStyleName("dStackPanel");

				substrateGrid
						.setWidget(
								0,
								1,
								new HTML(
										resultbySubstrateData[0].substrate.S_NL_Name
												+ " ("
												+ resultbySubstrateData[0].substrate.S_Uniprotid
												+ ")"));
				substrateGrid.setWidget(0, 0, new HTML(
						resultbySubstrateData[0].substrate.S_Symbol + ","));

				substrateGrid.getCellFormatter().setStyleName(0, 1,
						"subtrateColumnBig");
				substrateGrid.getCellFormatter().setStyleName(0, 0,
						"subtrateColumnSmall");
				substrateGrid.setStyleName("substrateTable");
				substrateGrid.setCellPadding(6);
				dStackPanel.add(lError);
				lError.addStyleName("lError");

			} else {

				int rows = resultbySubstrateData.length;

				// set up the table the bible info will go into.
				// I already init the grid var above so I can reference it other
				// methods
				// in this instance.

				substrateGrid = new Grid(1, 3);

				pResultPanel.add(lSubstrate);
				pResultPanel.add(substrateGrid);
				pResultPanel.add(lResult);
				lSubstrate.addStyleName("Label");
				lResult.addStyleName("Label");
				pResultPanel.add(dStackPanel);
				dStackPanel.addStyleName("dStackPanel");

				grid = new Grid(rows + 1, 7);
				dStackPanel.add(grid);

				// // tool-tip hover
				// name.setTitle("Protease Name");
				// symbol.setTitle("Protease Symbol");
				// uniprot.setTitle("Protease Uniprot ID");
				// ecnumber.setTitle("Protease EC Number");

				// go through the substrate database

				// for (int i=0; i<1; i++){
				substrateGrid
						.setWidget(
								0,
								1,
								new HTML(
										resultbySubstrateData[0].substrate.S_NL_Name
												+ " ("
												+ resultbySubstrateData[0].substrate.S_Uniprotid
												+ ")"));
				substrateGrid.setWidget(0, 0, new HTML(
						resultbySubstrateData[0].substrate.S_Symbol + ","));

				// // row style
				// boolean even = i % 2 == 0;
				// String style = "";
				// if (even == true) {
				// style = "rs-even";
				// } else {
				// style = "rs-odd";
				// }
				substrateGrid.getCellFormatter().setStyleName(0, 1,
						"subtrateColumnBig");
				substrateGrid.getCellFormatter().setStyleName(0, 0,
						"subtrateColumnSmall");

				// }

				substrateGrid.setStyleName("substrateTable");
				substrateGrid.setCellPadding(6);

				Label cssequence = new Label("CleavageSite sequence");
				Label p1 = new Label("P1");
				Label p1prime = new Label("P1'");
				Label name = new Label("Name");
				Label symbol = new Label("Symbol");
				Label uniprot = new Label("Uniprot ID");
				Label ecnumber = new Label("EC Number");

				// label row - Starts with 0 ordinal
				grid.setWidget(0, 0, cssequence);
				grid.setWidget(0, 1, p1);
				grid.setWidget(0, 2, p1prime);
				grid.setWidget(0, 3, name);
				grid.setWidget(0, 4, symbol);
				grid.setWidget(0, 5, uniprot);
				grid.setWidget(0, 6, ecnumber);

				// go through the cleavagesite database
				for (int i = 0; i < rows; i++) {
					grid.setWidget(i + 1, 0, new HTML(
							resultbySubstrateData[i].cleavageSite));

					String sp1 = Integer.toString(resultbySubstrateData[i].p1);
					String sp1prime = Integer
							.toString(resultbySubstrateData[i].p1prime);

					grid.setWidget(i + 1, 1, new HTML(sp1));
					grid.setWidget(i + 1, 2, new HTML(sp1prime));
					grid.setWidget(i + 1, 3, new HTML(
							resultbySubstrateData[i].protease.P_NL_Name));
					grid.setWidget(i + 1, 4, new HTML(
							resultbySubstrateData[i].protease.P_Symbol));
					grid.setWidget(i + 1, 5, new HTML(
							resultbySubstrateData[i].protease.P_Uniprotid));
					grid.setWidget(i + 1, 6, new HTML(
							resultbySubstrateData[i].protease.P_Ecnumber));

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

				grid.setStyleName("cleavagesiteTable");
				grid.setCellPadding(6);

				grid.getRowFormatter().addStyleName(0, "proteaseTableHeader");
				grid.getCellFormatter().addStyleName(0, 0,
						"proteaseNumericColumnBIG");
				grid.getCellFormatter().addStyleName(0, 1,
						"proteaseNumericColumnSMALL");
				grid.getCellFormatter().addStyleName(0, 2,
						"proteaseNumericColumnSMALL");
				grid.getCellFormatter().addStyleName(0, 3,
						"proteaseNumericColumnSMALL");

				// // observer grid
				// grid.addTableListener(this);
			}
		}
	}
}
