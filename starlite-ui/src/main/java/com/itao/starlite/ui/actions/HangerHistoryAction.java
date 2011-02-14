package com.itao.starlite.ui.actions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
	@Result(name="redirect", type=ServletRedirectResult.class, value="hangerHistory.action?errorMessage=${errorMessage}&id=${id}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="hanger.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
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
	
	public String hours="not calculated";
	public String mins="not calculated";
	public String time = "not calculated";
	
	//EDIT HOURS
	public String newHours;
	public String newMins;
	public String reasonForUpdate;
	
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
		
		//this.errorMessage="";
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
        Tab editHistory = (Tab)this.tableTabs[2];
        tableTabs = new Tab[] {view,history,editHistory};
		
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
	@SuppressWarnings("unchecked")
	public String viewEditHistory()
	/*-----------------------------------------------------------------*/	
	{
		this.tab = "history";
		this.current = "history";
		//Prepare the tabs and set the breadcrumbs
        prepareTabs();
        prepare();
        
        this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='hanger.action'>Hanger Management</a>"),
				new Breadcrumb("<a href='hangerHistory.action?id="+id+"'>Ticket</a>")
				};
		
        //Set the current History tab to active
        Tab view = (Tab)this.tableTabs[0];
        view.setCurrent(false);
        Tab history = (Tab)this.tableTabs[1];
        history.setCurrent(false);
        Tab editHistory = (Tab)this.tableTabs[2];
        editHistory.setCurrent(true);
        tableTabs = new Tab[] {view,history,editHistory};
		
		//get all the ticket info for the current user
        List<JobHistory> historyList = this.manager.getAllEditHrsTicketsByParentID(this.id);
		Collections.sort(historyList);
		
		this.currentUser = manager.getCrewMemberByCode(this.user.getUsername());
        
				
		//if a ticket was selected, create the table to display info
		if (this.jobTicket != null)
		{
        TableFacade tableFacade = this.createEditHistoryTable();
		
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
		tableFacade.setColumnProperties("jobAircraft","jobTaskValue","assignedTo","jobStatus","jobTimeStamp","totalTaskHours");		
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
	    
		
		List<JobHistory> nonEditHistoryTickets = manager.getAllNonEditHistroryTicketsByParentID(id);
		//Collections.sort(nonEditHistoryTickets);
		
		
		tableFacade.setItems(nonEditHistoryTickets);
		
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
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date date = ((JobHistory) item).getJobTimeStamp();
						//df.format(date);
						return df.format(date); 
					}
			});
		}
		else{			
			jobTimeStamp.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date = ((JobHistory) item).getJobTimeStamp();
					//df.format(date);
					return df.format(date); 
				}
			});
		}
		
		Column taskHRS = table.getRow().getColumn("totalTaskHours");
		taskHRS.setTitle("Total Task Hours");
		if (!limit.isExported()) {
			taskHRS.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobHistory) item).getTotalTaskHours() == null){
							return "";
						}
						//Float hours = ((JobHistory) item).getTotalTaskHours();
						//DecimalFormat twoDForm = new DecimalFormat("#.##");
						//return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(hours))+"</div>";
						
						String hours="";
						String mins="";
						try{
							String time = String.valueOf(((JobHistory) item).getTotalTaskHours());
							String hrs = time.substring(0, time.indexOf("."));
							String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
							float minsFloat = Float.valueOf(minsDec) * 60;
							int mins1 = Math.round(minsFloat);
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
					//Float hours = ((JobHistory) item).getTotalTaskHours();
					//DecimalFormat twoDForm = new DecimalFormat("#.##");
					//return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(hours))+"</div>";
					String hours="";
					String mins="";
					try{
						String time = String.valueOf(((JobHistory) item).getTotalTaskHours());
						String hrs = time.substring(0, time.indexOf("."));
						String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
						float minsFloat = Float.valueOf(minsDec) * 60;
						int mins1 = Math.round(minsFloat);
						if (mins1 < 10){mins="0"+mins1+"mins";}
						else {mins = ""+mins1+"mins";}
						hours = ""+hrs+"hrs&nbsp;";
						
						}
						catch (Exception ex){return "";}
						
				    return "<div style='text-align:right'>" + hours+mins+"</div>";
				}
			});
		}
		
		
		
		
		tableFacade.setView(new SearchTableView());
		histTableHtml = tableFacade.render();
		
		
		return tableFacade;
		}//createHistoryTable()
    /*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public TableFacade createEditHistoryTable()
    /*-----------------------------------------------------------------*/
	{
		this.tab = "editHistory";
		this.current = "editHistory";
		
		//JobTicketHistory is the table created via hibernate in JobHistory.class 	    
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("JobTicketHistoryEdit", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("jobAircraft","jobTaskValue","assignedTo","jobStatus","changedBy","changedByDate","changedByReason","changedByOldValue","changedByNewValue");		
		//tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
	    
		List<JobHistory> historyList = this.manager.getAllEditHrsTicketsByParentID(this.id);
		//Collections.sort(historyList);
		
		
		tableFacade.setItems(historyList);
		
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
		jobStatus.setTitle("Job Status");
		
		//The changeBy of the ticket
		Column changedBy = table.getRow().getColumn("changedBy");
		jobStatus.setTitle("Changed By");
		
		//The changeBy of the ticket
		Column changedByReason = table.getRow().getColumn("changedByReason");
		jobStatus.setTitle("Reason for Change");
		
		//The modify timestamp
		//Column jobTimeStamp = table.getRow().getColumn("jobTimeStamp");
		//jobTimeStamp.setTitle("Timestamp");
			
		Column jobTimeStamp = table.getRow().getColumn("changedByDate");
		jobTimeStamp.setTitle("Change Date");
		if (!limit.isExported()) {
			jobTimeStamp.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobHistory) item).getChangedByDate() == null){
							return "";
						}
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date date = ((JobHistory) item).getChangedByDate();
						//df.format(date);
						return df.format(date); 
					}
			});
		}
		else{			
			jobTimeStamp.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date = ((JobHistory) item).getChangedByDate();
					//df.format(date);
					return df.format(date); 
				}
			});
		}
		
		Column taskHRS = table.getRow().getColumn("changedByOldValue");
		taskHRS.setTitle("Old Hours");
		if (!limit.isExported()) {
			taskHRS.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobHistory) item).getChangedByOldValue() == null){
							return "";
						}
						//Float hours = ((JobHistory) item).getTotalTaskHours();
						//DecimalFormat twoDForm = new DecimalFormat("#.##");
						//return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(hours))+"</div>";
						
						String hours="";
						String mins="";
						try{
							String time = String.valueOf(((JobHistory) item).getChangedByOldValue());
							String hrs = time.substring(0, time.indexOf("."));
							String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
							float minsFloat = Float.valueOf(minsDec) * 60;
							int mins1 = Math.round(minsFloat);
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
					//Float hours = ((JobHistory) item).getTotalTaskHours();
					//DecimalFormat twoDForm = new DecimalFormat("#.##");
					//return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(hours))+"</div>";
					String hours="";
					String mins="";
					try{
						String time = String.valueOf(((JobHistory) item).getChangedByOldValue());
						String hrs = time.substring(0, time.indexOf("."));
						String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
						float minsFloat = Float.valueOf(minsDec) * 60;
						int mins1 = Math.round(minsFloat);
						if (mins1 < 10){mins="0"+mins1+"mins";}
						else {mins = ""+mins1+"mins";}
						hours = ""+hrs+"hrs&nbsp;";
						
						}
						catch (Exception ex){return "";}
						
				    return "<div style='text-align:right'>" + hours+mins+"</div>";
				}
			});
		}
		
		Column taskHRSNew = table.getRow().getColumn("changedByNewValue");
		taskHRSNew.setTitle("New Hours");
		if (!limit.isExported()) {
			taskHRSNew.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if(((JobHistory) item).getChangedByOldValue() == null){
							return "";
						}
						//Float hours = ((JobHistory) item).getTotalTaskHours();
						//DecimalFormat twoDForm = new DecimalFormat("#.##");
						//return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(hours))+"</div>";
						
						String hours="";
						String mins="";
						try{
							String time = String.valueOf(((JobHistory) item).getChangedByNewValue());
							String hrs = time.substring(0, time.indexOf("."));
							String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
							float minsFloat = Float.valueOf(minsDec) * 60;
							int mins1 = Math.round(minsFloat);
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
			taskHRSNew.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					//Float hours = ((JobHistory) item).getTotalTaskHours();
					//DecimalFormat twoDForm = new DecimalFormat("#.##");
					//return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(hours))+"</div>";
					String hours="";
					String mins="";
					try{
						String time = String.valueOf(((JobHistory) item).getChangedByNewValue());
						String hrs = time.substring(0, time.indexOf("."));
						String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
						float minsFloat = Float.valueOf(minsDec) * 60;
						int mins1 = Math.round(minsFloat);
						if (mins1 < 10){mins="0"+mins1+"mins";}
						else {mins = ""+mins1+"mins";}
						hours = ""+hrs+"hrs&nbsp;";
						
						}
						catch (Exception ex){return "";}
						
				    return "<div style='text-align:right'>" + hours+mins+"</div>";
				}
			});
		}
		
		
		
		tableFacade.setView(new SearchTableView());
		histTableHtml = tableFacade.render();
		
		
		return tableFacade;
		}//editHistoryTable()
    /*-----------------------------------------------------------------*/
	public String updateTicket() throws Exception
	/*-----------------------------------------------------------------*/
	{
		prepare();
		
		boolean pass = true;
		Float hours = 0.0F;
		Float mins = 0.0F;
		Float totalTime = 0.0F;
		
		//Make sure that there is a reason
		if (this.reasonForUpdate.compareTo("") == 0)
		{
			this.errorMessage = "Please enter a reason for changing the hours for this ticket";
			pass = false;
		}
		if (this.newHours.compareTo("") == 0)
		{
			this.errorMessage = "Please enter the New Hours";
			pass = false;
		}
		if (this.newMins.compareTo("") == 0)
		{
			this.errorMessage = "Please enter the New Minutes";
			pass = false;
		}
		
		if (pass)
		{
			
			//Update ticket hours
			try
			{
			     hours = Float.valueOf(this.newHours);
			     mins = Float.valueOf(this.newMins);
			     
			     mins = mins/60;
			     
			     totalTime = hours + mins;
			}
			catch (Exception ex)
			{
				this.errorMessage = "Unable to update ticket hours: Possible Reason:" + ex.getMessage().toString();
			}
			
			try
			{
				//Save History
				JobHistory temp = new JobHistory();
				temp.setAssignedTo(this.jobTicket.getAssignedTo().getPersonal().getFullName());
				temp.setJobAircraft(this.jobTicket.getAircraft().getRef());
				temp.setChangedBy(this.user.getUsername());
				temp.setChangedByReason(this.reasonForUpdate);
				temp.setChangedByDate(new Date());
				temp.setChangedByNewValue(String.valueOf(totalTime));
				temp.setChangedByOldValue(String.valueOf(this.jobTicket.getTotalTicketHours()));
				temp.setJobTaskValue(this.jobTicket.getJobTask().getJobTaskValue());
				temp.setSubTaskValue(this.jobTicket.getJobSubTask());
				temp.setJobStatus(jobTicket.getJobTicketStatus().getJobStatusValue());
				temp.setParentTicketNo(this.jobTicket.getJobTicketID());
				temp.setCaptureEdit(1); // 1 means 'yes this is an edit ticket'
				
				this.jobTicket.addJobHistory(temp);
				
	     		//Save Ticket
				this.jobTicket.setTotalTicketHours(totalTime);
				this.jobTicket = this.manager.saveJobTicket(jobTicket);
			}
			catch (Exception e)
			{
				this.errorMessage = "Unable to update the ticket: Possible Reason:" + e.getMessage().toString();
			}
		}
		return execute();
	}
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
		Tab editHistory = new Tab("View EDIT History", "hangerHistory!viewEditHistory.action?id="+id, tab.equals("editHistory"));
		
		//if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {jobs,history,editHistory};
			
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
			String hours="";
			String mins="";
			try{
				String time = String.valueOf(this.jobTicket.getTotalTicketHours());
				String hrs = time.substring(0, time.indexOf("."));
				String minsDec = "0."+time.substring(time.indexOf(".")+1,time.length());
				float minsFloat = Float.valueOf(minsDec) * 60;
				int mins1 = Math.round(minsFloat);
				if (mins1 < 10){mins="0"+mins1+"mins";}
				else {mins = ""+mins1+"mins";}
				hours = ""+hrs+"hrs&nbsp;";
				
				}
				catch (Exception ex){this.time= "";}
				
		    this.time = hours+mins;
			
		}
	}


	
	
}
