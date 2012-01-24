package com.itao.starlite.ui.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.hibernate.Session;
import org.hibernate.jmx.SessionFactoryStub;
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
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.FlightActualStatus;
import com.itao.starlite.model.FlightActuals;
import com.itao.starlite.model.FlightLog;
import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="flightActualsApproval!update.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="flightActualsApproval.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
@Permissions("ManagerView")
public class FlightActualsApprovalAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3344297237658297955L;
	public String tableHtml;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Flight Actuals Approval")};
	public String errorMessage = "";
	public String notificationMessage;
	
	public Tab[] tableTabs;
	public String tab = "flight plans";
	private Tab staticTab = new Tab("Static", "aircraft.action", false);
	public User user;

	public Integer typeId=0;
	public Integer statusId=0;
	public FlightPlan flightPlan = new FlightPlan();
	public FlightActuals selectedFlightActual = new FlightActuals();
	public FlightPlan flightVariance = new FlightPlan();
	public FlightLog flightLog = new FlightLog();
	
	public String customer = "";
	public String flightType ="";
	public String createDate="";
	
	public List<Aircraft> allAircrafts = new ArrayList<Aircraft>();
	public List<String> aircrafts = new ArrayList<String>();
	public ArrayList<String> pilots = new ArrayList<String>();
	public ArrayList<String> coPilots = new ArrayList<String>();
	public ArrayList<String> engineers = new ArrayList<String>();
	public ArrayList<AircraftType> aircraftTypesObj = new ArrayList<AircraftType>();
	public ArrayList<String> aircraftTypes = new ArrayList<String>();
	public ArrayList<JobTask> jobTasks = new ArrayList<JobTask>();
	public ArrayList<String> typeOfService = new ArrayList<String>();
	
	public int status;
	public int newVal;
	public int id = 0;
	
	@Inject
	private StarliteCoreManager manager;	
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public FlightActualsApprovalAction() {
		super();
	}
	
	private static Date parseDate(String str) throws ParseException {
		return df.parse(str);
	}
	

	
	/*---------------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	@Override
	/*---------------------------------------------------------------------*/
	public String execute() throws Exception
	/*---------------------------------------------------------------------*/
	{
		
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='aircraft.action'>Aircraft</a>"),
				new Breadcrumb("<a href='aircraft!flightActuals.action'>Flight Actuals</a>"),
				new Breadcrumb("Approval Steps")};
		
		prepareTabs();
		prepare();
		prepareData();
		
		if (this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusValue().compareToIgnoreCase("OPEN") == 0)
		{
			FlightActualStatus temp = new FlightActualStatus();
			//set to TO APPROVED status
			temp = this.manager.findStatusValueByID(status+1);
			
			if (temp != null )
			{
			this.selectedFlightActual.setNewActualStatusValueObject(temp);
			this.selectedFlightActual = this.manager.saveFlightActuals(selectedFlightActual);
			this.status=this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusID();
			this.id = this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusID();
			
			
			}
		}
		
		return SUCCESS;
	}
	/*---------------------------------------------------------------*/
	public String prepareData()
	/*---------------------------------------------------------------*/
	{
		this.flightPlan.getCustomer();
		
		this.aircraftTypesObj = (ArrayList<AircraftType>) manager.getAircraftTypes();
		for (int q=0; q< this.aircraftTypesObj.size(); q++)
		{
			AircraftType temp = (AircraftType)this.aircraftTypesObj.get(q);
		  this.aircraftTypes.add(temp.getName());
		}
		Collections.sort(this.aircraftTypes);
		
		this.jobTasks = (ArrayList<JobTask>) manager.getAllTasks();
		for (int w=0; w< this.jobTasks.size(); w++)
		{
			JobTask temp = (JobTask)this.jobTasks.get(w);
		    this.typeOfService.add(temp.getJobTaskValue());
		}
		Collections.sort(this.aircraftTypes);
		
		this.allAircrafts = this.manager.getAllAircraftRegs();
		for (int r=0; r< this.allAircrafts.size(); r++)
		{
			this.aircrafts.add(this.allAircrafts.get(r).getRef());
		}
		Collections.sort(this.aircrafts);
		 

		List<CrewMember> allCrew = this.manager.getAllCrew();
		Collections.sort(allCrew);
		
		for (int i =0; i< allCrew.size(); i++)
		{
			CrewMember temp = allCrew.get(i);
			if (temp.getRole() != null)
			{
				if (temp.getRole().getPosition() != null)
				{
					if (temp.getRole().getPosition().compareToIgnoreCase("pilot") == 0)
					{
						if (temp.getPersonal() != null)
						{
						 String name = temp.getPersonal().getFirstName() + " " + temp.getPersonal().getLastName();
						 this.pilots.add(name.trim());
						}
					}
					else if (temp.getRole().getPosition().toLowerCase().contains("co-pilot"))
					{
						if (temp.getPersonal() != null)
						{
						 String name = temp.getPersonal().getFirstName() + " " + temp.getPersonal().getLastName();
						 this.coPilots.add(name.trim());
						}
						
					}
					else if (temp.getRole().getPosition().toLowerCase().contains("engineer") || (temp.getRole().getPosition().compareToIgnoreCase("AME") == 0 ))
					{
						if (temp.getPersonal() != null)
						{
						 String name = temp.getPersonal().getFirstName() + " " + temp.getPersonal().getLastName();
						 if (this.engineers.contains(name.trim()) == false)
						 {
						 this.engineers.add(name.trim());
						 }
						}
						
					}
				}
			}
		}
		
		Collections.sort(this.pilots);
		Collections.sort(this.coPilots);
		Collections.sort(this.engineers);
		return ("done");
	}
	/*---------------------------------------------------------------------*/
	public String update() throws Exception
	/*---------------------------------------------------------------------*/
	{
		prepareTabs();
		prepare();
		prepareData();
		
		return "edit";
	   
	}
	/*---------------------------------------------------------------------*/
	/*---------------------------------------------------------------------*/
	public String updateStatus()
	/*---------------------------------------------------------------------*/
	{
		prepareTabs();
		prepare();
		
		
		FlightActualStatus temp = new FlightActualStatus();
		
		temp = this.manager.findStatusValueByID(id);
		
		if (temp != null && id != this.status && id !=0)
		{
		this.selectedFlightActual.setNewActualStatusValueObject(temp);
		this.selectedFlightActual = this.manager.saveFlightActuals(selectedFlightActual);
		this.status=this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusID();
		this.id = this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusID();
		
		}
		
		
		return "redirected-list";
	   
	}
	/*---------------------------------------------------------------------*/
	public String save()
	/*---------------------------------------------------------------------*/
	{
	    boolean pass = false;
	    this.errorMessage ="";
	    this.notificationMessage="";
		try
		{
			this.selectedFlightActual = manager.findFlightActualsByID(typeId);
			this.selectedFlightActual.setFlightPlan(flightPlan);
			this.selectedFlightActual = this.manager.saveFlightActuals(selectedFlightActual);
			this.status=this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusID();
			this.flightVariance = this.selectedFlightActual.getFlightVariance();
			this.notificationMessage = "The Flight Actuals has been updated.";
			pass = true;
		}
		catch(Exception ex)
		{
			pass=false;
			this.errorMessage = "Unable to save changes: Possible Reason: " +ex.getMessage().toString();
		}
			
			return "edit";
	}
	/*---------------------------------------------------------------------*/
	public String setNavPlan()
	{
		
		//set the bread crumbs
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("Flight Plan"),
				new Breadcrumb("Navigation Plan")};
		this.current = "Navigation Planning";
		
		return INPUT;
	}
	
	private void prepareTabs() {

		Tab aircraftGeneralTab = new Tab("General", "flightPlan.action", tab.equals("aircraft"));
		Tab aircraftNavPlanTab = new Tab("Navigation Planning", "#", tab.equals("aircraft type"));
		Tab aircraftFuelPlanTab = new Tab("Fuel Plan", "aircraft!flightActuals.action", tab.equals("flight actuals"));
		Tab aircraftPerformanceTab = new Tab("Performance", "aircraft!flightPlans.action", tab.equals("flight plans"));
		Tab aircraftLoadingTab = new Tab("Loading", "aircraft!flightMismatches.action", tab.equals("flight mismatches"));
		Tab aircraftWeatherTab = new Tab("Weather", "aircraft!flightMismatches.action", tab.equals("flight mismatches"));
		Tab aircraftInitRadioReportTab = new Tab("Radio Report", "aircraft!flightMismatches.action", tab.equals("flight mismatches"));
		Tab aircraftATCClearanceTab = new Tab("ATC Clearance", "aircraft!flightMismatches.action", tab.equals("flight mismatches"));
		Tab aircraftHOBBSTimesTab = new Tab("HOBBS Times", "aircraft!flightMismatches.action", tab.equals("flight mismatches"));
		
		if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {aircraftGeneralTab, aircraftNavPlanTab, aircraftFuelPlanTab,
				aircraftPerformanceTab, aircraftLoadingTab, aircraftWeatherTab, aircraftInitRadioReportTab, 
				aircraftATCClearanceTab, aircraftHOBBSTimesTab };
		
		this.current = "General";
		
	}

	@Override

	public void setUser(User arg0) {
		user = arg0;
	}
	
	
	public void prepare(){
		if (this.typeId == null) {
			 this.selectedFlightActual = new FlightActuals();
			 this.flightPlan = new FlightPlan();
			 this.flightLog = new FlightLog();
			 //this.status="OPEN";
		}
		else {
			this.selectedFlightActual = manager.findFlightActualsByID(typeId);
			if (this.selectedFlightActual == null)
			{
				this.selectedFlightActual = new FlightActuals();
				
				//this.status="OPEN";
			}
			else
			{
				this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusID();
				this.flightPlan = (FlightPlan)this.selectedFlightActual.getFlightPlan();
				this.flightVariance =  this.selectedFlightActual.getFlightVariance();
				this.flightLog = this.selectedFlightActual.getFlightLog();
				this.status=this.selectedFlightActual.getFlightActualStatus().getFlightActualStatusID();
				
			}
		}
	}
}
