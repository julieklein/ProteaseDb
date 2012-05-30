package com.google.gwt.proteasedb.client;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
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
import com.sun.source.tree.NewClassTree;

public class ProteasiXEntryPoint implements EntryPoint {

	// rpc init Var
	private DBConnectionAsync callProvider;
	// main widget panel
	private TabPanel mainPanel = new TabPanel();
	
	//All that is necessary for PROTEINSEARCH WIDGET (1)
	private FlowPanel proteinTab_1 = new FlowPanel();
	
	private VerticalPanel pResultPanel_1 = new VerticalPanel();
	private HorizontalPanel searchpanel_1 = new HorizontalPanel();
	private TextArea searchBox_1 = new TextArea();
	private Button searchButton_1 = new Button("Search");
	private DisclosurePanel dpProtease_1 = new DisclosurePanel(
			"Click to See/Hide Cleavage Site Results");
	private DisclosurePanel dpPeptide_1 = new DisclosurePanel(
			"Click to See/Hide Peptide Results");
	private Label lSubstrate_1 = new Label("Substrate");
	private Label lSubstrateOutput_1 = new Label();
	private Label lResult_1 = new Label("Results View");

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

	//All that is necessary for PEPTIDESEARCH WIDGET (2)
	private FlowPanel peptideTab_2 = new FlowPanel();
	private VerticalPanel pResultPanel_2 = new VerticalPanel();
	private HorizontalPanel searchpanel_2 = new HorizontalPanel();
	private TextArea searchBox_2 = new TextArea();
	private Button searchButton_2 = new Button("Search");
	private Label lResult_2 = new Label("Results View");
	private DisclosurePanel dpExistingPeptide_2 = new DisclosurePanel(
			"Click to See/Hide Existing Peptides");
	private DisclosurePanel dpNotFoundPeptide_2 = new DisclosurePanel(
			"Click to See/Hide Not Found Peptides");
	private Label lNotFound_2 = new Label();
	private Label lnoExisting_2 = new Label("Sorry, none of your peptides have been found in the database...");
	private Label lnoNotFound_2 = new Label("All your peptides have been found in the database!");
	
	
	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		// TODO Auto-generated method stub

		//Set Up the PROTEINSEARCH WIDGET
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
		//Set Up the MainPanel
				mainPanel.removeStyleName("gwt-TabPanelBottom");
				mainPanel.add(proteinTab_1, "Search by protein");
				mainPanel.selectTab(0);
		
		//Set Up the PEPTIDESEARCH Widget
		searchpanel_2.add(searchBox_2);
		searchBox_2.setHeight("68px");
		searchBox_2.setWidth("400px");
		searchpanel_2.add(searchButton_2);
		searchButton_2.addStyleDependentName("search");
		searchpanel_2.addStyleName("pSearchpanel");
		peptideTab_2.add(searchpanel_2);
		peptideTab_2
				.add(new HTML(
						"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));

		pResultPanel_2.addStyleName("pResultPanel");
		peptideTab_2.add(pResultPanel_2);
		pResultPanel_2.setWidth("700px");
		mainPanel.add(peptideTab_2, "Search by peptide");
		
		
		RootPanel rootPanel = RootPanel.get("protease");
		rootPanel.add(mainPanel);

		
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
				// keep search to setup prepared statement
				//init rpc
				rpcInit();
				generateSearchRequest_1();
				// start the process

			}
		});

		dpProtease_1.addOpenHandler(new OpenHandler() {

			@Override
			public void onOpen(OpenEvent event) {
				// TODO Auto-generated method stub

			}
		});

		dpPeptide_1.addOpenHandler(new OpenHandler() {

			@Override
			public void onOpen(OpenEvent event) {
				// TODO Auto-generated method stub

			}
		});
		
		// // start the process on PEPTIDESEARCH WIDGET
		// Listen for mouse events on the Add button.
				searchButton_2.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// keep search to setup prepared statement
						//init rpc
						rpcInit();
						generateSearchRequest_2();
						// start the process

					}
				});
				
				dpExistingPeptide_2.addOpenHandler(new OpenHandler() {

					@Override
					public void onOpen(OpenEvent event) {
						// TODO Auto-generated method stub

					}
				});

				dpNotFoundPeptide_2.addOpenHandler(new OpenHandler() {

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

	private void generateSearchRequest_1() {
		String search = searchBox_1.getText().toUpperCase().trim();

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
				searchRequest[i].setProteininputsymbol(string);
				searchRequest[i].setRequestnature("proteinRequest");
				System.out.println(string + "zzzz");
				i++;
			}
		}

		searchBox_1.setFocus(true);
		searchBox_1.setText("");

		getResulbySubstratetInfo_1(searchRequest);

	}

	private void getResulbySubstratetInfo_1(SearchRequest[] searchRequest) {

		//empty if anything is there already
		pResultPanel_1.clear();
		dpPeptide_1.clear();
		dpProtease_1.clear();
		
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
				
				pResultPanel_1.add(lResult_1);
				lResult_1.addStyleName("lResult");

				String substrateOutput = resultbySubstrateData[0].substrate.S_Symbol
						+ ", "
						+ resultbySubstrateData[0].substrate.S_NL_Name
						+ " ("
						+ resultbySubstrateData[0].substrate.S_Uniprotid
						+ ")";
				lSubstrateOutput_1 = new Label(substrateOutput);

		
				pResultPanel_1.add(lSubstrateOutput_1);
				lSubstrateOutput_1.addStyleName("lSubstrateOutput");
				int numbercs = 0;
				int numberpep = 0;

				for (ResultbySubstrateData resultbySubstrateData2 : resultbySubstrateData) {
					if (resultbySubstrateData2.getNature().equals("cleavagesite")) {
						numbercs++;
					}
					else if (resultbySubstrateData2.getNature().equals("peptide")) {
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
					dpProtease_1.add(lErrorcs);
					pResultPanel_1.add(dpProtease_1);
					dpProtease_1.addStyleDependentName("mydp");
					pResultPanel_1
					.add(new HTML(
							"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
					List<ResultbySubstrateData> resultpeplist = Arrays.asList(resultpep);
					lSubstrate_1.addStyleName("Label");
					pResultPanel_1.add(dpPeptide_1);
					dpPeptide_1.addStyleDependentName("mydp");
					pResultPanel_1.addStyleName("Label");
					createPeptideTable(resultpep, resultpeplist);

				} else if (!resultcs[0].getEntryValidity().contains(
						"Sorry, there is no cleavage site") && resultpep[0].getEntryValidity().contains(
								"Sorry, there is no peptide")) {
					List<ResultbySubstrateData> resultcslist = Arrays.asList(resultcs);
					lSubstrate_1.addStyleName("Label");
					pResultPanel_1.add(dpProtease_1);
					dpProtease_1.addStyleDependentName("mydp");
					pResultPanel_1
					.add(new HTML(
							"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
					dpPeptide_1.add(lErrorpep);
					pResultPanel_1.add(dpPeptide_1);
					dpPeptide_1.addStyleDependentName("mydp");
					pResultPanel_1.addStyleName("Label");
					createCleavageSiteTable(resultcs, resultcslist);

					
				}else if (!resultcs[0].getEntryValidity().contains(
						"Sorry, there is no cleavage site") && !resultpep[0].getEntryValidity().contains(
								"Sorry, there is no peptide")) {
					List<ResultbySubstrateData> resultcslist = Arrays.asList(resultcs);
					pResultPanel_1.add(dpProtease_1);
					dpProtease_1.addStyleDependentName("mydp");
					pResultPanel_1
					.add(new HTML(
							"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
					List<ResultbySubstrateData> resultpeplist = Arrays.asList(resultpep);
					lSubstrate_1.addStyleName("Label");
					pResultPanel_1.add(dpPeptide_1);
					dpPeptide_1.addStyleDependentName("mydp");
					pResultPanel_1.addStyleName("Label");
					createCleavageSiteTable(resultcs, resultcslist);
					createPeptideTable(resultpep, resultpeplist);
					
				}

				
				
				
			
		
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
				String start = null;
				if (resultbySubstrateData.peptide.start == 0) {
					start = "?";
				}else {
					start = Integer.toString(resultbySubstrateData.peptide.start);
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
					}else {
						end = Integer.toString(resultbySubstrateData.peptide.end);
					}
			        return end;
			      }

		}; 
		
		TextColumn<ResultbySubstrateData> structureCol = new TextColumn<ResultbySubstrateData>() {
			 @Override
		      public String getValue(ResultbySubstrateData resultbySubstrateData) {
		        return resultbySubstrateData.peptide.structure;
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
			 }else if (resultbySubstrateData.peptide.regulation.equals("")) {
				 regulation = "-" ;
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
		structureCol.setSortable(true);
		
		// Add the columns.
		peptideTable.addColumn(substrateCol,"Substrate\u25B2\u25BC" );
		peptideTable.addColumn(startCol,"Start\u25B2\u25BC" );
		peptideTable.addColumn(endCol, "End\u25B2\u25BC");
		peptideTable.addColumn(structureCol, "Structure/Function\u25B2\u25BC");
		peptideTable.addColumn(diseaseCol, "Disease\u25B2\u25BC");
		peptideTable.addColumn(regulationCol, "Regulation\u25B2\u25BC");

		peptideTable.setColumnWidth(substrateCol, 5, Unit.EM);
		peptideTable.setColumnWidth(startCol, 2, Unit.EM);
		peptideTable.setColumnWidth(endCol, 2, Unit.EM);
		peptideTable.setColumnWidth(structureCol, 20, Unit.EM);
		peptideTable.setColumnWidth(diseaseCol, 20, Unit.EM);
		peptideTable.setColumnWidth(regulationCol, 2, Unit.EM);
		

		
		dpPeptide_1.add(peptideTable);

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
		ListHandler<ResultbySubstrateData> structureColSortHandler = new ListHandler<ResultbySubstrateData>(
				peplist);
		structureColSortHandler.setComparator(structureCol,
		    new Comparator<ResultbySubstrateData>() {
		      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
		        if (o1 == o2) {
		          return 0;
		        }

		        // Compare the symbol columns.
		        if (o1 != null) {
		          return (o2 != null) ? o1.peptide.structure.compareTo(o2.peptide.structure) : 1;
		        }
		        return -1;
		      }
		    });
		peptideTable.addColumnSortHandler(structureColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		peptideTable.getColumnSortList().push(structureCol);
		
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

		dpProtease_1.add(cleavageSiteTable);

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
	
	private void generateSearchRequest_2() {
		String search = searchBox_2.getText().toUpperCase().trim();

		String splitSearch[] = search.split("\n");
		LinkedHashSet<String> set = new LinkedHashSet<String>();

		for (String searchSequence : splitSearch) {
			searchSequence.toUpperCase().trim();
			set.add(searchSequence);
			System.out.println(searchSequence + "uuu");
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
				searchRequest[i].setPeptideinputsequence(string);
				int j = i+1;
				String searchnumber = Integer.toString(j);
				searchRequest[i].setPeptideinputnumber("Search "+ searchnumber);
				System.out.println("Search "+ searchnumber);
				searchRequest[i].setRequestnature("peptideRequest");
				System.out.println(string + "zzzz");
				i++;
			}
		}

		searchBox_2.setFocus(true);
		searchBox_2.setText("");

		getResultbyPeptideInfo_2(searchRequest);

	}
	
	private void getResultbyPeptideInfo_2(SearchRequest[] searchRequest) {

		//empty if anything is there already
		pResultPanel_2.clear();		pResultPanel_1.clear();
		dpExistingPeptide_2.clear();
		dpNotFoundPeptide_2.clear();
		
		
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
		
		pResultPanel_2.add(lResult_2);
		lResult_2.addStyleName("lResult");
		
		int numberexisting = 0;
		int numbernotfound = 0;

		for (ResultbySubstrateData resultbySubstrateData2 : resultbySubstrateData) {
			if (!resultbySubstrateData2.getEntryValidity().contains("doesn't exist")) {
				numberexisting++;
			}
			else if (resultbySubstrateData2.getEntryValidity().contains("doesn't exist")) {
				numbernotfound++;
			}
			System.out.println(resultbySubstrateData2);
		}
		
		System.out.println(numberexisting + "numberexisting");
		System.out.println(numbernotfound + "numbernotfound");

		ResultbySubstrateData[] resultexisting = new ResultbySubstrateData[numberexisting];
		ResultbySubstrateData[] resultnotfound = new ResultbySubstrateData[numbernotfound];

		
		int k = 0;
		int l = 0;
		
		for (int i=0;i<resultbySubstrateData.length; i++) {
			if (!resultbySubstrateData[i].getEntryValidity().contains("doesn't exist")) {
				resultexisting[k] = resultbySubstrateData[i];
				k++;
			}else {
				if(resultbySubstrateData[i].getEntryValidity().contains("doesn't exist"))
					resultnotfound[l] = resultbySubstrateData[i];
				l++;
			}
		}
		if (!(numberexisting==0) && !(numbernotfound ==0)) {
			

		} else if ((numberexisting==0) && !(numbernotfound ==0)) {
			List<ResultbySubstrateData> resultnotfoundlist = Arrays.asList(resultnotfound);
			dpExistingPeptide_2.add(lnoExisting_2);
			pResultPanel_2.add(dpExistingPeptide_2);
			dpExistingPeptide_2.addStyleDependentName("mydp");
			pResultPanel_2
			.add(new HTML(
					"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));

			pResultPanel_2.add(dpNotFoundPeptide_2);
			dpNotFoundPeptide_2.addStyleDependentName("mydp");
			pResultPanel_2.addStyleName("Label");
			createNotFoundList(resultnotfound);

			
		} else if (!(numberexisting==0) && (numbernotfound ==0)) {
			List<ResultbySubstrateData> resultexistinglist = Arrays.asList(resultexisting);
			pResultPanel_2.add(dpExistingPeptide_2);
			dpExistingPeptide_2.addStyleDependentName("mydp");
			pResultPanel_2
			.add(new HTML(
					"<div><hr style=\"height:8px;;border-width:0;color:#9FB9A8;background-color:#9FB9A8;\"></div>"));
			dpNotFoundPeptide_2.add(lnoNotFound_2);
			pResultPanel_2.add(dpNotFoundPeptide_2);
			dpNotFoundPeptide_2.addStyleDependentName("mydp");
			pResultPanel_2.addStyleName("Label");
			createExistingPeptideTable(resultexisting, resultexistinglist);		
		}
	}
	
	private void createExistingPeptideTable(ResultbySubstrateData[] resultexisting,
			List<ResultbySubstrateData> resultexistinglist) {
		int rowsexisting = resultexisting.length;
		
		CellTable.Resources ptableresources = GWT.create(PTableResources.class);
		CellTable<ResultbySubstrateData> existingpeptideTable = new CellTable<ResultbySubstrateData>(rowsexisting, ptableresources);

		
		//Create columns
				TextColumn<ResultbySubstrateData> searchCol = new TextColumn<ResultbySubstrateData>() {
					 @Override
				      public String getValue(ResultbySubstrateData resultbySubstrateData) {
				        return resultbySubstrateData.peptide.searchnumber;
				      }

				}; 
				
				//Create columns
				TextColumn<ResultbySubstrateData> sequenceCol = new TextColumn<ResultbySubstrateData>() {
					 @Override
				      public String getValue(ResultbySubstrateData resultbySubstrateData) {
				        return resultbySubstrateData.peptide.sequence;
				      }

				}; 
		
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
				String start = null;
				if (resultbySubstrateData.peptide.start == 0) {
					start = "?";
				}else {
					start = Integer.toString(resultbySubstrateData.peptide.start);
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
					}else {
						end = Integer.toString(resultbySubstrateData.peptide.end);
					}
			        return end;
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
			 }else if (resultbySubstrateData.peptide.regulation.equals("")) {
				 regulation = "-" ;
			 }
			 return new SafeHtmlBuilder().appendHtmlConstant(regulation).toSafeHtml();
		      }
			

		}; 
		
		// Make the columns sortable.
		searchCol.setSortable(true);
		substrateCol.setSortable(true);
		startCol.setSortable(true);
		endCol.setSortable(true);
		diseaseCol.setSortable(true);
		regulationCol.setSortable(true);

		
		// Add the columns.
		existingpeptideTable.addColumn(searchCol,"Search Number\u25B2\u25BC" );
		existingpeptideTable.addColumn(sequenceCol,"Sequence" );
		existingpeptideTable.addColumn(substrateCol,"Substrate\u25B2\u25BC" );
		existingpeptideTable.addColumn(startCol,"Start\u25B2\u25BC" );
		existingpeptideTable.addColumn(endCol, "End\u25B2\u25BC");
		existingpeptideTable.addColumn(diseaseCol, "Disease\u25B2\u25BC");
		existingpeptideTable.addColumn(regulationCol, "Regulation\u25B2\u25BC");

		existingpeptideTable.setColumnWidth(searchCol, 5, Unit.EM);
		existingpeptideTable.setColumnWidth(sequenceCol, 20, Unit.EM);
		existingpeptideTable.setColumnWidth(substrateCol, 5, Unit.EM);
		existingpeptideTable.setColumnWidth(startCol, 2, Unit.EM);
		existingpeptideTable.setColumnWidth(endCol, 2, Unit.EM);
		existingpeptideTable.setColumnWidth(diseaseCol, 20, Unit.EM);
		existingpeptideTable.setColumnWidth(regulationCol, 2, Unit.EM);
		
		dpExistingPeptide_2.add(existingpeptideTable);

		 // Create a data provider.
		ListDataProvider<ResultbySubstrateData> existingdataProvider = new ListDataProvider<ResultbySubstrateData>();
		
 // Connect the table to the data provider.
		existingdataProvider.addDataDisplay(existingpeptideTable);
		
 // Add the data to the data provider, which automatically pushes it to the
		// widget
		List<ResultbySubstrateData> existinglist = existingdataProvider.getList();
		for (ResultbySubstrateData existingTable : resultexistinglist) {
			existinglist.add(existingTable);
		}
		
		// Add a ColumnSortEvent.ListHandler to connect sorting to the
				// java.util.List.
				ListHandler<ResultbySubstrateData> searchColSortHandler = new ListHandler<ResultbySubstrateData>(
						existinglist);
				searchColSortHandler.setComparator(searchCol,
				    new Comparator<ResultbySubstrateData>() {
				      public int compare(ResultbySubstrateData o1, ResultbySubstrateData o2) {
				        if (o1 == o2) {
				          return 0;
				        }

				        // Compare the symbol columns.
				        if (o1 != null) {
				          return (o2 != null) ? o1.peptide.searchnumber.compareTo(o2.peptide.searchnumber) : 1;
				        }
				        return -1;
				      }
				    });
				existingpeptideTable.addColumnSortHandler(searchColSortHandler);
				
		 // We know that the data is sorted alphabetically by default.
				existingpeptideTable.getColumnSortList().push(searchCol);
		
		
 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> pepsubstrateColSortHandler = new ListHandler<ResultbySubstrateData>(
				existinglist);
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
		existingpeptideTable.addColumnSortHandler(pepsubstrateColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		existingpeptideTable.getColumnSortList().push(substrateCol);

 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> startColSortHandler = new ListHandler<ResultbySubstrateData>(
				existinglist);
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
		existingpeptideTable.addColumnSortHandler(startColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		existingpeptideTable.getColumnSortList().push(startCol);
		
 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> endColSortHandler = new ListHandler<ResultbySubstrateData>(
				existinglist);
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
		existingpeptideTable.addColumnSortHandler(endColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		existingpeptideTable.getColumnSortList().push(endCol);
	
		
 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> diseaseColSortHandler = new ListHandler<ResultbySubstrateData>(
				existinglist);
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
		existingpeptideTable.addColumnSortHandler(diseaseColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		existingpeptideTable.getColumnSortList().push(diseaseCol);
		
		// Add a ColumnSortEvent.ListHandler to connect sorting to the
		// java.util.List.
		ListHandler<ResultbySubstrateData> regulationColSortHandler = new ListHandler<ResultbySubstrateData>(
				existinglist);
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
		existingpeptideTable.addColumnSortHandler(regulationColSortHandler);
		
 // We know that the data is sorted alphabetically by default.
		existingpeptideTable.getColumnSortList().push(regulationCol);
	}
	
	
	private void createNotFoundList(ResultbySubstrateData[] resultnotfound) {
		String notfound = "";
		int i=0;
		for (ResultbySubstrateData resultbySubstrateData : resultnotfound) {
			String searchnumber = resultnotfound[i].peptide.searchnumber;
			String sequence = resultnotfound[i].peptide.sequence;
			notfound = notfound + searchnumber + ": "+ sequence + "\n";
			i++;
		}
		lNotFound_2 = new Label (notfound);
		dpNotFoundPeptide_2.add(lNotFound_2);
	
	}
	
}
