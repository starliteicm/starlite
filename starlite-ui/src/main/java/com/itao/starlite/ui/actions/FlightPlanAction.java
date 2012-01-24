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
import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="aircraft!flightPlans.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="flightPlan.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
@Permissions("ManagerView")
public class FlightPlanAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3344297237658297955L;
	public String tableHtml;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Flight Plan")};
	public String errorMessage = "";
	public String notificationMessage;
	
	public Tab[] tableTabs;
	public String tab = "flight plans";
	private Tab staticTab = new Tab("Static", "aircraft.action", false);
	public User user;
	public Integer id = null;

	public FlightPlan flightPlan = new FlightPlan();
	public String customer = "";
	public String flightType ="";
	public String createDate="";
	public boolean edited = false;
	
	public List<Aircraft> allAircrafts = new ArrayList<Aircraft>();
	public List<String> aircrafts = new ArrayList<String>();
	public ArrayList<String> pilots = new ArrayList<String>();
	public ArrayList<String> coPilots = new ArrayList<String>();
	public ArrayList<String> engineers = new ArrayList<String>();
	public ArrayList<AircraftType> aircraftTypesObj = new ArrayList<AircraftType>();
	public ArrayList<String> aircraftTypes = new ArrayList<String>();
	public ArrayList<JobTask> jobTasks = new ArrayList<JobTask>();
	public ArrayList<String> typeOfService = new ArrayList<String>();
	public FlightActuals newRecord = new FlightActuals();
	
	@Inject
	private StarliteCoreManager manager;	
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public FlightPlanAction() {
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
				new Breadcrumb("<a href='aircraft!flightPlans.action'>Flight Plans</a>"),
				new Breadcrumb("New Flight Plan")};
		
		prepare();
		prepareTabs();
		prepareData();
		
		
		return SUCCESS;
	}
	
	/*---------------------------------------------------------------------*/
	public void prepareData()
	/*---------------------------------------------------------------------*/
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
		
		
	}
	/*---------------------------------------------------------------------*/
	public String save()
	/*---------------------------------------------------------------------*/
	{
	    boolean pass = false;
		try
		{
			List<FlightPlan> flightPlanList = manager.findFlightPlan(this.flightPlan.getCustomer(), this.flightPlan.getInvoiceNo(), this.flightPlan.getFlightNumber(), this.flightPlan.getFlightDateField(),this.flightPlan.getTakeoffField());
			
			if (flightPlanList.isEmpty())
			{
				this.flightPlan = manager.saveFlightPlan(this.flightPlan);
				this.notificationMessage = "Flight Plan ["+this.flightPlan.getFlightPlanID()+"] saved.";
				pass = true;
			}
			else
			{
				pass=false;
				errorMessage = "Unable to save the Flight Plan: Field combination already exists for:"+
				"<br/>Customer:      "+flightPlan.getCustomer()+
				"<br/>Invoice No:    "+this.flightPlan.getInvoiceNo()+
				"<br/>Flight No:     "+this.flightPlan.getFlightNumber()+
				"<br/>Flight Date:   "+this.flightPlan.getFlightDateField()+
				"<br/>Take off time: "+this.flightPlan.getTakeoffField();
			}
		}
		catch (Exception ex)
		{
			pass=false;
			errorMessage = "Unable to save the Flight Plan: <br />Possible Reason: " + ex.getMessage().toString();
		}
		
		//If it's not a duplicate and if there were no errors, then continue.
		if (pass)
		{
			try
			{
				//check to see if flight plan has a matching FlightLog in the FlightActuals table.
				FlightActuals temp = manager.findFlightActualsFlightPlan(this.flightPlan.getRegistrationField(), this.flightPlan.getFlight_TypeField(), this.flightPlan.getFlightDateField());
				
				if (temp == null)
				{
					//doesn't exist so create
					newRecord = new FlightActuals();
					newRecord.setFlightPlan(this.flightPlan);
					FlightActualStatus open = this.manager.findStatusValueByID(1);
					newRecord.setFlightActualStatus(open);
					newRecord = this.manager.saveFlightActuals(newRecord);
					this.notificationMessage = "Flight Plan ["+this.flightPlan.getFlightPlanID()+"] saved.";
				}
				else
				{
					//update
					temp.setFlightPlan(this.flightPlan);
					temp = this.manager.saveFlightActuals(temp);
				}
			}
			catch(Exception e)
			{
				errorMessage = "Unable to save the Flight Plan: <br />Possible Reason: " + e.getMessage().toString();
			}
		}
		return "redirect-list";
	}
	/*---------------------------------------------------------------------*/
	public String editedSave()
	/*---------------------------------------------------------------------*/
	{
		//prepare();
		this.edited = true;
	    boolean pass = false;
		try
		{
			FlightActuals orig = manager.findFlightActualsByPlanID(id);
			if (orig != null)
			{
			this.flightPlan.setFlightPlanID(id);
			orig.setFlightPlan(this.flightPlan);
			orig = manager.saveFlightActuals(orig);
			this.flightPlan = orig.getFlightPlan();
			this.notificationMessage = "Updates saved.";
			}
			else
			{
				List<FlightPlan> flightPlanList = manager.findFlightPlan(this.flightPlan.getCustomer(), this.flightPlan.getInvoiceNo(), this.flightPlan.getFlightNumber(), this.flightPlan.getFlightDateField(),this.flightPlan.getTakeoffField());
				
				if (flightPlanList.isEmpty())
				{
					this.flightPlan = manager.saveFlightPlan(this.flightPlan);
					this.notificationMessage = "Flight Plan ["+this.flightPlan.getFlightPlanID()+"] saved.";
					pass = true;
				}
			}
		}
		catch (Exception ex)
		{
			pass=false;
			errorMessage = "Unable to save the Flight Plan: <br />Possible Reason: " + ex.getMessage().toString();
		}

		return "redirect";
	}
	/*---------------------------------------------------------------------*/
	public String edit()
	/*---------------------------------------------------------------------*/
	{
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='aircraft.action'>Aircraft</a>"),
				new Breadcrumb("<a href='aircraft!flightPlans.action'>Flight Plans</a>"),
				new Breadcrumb("Edit Flight Plan")};
		
		prepare();
		prepareData();
		
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
	
	public void prepare()
	{
		if (this.id == null)
		{
			this.flightPlan = new FlightPlan();
	
		}
		else
		{
			try
			{
				this.flightPlan = this.manager.findFlightPlanById(id);
				
				if (this.flightPlan == null)
				{
					this.flightPlan = new FlightPlan();
				}
			}
			catch(Exception ex)
			{
				this.flightPlan = new FlightPlan();
			}
		}
		
	}
	

}
