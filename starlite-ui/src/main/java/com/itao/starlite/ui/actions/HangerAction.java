package com.itao.starlite.ui.actions;


import groovy.swing.factory.CollectionFactory;

import java.util.ArrayList;
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
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.editor.DroplistFilterEditor;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.FlightActuals;
import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobHistory;
import com.itao.starlite.model.JobStatus;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.model.JobTicket;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SearchTableView;
import com.itao.starlite.ui.jmesa.SimpleTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Celeste Groenewald
 *
 */
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="hanger!admin.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="hanger.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
public class HangerAction extends ActionSupport implements UserAware, Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	public String tab = "newJob";
	public String current = "newJob";
	
	public Tab[] tableTabs;
	public String tableHtml;
	public String valTableHtml;
	public String histTableHtml;
	
	public String notificationMessage;
	public String errorMessage;
	
	public Integer id;
	
	public List<Aircraft> aircrafts;
	public List<JobTask> jobTasks;
	public List<JobTicket> jobTicketsForUser;
	List<JobStatus> jobStatusList;
	
	public JobTicket jobTicket;
	public Aircraft aircraft;
	public JobTask jobTask;
	public String taskToPerform = "";
	public JobStatus jobStatus;
	
	public Integer newStatus=null;
	
	
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Hanger Management")};
	

	@Inject
	private StarliteCoreManager manager;
	
	//public Map jobTaskMatrix = new HashMap();
	public List<JobTicket> jobTicketMatrix = new ArrayList();
	private CrewMember currentUser = new CrewMember();
	private String errorMsg="";
	
	public String selectedValue = "none";
	public String newTask="none";
	
	public boolean isNotACrewMember = false;
	
	
		
	/*-----------------------------------------------------------------*/
	/**
	 * <p>The default function called when none is specified. Within Hanger Management it
	 * it will:</p>
	 * <OL>
     *   <LI>Retrieve the aircrafts and tasks via respective DAO's</LI>
     *   <LI>Retrieve the current JobTicket via DAO for current user (only WIP and SUSPENDED)</LI>
     *   <LI>Check if all JobTickets have 3 Status Buttons via initTaskList()</LI>
     *   <LI>Prepare an html table using the tasks on X-axis, aircrafts as Y-axis and the 3 
     *   buttons per JobTask as data for each corresponding sell. </LI>
     *</OL>
	 */
	/*-----------------------------------------------------------------*/
	@Override
	public String execute() throws Exception
	/*-----------------------------------------------------------------*/
	{
		prepare();
		prepareTabs();

		//get all the information to work with
		this.jobTasks = manager.getAllTasks();
	    this.aircrafts = manager.getAllAircraftRegs();
		this.jobTicketsForUser = manager.getAllTicketsByUser(this.user.getUsername());
		this.currentUser = manager.getCrewMemberByCode(this.user.getUsername());
		
		if (this.currentUser == null)
		{
			this.errorMessage = "Please note: You are currently not a member of Crew and can therefore not use all of the functionality of this Hanger Management module. Please contact your local administrator, if you feel you need access to this module.";
			this.isNotACrewMember = true;
		}
		 
		//Initialize the display table for the tasks
		initArrays();
		
	
		return SUCCESS;
	}// execute()
    /*-----------------------------------------------------------------*/
	/*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	private String initArrays()
	/*-----------------------------------------------------------------*/	
	{
		// check if the user is a CrewMember - otherwise skip this action.
		if (this.isNotACrewMember == false)
		{	
		    if ((this.aircrafts.isEmpty() == false) && (this.jobTasks.isEmpty() == false))
		    {
		    	
		    }

		}
		return "redirect-list";
	}//initTableForTasks()
	/*-----------------------------------------------------------------*/
	/**
	 * <p>Allows a user to add a new Job Task such as "P4-Servicing". The task table is
	 * created via hibernate through jobTask.java</p>
	 */
	/*-----------------------------------------------------------------*/
	public String saveTask()
	/*-----------------------------------------------------------------*/
	{
		boolean pass = true;
	    this.errorMsg="";
	    this.notificationMessage="";
	    this.errorMessage="";
	    
	    //Test if the user entered a value before clicking on the add button in hanger.ftl
		if (this.newTask.compareTo("") == 0)
		{
			this.errorMsg = "Error: Unable to add an empty job type. Please enter a value before adding a new job type.";
			pass = false;
		}
		
		else
		{
			try
			{
				//if valid, create a new Task and commit to the database
				JobTask newTask = new JobTask();
				newTask.setJobTaskValue(this.newTask);
				manager.saveJobTask(newTask);
				
			}
			catch(Exception ex)
			{
				pass=false;
				this.errorMsg = "ERROR: Unable to add task. Possible Reason: "+ex.getMessage().toString();
				
			}
		}
		
		if (pass == false)
		{
			//display the error message if there was an Exception
			this.errorMessage = this.errorMsg;
		}
		else
		{
			this.notificationMessage = this.newTask + " has been added.";
		}
		return "redirect";
	}
	/*-----------------------------------------------------------------*/
	/**
	 * <p>Allows a user to add a new Job Task such as "P4-Servicing". The task table is
	 * created via hibernate through jobTask.java</p>
	 */
	/*-----------------------------------------------------------------*/
	public String saveTicket()
	/*-----------------------------------------------------------------*/
	{
		boolean pass = true;
	    this.errorMsg="";
	    this.notificationMessage="";
	    this.errorMessage="";
	    
	    //Test if the user entered a value before clicking on the add button in hanger.ftl
		if (this.taskToPerform.compareTo("") == 0)
		{
			this.errorMsg = "Error: Unable to create a new job ticket. Please enter a Task value before submitting.";
			pass = false;
		}
		
		else
		{
			try
			{
				//if valid, create a new Ticket and commit to the database
				JobTicket newJobTicket = new JobTicket();
				Aircraft tempAircraft = manager.getAircraftByReg(this.aircraft.getRef());
				JobTask tempJobTask = manager.getJobTaskByValue(this.jobTask.getJobTaskValue());
				CrewMember assignedTo = manager.getCrewMemberByCode(this.user.getUsername());
				
				newJobTicket.createJobTicket(tempJobTask, tempAircraft, assignedTo, assignedTo, manager, this.taskToPerform);
				this.jobTicket = manager.saveJobTicket(newJobTicket);
				
			}
			catch(Exception ex)
			{
				pass=false;
				this.errorMsg = "ERROR: Unable to save new Ticket. Possible Reason: "+ex.getMessage().toString();
				
			}
		}
		
		if (pass == false)
		{
			//display the error message if there was an Exception
			this.errorMessage = this.errorMsg;
		}
		else
		{
			this.notificationMessage = "Ticket ["+this.jobTicket.getJobTicketID()+"]" + " has been added with status, " + this.jobTicket.getJobTicketStatus().getJobStatusValue();
		}
		return "redirect-list";
	}
	/*-----------------------------------------------------------------*/
	/**
	 * <p>Allows a user to add a new Task to the matrix. The task table is
	 * created via hibernate through jobTask.java</p>
	 */
	/*-----------------------------------------------------------------*/
	public String updateStatus()
	/*-----------------------------------------------------------------*/
	{
		//set the bread crumbs
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("Hanger Management"),
				new Breadcrumb("New Job")};
		
		prepare();
		
		boolean pass = false;
	    this.errorMsg="";
	    this.notificationMessage="";
	    this.errorMessage="";
	    JobTicket tempTicket=null;
	    
	    tempTicket = manager.getJobTicketByID(id);
	   

	    try
		{
	    	if (tempTicket != null)
	 	    {
	    	//Make sure that the Status Change Request was valid
	    	boolean valid = checkValidStatusChange(this.newStatus,tempTicket);
	    	pass= valid;
	    	if (valid)
	    		{
	    	    
	    		//determine action for dates and history
	    		//update history and add to ticket
	    		tempTicket = setDatesForAction(this.newStatus,tempTicket);
	    			
	    		//save the JobTicket's new State and Contents
		    		this.manager.saveJobTicket(tempTicket);
	    		}// if valid
	 	    }
    	}//try
		catch(Exception e)
		{
			pass=false;
			errorMsg+=e.getMessage().toString();
		}
		finally
		{
			if (pass)
			{
				//Get details for success message
				String aircraft = tempTicket.getAircraft().getRef();
				String job = tempTicket.getJobTask().getJobTaskValue();
				String status = tempTicket.getJobTicketStatus().getJobStatusValue();
				Integer ticketID = tempTicket.getJobTicketID();
				
				notificationMessage = "Ticket no."+ticketID+": The status of "+aircraft+" : " + job + " has been changed to " + status +".";
				errorMessage = "";
			}
			else
			{
			    errorMessage = "Unable to update Ticket Status: <br /> Possible Reason:" +this.errorMsg;
			}
		}
		return "redirect-list";
	}//updateStatus()
	/*-----------------------------------------------------------------*/
	/**
	 * <p>Make sure that the user is trying to put the button in the correct state.
	 * i.e. Can't close a ticket that's never been worked on.</p>
	 */
	/*-----------------------------------------------------------------*/	
	private boolean checkValidStatusChange(Integer newStatus,JobTicket ticket)
	/*-----------------------------------------------------------------*/
	{
		boolean valid = true;
		
		String previousStatus = ticket.getJobTicketStatus().getJobStatusValue();
		JobStatus status = manager.getJobStatusByID(newStatus);
		String selectedStatus = status.getJobStatusValue();
		
		if (previousStatus.compareTo(selectedStatus) == 0)
		{
			//Same status, so don't do anything
			errorMsg = "Status is unchanged. Please select a different status for the current job.";
			valid=false;
		}
		else if ((previousStatus.compareTo("OPEN") == 0) && (selectedStatus.compareTo("CLOSED")==0))
		{
			//if the user wants to close a ticket that's not been worked on.
			errorMsg = "Status is unchanged. Can't close a ticket that has not been worked on. Please select the green 'WORK' button first.";
			valid=false;
			
		}else if ((previousStatus.compareTo("OPEN") == 0) && (selectedStatus.compareTo("SUSPENDED")==0))
		{
			//if the user wants to suspend a ticket that's not been worked on.
			errorMsg = "Status is unchanged. Can't suspend a ticket that has not been worked on. Please select the green 'WORK' button first.";
			valid=false;
		}
		else if ((selectedStatus.compareTo("WIP")==0))
		{
			//if the user wants to put a ticket in WIP, make sure that no other tickets are currently WIP.
			boolean hasWIPTickets = manager.userHasWIPTickets(this.user.getUsername());
			if (hasWIPTickets)
			{
			errorMsg = "Status is unchanged. Can't work on a ticket if another ticket is already open. Please suspend all WIP tickets first.";
			valid=false;
			}
			else
			{
				valid=true;
			}
			
		}
		
		return(valid);
	}//checkValidStatusChange()
	/*-----------------------------------------------------------------*/
	/**
	 * <p>If the status change request was valid, then the History table needs to 
	 * be updated with the status change. The Timestamp needs to be updated on the 
	 * History table as well as the Ticket table.</p>
	 */
	/*-----------------------------------------------------------------*/
	private JobTicket setDatesForAction(Integer newStatus,JobTicket ticket)
	/*-----------------------------------------------------------------*/
	{
		//a valid status change
		//get ticket current values
		String previousStatus = ticket.getJobTicketStatus().getJobStatusValue();
		JobStatus status = manager.getJobStatusByID(newStatus);
		String selectedStatus = status.getJobStatusValue();
		
		JobHistory history = new JobHistory();
		history.setJobAircraft(ticket.getAircraft().getRef());
		history.setJobTaskValue(ticket.getJobTask().getJobTaskValue());
		history.setAssignedTo(ticket.getAssignedTo().getCode());

		//changed from OPEN to WIP
		if ((previousStatus.compareTo("OPEN") == 0) && (selectedStatus.compareTo("WIP")==0))
		{
			ticket.setStartTime(new Date());
			ticket.setJobTicketStatus(status);
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(selectedStatus);
		}
		else if ((previousStatus.compareTo("WIP") == 0) && (selectedStatus.compareTo("SUSPENDED")==0))
		{
			//changed from WIP to SUSPENDED
			ticket.setEndTime(new Date());
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
		}
		else if ((previousStatus.compareTo("WIP") == 0) && (selectedStatus.compareTo("CLOSED")==0))
		{
			//changed from WIP to CLOSED
			ticket.setEndTime(new Date());
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
		}
		else if ((previousStatus.compareTo("SUSPENDED") == 0) && (selectedStatus.compareTo("WIP")==0))
		{
			//changed from SUSPENDED to WIP
			ticket.setStartTime(new Date());
			ticket.setEndTime(null);
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
			
		}
		else if ((previousStatus.compareTo("SUSPENDED") == 0) && (selectedStatus.compareTo("CLOSED")==0))
		{
			//changed from SUSPENDED to CLOSED
			ticket.setStartTime(new Date());
			ticket.setEndTime(new Date());
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
		}
		//add History to ticket
		ticket.addJobHistory(history);
		

		return(ticket);
	}//setDatesForAction()
	/*-----------------------------------------------------------------*/
	public String newJob()
	/*-----------------------------------------------------------------*/
	{
		current="newJob";
		tab="newJob";
		prepareTabs();
		
		
		
		
	return "newJob";
	}
    /*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public String viewWIPTickets()
	/*-----------------------------------------------------------------*/
	{
		current="WIPTickets";
		tab="WIPTickets";
		prepareTabs();
		
		List<JobTicket> tickets = manager.getAllWIPTicketsByUser(this.user.getUsername());
		this.jobStatusList = manager.getAllJobStatusValues();
				
		Collections.sort(tickets);
		//Collections.reverse(tickets);
		
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicketWIP", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobTicketID", "aircraft", "jobTask", "jobSubTask","jobStatus","jobStatusChange","View" );		
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
		tableFacade.setItems(tickets);
		tableFacade.setMaxRows(15);
		
		

		
		Limit limit = tableFacade.getLimit();
		
		
		Table table = tableFacade.getTable();
		table.setCaption("WIP Tickets");
		table.getRow().setUniqueProperty("jobTicketID");
			
		Column jobTicketID = table.getRow().getColumn("jobTicketID");
		jobTicketID.setTitle("ID");
		
		Column aircraft = table.getRow().getColumn("aircraft");
		aircraft.setTitle("Aircraft");
		if (!limit.isExported()) {
			aircraft.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getAircraft().getRef() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getAircraft().getRef()+"</div>";
					}
			});
		}
		else{			
			aircraft.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getAircraft().getRef() ;
				}
			});
		}
		
		Column jobTask = table.getRow().getColumn("jobTask");
		jobTask.setTitle("Job");
		if (!limit.isExported()) {
			jobTask.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTask().getJobTaskValue() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobTask().getJobTaskValue()+"</div>";
					}
			});
		}
		else{			
			jobTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getJobTask().getJobTaskValue() ;
				}
			});
		}
		
		 
		Column jobStatus = table.getRow().getColumn("jobStatus");
		jobStatus.setTitle("Status");
		if (!limit.isExported()) {
			jobStatus.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobTicketStatus().getJobStatusValue()+"</div>";
					}
			});
		}
		else{			
			jobStatus.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getJobTicketStatus().getJobStatusValue() ;
				}
			});
		}
		
		Column jobStatusPic = table.getRow().getColumn("jobStatusChange");
		jobStatusPic.setTitle("Quick Update");
		if (!limit.isExported()) {
			jobStatusPic.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						id = ((JobTicket) item).getJobTicketID();
						HtmlBuilder html = new HtmlBuilder();
						html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=3").quote().close();
						html.append("Suspend");
						html.aEnd();
						html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=4").quote().close();
						html.append("CLOSE");
						html.aEnd();
						
					    return html.toString(); 
					}
			});
		}
		else{			
			jobStatusPic.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					id = ((JobTicket) item).getJobTicketID();
					HtmlBuilder html = new HtmlBuilder();
					html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=3").quote().close();
					html.append("Suspend");
					html.aEnd();
					html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=4").quote().close();
					html.append("CLOSE");
					html.aEnd();
					
				    return html.toString(); 
				}
			});
		}
		
		
		Column jobSubTask = table.getRow().getColumn("jobSubTask");
		jobSubTask.setTitle("Task");
		if (!limit.isExported()) {
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobSubTask() == null){
							return "";
						}
						if (((JobTicket) item).getJobSubTask().length() > 15)
						{
							return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().substring(0,15)+"</div>";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask()+"</div>";
					}
			});
		}
		else{			
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) 
				{
					if (((JobTicket) item).getJobSubTask().length() > 15)
					{
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().substring(0,15)+"</div>";
					}
					return ((JobTicket) item).getJobSubTask();
				}
			});
		}
		
		Column jobView = table.getRow().getColumn("View");
		jobView.setTitle("View Details");
		if (!limit.isExported()) {
			jobView.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						id = ((JobTicket) item).getJobTicketID();
						HtmlBuilder html = new HtmlBuilder();
						html.a().href().quote().append("hangerHistory.action?id="+id).quote().close();
						html.append("View");
						html.aEnd();
						
					    return html.toString(); 
					}
			});
		}
		else{			
			jobView.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					id = ((JobTicket) item).getJobTicketID();
					HtmlBuilder html = new HtmlBuilder();
					html.a().href().quote().append("hangerHistory.action?id="+id).quote().close();
					html.append("View");
					html.aEnd();
					
				    return html.toString(); 
				}
			});
		}
		
		
		
		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
				
		
	return "WIPTickets";
	}
    /*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public String viewSuspendedTickets()
	/*-----------------------------------------------------------------*/
	{
		current="suspendedTickets";
		tab="suspendedTickets";
		prepareTabs();
		
		List<JobTicket> tickets = manager.getAllSUSPENDEDTicketsByUser(this.user.getUsername());
		this.jobStatusList = manager.getAllJobStatusValues();
				
		Collections.sort(tickets);
		//Collections.reverse(tickets);
		
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicketSuspend", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobTicketID", "aircraft", "jobTask", "jobSubTask","jobStatus","jobStatusChange","View" );		
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
		tableFacade.setItems(tickets);
		tableFacade.setMaxRows(15);
		
		

		
		Limit limit = tableFacade.getLimit();
		
		
		Table table = tableFacade.getTable();
		table.setCaption("Suspended Tickets");
		table.getRow().setUniqueProperty("jobTicketID");
			
		Column jobTicketID = table.getRow().getColumn("jobTicketID");
		jobTicketID.setTitle("ID");
		
		Column aircraft = table.getRow().getColumn("aircraft");
		aircraft.setTitle("Aircraft");
		if (!limit.isExported()) {
			aircraft.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getAircraft().getRef() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getAircraft().getRef()+"</div>";
					}
			});
		}
		else{			
			aircraft.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getAircraft().getRef() ;
				}
			});
		}
		
		Column jobTask = table.getRow().getColumn("jobTask");
		jobTask.setTitle("Job");
		if (!limit.isExported()) {
			jobTask.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTask().getJobTaskValue() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobTask().getJobTaskValue()+"</div>";
					}
			});
		}
		else{			
			jobTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getJobTask().getJobTaskValue() ;
				}
			});
		}
		
		 
		Column jobStatus = table.getRow().getColumn("jobStatus");
		jobStatus.setTitle("Status");
		if (!limit.isExported()) {
			jobStatus.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobTicketStatus().getJobStatusValue()+"</div>";
					}
			});
		}
		else{			
			jobStatus.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getJobTicketStatus().getJobStatusValue() ;
				}
			});
		}
		
		Column jobStatusPic = table.getRow().getColumn("jobStatusChange");
		jobStatusPic.setTitle("Quick Update");
		if (!limit.isExported()) {
			jobStatusPic.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						id = ((JobTicket) item).getJobTicketID();
						HtmlBuilder html = new HtmlBuilder();
						html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=2").quote().close();
						html.append("WIP");
						html.aEnd();
						html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=4").quote().close();
						html.append("CLOSE");
						html.aEnd();
						
					    return html.toString(); 
					}
			});
		}
		else{			
			jobStatusPic.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					id = ((JobTicket) item).getJobTicketID();
					HtmlBuilder html = new HtmlBuilder();
					html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=2").quote().close();
					html.append("WIP");
					html.aEnd();
					html.a().href().quote().append("hanger!updateStatus.action?id="+id+"&newStatus=4").quote().close();
					html.append("CLOSE");
					html.aEnd();
					
				    return html.toString(); 
				}
			});
		}
		
		
		Column jobSubTask = table.getRow().getColumn("jobSubTask");
		jobSubTask.setTitle("Task");
		if (!limit.isExported()) {
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobSubTask() == null){
							return "";
						}
						if (((JobTicket) item).getJobSubTask().length() > 15)
						{
							return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().substring(0,15)+"</div>";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask()+"</div>";
					}
			});
		}
		else{			
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) 
				{
					if (((JobTicket) item).getJobSubTask().length() > 15)
					{
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().substring(0,15)+"</div>";
					}
					return ((JobTicket) item).getJobSubTask();
				}
			});
		}
		
		
		Column jobView = table.getRow().getColumn("View");
		jobView.setTitle("View Details");
		if (!limit.isExported()) {
			jobView.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						id = ((JobTicket) item).getJobTicketID();
						HtmlBuilder html = new HtmlBuilder();
						html.a().href().quote().append("hangerHistory.action?id="+id).quote().close();
						html.append("View");
						html.aEnd();
						
					    return html.toString(); 
					}
			});
		}
		else{			
			jobView.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					id = ((JobTicket) item).getJobTicketID();
					HtmlBuilder html = new HtmlBuilder();
					html.a().href().quote().append("hangerHistory.action?id="+id).quote().close();
					html.append("View");
					html.aEnd();
					
				    return html.toString(); 
				}
			});
		}
		
		
		
		
		
		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
				
		//this.jobTicket.getJobTicketStatus().get;
	return "suspendedTickets";
	}
    /*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public String viewClosedTickets()
	/*-----------------------------------------------------------------*/
	{
		current="closedTickets";
		tab="closedTickets";
		prepareTabs();
		
		List<JobTicket> tickets = manager.getAllCLOSEDTicketsByUser(this.user.getUsername());
		this.jobStatusList = manager.getAllJobStatusValues();
				
		Collections.sort(tickets);
		//Collections.reverse(tickets);
		
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicketClosed", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobTicketID", "aircraft", "jobTask", "jobSubTask","jobStatus","View" );		
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
		tableFacade.setItems(tickets);
		tableFacade.setMaxRows(15);
		
		

		
		Limit limit = tableFacade.getLimit();
		
		
		Table table = tableFacade.getTable();
		table.setCaption("Closed Tickets");
		table.getRow().setUniqueProperty("jobTicketID");
			
		Column jobTicketID = table.getRow().getColumn("jobTicketID");
		jobTicketID.setTitle("ID");
		
		Column aircraft = table.getRow().getColumn("aircraft");
		aircraft.setTitle("Aircraft");
		if (!limit.isExported()) {
			aircraft.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getAircraft().getRef() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getAircraft().getRef()+"</div>";
					}
			});
		}
		else{			
			aircraft.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getAircraft().getRef() ;
				}
			});
		}
		
		Column jobTask = table.getRow().getColumn("jobTask");
		jobTask.setTitle("Job");
		if (!limit.isExported()) {
			jobTask.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTask().getJobTaskValue() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobTask().getJobTaskValue()+"</div>";
					}
			});
		}
		else{			
			jobTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getJobTask().getJobTaskValue() ;
				}
			});
		}
		
		 
		Column jobStatus = table.getRow().getColumn("jobStatus");
		jobStatus.setTitle("Status");
		if (!limit.isExported()) {
			jobStatus.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobTicketStatus().getJobStatusValue()+"</div>";
					}
			});
		}
		else{			
			jobStatus.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return ((JobTicket) item).getJobTicketStatus().getJobStatusValue() ;
				}
			});
		}
		
		
		
		Column jobSubTask = table.getRow().getColumn("jobSubTask");
		jobSubTask.setTitle("Task");
		if (!limit.isExported()) {
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobSubTask() == null){
							return "";
						}
						if (((JobTicket) item).getJobSubTask().length() > 15)
						{
							return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().substring(0,15)+"</div>";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask()+"</div>";
					}
			});
		}
		else{			
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) 
				{
					if (((JobTicket) item).getJobSubTask().length() > 15)
					{
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().substring(0,15)+"</div>";
					}
					return ((JobTicket) item).getJobSubTask();
				}
			});
		}
		
		
		Column jobView = table.getRow().getColumn("View");
		jobView.setTitle("View Details");
		if (!limit.isExported()) {
			jobView.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getJobTicketStatus().getJobStatusValue() == null){
							return "";
						}
						id = ((JobTicket) item).getJobTicketID();
						HtmlBuilder html = new HtmlBuilder();
						html.a().href().quote().append("hangerHistory.action?id="+id).quote().close();
						html.append("View");
						html.aEnd();
						
					    return html.toString(); 
					}
			});
		}
		else{			
			jobView.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					id = ((JobTicket) item).getJobTicketID();
					HtmlBuilder html = new HtmlBuilder();
					html.a().href().quote().append("hangerHistory.action?id="+id).quote().close();
					html.append("View");
					html.aEnd();
					
				    return html.toString(); 
				}
			});
		}
		
		
		
		
		
		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
				
		//this.jobTicket.getJobTicketStatus().get;
	return "closedTickets";
	}
    /*-----------------------------------------------------------------*/
	public String admin()
	/*-----------------------------------------------------------------*/
	{
		current="admin";
		tab="admin";
		prepareTabs();
		
	return "admin";
	}
    /*-----------------------------------------------------------------*/
	
	

	
	public String edit(){
		
	    return "redirect-list";
	}

	public String save(){
		
		return "redirect";
	}
	
	
	private void prepareTabs() {

		Tab newJobTab = new Tab("New Job", "hanger.action", tab.equals("newJob"));
		Tab currentWIPTab = new Tab("WIP Jobs", "hanger!viewWIPTickets.action", tab.equals("WIPTickets"));
		Tab currentSuspendedTab = new Tab("SUSPENDED Jobs", "hanger!viewSuspendedTickets.action", tab.equals("suspendedTickets"));
		Tab currentClosedTab = new Tab("CLOSED Jobs", "hanger!viewClosedTickets.action", tab.equals("closedTickets"));
		Tab adminTab = new Tab("Administration", "hanger!admin.action", tab.equals("admin"));
	
		tableTabs = new Tab[] {newJobTab, currentWIPTab, currentSuspendedTab,currentClosedTab, adminTab };

		
	}
	
	
		
	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}
	
	public void prepare()
	{
		if (aircraft == null)
		{
			this.aircraft = new Aircraft();
			this.aircraft.setRef("");
		}
		if (jobTask == null)
		{
			this.jobTask = new JobTask();
			this.jobTask.setJobTaskValue("");
		}
		
		if (this.aircrafts == null)
		{this.aircrafts = new ArrayList<Aircraft>();}
		if (this.jobTasks == null)
		{this.jobTasks = new ArrayList<JobTask>();}
		
		
		if (id == null) 
		{
			this.jobTicket = new JobTicket();
		}
		else {
			this.jobTicket = manager.getJobTicketByID(id);
		}
		
		if (this.newStatus == null)
		{
			this.newStatus = 3;
		}
	}


	
	
}
