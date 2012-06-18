package com.google.gwt.proteasedb.client;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import java_cup.parse_action;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DisclosurePanelImages;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.view.client.ListDataProvider;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.source.tree.NewClassTree;

public class ProteasiXEntryPoint implements EntryPoint {

	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel

	private TabPanel mainTabPanel = new TabPanel();
	// private Label loading = new Label("loading please wait...");
	private Image loading = new Image();
	private Label lbl = new Label();
	private HorizontalPanel ploading = new HorizontalPanel();
	private Label lResult = new Label("Result View");

	// All that is necessary for PROTEINSEARCH WIDGET (1)
	private FlowPanel proteinTab_1 = new FlowPanel();
	private HorizontalPanel searchpanel_1 = new HorizontalPanel();
	private TextArea searchBox_1 = new TextArea();
	private Button searchButton_1 = new Button("Search");
	private TabPanel proteinTabPanel_1 = new TabPanel();
	private FlowPanel protein_summ_1 = new FlowPanel();
	private FlowPanel protein_cs_1 = new FlowPanel();
	private FlowPanel protein_pep_1 = new FlowPanel();

	private VerticalPanel pResultPanel_1 = new VerticalPanel();
	private DisclosurePanel dpDisPeptide_1 = new DisclosurePanel(
			"Click to See/Hide Peptides already associated with disease");
	private DisclosurePanel dpStruPeptide_1 = new DisclosurePanel(
			"Click to See/Hide Peptides associated with structure/function");

	// new css for CleavageSiteTable
	interface CSTableResources extends CellTable.Resources {
		@Source({ CellTable.Style.DEFAULT_CSS, "CleavageSiteTable.css" })
		CSTableStyle cellTableStyle();
	}

	interface CSTableStyle extends CellTable.Style {
	}

	// new css for PeptideTable
	interface PTableResources extends CellTable.Resources {
		@Source({ CellTable.Style.DEFAULT_CSS, "CleavageSiteTable.css" })
		PTableStyle cellTableStyle();
	}

	interface PTableStyle extends CellTable.Style {
	}

	// All that is necessary for PEPTIDESEARCH WIDGET (2)
	private FlowPanel peptideTab_2 = new FlowPanel();
	private TabPanel peptideTabPanel_2 = new TabPanel();
	private FlowPanel peptide_summ_2 = new FlowPanel();
	private FlowPanel peptide_cs_2 = new FlowPanel();
	private FlowPanel peptide_pep_2 = new FlowPanel();

	private HorizontalPanel search_2 = new HorizontalPanel();
	private VerticalPanel searchpanelId_2 = new VerticalPanel();
	private TextArea searchBoxId_2 = new TextArea();
	private VerticalPanel searchpanelUni_2 = new VerticalPanel();
	private TextArea searchBoxUni_2 = new TextArea();
	private VerticalPanel searchpanelStartEnd_2 = new VerticalPanel();
	private TextArea searchBoxStartEnd_2 = new TextArea();
	private VerticalPanel searchpanelSequence_2 = new VerticalPanel();
	private TextArea searchBoxSequence_2 = new TextArea();
	private VerticalPanel searchpanelList_2 = new VerticalPanel();
	private ListBox listcs_2 = new ListBox();
	private VerticalPanel buttonpanel_2 = new VerticalPanel();
	private Button exampleButton_2 = new Button("Example");
	private Button deleteButton_2 = new Button("Delete");
	private Button searchButton_2 = new Button("Search");

	private VerticalPanel pResultPanel_2 = new VerticalPanel();
	private DisclosurePanel dpExistingPeptide_2 = new DisclosurePanel(
			"Click to See/Hide Existing Peptides");
	private DisclosurePanel dpNotFoundPeptide_2 = new DisclosurePanel(
			"Click to See/Hide Not Found Peptides");

	private Label lnoExisting_2 = new Label(
			"Sorry, none of your peptides have been found in the database...");
	private Label lnoNotFound_2 = new Label(
			"All your peptides have been found in the database!");

	// // All that is necessary for CSSEARCH WIDGET (2)
	// private FlowPanel csTab_3 = new FlowPanel();
	// private VerticalPanel pResultPanel_3 = new VerticalPanel();
	// private HorizontalPanel searchpanel_3 = new HorizontalPanel();
	// private TextArea searchBox_3 = new TextArea();
	// private Button searchButton_3 = new Button("Search");

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		// TODO Auto-generated method stub

		// Set up loading

		// Hook up a load listener, so that we can be informed if the image
		// fails
		// to load.
		loading.addLoadListener(new LoadListener() {
			public void onError(Widget sender) {
				lbl.setText("An error occurred while loading.");
			}

			public void onLoad(Widget sender) {
			}
		});

		// Point the image at a real URL.
		loading.setUrl("/Images/ajax-loader.gif");
		ploading.add(loading);
		proteinTab_1.add(ploading);
		peptideTab_2.add(ploading);

		// Set Up the PROTEINSEARCH WIDGET
		searchpanel_1.add(searchBox_1);
		searchBox_1.setHeight("68px");
		searchpanel_1.add(searchButton_1);
		searchButton_1.addStyleDependentName("search");
		searchpanel_1.addStyleName("pSearchpanel");
		proteinTab_1.add(searchpanel_1);
		proteinTab_1
				.add(new HTML(
						"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
		pResultPanel_1.addStyleName("pResultPanel");
		proteinTab_1.add(pResultPanel_1);
		pResultPanel_1.setWidth("700px");

		// Set Up the PEPTIDESEARCH Widget
		searchpanelId_2.add(new HTML(
				"<div align=\"center\">Peptide ID<br />(optionnal)</div>"));
		searchpanelId_2.add(searchBoxId_2);
		searchpanelId_2.addStyleName("pSearchpanel");
		searchBoxId_2.setHeight("68px");
		searchBoxId_2.setWidth("68px");

		searchpanelUni_2.add(new HTML(
				"<div align=\"center\">Substrate<br />UniprotID</div>"));
		searchpanelUni_2.add(searchBoxUni_2);
		searchpanelUni_2.addStyleName("pSearchpanel");
		searchBoxUni_2.setHeight("68px");
		searchBoxUni_2.setWidth("100px");

		searchpanelStartEnd_2.add(new HTML(
				"<div align=\"center\">Peptide<br />Start-End</div>"));
		searchpanelStartEnd_2.add(searchBoxStartEnd_2);
		searchpanelStartEnd_2.addStyleName("pSearchpanel");
		searchBoxStartEnd_2.setHeight("68px");
		searchBoxStartEnd_2.setWidth("100px");

		searchpanelSequence_2
				.add(new HTML(
						"<div align=\"center\">Peptide sequence<br />(optionnal)</div>"));
		searchpanelSequence_2.add(searchBoxSequence_2);
		searchpanelSequence_2.addStyleName("pSearchpanel");
		searchBoxSequence_2.setHeight("68px");
		searchBoxSequence_2.setWidth("400px");

		searchpanelList_2
				.add(new HTML(
						"<div align=\"center\">Number of mismatches<br />in cleavage sites</div>"));
		listcs_2.addItem("0 mismatch");
		listcs_2.addItem("up to 1 mismatch");
		listcs_2.addItem("up to 2 mismatches");
		listcs_2.addItem("up to 3 mismatches");
		searchpanelList_2.add(listcs_2);
		searchpanelList_2.addStyleName("pSearchpanel");

		search_2.add(searchpanelId_2);
		search_2.add(searchpanelUni_2);
		search_2.add(searchpanelStartEnd_2);
		search_2.add(searchpanelSequence_2);
		search_2.add(searchpanelList_2);
		buttonpanel_2.add(new HTML("<div><br /><div>"));
		buttonpanel_2.add(exampleButton_2);
		exampleButton_2.addStyleDependentName("search");
		buttonpanel_2.add(new HTML("<div><br /><div>"));
		buttonpanel_2.add(deleteButton_2);
		deleteButton_2.addStyleDependentName("search");
		buttonpanel_2.add(new HTML("<div><br /><div>"));
		buttonpanel_2.add(searchButton_2);
		searchButton_2.addStyleDependentName("search");
		search_2.add(buttonpanel_2);
		search_2.addStyleName("pSearchpanel");

		peptideTab_2.add(search_2);

		peptideTab_2
				.add(new HTML(
						"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
		pResultPanel_2.addStyleName("pResultPanel");
		peptideTab_2.add(pResultPanel_2);
		pResultPanel_2.setWidth("700px");

		// Set Up the MainPanel
		mainTabPanel.removeStyleName("gwt-TabPanelBottom");
		mainTabPanel.add(peptideTab_2, "Search by peptide(s)");
		mainTabPanel.add(proteinTab_1, "Search by protein(s)");
		mainTabPanel.selectTab(0);
		mainTabPanel.setSize("1000px", "200px");

		// // Set Up the CSSEARCH Widget
		// searchpanel_3.add(searchBox_3);
		// searchBox_3.setHeight("68px");
		// searchpanel_3.add(searchButton_3);
		// searchButton_3.addStyleDependentName("search");
		// searchpanel_3.addStyleName("pSearchpanel");
		// csTab_3.add(searchpanel_3);
		// csTab_3.add(new HTML(
		// "<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
		// pResultPanel_3.addStyleName("pResultPanel");
		// csTab_3.add(pResultPanel_3);
		// pResultPanel_3.setWidth("700px");
		// mainTabPanel.add(csTab_3, "Search for cleavage sites");

		ploading.setVisible(false);
		RootPanel rootPanel = RootPanel.get("protease");
		rootPanel.add(mainTabPanel);

		// // start the process on PROTEINSEARCH WIDGET

		// Move cursor focus to the input box.
		searchBox_1.setFocus(true);

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
		searchButton_1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ploading.setVisible(true);
				// keep search to setup prepared statement
				// init rpc
				rpcInit();
				generateSearchRequest_1();
				// start the process

			}
		});

		dpDisPeptide_1.addOpenHandler(new OpenHandler() {

			@Override
			public void onOpen(OpenEvent event) {
				// TODO Auto-generated method stub

			}
		});

		dpStruPeptide_1.addOpenHandler(new OpenHandler() {

			@Override
			public void onOpen(OpenEvent event) {
				// TODO Auto-generated method stub

			}
		});

		// // start the process on PEPTIDESEARCH WIDGET

		// Listen for mouse events on the Add button.
		exampleButton_2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				searchBoxId_2.setText("Peptide*1\nPeptide*2");
				searchBoxUni_2.setText("P01375\nP01042");
				searchBoxStartEnd_2.setText("56-67\n6-23");
				searchBoxSequence_2.setText("UVWXYZAB");
			}
		});

		// Listen for mouse events on the Add button.
		deleteButton_2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// keep search to setup prepared statement
				// init rpc
				searchBoxId_2.setText("");
				searchBoxUni_2.setText("");
				searchBoxStartEnd_2.setText("");
				searchBoxSequence_2.setText("");
				listcs_2.setItemSelected(0, true);
			}
		});

		// Listen for mouse events on the Add button.
		searchButton_2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ploading.setVisible(true);
				// keep search to setup prepared statement
				// init rpc
				rpcInit();
				generateSearchRequest_2();
				// start the process

			}
		});

		// dpExistingPeptide_2.addOpenHandler(new OpenHandler() {
		//
		// @Override
		// public void onOpen(OpenEvent event) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// dpNotFoundPeptide_2.addOpenHandler(new OpenHandler() {
		//
		// @Override
		// public void onOpen(OpenEvent event) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// // // start the process on CSSEARCH WIDGET
		// // Listen for mouse events on the Add button.
		// searchButton_3.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// ploading.setVisible(true);
		// // keep search to setup prepared statement
		// // init rpc
		// rpcInit();
		// generateSearchRequest_3();
		// // start the process
		//
		// }
		// });

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

	private int generateSearchRequest_1() {
		String search = searchBox_1.getText().toUpperCase().trim();

		String splitSearch[] = search.split("\n");
		LinkedHashSet<String> set = new LinkedHashSet<String>();

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
				searchRequest[i].setProteininputsymbol(string);
				searchRequest[i].setRequestnature("proteinRequest");
				System.out.println(string + "zzzz");
				i++;
			}
		}

		searchBox_1.setFocus(true);
		// searchBox_1.setText("");

		getResulbySubstratetInfo_1(searchRequest);
		return size;
	}

	private void getResulbySubstratetInfo_1(SearchRequest[] searchRequest) {

		// empty if anything is there already
		pResultPanel_1.clear();
		protein_summ_1.clear();
		protein_cs_1.clear();
		protein_pep_1.clear();

		// dpPeptide_1.clear();
		// dpProtease_1.clear();

		AsyncCallback callback = new AsyncCallback() {

			// fail
			public void onFailure(Throwable ex) {
				RootPanel.get().add(new HTML(ex.toString()));
			}

			// success
			public void onSuccess(Object result) {

				ResultbySubstrateData[] resultbySubstrate = (ResultbySubstrateData[]) result;

				// draw PROTEIN info
				drawResultbySubstrateInfo_1(resultbySubstrate);

			}
		};

		// remote procedure call to the server to get the bible info
		callProvider.getResultInfo(searchRequest, callback);
	}

	/**
	 * draw bible info to screen after rpc callback
	 * 
	 * @param bibleData
	 */
	private void drawResultbySubstrateInfo_1(
			ResultbySubstrateData[] resultbySubstrateData) {

		// if null nothing to do, then exit
		// this will prevent errors from showing up
		if (resultbySubstrateData == null) {
			return;
		}

		// set up the table the bible info will go into.
		// I already init the grid var above so I can reference it other
		// methods
		// in this instance.

		pResultPanel_1.add(lResult);
		lResult.addStyleName("lResult");
		pResultPanel_1.add(proteinTabPanel_1);
		proteinTabPanel_1.add(protein_summ_1, "Summary");
		proteinTabPanel_1.add(protein_cs_1, "Cleavage sites");
		proteinTabPanel_1.add(protein_pep_1, "Peptides");
		dpDisPeptide_1.clear();
		dpStruPeptide_1.clear();
		protein_pep_1.add(dpDisPeptide_1);
		protein_pep_1.add(dpStruPeptide_1);
		dpDisPeptide_1.setOpen(true);
		dpStruPeptide_1.setOpen(true);
		proteinTabPanel_1.selectTab(0);

		// String substrateOutput = resultbySubstrateData[0].substrate.S_Symbol
		// + ", "
		// + resultbySubstrateData[0].substrate.S_NL_Name
		// + " ("
		// + resultbySubstrateData[0].substrate.S_Uniprotid
		// + ")";
		// lSubstrateOutput_1 = new Label(substrateOutput);
		//
		//
		// pResultPanel_1.add(lSubstrateOutput_1);
		// lSubstrateOutput_1.addStyleName("lSubstrateOutput");
		int numbercs = 0;
		int numberdispep = 0;
		int numbersumm = 0;
		int numberstrupep = 0;

		for (ResultbySubstrateData resultbySubstrateData2 : resultbySubstrateData) {
			if (resultbySubstrateData2.getNature().equals("cleavagesite")) {
				numbercs++;
			} else if (resultbySubstrateData2.getNature().equals("peptide")
					&& !(resultbySubstrateData2.peptide.disease == null)
					&& resultbySubstrateData2.peptide.structure == null) {
				numberdispep++;
			} else if (resultbySubstrateData2.getNature().equals("peptide")
					&& resultbySubstrateData2.peptide.disease == null) {
				numberstrupep++;

			}
			if (!(resultbySubstrateData2.getSubvalidity_1() == null)) {
				numbersumm++;
			}
			System.out.println(resultbySubstrateData2);
		}

		System.out.println(numbercs + "numbercs");
		System.out.println(numberdispep + "numberpep");
		System.out.println(numberstrupep + "numberstrupep");

		ResultbySubstrateData[] resultcs = new ResultbySubstrateData[numbercs];
		ResultbySubstrateData[] resultdispep = new ResultbySubstrateData[numberdispep];
		ResultbySubstrateData[] resultsumm = new ResultbySubstrateData[numbersumm];
		ResultbySubstrateData[] resultstrupep = new ResultbySubstrateData[numberstrupep];

		int k = 0;
		int l = 0;
		int m = 0;
		int n = 0;

		for (int i = 0; i < resultbySubstrateData.length; i++) {
			if (resultbySubstrateData[i].getNature().equals("cleavagesite")) {
				resultcs[k] = resultbySubstrateData[i];
				k++;
			} else if (resultbySubstrateData[i].getNature().equals("peptide")
					&& !(resultbySubstrateData[i].peptide.disease == null)
					&& resultbySubstrateData[i].peptide.structure == null) {
				resultdispep[l] = resultbySubstrateData[i];
				l++;
			} else if (resultbySubstrateData[i].getNature().equals("peptide")
					&& resultbySubstrateData[i].peptide.disease == null) {
				resultstrupep[n] = resultbySubstrateData[i];
				System.out.println(resultbySubstrateData[i].peptide.structure);
				n++;
			}
			if (!(resultbySubstrateData[i].getSubvalidity_1() == null)) {
				resultsumm[m] = resultbySubstrateData[i];
				m++;
			}
		}

		List<ResultbySubstrateData> resultdispeplist = Arrays
				.asList(resultdispep);
		List<ResultbySubstrateData> resultstrupeplist = Arrays
				.asList(resultstrupep);
		List<ResultbySubstrateData> resultcslist = Arrays.asList(resultcs);
		List<ResultbySubstrateData> resultsummlist = Arrays.asList(resultsumm);

		createSummTable_1(resultsumm, resultsummlist);
		createCleavageSiteTable_1(resultcs, resultcslist);
		createdisPeptideTable_1(resultdispep, resultdispeplist);
		createstruPeptideTable_1(resultstrupep, resultstrupeplist);
		ploading.setVisible(false);

	}

	private void createSummTable_1(ResultbySubstrateData[] resultsumm,
			List<ResultbySubstrateData> resultsummlist) {

		int rowsumm = resultsumm.length;
		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> summTable = new CellTable<ResultbySubstrateData>(
				rowsumm, ptableresources);
		summTable.setWidth("1000px");

		TextColumn<ResultbySubstrateData> inputCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = resultbySubstrateData.getInput();
				return input;
			}
		};

		Column<ResultbySubstrateData, SafeHtml> protCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = null;
				if (resultbySubstrateData.getSubvalidity_1().equals("yes")) {
					input = "<p align=\"left\">"
							+ resultbySubstrateData.substrate.S_Symbol + ", "
							+ resultbySubstrateData.substrate.S_NL_Name + ", "
							+ resultbySubstrateData.substrate.S_Uniprotid
							+ "</p>";
				} else if (resultbySubstrateData.getSubvalidity_1()
						.equals("no")) {
					input = "<p align=\"left\">Sorry, not found in the database...</p>";
				}

				return new SafeHtmlBuilder().appendHtmlConstant(input)
						.toSafeHtml();
			}

		};

		Column<ResultbySubstrateData, SafeHtml> csCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = null;
				if (resultbySubstrateData.getCsvalidity_1().equals("yes")) {
					input = "<div>"
							+ "<img src=\"/Images/check.png\"width=\"30\"/></div>";

				} else if (resultbySubstrateData.getCsvalidity_1().equals("no")) {
					input = "<div>"
							+ "<img src=\"/Images/cross.png\"width=\"25\"/></div>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(input)
						.toSafeHtml();
			}
		};

		Column<ResultbySubstrateData, SafeHtml> dispepCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = null;
				if (resultbySubstrateData.getDispepvalidity_1().equals("yes")) {
					input = "<div>"
							+ "<img src=\"/Images/check.png\"width=\"30\"/></div>";

				} else if (resultbySubstrateData.getDispepvalidity_1().equals(
						"no")) {
					input = "<div>"
							+ "<img src=\"/Images/cross.png\"width=\"25\"/></div>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(input)
						.toSafeHtml();
			}
		};

		Column<ResultbySubstrateData, SafeHtml> strupepCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = null;
				if (resultbySubstrateData.getStrupepvalidity_1().equals("yes")) {
					input = "<div>"
							+ "<img src=\"/Images/check.png\"width=\"30\"/></div>";

				} else if (resultbySubstrateData.getStrupepvalidity_1().equals(
						"no")) {
					input = "<div>"
							+ "<img src=\"/Images/cross.png\"width=\"25\"/></div>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(input)
						.toSafeHtml();
			}
		};
		// Add the columns.
		summTable.addColumn(inputCol, "Input");
		summTable.addColumn(protCol, "Protein");
		summTable.addColumn(csCol, "Cleavage Sites");
		summTable.addColumn(dispepCol, "Peptides in disease");
		summTable.addColumn(strupepCol, "Peptides with structure/function");
		protein_summ_1.add(summTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> summdataProvider = new ListDataProvider<ResultbySubstrateData>();

		// Connect the table to the data provider.
		summdataProvider.addDataDisplay(summTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<ResultbySubstrateData> summlist = summdataProvider.getList();
		for (ResultbySubstrateData summresultTable : resultsummlist) {
			summlist.add(summresultTable);
		}

	}

	private void createdisPeptideTable_1(ResultbySubstrateData[] resultdispep,
			List<ResultbySubstrateData> resultdispeplist) {

		int rowsdispep = resultdispep.length;

		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> dispepTable = new CellTable<ResultbySubstrateData>(
				rowsdispep, ptableresources);
		dispepTable.setWidth("900px");

		// Create columns
		Column<ResultbySubstrateData, SafeHtml> substrateCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String substrate = resultbySubstrateData.substrate.S_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.substrate.S_Uniprotid
								+ "\"target=\"_blank\">" + substrate + "</a>")
						.toSafeHtml();

			}

		};

		TextColumn<ResultbySubstrateData> startCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String start = null;
				if (resultbySubstrateData.peptide.start == 0) {
					start = "?";
				} else {
					start = Integer
							.toString(resultbySubstrateData.peptide.start);
				}
				return start;
			}

		};

		TextColumn<ResultbySubstrateData> endCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String end = null;
				if (resultbySubstrateData.peptide.end == 0) {
					end = "?";
				} else {
					end = Integer.toString(resultbySubstrateData.peptide.end);
				}
				return end;
			}

		};

		Column<ResultbySubstrateData, SafeHtml> sequenceCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String sequence = "<p align=\"left\"><font size=\"1\">"
						+ resultbySubstrateData.peptide.sequence
						+ "</font></p>";
				return new SafeHtmlBuilder().appendHtmlConstant(sequence)
						.toSafeHtml();
			}

		};

		TextColumn<ResultbySubstrateData> diseaseCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				return resultbySubstrateData.peptide.disease;
			}

		};

		Column<ResultbySubstrateData, SafeHtml> regulationCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String regulation = null;
				if (resultbySubstrateData.peptide.regulation.equals("Down")) {
					regulation = "<div style=\"background:#008F29;border:1px solid black;width:30px;height:30px;margin-left:26px\"></div>"
							+ resultbySubstrateData.peptide.regulation;
				} else if (resultbySubstrateData.peptide.regulation
						.equals("Up")) {
					regulation = "<div style=\"background:red;border:1px solid black;width:30px;height:30px;margin-left:26px\"></div>"
							+ resultbySubstrateData.peptide.regulation;
				} else if (resultbySubstrateData.peptide.regulation.equals("")) {
					regulation = "-";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(regulation)
						.toSafeHtml();
			}

		};

		// Column<ResultbySubstrateData, SafeHtml> extlinkCol = new
		// Column<ResultbySubstrateData, SafeHtml>(
		// new SafeHtmlCell()) {
		// @Override
		// public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData)
		// {
		// String external = resultbySubstrateData.externallink;
		// String externallink = "";
		// if (external.contains("uniprot")) {
		// externallink = "<a href=\"" + external
		// + "\"target=\"_blank\">"
		// + "<img src=\"/Images/logo.gif\"width=\"40\"/></a>";
		// }
		// return new SafeHtmlBuilder().appendHtmlConstant(externallink)
		// .toSafeHtml();
		//
		// }
		//
		// };

		Column<ResultbySubstrateData, SafeHtml> pmidCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String external = resultbySubstrateData.pmid;
				String pmid = "";
				if (external.contains(";")) {
					String valuesplit[] = external.split(";");
					int i = 0;
					for (String string : valuesplit) {
						valuesplit[i].trim();
						pmid = pmid
								+ "; "
								+ "<a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
								+ valuesplit[i] + "\"target=\"_blank\">"
								+ valuesplit[i] + "</a>";
						i++;
					}
					pmid = pmid.replaceFirst("; ", "");
				} else {
					pmid = "";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(pmid)
						.toSafeHtml();

			}

		};

		// Make the columns sortable.
		substrateCol.setSortable(true);
		startCol.setSortable(true);
		endCol.setSortable(true);
		diseaseCol.setSortable(true);
		regulationCol.setSortable(true);

		// Add the columns.
		dispepTable.addColumn(substrateCol, "\u25B2Substrate\u25BC");
		dispepTable.addColumn(startCol, "\u25B2Start\u25BC");
		dispepTable.addColumn(endCol, "\u25B2End\u25BC");
		dispepTable.addColumn(sequenceCol, "Sequence");
		dispepTable.addColumn(diseaseCol, "\u25B2Disease\u25BC");
		dispepTable.addColumn(regulationCol, "\u25B2Regulation\u25BC");
		// dispepTable.addColumn(extlinkCol, "External Link");
		dispepTable.addColumn(pmidCol, "Publication");

		dispepTable.setColumnWidth(substrateCol, 5, Unit.EM);
		dispepTable.setColumnWidth(startCol, 2, Unit.EM);
		dispepTable.setColumnWidth(endCol, 2, Unit.EM);
		dispepTable.setColumnWidth(sequenceCol, 20, Unit.EM);
		dispepTable.setColumnWidth(diseaseCol, 20, Unit.EM);
		dispepTable.setColumnWidth(regulationCol, 2, Unit.EM);

		dpDisPeptide_1.add(dispepTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> pepdataProvider = new ListDataProvider<ResultbySubstrateData>();

		// Connect the table to the data provider.
		pepdataProvider.addDataDisplay(dispepTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<ResultbySubstrateData> peplist = pepdataProvider.getList();
		for (ResultbySubstrateData pepresultTable : resultdispeplist) {
			peplist.add(pepresultTable);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> pepsubstrateColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		pepsubstrateColSortHandler.setComparator(substrateCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.substrate.S_Symbol
									.compareTo(o2.substrate.S_Symbol) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(pepsubstrateColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(substrateCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> startColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		startColSortHandler.setComparator(startCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.peptide.start).compareTo(
									Integer.toString(o2.peptide.start)) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(startColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(startCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> endColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		endColSortHandler.setComparator(endCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.peptide.end).compareTo(
									Integer.toString(o2.peptide.end)) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(endColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(endCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> diseaseColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		diseaseColSortHandler.setComparator(diseaseCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.peptide.disease
									.compareTo(o2.peptide.disease) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(diseaseColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(diseaseCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> regulationColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		regulationColSortHandler.setComparator(regulationCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.peptide.regulation
									.compareTo(o2.peptide.regulation) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(regulationColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(regulationCol);
	}

	private void createstruPeptideTable_1(
			ResultbySubstrateData[] resultstrupep,
			List<ResultbySubstrateData> resultstrupeplist) {

		int rowsstrupep = resultstrupep.length;

		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> strupepTable = new CellTable<ResultbySubstrateData>(
				rowsstrupep, ptableresources);
		strupepTable.setWidth("900px");

		// Create columns
		Column<ResultbySubstrateData, SafeHtml> substrateCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String substrate = resultbySubstrateData.substrate.S_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.substrate.S_Uniprotid
								+ "\"target=\"_blank\">" + substrate + "</a>")
						.toSafeHtml();

			}

		};

		TextColumn<ResultbySubstrateData> startCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String start = null;
				if (resultbySubstrateData.peptide.start == 0) {
					start = "?";
				} else {
					start = Integer
							.toString(resultbySubstrateData.peptide.start);
				}
				return start;
			}

		};

		TextColumn<ResultbySubstrateData> endCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String end = null;
				if (resultbySubstrateData.peptide.end == 0) {
					end = "?";
				} else {
					end = Integer.toString(resultbySubstrateData.peptide.end);
				}
				return end;
			}

		};

		Column<ResultbySubstrateData, SafeHtml> structureCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String structure = "<p align=\"left\">"
						+ resultbySubstrateData.peptide.structure + "</p>";
				return new SafeHtmlBuilder().appendHtmlConstant(structure)
						.toSafeHtml();
			}

		};

		Column<ResultbySubstrateData, SafeHtml> extlinkCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String external = resultbySubstrateData.externallink;
				String externallink = "";
				if (external.contains("uniprot")) {
					externallink = "<a href=\"" + external
							+ "\"target=\"_blank\">"
							+ "<img src=\"/Images/logo.gif\"width=\"40\"/></a>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(externallink)
						.toSafeHtml();

			}

		};

		// Make the columns sortable.
		substrateCol.setSortable(true);
		startCol.setSortable(true);
		endCol.setSortable(true);
		structureCol.setSortable(true);

		// Add the columns.

		strupepTable.addColumn(substrateCol, "\u25B2Substrate\u25BC");
		strupepTable.addColumn(startCol, "\u25B2Start\u25BC");
		strupepTable.addColumn(endCol, "\u25B2End\u25BC");
		strupepTable.addColumn(structureCol, "\u25B2Structure/Function\u25BC");
		strupepTable.addColumn(extlinkCol, "External Link");

		strupepTable.setColumnWidth(substrateCol, 5, Unit.EM);
		strupepTable.setColumnWidth(startCol, 2, Unit.EM);
		strupepTable.setColumnWidth(endCol, 2, Unit.EM);
		strupepTable.setColumnWidth(structureCol, 20, Unit.EM);
		strupepTable.setColumnWidth(extlinkCol, 5, Unit.EM);

		dpStruPeptide_1.add(strupepTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> pepdataProvider = new ListDataProvider<ResultbySubstrateData>();

		// Connect the table to the data provider.
		pepdataProvider.addDataDisplay(strupepTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<ResultbySubstrateData> strupeplist = pepdataProvider.getList();
		for (ResultbySubstrateData strupepresultTable : resultstrupeplist) {
			strupeplist.add(strupepresultTable);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.

		ListHandler<ResultbySubstrateData> strupepsubstrateColSortHandler = new ListHandler<ResultbySubstrateData>(
				strupeplist);
		strupepsubstrateColSortHandler.setComparator(substrateCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.substrate.S_Symbol
									.compareTo(o2.substrate.S_Symbol) : 1;
						}
						return -1;
					}
				});
		strupepTable.addColumnSortHandler(strupepsubstrateColSortHandler);

		// We know that the data is sorted alphabetically by default.
		strupepTable.getColumnSortList().push(substrateCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> strustartColSortHandler = new ListHandler<ResultbySubstrateData>(
				strupeplist);
		strustartColSortHandler.setComparator(startCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.peptide.start).compareTo(
									Integer.toString(o2.peptide.start)) : 1;
						}
						return -1;
					}
				});
		strupepTable.addColumnSortHandler(strustartColSortHandler);

		// We know that the data is sorted alphabetically by default.
		strupepTable.getColumnSortList().push(startCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> struendColSortHandler = new ListHandler<ResultbySubstrateData>(
				strupeplist);
		struendColSortHandler.setComparator(endCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.peptide.end).compareTo(
									Integer.toString(o2.peptide.end)) : 1;
						}
						return -1;
					}
				});
		strupepTable.addColumnSortHandler(struendColSortHandler);

		// We know that the data is sorted alphabetically by default.
		strupepTable.getColumnSortList().push(endCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> structureColSortHandler = new ListHandler<ResultbySubstrateData>(
				strupeplist);
		structureColSortHandler.setComparator(structureCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.peptide.structure
									.compareTo(o2.peptide.structure) : 1;
						}
						return -1;
					}
				});
		strupepTable.addColumnSortHandler(structureColSortHandler);

		// We know that the data is sorted alphabetically by default.
		strupepTable.getColumnSortList().push(structureCol);
	}

	private void createCleavageSiteTable_1(ResultbySubstrateData[] resultcs,
			List<ResultbySubstrateData> resultcslist) {
		int rowscs = resultcs.length;
		// cleavageSiteGrid = new Grid(rowscs + 1, 8);
		//
		CellTable.Resources cstableresources = GWT
				.create(CSTableResources.class);
		CellTable<ResultbySubstrateData> cleavageSiteTable = new CellTable<ResultbySubstrateData>(
				rowscs, cstableresources);
		cleavageSiteTable.setWidth("1000px");

		// Create columns
		Column<ResultbySubstrateData, SafeHtml> substrateCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String substrate = resultbySubstrateData.substrate.S_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.substrate.S_Uniprotid
								+ "\"target=\"_blank\">" + substrate + "</a>")
						.toSafeHtml();

			}

		};

		Column<ResultbySubstrateData, SafeHtml> cssequenceCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {

				String cleavageSite = resultbySubstrateData.cleavageSite;
				String begin = cleavageSite.substring(0, 2);
				String end = cleavageSite.substring(4, 6);
				String middle1 = cleavageSite.substring(2, 3);
				String middle2 = cleavageSite.substring(3, 4);

				return new SafeHtmlBuilder().appendHtmlConstant(
						"<p>" + begin + "<strong><u>" + middle1 + "\u00A6"
								+ middle2 + "</u></strong>" + end + "</p>")
						.toSafeHtml();
			}

		};

		TextColumn<ResultbySubstrateData> p1Col = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String sp1 = Integer.toString(resultbySubstrateData.p1);
				return sp1;
			}

		};

		TextColumn<ResultbySubstrateData> p1primeCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String sp1prime = Integer
						.toString(resultbySubstrateData.p1prime);
				return sp1prime;
			}

		};

		TextColumn<ResultbySubstrateData> protNameCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				return resultbySubstrateData.protease.P_NL_Name;
			}

		};

		Column<ResultbySubstrateData, SafeHtml> protSymbolCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String symbol = resultbySubstrateData.protease.P_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.protease.P_Uniprotid
								+ "\"target=\"_blank\">" + symbol + "</a>")
						.toSafeHtml();

			}

		};

		Column<ResultbySubstrateData, SafeHtml> extlinkCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String external = resultbySubstrateData.externallink;
				String valuesplit[] = external.split(";");
				int i = 0;
				String externallink = "";

				for (String string : valuesplit) {
					valuesplit[i].trim();
					if (valuesplit[i].contains("cutdb")) {
						externallink = externallink
								+ "<a href=\""
								+ valuesplit[i]
								+ "\"target=\"_blank\">"
								+ "<img src=\"/Images/PMAP-logoV8-2blue.png\"width=\"30\"/></a>";
					} else if (valuesplit[i].contains("uniprot")) {
						externallink = externallink
								+ "<a href=\""
								+ valuesplit[i]
								+ "\"target=\"_blank\">"
								+ "<img src=\"/Images/logo.gif\"width=\"40\"/></a>";
					}

					i++;
				}

				return new SafeHtmlBuilder().appendHtmlConstant(externallink)
						.toSafeHtml();

			}

		};

		Column<ResultbySubstrateData, SafeHtml> pmidCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String external = resultbySubstrateData.pmid;
				String valuesplit[] = external.split(";");
				int i = 0;
				String pmid = "";

				for (String string : valuesplit) {
					valuesplit[i].trim();
					pmid = pmid + "; "
							+ "<a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
							+ valuesplit[i] + "\"target=\"_blank\">"
							+ valuesplit[i] + "</a>";
					i++;
				}
				pmid = pmid.replaceFirst("; ", "");
				return new SafeHtmlBuilder().appendHtmlConstant(pmid)
						.toSafeHtml();

			}

		};

		// Make the columns sortable.
		substrateCol.setSortable(true);
		p1Col.setSortable(true);
		p1primeCol.setSortable(true);
		protSymbolCol.setSortable(true);
		protNameCol.setSortable(true);

		// Add the columns.
		cleavageSiteTable.addColumn(substrateCol, "\u25B2Substrate\u25BC");
		cleavageSiteTable.addColumn(cssequenceCol, "Cleavage Site");
		cleavageSiteTable.addColumn(p1Col, "\u25B2P1\u25BC");
		cleavageSiteTable.addColumn(p1primeCol, "\u25B2P1'\u25BC");
		cleavageSiteTable.addColumn(protSymbolCol,
				"Protease \u25B2Symbol\u25BC");
		cleavageSiteTable.addColumn(protNameCol, "\u25B2Protease Name\u25BC");
		cleavageSiteTable.addColumn(extlinkCol, "External Link");
		cleavageSiteTable.addColumn(pmidCol, "Publication");
		// cleavageSiteTable.addColumn(protIDCol, "Protease ID");
		// cleavageSiteTable.addColumn(protECCol, "EC Number");

		cleavageSiteTable.setColumnWidth(substrateCol, 5, Unit.EM);
		cleavageSiteTable.setColumnWidth(cssequenceCol, 5, Unit.EM);
		cleavageSiteTable.setColumnWidth(p1Col, 2, Unit.EM);
		cleavageSiteTable.setColumnWidth(p1primeCol, 2, Unit.EM);
		cleavageSiteTable.setColumnWidth(protSymbolCol, 5, Unit.EM);
		cleavageSiteTable.setColumnWidth(protNameCol, 20, Unit.EM);
		// cleavageSiteTable.setColumnWidth(protIDCol, 5, Unit.EM);
		// cleavageSiteTable.setColumnWidth(protECCol, 5, Unit.EM);

		protein_cs_1.add(cleavageSiteTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> csdataProvider = new ListDataProvider<ResultbySubstrateData>();

		// Connect the table to the data provider.
		csdataProvider.addDataDisplay(cleavageSiteTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<ResultbySubstrateData> cslist = csdataProvider.getList();
		for (ResultbySubstrateData csresultTable : resultcslist) {
			cslist.add(csresultTable);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> substrateColSortHandler = new ListHandler<ResultbySubstrateData>(
				cslist);
		substrateColSortHandler.setComparator(substrateCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.substrate.S_Symbol
									.compareTo(o2.substrate.S_Symbol) : 1;
						}
						return -1;
					}
				});
		cleavageSiteTable.addColumnSortHandler(substrateColSortHandler);

		// We know that the data is sorted alphabetically by default.
		cleavageSiteTable.getColumnSortList().push(substrateCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> p1ColSortHandler = new ListHandler<ResultbySubstrateData>(
				cslist);
		p1ColSortHandler.setComparator(p1Col,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(o1.p1)
									.compareTo(Integer.toString(o2.p1)) : 1;
						}
						return -1;
					}
				});
		cleavageSiteTable.addColumnSortHandler(p1ColSortHandler);

		// We know that the data is sorted alphabetically by default.
		cleavageSiteTable.getColumnSortList().push(p1Col);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> p1primeColSortHandler = new ListHandler<ResultbySubstrateData>(
				cslist);
		p1primeColSortHandler.setComparator(p1primeCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(o1.p1prime)
									.compareTo(Integer.toString(o2.p1prime))
									: 1;
						}
						return -1;
					}
				});
		cleavageSiteTable.addColumnSortHandler(p1primeColSortHandler);

		// We know that the data is sorted alphabetically by default.
		cleavageSiteTable.getColumnSortList().push(p1primeCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> protSymbolColSortHandler = new ListHandler<ResultbySubstrateData>(
				cslist);
		protSymbolColSortHandler.setComparator(protSymbolCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.protease.P_Symbol
									.compareTo(o2.protease.P_Symbol) : 1;
						}
						return -1;
					}
				});
		cleavageSiteTable.addColumnSortHandler(protSymbolColSortHandler);

		// We know that the data is sorted alphabetically by default.
		cleavageSiteTable.getColumnSortList().push(protSymbolCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> protNameColSortHandler = new ListHandler<ResultbySubstrateData>(
				cslist);
		protNameColSortHandler.setComparator(protNameCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.protease.P_NL_Name
									.compareTo(o2.protease.P_NL_Name) : 1;
						}
						return -1;
					}
				});
		cleavageSiteTable.addColumnSortHandler(protNameColSortHandler);

		// We know that the data is sorted alphabetically by default.
		cleavageSiteTable.getColumnSortList().push(protNameCol);
	}

	private void generateSearchRequest_2() {

		String subUni = searchBoxUni_2.getText().toUpperCase().trim();
		String splitSearchUni[] = subUni.split("\n");
		LinkedList<String> setUni = new LinkedList<String>();
		for (String searchUni : splitSearchUni) {
			searchUni.toUpperCase().trim();
			setUni.add(searchUni);
			System.out.println(searchUni + "UNI");
		}
		int sizeUni = setUni.size();

		String pepID = searchBoxId_2.getText().toUpperCase().trim();
		LinkedList<String> setID = null;
		if ((!pepID.equalsIgnoreCase(""))) {
			String splitSearchID[] = pepID.split("\n");
			setID = new LinkedList<String>();
			for (String searchID : splitSearchID) {
				searchID.toUpperCase().trim();
				setID.add(searchID);
				System.out.println(searchID + "ID");
			}
		} else {
			setID = new LinkedList<String>();
			for (int i = 0; i < sizeUni; i++) {
				int j = i + 1;
				String searchID = "Search " + j;
				setID.add(searchID);
				System.out.println(searchID + "ID");
			}
		}

		String pepStartEnd = searchBoxStartEnd_2.getText().toUpperCase().trim()
				.toString();
		String splitSearchStartEnd[] = pepStartEnd.split("\n");
		LinkedList<String> setStartEnd = new LinkedList<String>();
		for (String searchStartEnd : splitSearchStartEnd) {
			searchStartEnd.toUpperCase().trim();
			setStartEnd.add(searchStartEnd);
			System.out.println(searchStartEnd + "STARTEND");
		}

		String pepSeq = searchBoxSequence_2.getText().trim();
		LinkedList<String> setSeq = null;
		if (!(pepSeq == null)) {
			String splitSearchSeq[] = pepSeq.split("\n");
			setSeq = new LinkedList<String>();
			for (String searchSeq : splitSearchSeq) {
				searchSeq.trim();
				setSeq.add(searchSeq);
				System.out.println(searchSeq + "ABCDEFGHIJ");
			}
		}

		int pepMismIndex = listcs_2.getSelectedIndex();
		String pepMism = listcs_2.getItemText(pepMismIndex);
		int pepMismNumber = 0;

		if (pepMism.contains("0")) {
			pepMismNumber = 0;
		} else if (pepMism.contains("1")) {
			pepMismNumber = 1;
		} else if (pepMism.contains("2")) {
			pepMismNumber = 2;
		} else if (pepMism.contains("3")) {
			pepMismNumber = 3;
		}

		SearchRequest[] searchRequest = new SearchRequest[sizeUni];
		int i = 0;

		Iterator iteratorID = setID.iterator();
		while (iteratorID.hasNext()) {
			String id = iteratorID.next().toString();
			String valuesplitID[] = id.split("\n");
			for (String string : valuesplitID) {
				searchRequest[i] = new SearchRequest();
				searchRequest[i].setPeptideinputnumber(string);
				searchRequest[i].setPeptideinputmismatch(pepMismNumber);
				searchRequest[i].setRequestnature("peptideRequest");
				i++;
			}
		}

		i = 0;
		Iterator iteratorUni = setUni.iterator();
		while (iteratorUni.hasNext()) {
			String uni = iteratorUni.next().toString();
			String valuesplitUni[] = uni.split("\n");
			for (String string : valuesplitUni) {
				searchRequest[i].setPeptideinputuni(string);
				i++;
			}
		}

		i = 0;
		Iterator iteratorStartEnd = setStartEnd.iterator();
		while (iteratorStartEnd.hasNext()) {
			String startend = iteratorStartEnd.next().toString();
			String valuesplitStartEnd[] = startend.split("\n");
			for (String string : valuesplitStartEnd) {
				String splitStartEnd[] = string.split("-");
				int start = Integer.parseInt(splitStartEnd[0]);
				int end = Integer.parseInt(splitStartEnd[1]);
				searchRequest[i].setPeptideinputstart(start);
				searchRequest[i].setPeptideinputend(end);
				System.out.println(start);
				System.out.println(end);
				i++;
			}
		}

		// searchBox_2.setFocus(true);
		// searchBox_2.setText("");

		getResultbyPeptideInfo_2(searchRequest);

	}

	private void getResultbyPeptideInfo_2(SearchRequest[] searchRequest) {

		// empty if anything is there already
		pResultPanel_2.clear();
		peptide_summ_2.clear();
		peptide_cs_2.clear();
		peptide_pep_2.clear();
		// dpExistingPeptide_2.clear();
		// dpNotFoundPeptide_2.clear();

		AsyncCallback callback = new AsyncCallback() {

			// fail
			public void onFailure(Throwable ex) {
				RootPanel.get().add(new HTML(ex.toString()));
			}

			// success
			public void onSuccess(Object result) {

				ResultbySubstrateData[] resultbySubstrate = (ResultbySubstrateData[]) result;

				// draw PEPTIDE info
				drawResultbyPeptideInfo_2(resultbySubstrate);

			}
		};

		// remote procedure call to the server to get the bible info
		callProvider.getResultInfo(searchRequest, callback);
	}

	private void drawResultbyPeptideInfo_2(
			ResultbySubstrateData[] resultbySubstrateData) {

		// if null nothing to do, then exit
		// this will prevent errors from showing up
		if (resultbySubstrateData == null) {
			return;
		}

		pResultPanel_2.add(lResult);
		lResult.addStyleName("lResult");
		pResultPanel_2.add(peptideTabPanel_2);
		peptideTabPanel_2.add(peptide_summ_2, "Summary");
		peptideTabPanel_2.add(peptide_cs_2, "Cleavage sites");
		peptideTabPanel_2.add(peptide_pep_2, "Peptides");
		peptideTabPanel_2.selectTab(0);
		ploading.setVisible(false);

		int numbercs_2 = 0;
		int numberdispep_2 = 0;
		int numbersumm_2 = 0;

		for (ResultbySubstrateData resultbySubstrateData2 : resultbySubstrateData) {
			if (resultbySubstrateData2.getNature().equals("cleavagesite")) {
				numbercs_2++;
			} else if (resultbySubstrateData2.getNature().equals("peptide")
					&& !(resultbySubstrateData2.peptide.disease == null)
					&& resultbySubstrateData2.peptide.structure == null) {
				numberdispep_2++;
			}
			if (!(resultbySubstrateData2.getSubvalidity_1() == null)) {
				numbersumm_2++;
			}
			System.out.println(resultbySubstrateData2);
		}

		System.out.println(numbercs_2 + "numbercs");
		System.out.println(numberdispep_2 + "numberpep");

		ResultbySubstrateData[] resultcs_2 = new ResultbySubstrateData[numbercs_2];
		ResultbySubstrateData[] resultdispep_2 = new ResultbySubstrateData[numberdispep_2];
		ResultbySubstrateData[] resultsumm_2 = new ResultbySubstrateData[numbersumm_2];

		int k = 0;
		int l = 0;
		int m = 0;

		for (int i = 0; i < resultbySubstrateData.length; i++) {
			if (resultbySubstrateData[i].getNature().equals("cleavagesite")) {
				resultcs_2[k] = resultbySubstrateData[i];
				k++;
			} else if (resultbySubstrateData[i].getNature().equals("peptide")
					&& !(resultbySubstrateData[i].peptide.disease == null)
					&& resultbySubstrateData[i].peptide.structure == null) {
				resultdispep_2[l] = resultbySubstrateData[i];
				l++;
			}
			if (!(resultbySubstrateData[i].getSubvalidity_1() == null)) {
				resultsumm_2[m] = resultbySubstrateData[i];
				m++;
			}
		}

		Map<String, List<Set<String>>> hmap = new HashMap<String, List<Set<String>>>();
		for (int i = 0; i < numbercs_2; i++) {
			String key = resultcs_2[i].CSInput_number
					+ resultcs_2[i].substrate.S_Symbol
//					+ resultcs_2[i].CS_terminus 
					+ resultcs_2[i].CS_database
					+ resultcs_2[i].protease.P_Symbol;
			if (!hmap.containsKey(key)) {
				List value = new ArrayList<Set<String>>();
				for (int j = 0; j < 10; j++) {
					value.add(new HashSet<String>());
				}
				hmap.put(key, value);
			}
			hmap.get(key).get(0).add(resultcs_2[i].CSInput_number);
			hmap.get(key).get(1).add(resultcs_2[i].CS_NorCterm);
			hmap.get(key).get(2).add(resultcs_2[i].substrate.S_Symbol);
			hmap.get(key).get(3).add(resultcs_2[i].peptide.sequence);
			hmap.get(key).get(4).add(resultcs_2[i].CS_terminus);
			hmap.get(key).get(5).add(resultcs_2[i].CS_database);
			hmap.get(key).get(6).add(resultcs_2[i].CS_mismatch);
			hmap.get(key).get(7).add(resultcs_2[i].protease.P_Symbol);
			hmap.get(key).get(8).add(resultcs_2[i].externallink);
			hmap.get(key).get(9).add(resultcs_2[i].pmid);
		}

		Iterator iterator = hmap.values().iterator();
		int numbercsSHORT_2 = hmap.size();
		ResultbySubstrateData[] resultcsSHORT_2 = new ResultbySubstrateData[numbercsSHORT_2];
		int i = 0;
		while (iterator.hasNext()) {
			SubstrateData substrate = new SubstrateData();
			ProteaseData protease = new ProteaseData();
			PeptideData peptide = new PeptideData();
			resultcsSHORT_2[i] = new ResultbySubstrateData();
			String values = iterator.next().toString();
			String splitarray[] = values.split("\\], \\[");
			String input = splitarray[0];
			input = input.replaceAll("\\[", "");
			resultcsSHORT_2[i].CSInput_number = input;
			resultcsSHORT_2[i].CS_NorCterm = splitarray[1];
			substrate.S_Symbol = splitarray[2];
			peptide.sequence = splitarray[3];
			resultcsSHORT_2[i].CS_terminus = splitarray[4];
			resultcsSHORT_2[i].CS_database = splitarray[5];
			protease.P_Symbol = splitarray[7];
			resultcsSHORT_2[i].CS_mismatch = splitarray[6];
			String externallink = splitarray[8].toString();
			resultcsSHORT_2[i].externallink = externallink;
			String pmid = splitarray[9].toString();
			pmid = pmid.replaceAll("\\]", "");
//			pmid = pmid.replaceFirst(", ", "");
			resultcsSHORT_2[i].pmid = pmid;
			resultcsSHORT_2[i].setProtease(protease);
			resultcsSHORT_2[i].setSubstrate(substrate);
			resultcsSHORT_2[i].setPeptide(peptide);
			i++;
		}

		List<ResultbySubstrateData> resultdispeplist = Arrays
				.asList(resultdispep_2);
		List<ResultbySubstrateData> resultcslist = Arrays
				.asList(resultcsSHORT_2);
		List<ResultbySubstrateData> resultsummlist = Arrays
				.asList(resultsumm_2);

		createSummTable_2(resultsumm_2, resultsummlist);
		createCleavageSiteTable_2(resultcsSHORT_2, resultcslist);
		createdisPeptideTable_2(resultdispep_2, resultdispeplist);
		ploading.setVisible(false);

	}

	private void createSummTable_2(ResultbySubstrateData[] resultsumm,
			List<ResultbySubstrateData> resultsummlist) {

		int rowsumm = resultsumm.length;
		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> summTable = new CellTable<ResultbySubstrateData>(
				rowsumm, ptableresources);
		summTable.setWidth("1000px");

		TextColumn<ResultbySubstrateData> inputNumberCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = resultbySubstrateData.peptide.searchnumber;
				return input;
			}
		};

		Column<ResultbySubstrateData, Number> inputStartCol = new Column<ResultbySubstrateData, Number>(
				new NumberCell()) {
			@Override
			public Number getValue(ResultbySubstrateData resultbySubstrateData) {
				int input = resultbySubstrateData.peptide.start;
				return input;
			}
		};

		Column<ResultbySubstrateData, Number> inputEndCol = new Column<ResultbySubstrateData, Number>(
				new NumberCell()) {
			@Override
			public Number getValue(ResultbySubstrateData resultbySubstrateData) {
				int input = resultbySubstrateData.peptide.end;
				return input;
			}
		};

		Column<ResultbySubstrateData, SafeHtml> inputSeqCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String sequence = "<p align=\"left\"><font size=\"1\">"
						+ resultbySubstrateData.peptide.sequence
						+ "</font></p>";
				return new SafeHtmlBuilder().appendHtmlConstant(sequence)
						.toSafeHtml();
			}

		};

		Column<ResultbySubstrateData, SafeHtml> protCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = null;
				if (resultbySubstrateData.getSubvalidity_1().equals("yes")) {
					input = "<p align=\"left\">"
							+ resultbySubstrateData.substrate.S_Symbol + ", "
							+ resultbySubstrateData.substrate.S_NL_Name + ", "
							+ resultbySubstrateData.substrate.S_Uniprotid
							+ "</p>";
				} else if (resultbySubstrateData.getSubvalidity_1()
						.equals("no")) {
					input = "<p align=\"left\">Sorry, not found in the database...</p>";
				}

				return new SafeHtmlBuilder().appendHtmlConstant(input)
						.toSafeHtml();
			}

		};

		Column<ResultbySubstrateData, SafeHtml> csCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = null;
				if (resultbySubstrateData.getCsvalidity_1().equals("yes")) {
					input = "<div>"
							+ "<img src=\"/Images/check.png\"width=\"30\"/></div>";

				} else if (resultbySubstrateData.getCsvalidity_1().equals("no")) {
					input = "<div>"
							+ "<img src=\"/Images/cross.png\"width=\"25\"/></div>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(input)
						.toSafeHtml();
			}
		};

		Column<ResultbySubstrateData, SafeHtml> dispepCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = null;
				if (resultbySubstrateData.getDispepvalidity_1().equals("yes")) {
					input = "<div>"
							+ "<img src=\"/Images/check.png\"width=\"30\"/></div>";

				} else if (resultbySubstrateData.getDispepvalidity_1().equals(
						"no")) {
					input = "<div>"
							+ "<img src=\"/Images/cross.png\"width=\"25\"/></div>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(input)
						.toSafeHtml();
			}
		};

		// Add the columns.
		summTable.addColumn(inputNumberCol, "PeptideID");
		summTable.addColumn(protCol, "Protein");
		summTable.addColumn(inputStartCol, "Start");
		summTable.addColumn(inputEndCol, "End");
		summTable.addColumn(inputSeqCol, "Sequence");
		summTable.addColumn(csCol, "Cleavage Sites");
		summTable.addColumn(dispepCol, "Peptides in disease");
		peptide_summ_2.add(summTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> summdataProvider = new ListDataProvider<ResultbySubstrateData>();

		// Connect the table to the data provider.
		summdataProvider.addDataDisplay(summTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<ResultbySubstrateData> summlist = summdataProvider.getList();
		for (ResultbySubstrateData summresultTable : resultsummlist) {
			summlist.add(summresultTable);
		}

	}

	private void createdisPeptideTable_2(ResultbySubstrateData[] resultdispep,
			List<ResultbySubstrateData> resultdispeplist) {

		int rowsdispep = resultdispep.length;

		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> dispepTable = new CellTable<ResultbySubstrateData>(
				rowsdispep, ptableresources);
		dispepTable.setWidth("900px");

		// Create columns

		TextColumn<ResultbySubstrateData> inputNumberCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = resultbySubstrateData.peptide.searchnumber;
				return input;
			}
		};

		Column<ResultbySubstrateData, SafeHtml> substrateCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String substrate = resultbySubstrateData.substrate.S_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.substrate.S_Uniprotid
								+ "\"target=\"_blank\">" + substrate + "</a>")
						.toSafeHtml();

			}

		};

		TextColumn<ResultbySubstrateData> startCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String start = null;
				if (resultbySubstrateData.peptide.start == 0) {
					start = "?";
				} else {
					start = Integer
							.toString(resultbySubstrateData.peptide.start);
				}
				return start;
			}

		};

		TextColumn<ResultbySubstrateData> endCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String end = null;
				if (resultbySubstrateData.peptide.end == 0) {
					end = "?";
				} else {
					end = Integer.toString(resultbySubstrateData.peptide.end);
				}
				return end;
			}

		};

		Column<ResultbySubstrateData, SafeHtml> sequenceCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String sequence = "<p align=\"left\"><font size=\"1\">"
						+ resultbySubstrateData.peptide.sequence
						+ "</font></p>";
				return new SafeHtmlBuilder().appendHtmlConstant(sequence)
						.toSafeHtml();
			}

		};

		TextColumn<ResultbySubstrateData> diseaseCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				return resultbySubstrateData.peptide.disease;
			}

		};

		Column<ResultbySubstrateData, SafeHtml> regulationCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String regulation = null;
				if (resultbySubstrateData.peptide.regulation.equals("Down")) {
					regulation = "<div style=\"background:#008F29;border:1px solid black;width:30px;height:30px;margin-left:26px\"></div>"
							+ resultbySubstrateData.peptide.regulation;
				} else if (resultbySubstrateData.peptide.regulation
						.equals("Up")) {
					regulation = "<div style=\"background:red;border:1px solid black;width:30px;height:30px;margin-left:26px\"></div>"
							+ resultbySubstrateData.peptide.regulation;
				} else if (resultbySubstrateData.peptide.regulation.equals("")) {
					regulation = "-";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(regulation)
						.toSafeHtml();
			}

		};

		// Column<ResultbySubstrateData, SafeHtml> extlinkCol = new
		// Column<ResultbySubstrateData, SafeHtml>(
		// new SafeHtmlCell()) {
		// @Override
		// public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData)
		// {
		// String external = resultbySubstrateData.externallink;
		// String externallink = "";
		// if (external.contains("uniprot")) {
		// externallink = "<a href=\"" + external
		// + "\"target=\"_blank\">"
		// + "<img src=\"/Images/logo.gif\"width=\"40\"/></a>";
		// }
		// return new SafeHtmlBuilder().appendHtmlConstant(externallink)
		// .toSafeHtml();
		//
		// }
		//
		// };

		Column<ResultbySubstrateData, SafeHtml> pmidCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String external = resultbySubstrateData.pmid;
				String pmid = "";
				if (external.contains(";")) {
					String valuesplit[] = external.split(";");
					int i = 0;
					for (String string : valuesplit) {
						valuesplit[i].trim();
						pmid = pmid
								+ "; "
								+ "<a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
								+ valuesplit[i] + "\"target=\"_blank\">"
								+ valuesplit[i] + "</a>";
						i++;
					}
					pmid = pmid.replaceFirst("; ", "");
				} else {
					pmid = "";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(pmid)
						.toSafeHtml();

			}

		};

		// Make the columns sortable.
		inputNumberCol.setSortable(true);
		substrateCol.setSortable(true);
		startCol.setSortable(true);
		endCol.setSortable(true);
		diseaseCol.setSortable(true);
		regulationCol.setSortable(true);

		// Add the columns.
		dispepTable.addColumn(inputNumberCol, "\u25B2PeptideID\u25BC");
		dispepTable.addColumn(substrateCol, "\u25B2Substrate\u25BC");
		dispepTable.addColumn(startCol, "\u25B2Start\u25BC");
		dispepTable.addColumn(endCol, "\u25B2End\u25BC");
		dispepTable.addColumn(sequenceCol, "Sequence");
		dispepTable.addColumn(diseaseCol, "\u25B2Disease\u25BC");
		dispepTable.addColumn(regulationCol, "\u25B2Regulation\u25BC");
		// dispepTable.addColumn(extlinkCol, "External Link");
		dispepTable.addColumn(pmidCol, "Publication");

		dispepTable.setColumnWidth(substrateCol, 5, Unit.EM);
		dispepTable.setColumnWidth(startCol, 2, Unit.EM);
		dispepTable.setColumnWidth(endCol, 2, Unit.EM);
		dispepTable.setColumnWidth(sequenceCol, 20, Unit.EM);
		dispepTable.setColumnWidth(diseaseCol, 20, Unit.EM);
		dispepTable.setColumnWidth(regulationCol, 2, Unit.EM);

		peptide_pep_2.add(dispepTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> pepdataProvider = new ListDataProvider<ResultbySubstrateData>();

		// Connect the table to the data provider.
		pepdataProvider.addDataDisplay(dispepTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<ResultbySubstrateData> peplist = pepdataProvider.getList();
		for (ResultbySubstrateData pepresultTable : resultdispeplist) {
			peplist.add(pepresultTable);
		}

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> pepNumberColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		pepNumberColSortHandler.setComparator(inputNumberCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.peptide.searchnumber
									.compareTo(o2.peptide.searchnumber) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(pepNumberColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(inputNumberCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> pepsubstrateColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		pepsubstrateColSortHandler.setComparator(substrateCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.substrate.S_Symbol
									.compareTo(o2.substrate.S_Symbol) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(pepsubstrateColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(substrateCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> startColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		startColSortHandler.setComparator(startCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.peptide.start).compareTo(
									Integer.toString(o2.peptide.start)) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(startColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(startCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> endColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		endColSortHandler.setComparator(endCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? Integer.toString(
									o1.peptide.end).compareTo(
									Integer.toString(o2.peptide.end)) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(endColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(endCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> diseaseColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		diseaseColSortHandler.setComparator(diseaseCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.peptide.disease
									.compareTo(o2.peptide.disease) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(diseaseColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(diseaseCol);

		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> regulationColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		regulationColSortHandler.setComparator(regulationCol,
				new Comparator<ResultbySubstrateData>() {
					public int compare(ResultbySubstrateData o1,
							ResultbySubstrateData o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the symbol columns.
						if (o1 != null) {
							return (o2 != null) ? o1.peptide.regulation
									.compareTo(o2.peptide.regulation) : 1;
						}
						return -1;
					}
				});
		dispepTable.addColumnSortHandler(regulationColSortHandler);

		// We know that the data is sorted alphabetically by default.
		dispepTable.getColumnSortList().push(regulationCol);
	}

	private void createCleavageSiteTable_2(ResultbySubstrateData[] resultcs,
			List<ResultbySubstrateData> resultcslist) {

		int rowscs = resultcs.length;
		System.out.println(rowscs);
		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> csTable = new CellTable<ResultbySubstrateData>(
				rowscs, ptableresources);
		csTable.setWidth("900px");

		// Create columns

		TextColumn<ResultbySubstrateData> inputNumberCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = resultbySubstrateData.CSInput_number + " / "
						+ resultbySubstrateData.CS_NorCterm;
				return input;
			}
		};

		Column<ResultbySubstrateData, SafeHtml> substrateCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String substrate = resultbySubstrateData.substrate.S_Symbol;
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<a href=\"http://www.uniprot.org/uniprot/"
								+ resultbySubstrateData.substrate.S_Uniprotid
								+ "\"target=\"_blank\">" + substrate + "</a>")
						.toSafeHtml();

			}

		};

		Column<ResultbySubstrateData, SafeHtml> sequenceCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String sequence = null;
				if (resultbySubstrateData.CS_NorCterm.contains("N")) {
					sequence = "<p align=\"left\"><font size=\"1\"><b>"
							+ resultbySubstrateData.peptide.sequence.substring(
									0, 3)
							+ "</b>"
							+ resultbySubstrateData.peptide.sequence
									.substring(3) + "</font></p>";
				}
				if (resultbySubstrateData.CS_NorCterm.contains("C")) {
					int length = resultbySubstrateData.peptide.sequence
							.length();
					sequence = "<p align=\"left\"><font size=\"1\">"
							+ resultbySubstrateData.peptide.sequence.substring(
									0, length - 3)
							+ "<u>"
							+ resultbySubstrateData.peptide.sequence
									.substring(length - 3) + "</u></font></p>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(sequence)
						.toSafeHtml();
			}

		};

		Column<ResultbySubstrateData, SafeHtml> searchedCSCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {

				String cleavageSite = resultbySubstrateData.CS_terminus;
				String begin = cleavageSite.substring(0, 3);
				String end = cleavageSite.substring(3, 6);
				String cs = null;
				if (resultbySubstrateData.CS_NorCterm.contains("N")) {
					cs = "<p>" + begin + "\u00A6 <u>" + end + "</u></p>";
				}
				if (resultbySubstrateData.CS_NorCterm.contains("C")) {
					cs = "<p><u>" + begin + "</u>\u00A6" + end + "</p>";
				}

				return new SafeHtmlBuilder().appendHtmlConstant(cs)
						.toSafeHtml();
			}

		};

		Column<ResultbySubstrateData, SafeHtml> foundCSCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String searched = resultbySubstrateData.CS_terminus;
				String found = resultbySubstrateData.CS_database;
				String searchedsplit[] = searched.split("");
				String foundsplit[] = found.split("");
				String one = null;
				String two = null;
				String three = null;
				String four = null;
				String five = null;
				String six = null;
				if (searchedsplit[1].equals(foundsplit[1])) {
					one = searchedsplit[1];
				} else {
					one = "<font color =\"red\">" + foundsplit[1].toLowerCase()
							+ "</font>";
				}
				if (searchedsplit[2].equals(foundsplit[2])) {
					two = searchedsplit[2];
				} else {
					two = "<font color =\"red\">" + foundsplit[2].toLowerCase()
							+ "</font>";
				}
				if (searchedsplit[3].equals(foundsplit[3])) {
					three = searchedsplit[3];
				} else {
					three = "<font color =\"red\">"
							+ foundsplit[3].toLowerCase() + "</font>";
				}
				if (searchedsplit[4].equals(foundsplit[4])) {
					four = searchedsplit[4];
				} else {
					four = "<font color =\"red\">"
							+ foundsplit[4].toLowerCase() + "</font>";
				}
				if (searchedsplit[5].equals(foundsplit[5])) {
					five = searchedsplit[5];
				} else {
					five = "<font color =\"red\">"
							+ foundsplit[5].toLowerCase() + "</font>";
				}
				if (searchedsplit[6].equals(foundsplit[6])) {
					six = searchedsplit[6];
				} else {
					six = "<font color =\"red\">" + foundsplit[6].toLowerCase()
							+ "</font>";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(
						"<p>" + one + two + three + "" + "\u00A6" + four + five
								+ six + "</p>").toSafeHtml();

			}

		};

		TextColumn<ResultbySubstrateData> proteaseCol = new TextColumn<ResultbySubstrateData>() {
			@Override
			public String getValue(ResultbySubstrateData resultbySubstrateData) {
				String input = resultbySubstrateData.protease.P_Symbol;
				return input;
			}
		};
		
		
		Column<ResultbySubstrateData, SafeHtml> mismatchCSCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String mismatch = resultbySubstrateData.CS_mismatch;
				return new SafeHtmlBuilder().appendHtmlConstant(
						mismatch).toSafeHtml();

			}

		};
		

		Column<ResultbySubstrateData, SafeHtml> extlinkCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String external = resultbySubstrateData.externallink;
				external = external.replaceAll(", ", "; ");
				String externallink = "";
				if (!(external==null)){
				if (external.contains(";")) {
				String valuesplit[] = external.split(";");
				int i = 0;
				for (String string : valuesplit) {
					valuesplit[i].trim();
					if (valuesplit[i].contains("cutdb")) {
						externallink = externallink
								+ "<a href=\""
								+ valuesplit[i]
								+ "\"target=\"_blank\">"
								+ "<img src=\"/Images/PMAP-logoV8-2blue.png\"width=\"30\"/></a>";
					} else if (valuesplit[i].contains("uniprot")) {
						externallink = externallink
								+ "<a href=\""
								+ valuesplit[i]
								+ "\"target=\"_blank\">"
								+ "<img src=\"/Images/logo.gif\"width=\"40\"/></a>";
					}

					i++;
				}
				externallink = externallink.replaceFirst("; ", "");
				}else {
					if (external.contains("cutdb")) {
						externallink = "<a href=\""
								+ external
								+ "\"target=\"_blank\">"
								+ "<img src=\"/Images/PMAP-logoV8-2blue.png\"width=\"30\"/></a>";
					} else if (external.contains("uniprot")) {
						externallink = "<a href=\""
								+ external
								+ "\"target=\"_blank\">"
								+ "<img src=\"/Images/logo.gif\"width=\"40\"/></a>";
					}
				}
				}else {
					externallink="";
				}

				return new SafeHtmlBuilder().appendHtmlConstant(externallink)
						.toSafeHtml();

			}

		};

		Column<ResultbySubstrateData, SafeHtml> pmidCol = new Column<ResultbySubstrateData, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				String external = resultbySubstrateData.pmid;
				external = external.replaceAll(", ", "; ");
				String pmid = "";
				if (!(external == null)) {
				if (external.contains(";")) {
					String valuesplit[] = external.split(";");
					int i = 0;
					for (String string : valuesplit) {
						valuesplit[i].trim();
						pmid = pmid
								+ "; "
								+ "<a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
								+ valuesplit[i] + "\"target=\"_blank\">"
								+ valuesplit[i] + "</a>";
						i++;
					}
					pmid = pmid.replaceFirst("; ", "");

				} else {
					pmid = "<a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"
							+ external + "\"target=\"_blank\">"
							+ external + "</a>";
				}
				}else {
					pmid = "";
				}
				return new SafeHtmlBuilder().appendHtmlConstant(pmid)
						.toSafeHtml();

			}

		};
		
		inputNumberCol.setSortable(true);
		substrateCol.setSortable(true);
		mismatchCSCol.setSortable(true);
		proteaseCol.setSortable(true);
		

		csTable.addColumn(inputNumberCol, "\u25B2PeptideID\u25BC");
		csTable.addColumn(substrateCol, "\u25B2Protein\u25BC");
		csTable.addColumn(sequenceCol, "Peptide Sequence");
		csTable.addColumn(searchedCSCol, "Searched CS");
		csTable.addColumn(foundCSCol, "Found CS");
		csTable.addColumn(mismatchCSCol, "\u25B2Mism.\u25BC.");
		csTable.addColumn(proteaseCol, "\u25B2Protease\u25BC");
		csTable.addColumn(extlinkCol, "External Link");
		csTable.addColumn(pmidCol, "Publication");
		
		
		csTable.setColumnWidth(inputNumberCol, 10, Unit.EM);
		csTable.setColumnWidth(substrateCol, 5, Unit.EM);
		csTable.setColumnWidth(sequenceCol, 20, Unit.EM);
		csTable.setColumnWidth(searchedCSCol, 5, Unit.EM);
		csTable.setColumnWidth(foundCSCol, 5, Unit.EM);
		csTable.setColumnWidth(proteaseCol, 5 , Unit.EM);
		csTable.setColumnWidth(extlinkCol, 5, Unit.EM);
		csTable.setColumnWidth(pmidCol, 5 , Unit.EM);
		
		
		peptide_cs_2.add(csTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> csdataProvider = new ListDataProvider<ResultbySubstrateData>();

		// Connect the table to the data provider.
		csdataProvider.addDataDisplay(csTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget
		List<ResultbySubstrateData> cslist = csdataProvider.getList();
		for (ResultbySubstrateData csresultTable : resultcslist) {
			cslist.add(csresultTable);
		}
		
		
		// Add a ColumnSortEvent.ListHandler to connect sorting to the
				// java.util.List.
				ListHandler<ResultbySubstrateData> inputNumberColSortHandler = new ListHandler<ResultbySubstrateData>(
						cslist);
				inputNumberColSortHandler.setComparator(inputNumberCol,
						new Comparator<ResultbySubstrateData>() {
							public int compare(ResultbySubstrateData o1,
									ResultbySubstrateData o2) {
								if (o1 == o2) {
									return 0;
								}

								// Compare the symbol columns.
								if (o1 != null) {
									return (o2 != null) ? o1.CSInput_number
											.compareTo(o2.CSInput_number) : 1;
								}
								return -1;
							}
						});
				csTable.addColumnSortHandler(inputNumberColSortHandler);

				// We know that the data is sorted alphabetically by default.
				csTable.getColumnSortList().push(inputNumberCol);
				
				// Add a ColumnSortEvent.ListHandler to connect sorting to the
				// java.util.List.
				ListHandler<ResultbySubstrateData> substrateColSortHandler = new ListHandler<ResultbySubstrateData>(
						cslist);
				substrateColSortHandler.setComparator(substrateCol,
						new Comparator<ResultbySubstrateData>() {
							public int compare(ResultbySubstrateData o1,
									ResultbySubstrateData o2) {
								if (o1 == o2) {
									return 0;
								}

								// Compare the symbol columns.
								if (o1 != null) {
									return (o2 != null) ? o1.substrate.S_Symbol
											.compareTo(o2.substrate.S_Symbol) : 1;
								}
								return -1;
							}
						});
				csTable.addColumnSortHandler(substrateColSortHandler);

				// We know that the data is sorted alphabetically by default.
				csTable.getColumnSortList().push(substrateCol);
				
				// Add a ColumnSortEvent.ListHandler to connect sorting to the
				// java.util.List.
				ListHandler<ResultbySubstrateData> mismatchColSortHandler = new ListHandler<ResultbySubstrateData>(
						cslist);
				mismatchColSortHandler.setComparator(mismatchCSCol,
						new Comparator<ResultbySubstrateData>() {
							public int compare(ResultbySubstrateData o1,
									ResultbySubstrateData o2) {
								if (o1 == o2) {
									return 0;
								}

								// Compare the symbol columns.
								if (o1 != null) {
									return (o2 != null) ? o1.CS_mismatch
											.compareTo(o2.CS_mismatch) : 1;
								}
								return -1;
							}
						});
				csTable.addColumnSortHandler(mismatchColSortHandler);

				// We know that the data is sorted alphabetically by default.
				csTable.getColumnSortList().push(mismatchCSCol);
				
				// Add a ColumnSortEvent.ListHandler to connect sorting to the
				// java.util.List.
				ListHandler<ResultbySubstrateData> proteaseColSortHandler = new ListHandler<ResultbySubstrateData>(
						cslist);
				proteaseColSortHandler.setComparator(proteaseCol,
						new Comparator<ResultbySubstrateData>() {
							public int compare(ResultbySubstrateData o1,
									ResultbySubstrateData o2) {
								if (o1 == o2) {
									return 0;
								}

								// Compare the symbol columns.
								if (o1 != null) {
									return (o2 != null) ? o1.protease.P_Symbol
											.compareTo(o2.protease.P_Symbol) : 1;
								}
								return -1;
							}
						});
				csTable.addColumnSortHandler(proteaseColSortHandler);

				// We know that the data is sorted alphabetically by default.
				csTable.getColumnSortList().push(proteaseCol);

	}
}
