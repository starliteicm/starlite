package com.itao.starlite.ui.actions;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
import com.itao.starlite.model.JobHistory;
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
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="hangerHistory.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
public class HangerHistoryAction extends ActionSupport implements UserAware, Preparable {
	
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	public String tab = "tickets";
	
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
	
	public String current="tickets";
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
		this.tab = "tickets";
		this.current = "tickets";
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='hanger.action'>Hanger Management</a>"),
				new Breadcrumb("<a href='hangerHistory.action?id="+id+"'>Ticket</a>")};
		
		this.errorMessage="";
		this.notificationMessage="";
		
		//set the first tab ("Tickets") as active
		prepareTabs();
		prepare();
		
		
		//get all the ticket info for the current user
		this.jobTicketsForUser = manager.getAllTicketsByUser(this.user.getUsername());		
		this.currentUser = manager.getCrewMemberByCode(this.user.getUsername());
		
		//create and display the table with all the user's tickets
		
		
		return SUCCESS;
	}// execute()
    /*-----------------------------------------------------------------*/
	
	/*-----------------------------------------------------------------*/
	/**
	 * <p>Tab that will allow the user to view the History of a selected ticket</p> 
	 */
	/*-----------------------------------------------------------------*/
	public String viewHistoryOfUser()
	/*-----------------------------------------------------------------*/	
	{
		this.tab = "history";
		this.current = "history";
		//Prepare the tabs and set the breadcrumbs
        prepareTabs();
        prepare();
        
        this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='hanger.action'>Hanger Management</a>"),
				new Breadcrumb("<a href='hangerHistory.action?id="+id+"'>Ticket</a>"),
				new Breadcrumb("<a href='hangerHistory!viewHistoryOfUser.action?id="+id+"'>History</a>")
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
	@SuppressWarnings("unchecked")
	public TableFacade createHistoryTable()
    /*-----------------------------------------------------------------*/
	{
		this.tab = "history";
		this.current = "history";
		
		//JobTicketHistory is the table created via hibernate in JobHistory.class 	    
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicketHistory", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobAircraft","jobTaskValue","assignedTo","jobStatus","jobTimeStamp");		
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
	    
		List<JobHistory> historyList = this.jobTicket.getJobTicketHistory();
		Collections.sort(historyList);
		
		tableFacade.setItems(this.jobTicket.getJobTicketHistory());
		
		tableFacade.setMaxRows(15);
		Limit limit = tableFacade.getLimit();
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("jobhistoryID");
		
		//Columns
		
		//Job History ID
		//Column id = table.getRow().getColumn("jobhistoryID");
		//id.setTitle("Job History ID");
		
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
		//Column jobTimeStamp = table.getRow().getColumn("jobTimeStamp");
		//jobTimeStamp.setTitle("Timestamp");
			
		Column jobTimeStamp = table.getRow().getColumn("jobTimeStamp");
		jobTimeStamp.setTitle("Timestamp");
		if (!limit.isExported()) {
			jobTimeStamp.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobHistory) item).getJobTimeStamp() == null){
							return "";
						}
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
						Date date = ((JobHistory) item).getJobTimeStamp();
						//df.format(date);
						return df.format(date); 
					}
			});
		}
		else{			
			jobTimeStamp.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date date = ((JobHistory) item).getJobTimeStamp();
					//df.format(date);
					return df.format(date); 
				}
			});
		}
		
		
		
		
		tableFacade.setView(new SearchTableView());
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

		Tab jobs = new Tab("Ticket", "hangerHistory.action?id="+id, tab.equals("tickets"));
		Tab history = new Tab("History", "hangerHistory!viewHistoryOfUser.action?id="+id, tab.equals("history"));
		
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
			this.jobTicket = manager.getJobTicketByID(id);
		}
	}


	
	
}
