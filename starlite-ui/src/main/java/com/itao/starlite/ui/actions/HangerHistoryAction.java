package com.itao.starlite.ui.actions;

import java.util.List;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.limit.ExportType;
import org.jmesa.limit.Limit;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.model.JobTicket;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SearchTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Celeste Groenewald
 *
 */
@Results({
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="hangerHistory.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
public class HangerHistoryAction extends ActionSupport implements UserAware, Preparable {
	
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	public String tab = "active";
	public Tab[] tableTabs;
	public String tableHtml;
	public String valTableHtml;
	public String histTableHtml;
	
	public String notificationMessage;
	public String errorMessage;
	
	//The id of the Selected Ticket under the Tickets Tab
	public Integer id;
	
	public List<Aircraft> aircrafts;
	public List<JobTask> jobTasks;
	public List<JobTicket> jobTicketsForUser;
	
	public String current="HangerHistory";
	public JobTicket jobTicket = null;
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Hanger Management")};
	
    @Inject
	private StarliteCoreManager manager;
	public String selectedValue = "none";
	private CrewMember currentUser;
	
	/*-----------------------------------------------------------------*/
	/**
	 * <p>The default function called when none is specified. Within HangerHistoryAction
	 * it will:</p>
	 * <OL>
     *   <LI>Retrieve the current JobTicket via DAO for current user (only WIP, SUSPENDED and CLOSED)</LI>
     *   <LI>Create a Table for all Tickets by the User that's not in OPEN</LI>
     *   <LI>Allow the user to click on a "View History" button that will take them
     *    to the corresponding Table located on the History Tab.</LI>
     *</OL>
	 */
	/*-----------------------------------------------------------------*/
	@Override
	public String execute() throws Exception
	/*-----------------------------------------------------------------*/
	{
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='hanger.action'>Hanger Management</a>"),
				new Breadcrumb("<a href='hangerHistory.action'>History</a>")};
		
		this.errorMessage="";
		this.notificationMessage="";
		
		//set the first tab ("Tickets") as active
		prepareTabs();
		Tab view = (Tab)this.tableTabs[0];
	    view.setCurrent(true);
		
		//get all the ticket info for the current user
		this.jobTicketsForUser = manager.getAllTicketsByUser(this.user.getUsername());		
		this.currentUser = manager.getCrewMemberByCode(this.user.getUsername());
		
		//create and display the table with all the user's tickets
		try
		{
        TableFacade tableFacade = createTable();
		
		
		Limit limit = tableFacade.getLimit();
		if (limit.isExported()) {
		    tableFacade.render();
		    return null;
		} 
		//sets the view to include search boxes
     	tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
		}
		catch (Exception ex)
		{
			this.errorMessage="ERROR: Possible Reason: " + ex.getMessage().toString();
			return "redirect-list";
		}
		
		return SUCCESS;
	}// execute()
    /*-----------------------------------------------------------------*/
	/**
	 * <p>Creates the main Job Ticket table. </p>
	 * <p>Search Boxes are included in the execute() section</p>
	 * <p>Export Functionality is currently not working</p>
	 * <p>Filter Boxes at the bottom of the table is set, but usage is unknown.</p>
	 */
	/*-----------------------------------------------------------------*/
	public TableFacade createTable(){
	/*-----------------------------------------------------------------*/	
		//JobTicket is the name of the table as created via hibernate in JobTicket.class
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicket", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobTicketID","aircraft","jobTask","assignedTo","jobTicketStatus","jobTicketID");
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
		//get all non-open tickets for the current user
		List<JobTicket> tableTicketList = manager.getAllNonOpenTicketsByUser(this.user.getUsername());
		tableFacade.setItems(tableTicketList);
		tableFacade.setMaxRows(15);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("jobTicketID");
    	Limit limit = tableFacade.getLimit();
		
    	//Define Columns for the table
    	Column ticketID =  table.getRow().getColumn("jobTicketID");
		ticketID.setTitle("Job Ticket ID");
	    
		//Aircraft
		Column aircraft = table.getRow().getColumn("aircraft");
		aircraft.setTitle("Aircraft");
		if (!limit.isExported()) {
			aircraft.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if((""+((JobTicket) item).getAircraft().getRef()).indexOf("Class") >= 0)
						{
							return (""+((JobTicket) item).getAircraft().getRef());
						}
						return ((JobTicket) item).getAircraft().getRef();
					}
			});
		}
		
		//JobTask
		Column jobtask = table.getRow().getColumn("jobTask");
		jobtask.setTitle("Job Task");
		if (!limit.isExported()) {
			jobtask.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if((""+((JobTicket) item).getJobTask().getJobTaskValue()).indexOf("Class") >= 0){
							return (""+((JobTicket) item).getJobTask().getJobTaskValue());
						}
						return ((JobTicket) item).getJobTask().getJobTaskValue();
					}
			});
		}
		
		//AME
		Column assignedto = table.getRow().getColumn("assignedTo");
		assignedto.setTitle("AME");
		if (!limit.isExported()) {
			assignedto.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if((""+((JobTicket) item).getAssignedTo().getCode()).indexOf("Class") >= 0){
							return (""+((JobTicket) item).getAssignedTo().getCode());
						}
						return ((JobTicket) item).getAssignedTo().getCode();
					}
			});
		}
		
		//Current Status
		Column jobTicketStatus =  table.getRow().getColumn("jobTicketStatus");
		jobTicketStatus.setTitle("Current Status");
		if (!limit.isExported()) {
			jobTicketStatus.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if((""+((JobTicket) item).getJobTicketStatus().getJobStatusValue()).indexOf("Class") >= 0){
							return (""+((JobTicket) item).getJobTicketStatus().getJobStatusValue());
						}
						return ((JobTicket) item).getJobTicketStatus().getJobStatusValue();
					}
			});
		}
		
	    //The View History Button
		Column refCol = table.getRow().getColumn("jobTicketID");
		refCol.setTitle("History");
		if (!limit.isExported()) {
		refCol.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowCount) {
				
				Object id = new BasicCellEditor().getValue(item, "jobTicketID", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				
				if(value == null){value="(blank)";}
				if("".equals(value)){value="(blank)";}
								
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("hangerHistory!viewHistoryOfUser.action?id="+id).quote().close();
				html.append("View History");
				html.aEnd();
				return html.toString();
			}
			
		});
		}
	
		tableFacade.setView(new PlainTableView());
		histTableHtml = tableFacade.render();

		return tableFacade;
		
	}//createTable()
	/*-----------------------------------------------------------------*/
	/**
	 * <p>Tab that will allow the user to view the History of a selected ticket</p> 
	 */
	/*-----------------------------------------------------------------*/
	public String viewHistoryOfUser()
	/*-----------------------------------------------------------------*/	
	{
		//Prepare the tabs and set the breadcrumbs
        prepareTabs();
        this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='hanger.action'>Hanger Management</a>"),
				new Breadcrumb("<a href='hangerHistory.action'>History</a>"),
				new Breadcrumb("<a href='hangerHistory!viewHistoryOfUser.action'>View</a>")
				};
		
        //Set the current History tab to active
        Tab view = (Tab)this.tableTabs[0];
        view.setCurrent(false);
        Tab history = (Tab)this.tableTabs[1];
        history.setCurrent(true);
        tableTabs = new Tab[] {view,history};
		
		//get all the ticket info for the current user
		this.jobTicketsForUser = manager.getAllTicketsByUser(this.user.getUsername());		
		this.currentUser = manager.getCrewMemberByCode(this.user.getUsername());
        
		//get the selected ticket	
		prepare();
		
		//if a ticket was selected, create the table to display info
		if (this.jobTicket != null)
		{
        TableFacade tableFacade = createHistoryTable();
		
		Limit limit = tableFacade.getLimit();
		if (limit.isExported()) {
		    tableFacade.render();
		    return null;
		} 
     	tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
		}
		return SUCCESS;
	}//viewHistoryOfUser()
    /*-----------------------------------------------------------------*/
	/**
	 * <p>Creates the History Table under the History Tab for the selected Ticket
	 * under the Tickets Tab.</p> 
	 */
	/*-----------------------------------------------------------------*/
	public TableFacade createHistoryTable()
    /*-----------------------------------------------------------------*/
	{
		//JobTicketHistory is the table created via hibernate in JobHistory.class 	    
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicketHistory", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobhistoryID","jobAircraft","jobTaskValue","assignedTo","jobStatus","jobTimeStamp");		
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
					
		tableFacade.setItems(this.jobTicket.getJobTicketHistory());
		tableFacade.setMaxRows(15);
		Limit limit = tableFacade.getLimit();
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("jobhistoryID");
		
		//Columns
		
		//Job History ID
		Column id = table.getRow().getColumn("jobhistoryID");
		id.setTitle("Job History ID");
		
		//Aircraft Name
		Column aircraft = table.getRow().getColumn("jobAircraft");
		aircraft.setTitle("Aircraft");
		
		//Task Name
		Column jobTaskValue = table.getRow().getColumn("jobTaskValue");
		jobTaskValue.setTitle("Job Task");
		
		//The AME Assigned-to name
		Column assignedTo = table.getRow().getColumn("assignedTo");
		assignedTo.setTitle("AME");
		
		//The status of the ticket
		Column jobStatus = table.getRow().getColumn("jobStatus");
		jobStatus.setTitle("jobStatus");
		
		//The modify timestamp
		Column jobTimeStamp = table.getRow().getColumn("jobTimeStamp");
		jobTimeStamp.setTitle("Timestamp");
			
		tableFacade.setView(new PlainTableView());
		histTableHtml = tableFacade.render();
		
		
		return tableFacade;
		}//createHistoryTable()
    /*-----------------------------------------------------------------*/
	/*-----------------------------------------------------------------*/
	
	public String edit(){
		
	    return "redirect-list";
	}

	public String save(){
		
		return "redirect";
	}
	
	
	private void prepareTabs() {

		Tab jobs = new Tab("Tickets", "hangerHistory.action", tab.equals("active"));
		Tab history = new Tab("History", "hangerHistory!viewHistoryOfUser.action", tab.equals("history"));
		
		//if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {jobs,history};
			
	}
	
	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}
	
	public void prepare(){
		if (id == null) {
			this.jobTicket = new JobTicket();
		}
		else {
			this.jobTicket = manager.getJobTicketByID(String.valueOf(id));
		}
	}


	
	
}
