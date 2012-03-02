package com.itao.starlite.ui.actions;


import groovy.swing.factory.CollectionFactory;

import java.text.DecimalFormat;
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
import com.itao.starlite.model.JobSubTask;
//import com.itao.starlite.model.FlightActuals;
//import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobHistory;
import com.itao.starlite.model.JobStatus;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.model.JobTicket;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SearchTableView;

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
	public String componentTable;
	
	public String notificationMessage;
	public String errorMessage;
	
	public Integer id;
	public List<JobTicket> tickets;
	
	public List<Aircraft> aircrafts;
	public List<JobTask> jobTasks;
	public List<JobSubTask> jobSubTasks;
	public List<JobTicket> jobTicketsForUser;
	List<JobStatus> jobStatusList;
	
	public JobTicket jobTicket;
	public Aircraft aircraft;
	public JobTask jobTask;
	public JobSubTask jobSubTask;
	
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
	public String newSubTaskCode="none";
	public String newSubTaskDesc="none";
	public String newSubTaskRef="none";
	
	public boolean isNotACrewMember = false;
	
	
		
	/*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
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
		//get all the information to work with
		this.jobSubTasks = manager.getAllSubTasks();
		Collections.sort(this.jobSubTasks);
		
		Collections.sort(this.jobTasks);
	    this.aircrafts = manager.getAllAircraftRegs();
	    Collections.sort(this.aircrafts);
		//this.jobTicketsForUser = manager.getAllTicketsByUser(this.user.getUsername());
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
	 * <p>Allows a user to add a new Job Sub Task such as "330-P4-PRE-001". The task table is
	 * created via hibernate through jobTask.java</p>
	 */
	/*-----------------------------------------------------------------*/
	public String saveSubTask()
	/*-----------------------------------------------------------------*/
	{
		boolean pass = true;
	    this.errorMsg="";
	    this.notificationMessage="";
	    this.errorMessage="";
	    
	    //Test if the user entered a value before clicking on the add button in hanger.ftl
		if (this.newTask.compareTo("") == 0)
		{
			this.errorMsg = "Error: Unable to add an empty task. Please enter a value before adding a new task.";
			pass = false;
		}
		else if (this.newSubTaskCode.compareTo("") == 0)
		{
			this.errorMsg = "Error: Unable to add task with an empty description. Please enter a value for the description.";
			pass = false;
		}
		
		else
		{
			try
			{
				//if valid, create a new Task and commit to the database
				JobSubTask newSubTask = new JobSubTask();
				newSubTask.setJobSubTaskCode(this.newSubTaskCode);
				newSubTask.setJobSubTaskDesc(this.newSubTaskDesc);
				newSubTask.setJobSubTaskRef(this.newSubTaskRef);
				
				manager.saveJobSubTask(newSubTask);
				
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
			this.notificationMessage = this.newSubTaskCode + " has been added.";
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
		if (this.newSubTaskCode.compareTo("") == 0)
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
				
				JobSubTask tempSubTask = this.manager.getJobSubTaskByValue(this.newSubTaskCode);
			
				
				newJobTicket.createJobTicket(tempJobTask, tempAircraft, assignedTo, assignedTo, manager, tempSubTask);
				//save ticket to get id of ticket and status
				this.jobTicket = manager.saveJobTicket(newJobTicket);
				//determine action for dates and history
	    		//update history and add to ticket
				this.jobTicket = setDatesForAction(0,this.jobTicket);
				//save new dates for ticket and history
				this.jobTicket = manager.saveJobTicket(this.jobTicket);
				
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
		JobStatus status = null;
		String selectedStatus = "";
		if (newStatus != 0)
		{
			//if not a new ticket, then get the status value
			status = manager.getJobStatusByID(newStatus);
			selectedStatus = status.getJobStatusValue();
		}
		else
		{
			//don't compare new ticket's status
			selectedStatus = "";
		}
		
		 
		
		JobHistory history = new JobHistory();
		if (id!=null)
		{
		history.setParentTicketNo(id);
		}
		else{history.setParentTicketNo(ticket.getJobTicketID());}
		
		history.setCaptureEdit(0);
		history.setJobAircraft(ticket.getAircraft().getRef());
		history.setJobTaskValue(ticket.getJobTask().getJobTaskValue());
		history.setJobSubTaskCode(ticket.getJobSubTask().getJobSubTaskCode());
		history.setJobSubTaskDesc(ticket.getJobSubTask().getJobSubTaskDesc());
		history.setAssignedTo(ticket.getAssignedTo().getCode());
		
		Double totalHours = ticket.getTotalTicketHours();

		//changed from OPEN to WIP
		if ((previousStatus.compareTo("OPEN") == 0) && (selectedStatus.compareTo("WIP")==0))
		{
			//OPEN is not used, so no hours calculated. Kept here for future use.
			ticket.setStartTime(new Date());
			ticket.setJobTicketStatus(status);
			ticket.setTotalTicketHours(0.0);
			history.setTotalTaskHours(ticket.getTotalTicketHours());
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(selectedStatus);
			
		}
		else if ((previousStatus.compareTo("WIP") == 0) && (selectedStatus.compareTo("SUSPENDED")==0))
		{
			//changed from WIP to SUSPENDED
			ticket.setEndTime(new Date());
			Date startTime = ticket.getStartTime();
			Date endTime = ticket.getEndTime();
			double startHours = (double)startTime.getTime();
			double endHours = (double)endTime.getTime();
			double difference = (double)endHours - startHours;
			double hours = (difference / (1000 * 60 * 60));
			totalHours += hours; 
			
			ticket.setTotalTicketHours(totalHours);
			history.setTotalTaskHours(hours);
			
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
		}
		else if ((previousStatus.compareTo("WIP") == 0) && (selectedStatus.compareTo("CLOSED")==0))
		{
			//changed from WIP to CLOSED
			ticket.setEndTime(new Date());
			
			Date startTime = ticket.getStartTime();
			Date endTime = ticket.getEndTime();
			double startHours = (double)startTime.getTime();
			double endHours = (double)endTime.getTime();
			double difference = (double)endHours - startHours;
			double hours = (difference / (1000 * 60 * 60));
			totalHours += hours; 
			
			ticket.setTotalTicketHours(totalHours);
			history.setTotalTaskHours(hours);
			
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
		}
		else if ((previousStatus.compareTo("SUSPENDED") == 0) && (selectedStatus.compareTo("WIP")==0))
		{
			//changed from SUSPENDED to WIP
			ticket.setStartTime(new Date());
			ticket.setEndTime(null);
			//no end time so set the hours to zero
			history.setTotalTaskHours(0.0);
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
			
		}
		else if ((previousStatus.compareTo("SUSPENDED") == 0) && (selectedStatus.compareTo("CLOSED")==0))
		{
			//changed from SUSPENDED to CLOSED
			ticket.setStartTime(new Date());
			ticket.setEndTime(new Date());
			//if it goes directly to closed, then there is no time to calculate
			history.setTotalTaskHours(0.0);
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
			ticket.setJobTicketStatus(status);
		}
		else if ((previousStatus.compareTo("WIP") == 0) && (selectedStatus.compareTo("")==0))
		{
			//Created a ticket that went straight into WIP
			ticket.setStartTime(new Date());
			//ticket.setEndTime(new Date());
			history.setTotalTaskHours(0.0);
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(previousStatus);
				
		}
		
			
	    if ((previousStatus.compareTo("SUSPENDED") == 0) && (selectedStatus.compareTo("")==0))
		{
			//Created a ticket that went straight into WIP
			ticket.setStartTime(new Date());
			ticket.setEndTime(ticket.getStartTime());
			history.setTotalTaskHours(0.0);
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(previousStatus);
				
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
						if (((JobTicket) item).getJobSubTask().getJobSubTaskCode().length() > 20)
						{
							return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode().substring(0,20)+"</div>";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode()+"</div>";
					}
			});
		}
		else{			
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) 
				{
					if (((JobTicket) item).getJobSubTask().getJobSubTaskCode().length() > 20)
					{
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode().substring(0,20)+"</div>";
					}
					return ((JobTicket) item).getJobSubTask().getJobSubTaskCode();
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
		
		
		
		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
				
		
	return "WIPTickets";
	}
    /*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public TableFacade viewSuspendedTickets()
	/*-----------------------------------------------------------------*/
	{
		current="suspendedTickets";
		tab="suspendedTickets";
		prepareTabs();
		
		//this.tickets = manager.getAllSUSPENDEDTicketsByUser(this.user.getUsername());
		//this.jobStatusList = manager.getAllJobStatusValues();
				
		Collections.sort(tickets);
		//Collections.reverse(tickets);
		
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("suspendedTickets", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobTicketID", "aircraft", "jobTask", "jobSubTask","jobStatus","jobStatusChange","totalTicketHours","View" );		
		tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
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
						if (((JobTicket) item).getJobSubTask().getJobSubTaskCode().length() > 20)
						{
							return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode().substring(0,20)+"</div>";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode()+"</div>";
					}
			});
		}
		else{			
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) 
				{
					if (((JobTicket) item).getJobSubTask().getJobSubTaskCode().length() > 20)
					{
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode().substring(0,20)+"</div>";
					}
					return ((JobTicket) item).getJobSubTask().getJobSubTaskCode();
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
		
		Column taskHRS = table.getRow().getColumn("totalTicketHours");
		taskHRS.setTitle("Total Ticket Hours");
		if (!limit.isExported()) {
			taskHRS.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getTotalTicketHours() == null){
							return "";
						}
						String hours="";
						String mins="";
						try{
							String time = String.valueOf(((JobTicket) item).getTotalTicketHours());
							String hrs = time.substring(0, time.indexOf("."));
							String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
							Double minsFloat = Double.valueOf(minsDec) * 60;
							long mins1 = Math.round(minsFloat);
							if (mins1 < 10){mins="0"+mins1+"mins";}
							else {mins = ""+mins1+"mins";}
							hours = ""+hrs+"hrs&nbsp;";
							
							}
							catch (Exception ex){return "";}
							
					    return "<div style='text-align:right'>" + hours+mins+"</div>";
					}
			});
		}
		else{			
			taskHRS.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					String hours="";
					String mins="";
					try{
						String time = String.valueOf(((JobTicket) item).getTotalTicketHours());
						String hrs = time.substring(0, time.indexOf("."));
						String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
						Double minsFloat = Double.valueOf(minsDec) * 60;
						long mins1 = Math.round(minsFloat);
						if (mins1 < 10){mins="0"+mins1+"mins";}
						else {mins = ""+mins1+"mins";}
						hours = ""+hrs+"hrs&nbsp;";
						
						}
						catch (Exception ex){return "";}
						
				    return "<div style='text-align:right'>" + hours+mins+"</div>";
				}
			});
		}
		
		
		
		
		//tableFacade.setView(new SearchTableView());
		//tableHtml = tableFacade.render();
				
		//this.jobTicket.getJobTicketStatus().get;
	//return "suspendedTickets";
		return tableFacade;
	}
    /*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public TableFacade viewClosedTickets()
	/*-----------------------------------------------------------------*/
	{
		current="closedTickets";
		tab="closedTickets";
		prepareTabs();
		
		//List<JobTicket> tickets = manager.getAllCLOSEDTicketsByUser(this.user.getUsername());
		//this.jobStatusList = manager.getAllJobStatusValues();
				
		Collections.sort(tickets);
		//Collections.reverse(tickets);
		
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicketClosed", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobTicketID", "aircraft", "jobTask", "jobSubTask","jobStatus","totalTicketHours","View" );		
		tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
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
						if (((JobTicket) item).getJobSubTask().getJobSubTaskCode().length() > 20)
						{
							return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode().substring(0,20)+"</div>";
						}
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode()+"</div>";
					}
			});
		}
		else{			
			jobSubTask.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) 
				{
					if (((JobTicket) item).getJobSubTask().getJobSubTaskCode().length() > 20)
					{
						return "<div style='text-align:left'>"+((JobTicket) item).getJobSubTask().getJobSubTaskCode().substring(0,20)+"</div>";
					}
					return ((JobTicket) item).getJobSubTask().getJobSubTaskCode();
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
		
		
		Column taskHRS = table.getRow().getColumn("totalTicketHours");
		taskHRS.setTitle("Total Ticket Hours");
		if (!limit.isExported()) {
			taskHRS.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobTicket) item).getTotalTicketHours() == null){
							return "";
						}
						String hours="";
						String mins="";
						try{
							String time = String.valueOf(((JobTicket) item).getTotalTicketHours());
							String hrs = time.substring(0, time.indexOf("."));
							String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
							Double minsFloat = Double.valueOf(minsDec) * 60;
							long mins1 = Math.round(minsFloat);
							if (mins1 < 10){mins="0"+mins1+"mins";}
							else {mins = ""+mins1+"mins";}
							hours = ""+hrs+"hrs&nbsp;";
							
							}
							catch (Exception ex){return "";}
							
					    return "<div style='text-align:right'>" + hours+mins+"</div>";
					}
			});
		}
		else{			
			taskHRS.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					String hours="";
					String mins="";
					try{
						String time = String.valueOf(((JobTicket) item).getTotalTicketHours());
						String hrs = time.substring(0, time.indexOf("."));
						String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
						Double minsFloat = Double.valueOf(minsDec) * 60;
						long mins1 = Math.round(minsFloat);
						if (mins1 < 10){mins="0"+mins1+"mins";}
						else {mins = ""+mins1+"mins";}
						hours = ""+hrs+"hrs&nbsp;";
						
						}
						catch (Exception ex){return "";}
						
				    return "<div style='text-align:right'>" + hours+mins+"</div>";
				}
			});
		}
		
		
		
		//tableFacade.setView(new SearchTableView());
		//tableHtml = tableFacade.render();
				
		//this.jobTicket.getJobTicketStatus().get;
	//return "closedTickets";
		return tableFacade;
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
	public String editSuspend(){
		prepare();
		tickets = manager.getAllSUSPENDEDTicketsByUser(this.user.getUsername());

			TableFacade tableFacade = viewSuspendedTickets();

			Limit limit = tableFacade.getLimit();
			if (limit.isExported()) {
				tableFacade.render();
				return null;
			} 
			tableFacade.setView(new SearchTableView());
			tableHtml = tableFacade.render();
			
		
			return "edit";
		
	}
	/*------------------------------------------------------------------*/
    public String editClosed(){
		prepare();
		tickets = manager.getAllCLOSEDTicketsByUser(this.user.getUsername());

			TableFacade tableFacade = this.viewClosedTickets();

			Limit limit = tableFacade.getLimit();
			if (limit.isExported()) {
				tableFacade.render();
				return null;
			} 
			tableFacade.setView(new SearchTableView());
			tableHtml = tableFacade.render();
			
		
			return "edit";
		
	}
	/*-----------------------------------------------------------------*/
    
	

	public String save(){
		
		return "redirect";
	}
	
	
	private void prepareTabs() 
	{

		Tab newJobTab = new Tab("New Job", "hanger.action", tab.equals("newJob"));
		Tab currentWIPTab = new Tab("WIP Jobs", "hanger!viewWIPTickets.action", tab.equals("WIPTickets"));
		Tab currentSuspendedTab = new Tab("SUSPENDED Jobs", "hanger!editSuspend.action", tab.equals("suspendedTickets"));
		Tab currentClosedTab = new Tab("CLOSED Jobs", "hanger!editClosed.action", tab.equals("closedTickets"));
		Tab adminTab = new Tab("Administration", "hanger!admin.action", tab.equals("admin"));
		//Tab editTab = new Tab("Edit Hours", "hanger!editHours.action", tab.equals("editHours"));
	
		if (user.hasPermission("hangerRead"))
		{
		tableTabs = new Tab[] {newJobTab, currentWIPTab, currentSuspendedTab,currentClosedTab};
		}
		if (user.hasPermission("hangerWrite"))
		{
			tableTabs = new Tab[] {newJobTab, currentWIPTab, currentSuspendedTab,currentClosedTab, adminTab};
		}
		
	}
	
	
		
	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}
	
	@SuppressWarnings("unchecked")
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
		if (jobSubTask == null)
		{
			this.jobSubTask = new JobSubTask();
			this.jobSubTask.setJobSubTaskCode("");
			this.jobSubTask.setJobSubTaskDesc("");
		}
		
		if (this.aircrafts == null)
		{this.aircrafts = new ArrayList<Aircraft>();}
		else {Collections.sort(this.aircrafts);}
		if (this.jobTasks == null)
		{this.jobTasks = new ArrayList<JobTask>();}
		else {this.jobTasks = manager.getAllTasks();Collections.sort(this.jobTasks);}
		if (this.jobSubTasks == null)
		{this.jobSubTasks = new ArrayList<JobSubTask>();}
		else {this.jobSubTasks = manager.getAllSubTasks();Collections.sort(this.jobSubTasks);}
		
		
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
