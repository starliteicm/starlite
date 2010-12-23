package com.itao.starlite.ui.actions;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.JobHistory;
import com.itao.starlite.model.JobStatus;
import com.itao.starlite.model.JobStatusButton;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.model.JobTicket;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Celeste Groenewald
 *
 */
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="hanger!updateStatus.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="hanger.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
public class HangerAction extends ActionSupport implements UserAware, Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	public String tab = "active";
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
	
	public String current="Hanger";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Hanger Management")};
	

	@Inject
	private StarliteCoreManager manager;
	
	//public Map jobTaskMatrix = new HashMap();
	public List<JobTicket> jobTicketMatrix = new ArrayList();
	private CrewMember currentUser = new CrewMember();
	private String errorMsg="";
	
	public String selectedValue = "none";
	public String newTask="none";
	
	
		
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
		prepareTabs();
		
		//get all the information to work with
		this.jobTasks = manager.getAllTasks();
	    this.aircrafts = manager.getAllAircraftRegs();
		this.jobTicketsForUser = manager.getAllTicketsByUser(this.user.getUsername());
		this.currentUser = manager.getCrewMemberByCode(this.user.getUsername());
		
		//Make sure that the status values are present for the buttons.
		initTaskList();
		
		//Initialize the display table for the tasks
		initTableForTasks();
		
		return SUCCESS;
	}// execute()
    /*-----------------------------------------------------------------*/
	/**
	 * <p>Initializes the task list. If a task doesn't have 3 buttons (WIP,SUSPEND and CLOSED),
	 * then create buttons for it.</p>
	 */
	/*-----------------------------------------------------------------*/
	private void initTaskList()
	/*-----------------------------------------------------------------*/	
	{
		JobStatus wip = manager.getJobStatusValue("WIP");
		JobStatus suspended = manager.getJobStatusValue("SUSPENDED");	
		JobStatus closed = manager.getJobStatusValue("CLOSED");
		//if the ticket doesn't have 3 buttons, then give it 3 default buttons
		for (int i =0; i<this.jobTicketsForUser.size();i++)
		{
		     JobTicket temp = this.jobTicketsForUser.get(i);
		     List<JobStatusButton> buttons = temp.getJobStatusButtons();
		     
		     if (buttons.size() < 3)
		     {
		       
		     JobStatusButton button = new JobStatusButton();
		     button.setJobStatus(wip);
		     button.setJobStatusBtnState(0);
		     buttons.add(button);
		    
		     button = new JobStatusButton();
		     button.setJobStatus(suspended);
		     button.setJobStatusBtnState(0);
		     buttons.add(button);
		     
		     button = new JobStatusButton();
		     button.setJobStatus(closed);
		     button.setJobStatusBtnState(0);
		     buttons.add(button);
		     
		     //commit changes
		     this.manager.saveJobTicket(temp);
		     }
		}
	}//initTaskList()
    /*-----------------------------------------------------------------*/
	/**
	 * <p>Initializes the table for the Tasks. Plots the tickets to the
	 * corresponding task and aircraft</p>
	 */
	/*-----------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	private String initTableForTasks()
	/*-----------------------------------------------------------------*/	
	{
		int y = 0; //aircraft axis
		int x = 0; //task axis
		JobTicket z = new JobTicket();
		List tickets = new ArrayList();
		
		//Build the matrix
		//Go through the aircrafts
		for (int i =0; i<this.aircrafts.size(); i++)
		{
			y=i; //current aircraft position
			
			//get the aircraft name
			Aircraft temp_air = (Aircraft)this.aircrafts.get(i);
			String keyAircraft = temp_air.getRef();
			
			//Go through the tasks
			for (int r=0; r<this.jobTasks.size(); r++)
			{
				x=r; //current task position
				
				//get the task name
				JobTask temp_task = (JobTask)this.jobTasks.get(r);
				String keyTask = temp_task.getJobTaskValue();
				
				boolean found = false;
				int foundat = 0;
				z = new JobTicket();
				
				for (int q=0; q<this.jobTicketsForUser.size(); q++)
				{
					/* go through the user's jobTickets, if there is a match,
					 * then continue, else we need to create a ticket for the 
					 * current combination.
					*/
				    JobTicket tempTicket = this.jobTicketsForUser.get(q);
				    Aircraft tempAircraft = tempTicket.getAircraft();
				    String curAircraft = tempAircraft.getRef();
				    JobTask tempTask = tempTicket.getJobTask();
				    String curTask = tempTask.getJobTaskValue();
				    
				    //Does it exist?
				    if (curAircraft.compareTo(keyAircraft) == 0)
				    {
				    	if (curTask.compareTo(keyTask) == 0)
				    	{
				    		found = true;
				    		foundat = q;
				    		break;
				    	}
				    }
				}// for jobTicketsForUser loop
				
				if (found)
				{
					//use existing job
					JobTicket ft = this.jobTicketsForUser.get(foundat);
					ft.setPosX(x);
					ft.setPosY(y);
					z = ft;
					//this.manager.saveJobTicket(z);
    			}
				else
				{
					//create a default job for this
					JobTicket jobTicket = new JobTicket();
					List<JobStatusButton> tempButtonList = jobTicket.createButtons(manager);
					jobTicket.createJobTicket(temp_task,temp_air,this.currentUser,this.currentUser,tempButtonList,manager);
					
					z = jobTicket;
					z.setPosX(x);
					z.setPosY(y);
					
					try
					{
						
					//update Ticket History
					JobHistory history = new JobHistory();
					history.setJobAircraft(z.getAircraft().getRef());
					history.setJobTaskValue(z.getJobTask().getJobTaskValue());
					history.setAssignedTo(z.getAssignedTo().getCode());
					history.setJobStatus(z.getJobTicketStatus().getJobStatusValue());
					history.setJobTimeStamp(z.getCreateDate());
					
					z.addJobHistory(history);
						
					//commit to database
					this.manager.saveJobTicket(z);
					}
					catch (Exception e)
					{
						this.errorMessage = "Error: Possible Reason: " + this.errorMsg;
						return "redirect-list";
					}
					
				}
				//Add to the ticket array
				tickets.add(z);				
			}// for jobTasks loop
		}// for aircrafts loop
	
		//make the matrix available to hanger.ftl
		this.jobTicketMatrix = tickets;

		return "redirect-list";
	}//initTableForTasks()
	/*-----------------------------------------------------------------*/
	/**
	 * <p>Allows a user to add a new Task to the matrix. The task table is
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
			this.errorMsg = "Error: Unable to add an empty task. Please enter a value before adding a task.";
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
				new Breadcrumb("Job Update")};
		
		//Get the values from the button that the user clicked on
		String ticketID = this.selectedValue.substring(0, this.selectedValue.indexOf(","));
		Integer buttonID = Integer.valueOf(this.selectedValue.substring(this.selectedValue.indexOf(",")+1,this.selectedValue.length()));
		
		boolean pass = false;
	    this.errorMsg="";
	    this.notificationMessage="";
	    this.errorMessage="";
	    JobTicket temp=null;

	    try
		{
	    // get the selected ticket	
	    temp = this.manager.getJobTicketByID(ticketID);
	    
	    // get the buttons for the selected ticket
	    for (int i=0; i< temp.getJobStatusButtons().size(); i++)
	    {
        	JobStatusButton button = temp.getJobStatusButtons().get(i);

        	//find the button that the user clicked on
	    	if (button.getJobStatusBtnID() == buttonID)
	    	{
	    		//Make sure that the Status Change Request was valid
	    		boolean valid = checkValidStatusChange(temp,button);
	    		pass= valid;
	    		if (valid)
	    		{
	    			//turn state of all 3 buttons OFF
	    			for (int q=0;q<temp.getJobStatusButtons().size();q++)
	    			{
	    				JobStatusButton btn = temp.getJobStatusButtons().get(q);
	    				btn.setJobStatusBtnState(0);
	    		    }
	    			
	    		    //determine action for dates and history
	    			//update history and add to ticket
	    			temp = setDatesForAction(temp,button);
	    			
	    			//turn the selected button ON
	    			button.setJobStatusBtnState(1);
		    		temp.setJobTicketStatus(button.getJobStatus());
		    		
		    		//save the JobTicket's new State and Contents
		    		this.manager.saveJobTicket(temp);
	    		}// if valid
	    	}//if buttons
	    }//for buttons
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
				String aircraft = temp.getAircraft().getRef();
				String job = temp.getJobTask().getJobTaskValue();
				String status = temp.getJobTicketStatus().getJobStatusValue();
				
				notificationMessage = "Ticket no."+ticketID+": The status of "+aircraft+" : " + job + " has been changed to " + status +".";
				errorMessage = "";
			}
			else
			{
			    errorMessage = "Unable to update Ticket Status: <br /><br /> Possible Reason: +" +errorMsg;
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
	private boolean checkValidStatusChange(JobTicket ticket, JobStatusButton selectedBtn)
	/*-----------------------------------------------------------------*/
	{
		boolean valid = true;
		
		String previousStatus = ticket.getJobTicketStatus().getJobStatusValue();
		String selectedStatus = selectedBtn.getJobStatus().getJobStatusValue();
		
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
		return(valid);
	}//checkValidStatusChange()
	/*-----------------------------------------------------------------*/
	/**
	 * <p>If the status change request was valid, then the History table needs to 
	 * be updated with the status change. The Timestamp needs to be updated on the 
	 * History table as well as the Ticket table.</p>
	 */
	/*-----------------------------------------------------------------*/
	private JobTicket setDatesForAction(JobTicket ticket, JobStatusButton selectedBtn)
	/*-----------------------------------------------------------------*/
	{
		//a valid status change
		//get ticket current values
		String previousStatus = ticket.getJobTicketStatus().getJobStatusValue();
		String selectedStatus = selectedBtn.getJobStatus().getJobStatusValue();
		
		JobHistory history = new JobHistory();
		history.setJobAircraft(ticket.getAircraft().getRef());
		history.setJobTaskValue(ticket.getJobTask().getJobTaskValue());
		history.setAssignedTo(ticket.getAssignedTo().getCode());

		//changed from OPEN to WIP
		if ((previousStatus.compareTo("OPEN") == 0) && (selectedStatus.compareTo("WIP")==0))
		{
			ticket.setStartTime(new Date());
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(selectedStatus);
		}
		else if ((previousStatus.compareTo("WIP") == 0) && (selectedStatus.compareTo("SUSPENDED")==0))
		{
			//changed from WIP to SUSPENDED
			ticket.setEndTime(new Date());
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
		}
		else if ((previousStatus.compareTo("WIP") == 0) && (selectedStatus.compareTo("CLOSED")==0))
		{
			//changed from WIP to CLOSED
			ticket.setEndTime(new Date());
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
		}
		else if ((previousStatus.compareTo("SUSPENDED") == 0) && (selectedStatus.compareTo("WIP")==0))
		{
			//changed from SUSPENDED to WIP
			ticket.setStartTime(new Date());
			ticket.setEndTime(null);
			history.setJobTimeStamp(ticket.getStartTime());
			history.setJobStatus(selectedStatus);
			
		}
		else if ((previousStatus.compareTo("SUSPENDED") == 0) && (selectedStatus.compareTo("CLOSED")==0))
		{
			//changed from SUSPENDED to CLOSED
			ticket.setStartTime(new Date());
			ticket.setEndTime(new Date());
			history.setJobTimeStamp(ticket.getEndTime());
			history.setJobStatus(selectedStatus);
		}
		//add History to ticket
		ticket.addJobHistory(history);		
		return(ticket);
	}//setDatesForAction()
	/*-----------------------------------------------------------------*/

	
	public String edit(){
		
	    return "redirect-list";
	}

	public String save(){
		
		return "redirect";
	}
	
	
	private void prepareTabs() {

		Tab jobs = new Tab("Active", "hanger.action", tab.equals("active"));
		jobs.setCurrent(true);
		//if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {jobs};
			
	}
	
		
	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}
	
	public void prepare(){
		if (id == null) {
			//this.myTest = new MyTest();
		}
		else {
			//component = manager.getComponent(id);
		}
	}


	
	
}
