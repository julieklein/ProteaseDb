package com.google.gwt.proteasedb.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

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
import com.google.gwt.user.client.ui.TextArea;

public class ProteaseDb implements EntryPoint {
	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel pResultPanel = new VerticalPanel();
	// private VerticalPanel pCleavagesitePanel = new VerticalPanel();
	private HorizontalPanel searchpanel = new HorizontalPanel();
	private TextArea searchBox = new TextArea();
	private Button searchButton = new Button("Search");
	private DecoratedStackPanel dStackPanel = new DecoratedStackPanel();
	private Label lSubstrate = new Label("Substrate");
	private Label lSubstrateOutput = new Label();
	private Label lResult = new Label("Result");
	private Label lError = new Label();

	// table for the bible info
	private Grid cleavageSiteGrid = null;
	private Grid substrateGrid = null;
	private Grid peptideGrid = null;
	
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// init the rpc

		searchpanel.add(searchBox);
		searchBox.setHeight("68px");

		searchpanel.add(searchButton);
		searchButton.addStyleDependentName("search");
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

		// // Listen for keyboard events in the input box.
		// searchBox.addKeyPressHandler(new KeyPressHandler() {
		// public void onKeyPress(KeyPressEvent event) {
		// if (event.getCharCode() == KeyCodes.KEY_ENTER) {
		// generateSearchRequest();
		//
		// }
		// }
		// });

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
		String search = searchBox.getText().toUpperCase().trim();

		String splitSearch[] = search.split("\n");
		SortedSet<String> set = new TreeSet<String>();

		for (String searchSymbol : splitSearch) {
			searchSymbol.toUpperCase().trim();
			set.add(searchSymbol);
			System.out.println(searchSymbol + "uuu");
		}

		int size = set.size();
		System.out.println(size);

		Iterator iterator = set.iterator();
		SearchRequest[] searchRequest = new SearchRequest[size];
		int i = 0;

		while (iterator.hasNext()) {
			String value = iterator.next().toString();
			String valuesplit[] = value.split("\n");
			System.out.println(value);

			for (String string : valuesplit) {
				searchRequest[i] = new SearchRequest();
				searchRequest[i].setInput(string);
				System.out.println(string + "zzzz");
				i++;
			}
		}

		searchBox.setFocus(true);
		searchBox.setText("");

		getResultbySubstrateInfo(searchRequest);

	}

	private void getResultbySubstrateInfo(SearchRequest[] searchRequest) {

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
		callProvider.getResultbySubstrateInfo(searchRequest, callback);
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
				"Sorry, there is no cleavage site")) {
			// pResultPanel.add(lSubstrate);
			lSubstrate.addStyleName("Label");
			pResultPanel.add(lError);
			lError.addStyleName("lError");
		} else {

			// set up the table the bible info will go into.
			// I already init the grid var above so I can reference it other
			// methods
			// in this instance.

			String substrateOutput = resultbySubstrateData[0].substrate.S_Symbol
					+ ", "
					+ resultbySubstrateData[0].substrate.S_NL_Name
					+ " ("
					+ resultbySubstrateData[0].substrate.S_Uniprotid
					+ ")";
			lSubstrateOutput = new Label(substrateOutput);

			// pResultPanel.add(lSubstrate);
			pResultPanel.add(lSubstrateOutput);
			lSubstrateOutput.addStyleName("lSubstrateOutput");
			int numbercs = 0;
			int numberpep = 0;

			for (ResultbySubstrateData resultbySubstrateData2 : resultbySubstrateData) {
				if (resultbySubstrateData2.getNature().equals("cleavagesite")) {
					numbercs++;
				}
				if (resultbySubstrateData2.getNature().equals("peptide")) {
					numberpep++;
				}
				System.out.println(resultbySubstrateData2);
			}

			System.out.println(numbercs + "numbercs");
			System.out.println(numberpep + "numberpep");

			ResultbySubstrateData[] resultcs = new ResultbySubstrateData[numbercs];
			ResultbySubstrateData[] resultpep = new ResultbySubstrateData[numberpep];

			
			int k = 0;
			int l = 0;
			
			for (int i=0;i<resultbySubstrateData.length; i++) {
				if (resultbySubstrateData[i].getNature().equals("cleavagesite")) {
					resultcs[k] = resultbySubstrateData[i];
					k++;
				}else {
					if(resultbySubstrateData[i].getNature().equals("peptide"))
						resultpep[l] = resultbySubstrateData[i];
					l++;
				}
			}
			
			
			System.out.println(resultpep.length + "resultpep");
			System.out.println(resultcs.length + "resultcs");

			pResultPanel.add(lResult);
			lSubstrate.addStyleName("Label");
			lResult.addStyleName("Label");
			pResultPanel.add(dStackPanel);
			dStackPanel.addStyleName("dStackPanel");

			int rowscs = resultcs.length;
			cleavageSiteGrid = new Grid(rowscs + 1, 8);

			dStackPanel.add(cleavageSiteGrid);

			Label substrate = new Label("Substrate");
			Label cssequence = new Label("Cleavage Site");
			Label p1 = new Label("P1");
			Label p1prime = new Label("P1'");
			Label name = new Label("Protease Name");
			Label symbol = new Label("Protease Symbol");
			Label uniprot = new Label("Protease ID");
			Label ecnumber = new Label("EC Number");

			// label row - Starts with 0 ordinal
			cleavageSiteGrid.setWidget(0, 0, substrate);
			cleavageSiteGrid.setWidget(0, 1, cssequence);
			cleavageSiteGrid.setWidget(0, 2, p1);
			cleavageSiteGrid.setWidget(0, 3, p1prime);
			cleavageSiteGrid.setWidget(0, 4, name);
			cleavageSiteGrid.setWidget(0, 5, symbol);
			cleavageSiteGrid.setWidget(0, 6, uniprot);
			cleavageSiteGrid.setWidget(0, 7, ecnumber);

			// go through the cleavagesite database
			for (int i = 0; i < rowscs; i++) {
				String substrateSymbol = resultcs[i].substrate.S_Symbol;
				String cleavageSite = resultcs[i].cleavageSite;
				String begin = cleavageSite.substring(0, 3);
				String end = cleavageSite.substring(5, 8);
				String middle1 = cleavageSite.substring(3, 4);
				String middle2 = cleavageSite.substring(4, 5);

				cleavageSiteGrid.setWidget(i + 1, 0, new HTML(substrateSymbol));

				cleavageSiteGrid.setWidget(i + 1, 1, new HTML("<p>" + begin
						+ "<strong><u>" + middle1 + "\u00A6" + middle2
						+ "</u></strong>" + end + "</p>"));

				String sp1 = Integer.toString(resultcs[i].p1);
				String sp1prime = Integer.toString(resultcs[i].p1prime);
				cleavageSiteGrid.setWidget(i + 1, 2, new HTML(sp1));
				cleavageSiteGrid.setWidget(i + 1, 3, new HTML(sp1prime));
				cleavageSiteGrid.setWidget(i + 1, 4, new HTML(
						resultcs[i].protease.P_Symbol));
				cleavageSiteGrid.setWidget(i + 1, 5, new HTML(
						resultcs[i].protease.P_NL_Name));
				cleavageSiteGrid.setWidget(i + 1, 6, new HTML(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultcs[i].protease.P_Uniprotid
								+ "\">"
								+ resultcs[i].protease.P_Uniprotid
								+ "</a>"));
				cleavageSiteGrid.setWidget(i + 1, 7, new HTML(
						resultcs[i].protease.P_Ecnumber));

				// row style
				boolean even = i % 2 == 0;
				String style = "";
				if (even == true) {
					style = "rs-even";
				} else {
					style = "rs-odd";
				}
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 0,
						"cleavagetsiteTableColor");
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 1,
						"cleavagetsiteTableColor");
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 2,
						"cleavagetsiteTableColor");
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 3,
						"cleavagetsiteTableColor");
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 5,
						"cleavagetsiteTableColor");
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 4,
						"cleavagetsiteTableColor");
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 6,
						"cleavagetsiteTableColor");
				cleavageSiteGrid.getCellFormatter().setStyleName(i + 1, 7,
						"cleavagetsiteTableColor");

				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 0,
						"cleavagesiteTableMedium");
				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 1,
						"cleavagesiteTableMedium");
				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 2,
						"cleavagesiteTableSmall");
				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 3,
						"cleavagesiteTableSmall");
				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 5,
						"cleavagesiteTableBig");
				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 4,
						"cleavagesiteTableMedium");
				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 6,
						"cleavagesiteTableMedium");
				cleavageSiteGrid.getCellFormatter().addStyleName(i + 1, 7,
						"cleavagesiteTableMedium");
			}

			cleavageSiteGrid.addStyleName("cleavagesiteTable");
			cleavageSiteGrid.setCellPadding(6);

			cleavageSiteGrid.getCellFormatter().addStyleName(0, 0,
					"cleavagesiteTableMedium");
			cleavageSiteGrid.getCellFormatter().addStyleName(0, 1,
					"cleavagesiteTableMedium");
			cleavageSiteGrid.getCellFormatter().addStyleName(0, 2,
					"cleavagesiteTableSmall");
			cleavageSiteGrid.getCellFormatter().addStyleName(0, 3,
					"cleavagesiteTableSmall");
			cleavageSiteGrid.getCellFormatter().addStyleName(0, 5,
					"cleavagesiteTableBig");
			cleavageSiteGrid.getCellFormatter().addStyleName(0, 4,
					"cleavagesiteTableMedium");
			cleavageSiteGrid.getCellFormatter().addStyleName(0, 6,
					"cleavagesiteTableMedium");
			cleavageSiteGrid.getCellFormatter().addStyleName(0, 7,
					"cleavagesiteTableMedium");

			cleavageSiteGrid.getRowFormatter().addStyleName(0,
					"cleavagesiteTableHeader");
			// // observer grid
			// grid.addTableListener(this);
			
			
			int rowspep = resultpep.length;
			peptideGrid = new Grid(rowspep + 1, 5);

			dStackPanel.add(peptideGrid);

			Label pepsubstrate = new Label("Substrate");	
			Label start = new Label("Start");
			Label end = new Label("End");
			Label disease = new Label("Disease");
			Label regulation = new Label("Regulation");


			// label row - Starts with 0 ordinal
			peptideGrid.setWidget(0, 0, pepsubstrate);
			peptideGrid.setWidget(0, 1, start);
			peptideGrid.setWidget(0, 2, end);
			peptideGrid.setWidget(0, 3, disease);
			peptideGrid.setWidget(0, 4, regulation);


			// go through the cleavagesite database
			for (int i = 0; i < rowspep; i++) {
				String pepsubstrateSymbol = resultpep[i].substrate.S_Symbol;
			
				String pepdisease = resultpep[i].peptide.disease;
				String pepregulation = resultpep[i].peptide.regulation;
				

				peptideGrid.setWidget(i + 1, 0, new HTML(pepsubstrateSymbol));

				String pepstart = Integer.toString(resultpep[i].peptide.start);
				String pepend = Integer.toString(resultpep[i].peptide.end);
				peptideGrid.setWidget(i + 1, 1, new HTML(pepstart));
				peptideGrid.setWidget(i + 1, 2, new HTML(pepend));
				peptideGrid.setWidget(i + 1, 3, new HTML(
						pepdisease));
				if (pepregulation.equals("Down")) {
				peptideGrid.setWidget(i + 1, 4, new HTML("<div style=\"background:#008F29;border:1px solid black;width:30px;height:30px;margin-left:15px\"></div>" + pepregulation));
				}else if (pepregulation.equals("Up")) {
				peptideGrid.setWidget(i + 1, 4, new HTML("<div style=\"background:red;border:1px solid black;width:30px;height:30px;margin-left:15px\"></div>" + pepregulation));
				}
				// row style
				boolean even = i % 2 == 0;
				String style = "";
				if (even == true) {
					style = "rs-even";
				} else {
					style = "rs-odd";
				}
				
				peptideGrid.getRowFormatter().addStyleName(i+1, style);
				peptideGrid.getCellFormatter().addStyleName(i + 1, 0,
						"cleavagetsiteTableColor");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 1,
						"cleavagetsiteTableColor");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 2,
						"cleavagetsiteTableColor");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 3,
						"cleavagetsiteTableColor");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 4,
						"cleavagetsiteTableColor");
				

				peptideGrid.getCellFormatter().addStyleName(i + 1, 0,
						"cleavagesiteTableMedium");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 1,
						"cleavagesiteTableSmall");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 2,
						"cleavagesiteTableSmall");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 3,
						"cleavagesiteTableBig");
				peptideGrid.getCellFormatter().addStyleName(i + 1, 4,
						"cleavagesiteTableSmall");
			}

			peptideGrid.setStyleName("cleavagesiteTable");
			peptideGrid.setCellPadding(6);
			
			

			peptideGrid.getCellFormatter().addStyleName(0, 0,
					"cleavagesiteTableMedium");
			peptideGrid.getCellFormatter().addStyleName(0, 1,
					"cleavagesiteTableSmall");
			peptideGrid.getCellFormatter().addStyleName(0, 2,
					"cleavagesiteTableSmall");
			peptideGrid.getCellFormatter().addStyleName(0, 3,
					"cleavagesiteTableBig");
			peptideGrid.getCellFormatter().addStyleName(0, 4,
					"cleavagesiteTableSmall");
			

			peptideGrid.getRowFormatter().addStyleName(0,
					"cleavagesiteTableHeader");
		}
	
	}

}
