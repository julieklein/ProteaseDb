package com.google.gwt.proteasedb.client;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.view.client.ListDataProvider;

public class ProteasiXEntryPoint implements EntryPoint {

	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel

	private TabPanel mainPanel = new TabPanel();
	private FlowPanel proteinTab = new FlowPanel();
	private VerticalPanel pResultPanel = new VerticalPanel();
	// private VerticalPanel pCleavagesitePanel = new VerticalPanel();
	private HorizontalPanel searchpanel = new HorizontalPanel();

	private TextArea searchBox = new TextArea();
	private Button searchButton = new Button("Search");
	private DisclosurePanel dpProtease = new DisclosurePanel(
			"Click to See/Hide Cleavage Site Results");
	private DisclosurePanel dpPeptide = new DisclosurePanel(
			"Click to See/Hide Peptide Results");
	private Label lSubstrate = new Label("Substrate");
	private Label lSubstrateOutput = new Label();
	private Label lResult = new Label("Results View");
	private Label lError = new Label();
	private FlowPanel dpProteaseHeader = new FlowPanel();

	// table for the bible info

	// private Grid cleavageSiteGrid = null;
	// private Grid substrateGrid = null;
	// private Grid peptideGrid = null;

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

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		// TODO Auto-generated method stub
		// init the rpc

		searchpanel.add(searchBox);
		searchBox.setHeight("68px");

		searchpanel.add(searchButton);
		searchButton.addStyleDependentName("search");
		searchpanel.addStyleName("pSearchpanel");
		proteinTab.add(searchpanel);
		proteinTab
				.add(new HTML(
						"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));

		pResultPanel.addStyleName("pResultPanel");
		proteinTab.add(pResultPanel);
		pResultPanel.setWidth("700px");

		mainPanel.removeStyleName("gwt-TabPanelBottom");

		mainPanel.add(proteinTab, "Search by protein");

		mainPanel.selectTab(0);

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

		dpProtease.addOpenHandler(new OpenHandler() {

			@Override
			public void onOpen(OpenEvent event) {
				// TODO Auto-generated method stub

			}
		});

		dpPeptide.addOpenHandler(new OpenHandler() {

			@Override
			public void onOpen(OpenEvent event) {
				// TODO Auto-generated method stub

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
		dpPeptide.clear();
		dpProtease.clear();
		// dStackPanel.clear();
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

			
				// set up the table the bible info will go into.
				// I already init the grid var above so I can reference it other
				// methods
				// in this instance.
				
				pResultPanel.add(lResult);
				lResult.addStyleName("lResult");

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
				
				String errorcs = resultcs[0].getEntryValidity();
				String errorpep = resultpep[0].getEntryValidity();
				
				Label lErrorcs = new Label(errorcs);
				Label lErrorpep = new Label(errorpep);

				
				
				if (resultcs[0].getEntryValidity().contains(
						"Sorry, there is no cleavage site") && !resultpep[0].getEntryValidity().contains(
								"Sorry, there is no peptide")) {
					dpProtease.add(lErrorcs);
					pResultPanel.add(dpProtease);
					dpProtease.addStyleDependentName("mydp");
					pResultPanel
					.add(new HTML(
							"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
					List<ResultbySubstrateData> resultpeplist = Arrays.asList(resultpep);
					lSubstrate.addStyleName("Label");
					pResultPanel.add(dpPeptide);
					dpPeptide.addStyleDependentName("mydp");
					pResultPanel.addStyleName("Label");
					createPeptideTable(resultpep, resultpeplist);

				} else if (!resultcs[0].getEntryValidity().contains(
						"Sorry, there is no cleavage site") && resultpep[0].getEntryValidity().contains(
								"Sorry, there is no peptide")) {
					List<ResultbySubstrateData> resultcslist = Arrays.asList(resultcs);
					lSubstrate.addStyleName("Label");
					pResultPanel.add(dpProtease);
					dpProtease.addStyleDependentName("mydp");
					pResultPanel
					.add(new HTML(
							"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
					dpPeptide.add(lErrorpep);
					pResultPanel.add(dpPeptide);
					dpPeptide.addStyleDependentName("mydp");
					pResultPanel.addStyleName("Label");
					createCleavageSiteTable(resultcs, resultcslist);

					
				}else if (!resultcs[0].getEntryValidity().contains(
						"Sorry, there is no cleavage site") && !resultpep[0].getEntryValidity().contains(
								"Sorry, there is no peptide")) {
					List<ResultbySubstrateData> resultcslist = Arrays.asList(resultcs);
					pResultPanel.add(dpProtease);
					dpProtease.addStyleDependentName("mydp");
					pResultPanel
					.add(new HTML(
							"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
					List<ResultbySubstrateData> resultpeplist = Arrays.asList(resultpep);
					lSubstrate.addStyleName("Label");
					pResultPanel.add(dpPeptide);
					dpPeptide.addStyleDependentName("mydp");
					pResultPanel.addStyleName("Label");
					createCleavageSiteTable(resultcs, resultcslist);
					createPeptideTable(resultpep, resultpeplist);
					
				}

				
//				List<ResultbySubstrateData> resultcslist = Arrays.asList(resultcs);
//				List<ResultbySubstrateData> resultpeplist = Arrays.asList(resultpep);
//
//				
//				System.out.println(resultpep.length + "resultpep");
//				System.out.println(resultcs.length + "resultcs");
//
//				
//				lSubstrate.addStyleName("Label");
//				
//				pResultPanel.add(dpProtease);
//				pResultPanel
//				.add(new HTML(
//						"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
//				pResultPanel.add(dpPeptide);
//				dpProtease.addStyleDependentName("mydp");
//				dpPeptide.addStyleDependentName("mydp");
//
//				pResultPanel.addStyleName("Label");
//
//				createCleavageSiteTable(resultcs, resultcslist);
//			   
//				createPeptideTable(resultpep, resultpeplist);
//				
				
				
				
				
				
			
		
		}

	private void createPeptideTable(ResultbySubstrateData[] resultpep,
			List<ResultbySubstrateData> resultpeplist) {
		int rowspep = resultpep.length;
//				peptideGrid = new Grid(rowspep + 1, 5);
		
		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> peptideTable = new CellTable<ResultbySubstrateData>(rowspep, ptableresources);

		//Create columns
		TextColumn<ResultbySubstrateData> substrateCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return resultbySubstrateData.substrate.S_Symbol;
		      }

		}; 
		
		TextColumn<ResultbySubstrateData> startCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return Integer.toString(resultbySubstrateData.peptide.start);
		      }

		}; 
		
		TextColumn<ResultbySubstrateData> endCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return Integer.toString(resultbySubstrateData.peptide.end);
		      }

		}; 
		
		TextColumn<ResultbySubstrateData> diseaseCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return resultbySubstrateData.peptide.disease;
		      }

		}; 
		
		
		Column<ResultbySubstrateData, SafeHtml> regulationCol = new Column<ResultbySubstrateData, SafeHtml>(new SafeHtmlCell()) {
			 @Override
		      public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
			String regulation = null;
			 if (resultbySubstrateData.peptide.regulation.equals("Down")) {
		        regulation ="<div style=\"background:#008F29;border:1px solid black;width:30px;height:30px;margin-left:26px\"></div>" + resultbySubstrateData.peptide.regulation ;
			 }else if (resultbySubstrateData.peptide.regulation.equals("Up")) {
				 regulation = "<div style=\"background:red;border:1px solid black;width:30px;height:30px;margin-left:26px\"></div>" + resultbySubstrateData.peptide.regulation ;
			 }
			 return new SafeHtmlBuilder().appendHtmlConstant(regulation).toSafeHtml();
		      }
			

		}; 
		
		
		
		// Make the columns sortable.
		substrateCol.setSortable(true);
		startCol.setSortable(true);
		endCol.setSortable(true);
		diseaseCol.setSortable(true);
		regulationCol.setSortable(true);
		
		// Add the columns.
		peptideTable.addColumn(substrateCol,"Substrate\u25B2\u25BC" );
		peptideTable.addColumn(startCol,"Start\u25B2\u25BC" );
		peptideTable.addColumn(endCol, "End\u25B2\u25BC");
		peptideTable.addColumn(diseaseCol, "Disease\u25B2\u25BC");
		peptideTable.addColumn(regulationCol, "Regulation\u25B2\u25BC");

		peptideTable.setColumnWidth(substrateCol, 5, Unit.EM);
		peptideTable.setColumnWidth(startCol, 2, Unit.EM);
		peptideTable.setColumnWidth(endCol, 2, Unit.EM);
		peptideTable.setColumnWidth(diseaseCol, 20, Unit.EM);
		peptideTable.setColumnWidth(regulationCol, 2, Unit.EM);

		
		dpPeptide.add(peptideTable);

		 // Create a data provider.
		ListDataProvider<ResultbySubstrateData> pepdataProvider = new ListDataProvider<ResultbySubstrateData>();
		
 // Connect the table to the data provider.
		pepdataProvider.addDataDisplay(peptideTable);
		
 // Add the data to the data provider, which automatically pushes it to the
		// widget
		List<ResultbySubstrateData> peplist = pepdataProvider.getList();
		for (ResultbySubstrateData pepresultTable : resultpeplist) {
		  peplist.add(pepresultTable);
		}
		
 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> pepsubstrateColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		pepsubstrateColSortHandler.setComparator(substrateCol,
		    new Comparator<ResultbySubstrateData>() {
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? o1.substrate.S_Symbol.compareTo(o2.substrate.S_Symbol) : 1;
		        }
		        return -1;
		      }
		    });
		peptideTable.addColumnSortHandler(pepsubstrateColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		peptideTable.getColumnSortList().push(substrateCol);

 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> startColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		startColSortHandler.setComparator(startCol,
		    new Comparator<ResultbySubstrateData>() {
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? Integer.toString(o1.peptide.start).compareTo(Integer.toString(o2.peptide.start)) : 1;
		        }
		        return -1;
		      }
		    });
		peptideTable.addColumnSortHandler(startColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		peptideTable.getColumnSortList().push(startCol);
		
 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> endColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		endColSortHandler.setComparator(endCol,
		    new Comparator<ResultbySubstrateData>() {
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? Integer.toString(o1.peptide.end).compareTo(Integer.toString(o2.peptide.end)) : 1;
		        }
		        return -1;
		      }
		    });
		peptideTable.addColumnSortHandler(endColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		peptideTable.getColumnSortList().push(endCol);
		
		
 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> diseaseColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		diseaseColSortHandler.setComparator(diseaseCol,
		    new Comparator<ResultbySubstrateData>() {
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? o1.peptide.disease.compareTo(o2.peptide.disease) : 1;
		        }
		        return -1;
		      }
		    });
		peptideTable.addColumnSortHandler(diseaseColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		peptideTable.getColumnSortList().push(diseaseCol);
		
		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> regulationColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		regulationColSortHandler.setComparator(regulationCol,
		    new Comparator<ResultbySubstrateData>() {
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? o1.peptide.regulation.compareTo(o2.peptide.regulation) : 1;
		        }
		        return -1;
		      }
		    });
		peptideTable.addColumnSortHandler(regulationColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		peptideTable.getColumnSortList().push(regulationCol);
	}

	private TextColumn<ResultbySubstrateData> createCleavageSiteTable(
			ResultbySubstrateData[] resultcs,
			List<ResultbySubstrateData> resultcslist) {
		int rowscs = resultcs.length;
//				cleavageSiteGrid = new Grid(rowscs + 1, 8);
//				
		CellTable.Resources cstableresources = GWT.create(CSTableResources.class);
		CellTable<ResultbySubstrateData> cleavageSiteTable = new CellTable<ResultbySubstrateData>(rowscs, cstableresources);

		//Create columns
		TextColumn<ResultbySubstrateData> substrateCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return resultbySubstrateData.substrate.S_Symbol;
		      }

		}; 
		
		Column<ResultbySubstrateData, SafeHtml> cssequenceCol = new Column<ResultbySubstrateData, SafeHtml>(new SafeHtmlCell()) {
			 @Override
		      public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
				 
				 String cleavageSite = resultbySubstrateData.cleavageSite;
					String begin = cleavageSite.substring(0, 3);
					String end = cleavageSite.substring(5, 8);
					String middle1 = cleavageSite.substring(3, 4);
					String middle2 = cleavageSite.substring(4, 5);
				 
		        return new SafeHtmlBuilder().appendHtmlConstant("<p>" + begin
						+ "<strong><u>" + middle1 + "\u00A6" + middle2
						+ "</u></strong>" + end + "</p>"
		        		).toSafeHtml() ;
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
				 String sp1prime = Integer.toString(resultbySubstrateData.p1prime);
			     return sp1prime;
		      }

		}; 
		
		TextColumn<ResultbySubstrateData> protNameCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return resultbySubstrateData.protease.P_NL_Name;
		      }

		}; 

		TextColumn<ResultbySubstrateData> protSymbolCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return resultbySubstrateData.protease.P_Symbol;
		      }

		}; 
		
		Column<ResultbySubstrateData, SafeHtml> protIDCol = new Column<ResultbySubstrateData, SafeHtml>(new SafeHtmlCell()) {
			 @Override
		      public SafeHtml getValue(ResultbySubstrateData resultbySubstrateData) {
		        return new SafeHtmlBuilder().appendHtmlConstant("<a href=\"http://www.uniprot.org/uniprot/"
							+ resultbySubstrateData.protease.P_Uniprotid
							+ "\">"
							+ resultbySubstrateData.protease.P_Uniprotid
							+ "</a>").toSafeHtml() ;
		        
		      }

		}; 
		
		TextColumn<ResultbySubstrateData> protECCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return resultbySubstrateData.protease.P_Ecnumber;
		      }

		}; 
		
		// Make the columns sortable.
		substrateCol.setSortable(true);
		p1Col.setSortable(true);
		p1primeCol.setSortable(true);
		protSymbolCol.setSortable(true);
		protNameCol.setSortable(true);
		
		
		// Add the columns.
		cleavageSiteTable.addColumn(substrateCol,"Substrate\u25B2\u25BC" );
		cleavageSiteTable.addColumn(cssequenceCol,"Cleavage Site" );
		cleavageSiteTable.addColumn(p1Col, "P1\u25B2\u25BC");
		cleavageSiteTable.addColumn(p1primeCol, "P1'\u25B2\u25BC");
		cleavageSiteTable.addColumn(protSymbolCol, "Protease Symbol\u25B2\u25BC");
		cleavageSiteTable.addColumn(protNameCol, "Protease Name\u25B2\u25BC");
		cleavageSiteTable.addColumn(protIDCol, "Protease ID");
		cleavageSiteTable.addColumn(protECCol, "EC Number");
		
		
		cleavageSiteTable.setColumnWidth(substrateCol, 5, Unit.EM);
		cleavageSiteTable.setColumnWidth(cssequenceCol, 5, Unit.EM);
		cleavageSiteTable.setColumnWidth(p1Col, 2, Unit.EM);
		cleavageSiteTable.setColumnWidth(p1primeCol, 2, Unit.EM);	
		cleavageSiteTable.setColumnWidth(protSymbolCol, 5, Unit.EM);
		cleavageSiteTable.setColumnWidth(protNameCol, 20, Unit.EM);
		cleavageSiteTable.setColumnWidth(protIDCol, 5, Unit.EM);
		cleavageSiteTable.setColumnWidth(protECCol, 5, Unit.EM);

		dpProtease.add(cleavageSiteTable);

		// Create a data provider.
		ListDataProvider<ResultbySubstrateData> csdataProvider = new ListDataProvider<ResultbySubstrateData>();
		
 // Connect the table to the data provider.
		csdataProvider.addDataDisplay(cleavageSiteTable);
		
 // Add the data to the data provider, which automatically pushes it to the
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
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? o1.substrate.S_Symbol.compareTo(o2.substrate.S_Symbol) : 1;
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
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? Integer.toString(o1.p1).compareTo(Integer.toString(o2.p1)) : 1;
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
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? Integer.toString(o1.p1prime).compareTo(Integer.toString(o2.p1prime)) : 1;
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
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? o1.protease.P_Symbol.compareTo(o2.protease.P_Symbol) : 1;
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
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? o1.protease.P_NL_Name.compareTo(o2.protease.P_NL_Name) : 1;
		        }
		        return -1;
		      }
		    });
		cleavageSiteTable.addColumnSortHandler(protNameColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		cleavageSiteTable.getColumnSortList().push(protNameCol);
		return substrateCol;
	}
}
