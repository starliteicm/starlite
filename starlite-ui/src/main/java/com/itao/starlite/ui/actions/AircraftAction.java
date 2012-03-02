package com.itao.starlite.ui.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.worksheet.Worksheet;
import org.jmesa.worksheet.WorksheetCallbackHandler;
import org.jmesa.worksheet.WorksheetColumn;
import org.jmesa.worksheet.WorksheetRow;
import org.jmesa.worksheet.WorksheetUtils;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.FlightActuals;
import com.itao.starlite.model.FlightLog;
import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobTicket;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SearchTableView;
import com.opensymphony.xwork2.ActionSupport;
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="aircraft!flightMismatches.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="aircraft.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
@Permissions("ManagerView")
public class AircraftAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3344297237658297955L;
	public String tableHtml;
	//public String tableHtml2;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Aircraft")};
	
	public Tab[] tableTabs;
   	public String tab = "aircraft";
	private Tab staticTab = new Tab("Static", "aircraft.action", false);
		public User user;
	
	//public Tab[] tableTabs = new Tab[] {staticTab, dynamicTab, documentsTab};
	
	public String saveDisabled = "disabled=\"disabled\"";
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	@Transient
	public FlightActuals selectedFlightActual = new FlightActuals();
	
	@Transient
	public List<FlightPlan> flightPlans = new ArrayList<FlightPlan>();
	@Transient
	public List<FlightLog>  flightLogs  = new ArrayList<FlightLog>();
	
	//public FlightPlan flightPlan = new FlightPlan();
	
	//public FlightLog  flightLog  = new FlightLog();
	//@Transient
	public boolean emptyLog = false;
	@Transient
	public boolean emptyPlan = false;
	@Transient
	public List<FlightActuals> flightActuals = new ArrayList<FlightActuals>();
	public int flightPlan = 0;
	public int flightLog = 0;
	public String notificationMessage="";
	public String errorMessage="";
	
	
	public AircraftAction() {
		super();
	}
	
	private static Date parseDate(String str) throws ParseException {
		return df.parse(str);
	}
	
	@Inject
	private StarliteCoreManager manager;
	@Override
	public String execute() throws Exception {
		
		prepareTabs();
		
		List<Aircraft> aircraft = manager.getAllAircraft().aircraftList;
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("aircraftTable", ServletActionContext.getRequest());
		
		//tableFacade.setEditable(true);
		
		tableFacade.setColumnProperties("ref", "ownership", "type", "model", "licence", "aircraftMaintenanceOrganisation", "livery");
		
		tableFacade.setItems(aircraft);
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		
		Column refCol = table.getRow().getColumn("ref");
		refCol.setTitle("Reg");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "ref", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("aircraftInfo.action?id="+id).quote().close();
				html.append(value);
				html.aEnd();
				return html.toString();
			}
			
		});
		
//		Column corCol = table.getRow().getColumn("certificateOfRegistration");
//		corCol.setTitle("CoR");
//		
//		Column aocCol = table.getRow().getColumn("aircraftOperatingCertificate");
//		aocCol.setTitle("AoC");
//		
//		Column coaCol = table.getRow().getColumn("certificateOfAirworthiness");
//		coaCol.setTitle("CoA");
		
		Column amoCol = table.getRow().getColumn("aircraftMaintenanceOrganisation");
		amoCol.setTitle("AMO");
		
//		Column coaExpiry = table.getRow().getColumn("coaExpiryDate");
//		coaExpiry.setTitle("Expiry Date");
//		coaExpiry.getCellRenderer().setCellEditor(new DateOrBasicEditor());
		
		//((HtmlTable)table).getTableRenderer().setWidth("600px");
		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
		staticTab.setCurrent(true);
		handleWorksheet(tableFacade);
		return SUCCESS;
	}
	
	private void handleWorksheet(TableFacade tableFacade) {
		Worksheet worksheet = tableFacade.getWorksheet();
		
		if (worksheet == null)
			return;
		
		if (worksheet.hasChanges()) {
			saveDisabled = "";
		}
		
		if (!worksheet.isSaving() || !worksheet.hasChanges()) {
		    return;
		}
		
		List<String> uniquePropertyValues = WorksheetUtils.getUniquePropertyValues(worksheet);
		final Map<String, Aircraft> aircrafts = manager.getAircraftsById(uniquePropertyValues);
		
		worksheet.processRows(new WorksheetCallbackHandler() {

			public void process(WorksheetRow worksheetRow) {
				Collection<WorksheetColumn> columns = worksheetRow.getColumns();
				Aircraft a = aircrafts.get(worksheetRow.getUniqueProperty().getValue());
				for (WorksheetColumn worksheetColumn: columns) {
					Object changedValue = worksheetColumn.getChangedValue();
					String property = worksheetColumn.getProperty();
					
					boolean error = false;
					if (property.equals("coaExpiryDate")) {
						try {
							changedValue = AircraftAction.parseDate((String)changedValue);
						} catch (ParseException e) {
							error = true;
						}
					}
					try {
						if (!error)
							PropertyUtils.setProperty(a, property, changedValue);
					} catch (Exception e) {}
				}
				manager.saveAircraft(a);
			}
			
		});
	}
	
	public String types(){
		//XXX working on
		current="aircraft type";
		tab="aircraft type";
		prepareTabs();
		List<AircraftType> aircraftTypes = manager.getAircraftTypes();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("aircraftTable", ServletActionContext.getRequest());
		tableFacade.setColumnProperties("name","description","id");
		tableFacade.setItems(aircraftTypes);
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		Column refCol1 = table.getRow().getColumn("name");
		refCol1.setTitle("Aircraft Type");
		
		Column refCol2 = table.getRow().getColumn("description");
		refCol2.setTitle("Type Description");
		
		Column refCol = table.getRow().getColumn("id");
		refCol.setTitle("Edit");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("aircraft!addType.action?typeId="+value).quote().close();
				html.append("Edit");
				html.aEnd();
				return html.toString();
			}
			
		});

		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
		return "types";
	}

	public AircraftType aircraftType;
	public Integer typeId;
	public String typeName;
	public String typeDescription;
	public String addType(){
		if (!prepareAdd) {
			if (typeName == null || typeName.trim().equals("")) {
				errorMessage = "Aircraft Type Name must be entered";
				return ERROR;
			}
			try {
				AircraftType a = new AircraftType();
				a.setId(typeId);
				a.setName(typeName);
				a.setDescription(typeDescription);
				manager.saveAircraftType(a);
				return types();
			} catch (Throwable t) {
				errorMessage = "An unexpected error occured. Aircraft Type could not be added";
				return ERROR;
			}
		}
		else {
			if(typeId != null){
				aircraftType = manager.getAircraftType(typeId);
			}
		}
		return "addType";
	}
	
	public String removeType(){
		return "redirect-types";
	}
	
	public boolean prepareAdd = true;
	public String aircraftReg;
	//public String errorMessage;
	public Aircraft createdAircraft;
	@Permissions("UserAdmin")
	public String addAircraft() {
		if (!prepareAdd) {
			if (aircraftReg == null || aircraftReg.trim().equals("")) {
				errorMessage = "Aircraft Registration must be entered";
				return ERROR;
			}
			try {
				Aircraft a = new Aircraft();
				a.setRef(aircraftReg);
				manager.saveAircraft(a);
				createdAircraft = a;
				return "added";
			} catch (Throwable t) {
				errorMessage = "An unexpected error occured. Aircraft could not be added";
				return ERROR;
			}
		}
		return "add";
	}

	public void setUser(User arg0) {
		user = arg0;
	}
	
	private void prepareTabs() {

		Tab aircraftTab = new Tab("Aircraft", "aircraft.action", tab.equals("aircraft"));
		Tab aircraftTypeTab = new Tab("Aircraft Type", "aircraft!types.action", tab.equals("aircraft type"));
		Tab aircraftFlightActualsTab = new Tab("Flight Actuals", "aircraft!flightActuals.action", tab.equals("flight actuals"));
		Tab aircraftFlightPlansTab = new Tab("Flight Plans", "aircraft!flightPlans.action", tab.equals("flight plans"));
		Tab aircraftFlightMisconfigurationsTab = new Tab("Flight Mismatches", "aircraft!flightMismatches.action", tab.equals("flight mismatches"));
		
		if (user.hasPermission("ManagerView"))
		//	tableTabs = new Tab[] {aircraftTab, aircraftTypeTab,aircraftFlightActualsTab,aircraftFlightPlansTab,aircraftFlightMisconfigurationsTab};
		//	tableTabs = new Tab[] {aircraftTab, aircraftTypeTab};
			tableTabs = new Tab[] {aircraftTab, aircraftTypeTab};
	}
	
	/*---------------------------------------------------------*/
	public String flightActuals()
	/*---------------------------------------------------------*/
	{
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='aircraft.action'>Aircraft</a>"),
				new Breadcrumb("<a href='aircraft!flightActuals.action'>Flight Actuals</a>")};
		
		current="flight actuals";
		tab="flight actuals";
		prepareTabs();
		List<FlightActuals> flightActuals = manager.findAllFlightActuals();
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("FlightActuals", ServletActionContext.getRequest());
		tableFacade.setColumnProperties("flightLog_flightIDField","flightPlan_flightPlanID","flightActualStatus_flightActualStatusID","flightActualsID","flightActualsID");
		tableFacade.setItems(flightActuals);
		tableFacade.setMaxRows(15);
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("flightActualsID");
		
		Column refCol1 = table.getRow().getColumn("flightActualsID");
		refCol1.setTitle("ID");
	    refCol1.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return ((FlightActuals) item).getFlightActualsID();
					}
			});
	    
	    final int fieldLength = 20;
		
		Column refCol2 = table.getRow().getColumn("flightLog_flightIDField");
		refCol2.setTitle("Flight Log: Flight Date | Aircraft | Captain");
        refCol2.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						/*return (""+((FlightActuals) item).getFlightLog().getFlightDateField()+" | "+((FlightActuals) item).getFlightLog().getRegistrationField() +
								" | "+((FlightActuals) item).getFlightLog().getCaptainField());
								*/
						String arg1 = ((FlightActuals) item).getFlightLog().getFlightDateField().trim();
						String temp="";
						if ((arg1.length()<fieldLength))
						{
							for (int i=0; i<fieldLength-arg1.length();i++){temp += "&nbsp;";}
							arg1 += temp;
						}
						String arg2 = ((FlightActuals) item).getFlightLog().getRegistrationField().trim();
						temp="";
						if ((arg2.length()<fieldLength))
						{
							for (int i=0; i<fieldLength-arg2.length();i++){temp += "&nbsp;";}
							arg2 += temp;
						}
						String arg3 = ((FlightActuals) item).getFlightLog().getCaptainField().trim();
						temp="";
						if ((arg3.length()<fieldLength+10))
						{
							for (int i=0; i<fieldLength+10-arg3.length();i++){temp += "&nbsp;";}
							arg3 += temp;
						}
						
						temp = arg1 + "|" + arg2 +"|" + arg3;
						return (temp);
						
					}
			});
        
        Column refCol3 = table.getRow().getColumn("flightPlan_flightPlanID");
		refCol3.setTitle("Flight Plan: Invoice No | Customer");
		refCol3.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowCount) {
				//return (""+((FlightActuals) item).getFlightPlan().getCustomer()+" | "+((FlightActuals) item).getFlightPlan().getInvoiceNo());
				String arg1 = ((FlightActuals) item).getFlightPlan().getInvoiceNo();
				String temp="";
				if ((arg1.length()<5))
				{
					for (int i=0; i<5-arg1.length();i++){temp += "&nbsp;";}
					arg1 += temp;
				}
				String arg2 = ((FlightActuals) item).getFlightPlan().getCustomer();
				temp="";
				if ((arg2.length()<15))
				{
					for (int i=0; i<15-arg2.length();i++){temp += "&nbsp;";}
					arg2 += temp;
				}
				temp = arg1+" | "+arg2;
				return (temp);
			}
	    });
		
		Column refCol4 = table.getRow().getColumn("flightActualStatus_flightActualStatusID");
		refCol4.setTitle("Status");
		refCol4.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowCount) {
				return ((FlightActuals) item).getFlightActualStatus().getFlightActualStatusValue();
			}
	    });
		
		Column refCol = table.getRow().getColumn("flightActualsID");
		refCol.setTitle("View Details");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				//html.a().href().quote().append("aircraft!update.action?typeId="+value).quote().close();
				html.a().href().quote().append("flightActualsApproval.action?typeId="+value).quote().close();
				html.append("View");
				html.aEnd();
				return html.toString();
			}
			
		});

		//tableFacade.setView(new PlainTableView());
		//tableHtml = tableFacade.render();
		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
		
		return "flight actuals";
	}//flightActuals()
	/*---------------------------------------------------------*/
	/*---------------------------------------------------------*/
	public String flightPlans()
	/*---------------------------------------------------------*/
	{
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='aircraft.action'>Aircraft</a>"),
				new Breadcrumb("<a href='aircraft!flightPlans.action'>Flight Plans</a>")};
		
		current="flight plans";
		tab="flight plans";
		prepareTabs();
		
		List<FlightPlan> flightPlans = manager.findAllFlightPlans();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("FlightPlan", ServletActionContext.getRequest());
		tableFacade.setColumnProperties("customer","invoiceNo","flightType","flightPlanID","flightPlanID");
		tableFacade.setItems(flightPlans);
		tableFacade.setMaxRows(15);
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("flightPlanID");
		
		Column refCol1 = table.getRow().getColumn("flightPlanID");
		refCol1.setTitle("ID");
	    refCol1.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return ((FlightPlan) item).getFlightPlanID();
					}
			});
		
		Column refCol2 = table.getRow().getColumn("customer");
		refCol2.setTitle("Customer");
        refCol2.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return (""+((FlightPlan) item).getCustomer());
					}
			});
        
        Column refCol3 = table.getRow().getColumn("invoiceNo");
		refCol3.setTitle("Invoice No");
		refCol3.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowCount) {
				return (""+((FlightPlan) item).getInvoiceNo());
				}
	    });
		
		Column refCol4 = table.getRow().getColumn("flightType");
		refCol4.setTitle("Flight Type");
		refCol4.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowCount) {
				return ((FlightPlan) item).getFlight_TypeField();
			}
	    });
		
		Column refCol = table.getRow().getColumn("flightPlanID");
		refCol.setTitle("View Details");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("flightPlan!edit.action?id="+value+"&edited=true").quote().close();
				//html.a().href().quote().append("flightActualsApproval!addType.action?typeID="+value).quote().close();
				html.append("View");
				html.aEnd();
				return html.toString();
			}
			
		});

		//tableFacade.setView(new PlainTableView());
		//tableHtml = tableFacade.render();
		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
		return "flight plans";
	}//flightPlans()
	/*---------------------------------------------------------*/
	public String flightMismatches()
	/*---------------------------------------------------------*/
	{
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='aircraft.action'>Aircraft</a>"),
				new Breadcrumb("<a href='aircraft!flightMismatches.action'>Flight Mismatches</a>")};
		
		
		current="flight mismatches";
		tab="flight mismatches";
		prepareTabs();
		
		this.flightActuals = manager.findMismatchedFlightActuals();
		
		if (flightActuals.isEmpty() == false)
		{
		for (int i =0; i< flightActuals.size(); i++)
		{
		
			FlightActuals temp = flightActuals.get(i);
			FlightLog log = temp.getFlightLog();
			FlightPlan plan = temp.getFlightPlan();
			if (log == null)
			{
                  log = new FlightLog();
                  temp.setFlightLog(log);
                  //flightActuals.set(i,temp);
			}
			else {this.flightLogs.add(log);}
					
			if (plan == null)
			{
				plan = new FlightPlan();
				temp.setFlightPlan(plan);
				//flightActuals.set(i, temp);
			}
			else {this.flightPlans.add(plan);}
			
		}
		
		//if (this.flightLogs.isEmpty()) {this.emptyLog  = true;}
		//else {this.flightLog = this.manager.findFlightLogById(this.flightLogs.get(0).getFlightIDField());}
		//if (this.flightPlans.isEmpty()){this.emptyPlan = true;}
		//else {this.flightPlan = this.manager.findFlightPlanById(this.flightPlans.get(0).getFlightPlanID());}
		
		}
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("FlightActuals", ServletActionContext.getRequest());
		tableFacade.setColumnProperties("flightActualsID","flightLog_flightIDField","flightPlan_flightPlanID","flightActualStatus_flightActualStatusID");
		
		tableFacade.setItems(flightActuals);
		tableFacade.setMaxRows(15);
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("flightActualsID");
		
		Column refCol1 = table.getRow().getColumn("flightActualsID");
		refCol1.setTitle("ID");
	    refCol1.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return ((FlightActuals) item).getFlightActualsID();
					}
			});
	    
	    final int fieldLength = 20;
		
		Column refCol2 = table.getRow().getColumn("flightLog_flightIDField");
		refCol2.setTitle("Flight Log: Flight Date | Aircraft | Captain");
        refCol2.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) 
					{
						String wholeString="";
						if (((FlightActuals) item).getFlightLog() != null)
						{
						String arg1 = ((FlightActuals) item).getFlightLog().getFlightDateField();
						String temp="";
						if ((arg1.length()<fieldLength))
						{
							for (int i=0; i<fieldLength-arg1.length();i++){temp += "&nbsp;";}
							arg1 += temp+"|";
						}
						String arg2 = ((FlightActuals) item).getFlightLog().getRegistrationField();
						temp="";
						if ((arg2.length()<fieldLength))
						{
							for (int i=0; i<fieldLength-arg2.length();i++){temp += "&nbsp;";}
							arg2 += temp+"|";
						}
						String arg3 = ((FlightActuals) item).getFlightLog().getCaptainField();
						temp="";
						if ((arg3.length()<fieldLength+10))
						{
							for (int i=0; i<fieldLength+10-arg3.length();i++){temp += "&nbsp;";}
							arg3 += temp;
						}
						
					
						int id = ((FlightActuals) item).getFlightLog().getFlightIDField();
						if (id == 0)
						{
							//wholeString="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							//wholeString += arg1+arg2+arg3;
						}
						else
						{
						
						HtmlBuilder html = new HtmlBuilder();
						html.a().href().quote().append("flightLog.action?id="+id).quote().close();
						html.append("View");
						html.aEnd();
						
						wholeString = html.toString()+"&nbsp;"+arg1+arg2+arg3;
						}
						}
						return (wholeString);
						
					}
			});
        
       Column refCol3 = table.getRow().getColumn("flightPlan_flightPlanID");
		refCol3.setTitle("Flight Plan: Invoice No | Customer");
		refCol3.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowCount) 
			{
				String wholeString="";
				if (((FlightActuals) item).getFlightLog() != null)
				{
				//return (""+((FlightActuals) item).getFlightPlan().getCustomer()+" | "+((FlightActuals) item).getFlightPlan().getInvoiceNo());
				
				String arg1 = ((FlightActuals) item).getFlightPlan().getInvoiceNo();
				String temp="";
				if ((arg1.length()<5))
				{
					for (int i=0; i<5-arg1.length();i++){temp += "&nbsp;";}
					arg1 += temp;
				}
				String arg2 = ((FlightActuals) item).getFlightPlan().getCustomer();
				temp="";
				if ((arg2.length()<15))
				{
					for (int i=0; i<15-arg2.length();i++){temp += "&nbsp;";}
					arg2 += temp;
				}
				
				String arg3 = "";
				int id = ((FlightActuals) item).getFlightPlan().getFlightPlanID();
				if (id == 0)
				{
					wholeString = arg1+arg2;
				}
				else
				{
				
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("flightPlan!edit.action?id="+id+"&edited=true").quote().close();
				html.append("View");
				html.aEnd();
				
				wholeString = html.toString()+"&nbsp;"+arg1+"&nbsp;|&nbsp;"+arg2;
				}
				}
				return (wholeString);
			}
	    });
		
		Column refCol4 = table.getRow().getColumn("flightActualStatus_flightActualStatusID");
		refCol4.setTitle("Status");
		refCol4.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowCount) {
				return ((FlightActuals) item).getFlightActualStatus().getFlightActualStatusValue();
			}
	    });
		

		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();


		return "flight mismatches";
	}//flightMismatches()
	/*---------------------------------------------------------*/
	public String link()
	/*---------------------------------------------------------*/
	{
		//XXX working on
		current="flight mismatches";
		tab="flight mismatches";
		prepareTabs();
		prepare();
		
		String error ="";
		this.errorMessage="";
		
		
		boolean pass = true;
		if (pass)
		{
			try
			{
				
				this.flightActuals = manager.findMismatchedFlightActuals();
				FlightActuals plan = null;
				FlightActuals log = null;
				boolean logFound = false;
				boolean planFound = false;
				
				for (int i=0; i < this.flightActuals.size(); i++)
				{
					FlightActuals temp = this.flightActuals.get(i);
					
					if ((temp.getFlightLog() != null) && (logFound == false))
					{
						if (temp.getFlightLog().getFlightIDField() == flightLog)
						{
				    	    log = this.manager.findFlightActualsByID(temp.getFlightActualsID());
				    	    logFound = true;
						}
					}
					
					if ((temp.getFlightPlan() != null) && (planFound == false))
					{
						if (temp.getFlightPlan().flightPlanID == flightPlan)
						{
				    	    plan = this.manager.findFlightActualsByID(temp.getFlightActualsID());
				    	    planFound = true;
						}
					}
				
					if ((planFound) && (logFound))
					{
						break;
					}
				
				}
				if (log == null)  {pass = false; error="Unable to find the selected Flight Log. Please check your selection.";	} 
				if (plan == null) {pass = false; error="Unable to find the selected Flight Plan. Please check your selection.";	}
				
				if (pass)
				{
					//update the actual with the log and plan.
					FlightLog fl = log.getFlightLog();
					plan.setFlightLog(fl);
					
					
					try
					{
					plan = this.manager.saveFlightActuals(plan);
					log.setFlightLog(null);
					//log.setFlightPlan(null);
					//log.setFlightActualStatus(null);
					log = manager.saveFlightActuals(log);
					manager.deleteMismatchedFlightActual(log.getFlightActualsID());
					
					}
					catch (Exception ex)
					{
						pass = false; 
						this.errorMessage="Unable to link Flight Plan("+flightPlan+") to Flight Log ("+flightLog+"). Possible Reason:"+ex.getMessage().toString()+"<br/>" + error;
					}
					
				}
			}
			catch (Exception ex)
			{
				pass = false; 
				this.errorMessage="Unable to link Flight Plan("+flightPlan+") to Flight Log ("+flightLog+"). Possible Reason:"+ex.getMessage().toString()+"<br/>" + error;
				
			}
		}
		
		
		return "redirect";
	}//flightMismatches()
	/*---------------------------------------------------------*/
	public void prepare(){
		if (this.typeId == null) {
			 this.selectedFlightActual = new FlightActuals();
		}
		else {
			this.selectedFlightActual = manager.findFlightActualsByID(typeId);
			if (this.selectedFlightActual == null)
			{
				this.selectedFlightActual = new FlightActuals();
			}
		}
	}
}
