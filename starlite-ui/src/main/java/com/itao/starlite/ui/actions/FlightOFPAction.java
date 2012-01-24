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
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.FlightActualStatus;
import com.itao.starlite.model.FlightActuals;
import com.itao.starlite.model.FlightGCatagory;
import com.itao.starlite.model.FlightOFP;
import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="aircraft!flightOFP.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}&tab=${tab}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="FlightOFP.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
})
@Permissions("ManagerView")
public class FlightOFPAction extends ActionSupport implements UserAware {
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
	public String tab ="";
	private Tab staticTab = new Tab("Static", "aircraft.action", false);
	public User user;
	public Integer id = null;

	public FlightOFP flightOFP = new FlightOFP();
	public List<String> contracts = new ArrayList<String>();
	public String flightType ="";
	public String createDate="";
	public boolean edited = false;
	public int approval = 0;
	
	public String aircraftRef ="";
	public String gCategory="";
	public String ofpDate;
	public int status;
	public int newVal;
	public Integer typeId=0;
	
	
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
	public ArrayList<String> gCategoryList = new ArrayList<String>();
	//public ArrayList<FlightPlan> landingsList = new ArrayList<FlightPlan>();
	
	public String to1,to2,to3,to4;
	public String from1,from2,from3,from4;
	public String time1,time2,time3,time4;
	public String hobbsOn1,hobbsOn2,hobbsOn3,hobbsOn4;
	public String hobbsOff1,hobbsOff2,hobbsOff3,hobbsOff4;
	public String hobbsDiff1,hobbsDiff2,hobbsDiff3,hobbsDiff4;
	
	@Inject
	private StarliteCoreManager manager;
		
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public FlightOFPAction() {
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
				new Breadcrumb("<a href='aircraft!flightOFP.action'>Flight OFP</a>"),
				new Breadcrumb("New OFP")};
		//tab = "flight OFP New";
		
		prepare();
		prepareTabs();
		prepareData();
		if (flightOFP.getFlightActualStatus() == null)
		{
			this.flightOFP.setFlightActualStatus(manager.findStatusValueByID(1));
			this.status=this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
			this.id = this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
		}
		else
		{
			this.status=this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
			this.id = this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
		}
		return SUCCESS;
	}
	
	/*---------------------------------------------------------------------*/
	public void prepareData()
	/*---------------------------------------------------------------------*/
	{
		this.contracts = (ArrayList<String>) manager.findAllCharterNames();
		Collections.sort(this.contracts);
		
		this.gCategoryList = new ArrayList<String>();
		List<FlightGCatagory> tempGList = (ArrayList<FlightGCatagory>) manager.getAllGCategories();
		for (int q=0; q< tempGList.size(); q++)
		{
			FlightGCatagory temp = (FlightGCatagory)tempGList.get(q);
		  this.gCategoryList.add(temp.getValue());
		}
		Collections.sort(this.gCategoryList);
		
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
	public void setDates()
	/*---------------------------------------------------------------------*/
	{
		Date r = null;
		if (this.ofpDate != null)
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.ofpDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.flightOFP.setOFPDate(r);
		}		
	}
	/*---------------------------------------------------------------------*/
	public void setLandings(FlightOFP actual)
	/*---------------------------------------------------------------------*/
	{
		List<FlightPlan> tempLandings= new ArrayList<FlightPlan>();
		if (actual==null)
		{
		tempLandings = flightOFP.getLandings();
		}
		else
		{
			tempLandings = actual.getLandings();
		}
		if ((tempLandings.isEmpty()== true))
		{
			int counter = 0;
			if (this.from1.compareTo("") != 0){counter++;}
			if (this.from2.compareTo("") != 0){counter++;}
			if (this.from3.compareTo("") != 0){counter++;}
			if (this.from4.compareTo("") != 0){counter++;}
			for (int i=0; i<counter; i++)
			{
			  FlightPlan temp = new FlightPlan();
			  tempLandings.add(temp);
			}
		}
		else
		{
			int inlist = tempLandings.size();
					
			for (int i=0; i< (4-inlist); i++)
			{
			  FlightPlan temp = new FlightPlan();
			  tempLandings.add(temp);
			}
			
		}
		
		for (int i=0; i<tempLandings.size(); i++)
		{
			FlightPlan temp = tempLandings.get(i);
			switch(i)
			{
			case 0: {
				    temp.setInternalOFPRefNumber(this.flightOFP.getId());
					Double on = 0.0;
					Double off = 0.0;
					Double time = 0.00;
					Double duration = 0.0;
				    if (this.hobbsOn1.compareTo("") != 0){ on = Double.valueOf(this.hobbsOn1);}else {on=0.0;}
				    temp.setHobbsOn(Double.valueOf(on));
				    if (this.hobbsOff1.compareTo("") != 0) {off = Double.valueOf(this.hobbsOff1);}else {off=0.0;}
				    temp.setHobbsOff(Double.valueOf(off));
				    duration = off - on;
					temp.setHobbsDuration(duration);
					if (this.time1.compareTo("") != 0) {time = Double.valueOf(this.time1);}else {time=0.0;}
					temp.setFlightTime(time);
					temp.setStart_LocationField(this.from1);
					temp.setEnd_LocationField(this.to1);
					break;
					}
			case 1: {
				temp.setInternalOFPRefNumber(this.flightOFP.getId());
				Double on = 0.0;
				Double off = 0.0;
				Double time = 0.0;
				Double duration = 0.0;
			    if (this.hobbsOn2.compareTo("") != 0){ on = Double.valueOf(this.hobbsOn2);}else {on=0.0;}
			    temp.setHobbsOn(Double.valueOf(on));
			    if (this.hobbsOff2.compareTo("") != 0) {off = Double.valueOf(this.hobbsOff2);}else {off=0.0;}
			    temp.setHobbsOff(Double.valueOf(off));
			    duration = off - on;
				temp.setHobbsDuration(duration);
				if (this.time2.compareTo("") != 0) {time = Double.valueOf(this.time2);}else {time=0.0;}
				temp.setFlightTime(time);
				temp.setStart_LocationField(this.from2);
				temp.setEnd_LocationField(this.to2);
				break;
					
				}
			case 2: {
				temp.setInternalOFPRefNumber(this.flightOFP.getId());
				Double on = 0.0;
				Double off = 0.0;
				Double time = 0.00;
				Double duration = 0.0;
			    if (this.hobbsOn3.compareTo("") != 0){ on = Double.valueOf(this.hobbsOn3);}else {on=0.0;}
			    temp.setHobbsOn(Double.valueOf(on));
			    if (this.hobbsOff3.compareTo("") != 0) {off = Double.valueOf(this.hobbsOff3);}else {off=0.0;}
			    temp.setHobbsOff(Double.valueOf(off));
			    duration = off - on;
				temp.setHobbsDuration(duration);
				if (this.time3.compareTo("") != 0) {time = Double.valueOf(this.time3);}else {time=0.0;}
				temp.setFlightTime(time);
				temp.setStart_LocationField(this.from3);
				temp.setEnd_LocationField(this.to3);
				break;
				}
			case 3: 
			    {
			    	temp.setInternalOFPRefNumber(this.flightOFP.getId());
			    	Double on = 0.0;
					Double off = 0.0;
					Double time = 0.00;
					Double duration = 0.0;
				    if (this.hobbsOn4.compareTo("") != 0){ on = Double.valueOf(this.hobbsOn4);}else {on=0.0;}
				    temp.setHobbsOn(Double.valueOf(on));
				    if (this.hobbsOff4.compareTo("") != 0) {off = Double.valueOf(this.hobbsOff4);}else {off=0.0;}
				    temp.setHobbsOff(Double.valueOf(off));
				    duration = off - on;
					temp.setHobbsDuration(duration);
					if (this.time4.compareTo("") != 0) {time = Double.valueOf(this.time4);}else {time=0.0;}
					temp.setFlightTime(time);
					temp.setStart_LocationField(this.from4);
					temp.setEnd_LocationField(this.to4);
					break;
				}
				
			}
			
		}
		this.flightOFP.setLandings(tempLandings);
		
	}
	/*---------------------------------------------------------------------*/
	public void getLandings()
	/*---------------------------------------------------------------------*/
	{
		List<FlightPlan> tempLandings = flightOFP.getLandings();
	/*	if (tempLandings.isEmpty()!= false)
		{
			for (int i=0; i<tempLandings.size(); i++)
			{
			  FlightPlan temp = new FlightPlan();
			  tempLandings.add(temp);
			}
		}
	*/
		int counter = 0;
		if (tempLandings.isEmpty() == false) {counter = tempLandings.size();}
		for (int i=0; i<counter; i++)
		{
			FlightPlan temp = tempLandings.get(i);
			switch(i)
			{
			case 0: {
					this.hobbsOn1 = String.valueOf(temp.getHobbsOn());
					this.hobbsOff1 = String.valueOf(temp.getHobbsOff());
					this.hobbsDiff1 = String.valueOf(temp.getHobbsDuration());
					this.time1 = String.valueOf(temp.getFlightTime());
					this.from1 = String.valueOf(temp.getStart_LocationField()); 
					this.to1 = String.valueOf(temp.getEnd_LocationField());
					break;
					}
			case 1: {
				this.hobbsOn2 = String.valueOf(temp.getHobbsOn());
				this.hobbsOff2 = String.valueOf(temp.getHobbsOff());
				this.hobbsDiff2 = String.valueOf(temp.getHobbsDuration());
				this.time2 = String.valueOf(temp.getFlightTime());
				this.from2 = String.valueOf(temp.getStart_LocationField()); 
				this.to2 = String.valueOf(temp.getEnd_LocationField());
				break;
				}
			case 2: {
				this.hobbsOn3 = String.valueOf(temp.getHobbsOn());
				this.hobbsOff3 = String.valueOf(temp.getHobbsOff());
				this.hobbsDiff3 = String.valueOf(temp.getHobbsDuration());
				this.time3 = String.valueOf(temp.getFlightTime());
				this.from3 = String.valueOf(temp.getStart_LocationField()); 
				this.to3 = String.valueOf(temp.getEnd_LocationField());
				break;
				}
			case 3: {
				this.hobbsOn4 = String.valueOf(temp.getHobbsOn());
				this.hobbsOff4 = String.valueOf(temp.getHobbsOff());
				this.hobbsDiff4 = String.valueOf(temp.getHobbsDuration());
				this.time4 = String.valueOf(temp.getFlightTime());
				this.from4 = String.valueOf(temp.getStart_LocationField()); 
				this.to4 = String.valueOf(temp.getEnd_LocationField());
				break;
				}
				
			}
			
		}	
	}
	/*---------------------------------------------------------------------*/
	public String updateStatus() throws Exception
	/*---------------------------------------------------------------------*/
	{
				
		FlightActualStatus temp = new FlightActualStatus();
		if (typeId != null)
		{
		temp = this.manager.findStatusValueByID(typeId);
		}
		else{temp = null;}
		
		if (temp != null && typeId != this.status && typeId !=0)
		{
		this.flightOFP.setNewActualStatusValueObject(temp);
		//this.flightOFP = this.manager.saveFlightOFP(flightOFP);
		//this.status=this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
		//this.typeId = this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
		}

		return "";
	   
	}
	/*---------------------------------------------------------------------*/
	/*---------------------------------------------------------------------*/
	public String save() throws Exception
	/*---------------------------------------------------------------------*/
	{
	    boolean pass = false;
	    Date r;
		try
		{
			List<FlightOFP> flightOFPList = new ArrayList<FlightOFP>();
			
			if (flightOFPList.isEmpty())
			{
				setDates();
				setLandings(null);
				this.flightOFP.setAircraft(this.manager.getAircraftByReg(this.aircraftRef));
				this.flightOFP.setGCatagory(this.manager.getGCategoryByValue(this.gCategory));
				this.flightOFP.setCreateDate(new Date());
				//check if it exists
				flightOFPList = manager.findFlightOFP(this.flightOFP.getContract(), this.flightOFP.getInvoiceNo(), this.flightOFP.getOFPDate());
				if (flightOFPList.isEmpty()==true)
				{
			    this.flightOFP.setFlightActualStatus(manager.findStatusValueByID(2));
				this.flightOFP = manager.saveFlightOFP(this.flightOFP);
				this.status=this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
				this.notificationMessage = "OFP ["+this.flightOFP.id+"] saved.";
				List<FlightPlan> children = new ArrayList<FlightPlan>();
				
				for (FlightPlan child: children)
				{
					if (child.getStart_LocationField().compareToIgnoreCase("") == 0)
					{
						children.remove(child);
					}
				}
				for (FlightPlan child: children)
				{
					child.setInternalOFPRefNumber(this.flightOFP.getId());
				}
				this.flightOFP = manager.saveFlightOFP(this.flightOFP);
				}
				else
				{
			     this.errorMessage = "Unable to save OFP - Contract, Invoice # and Date already exists.";		
				}
				pass = true;
			}
			else
			{
				pass=false;
				errorMessage = "Unable to save the Flight Plan: Field combination already exists for:"+
				"<br/>Contract:      "+this.flightOFP.getContract()+
				"<br/>Invoice No:    "+this.flightOFP.getInvoiceNo()+
				"<br/>OFP Date:   "+this.flightOFP.getOFPDate();
				
			}
		}
		catch (Exception ex)
		{
			pass=false;
			errorMessage = "Unable to save the OFP: <br />Possible Reason: " + ex.getMessage().toString();
		}
		
		//If it's not a duplicate and if there were no errors, then continue.
		
		return "redirect";
	}
	/*---------------------------------------------------------------------*/
	public String editedSave() throws Exception
	/*---------------------------------------------------------------------*/
	{
		//prepare();
		
		if ((typeId != 0) && (this.approval == 1))
		{
			this.updateStatus();
		}
		
		this.edited = true;
	    boolean pass = false;
		try
		{
			FlightOFP temp = this.manager.findFlightOFPById(id);
			if ((typeId != 0)&& (this.approval == 1))
			{
			FlightActualStatus statusTemp = this.manager.findStatusValueByID(typeId);
			temp.setFlightActualStatus(statusTemp);
			}
			else
			{
				FlightActualStatus statusTemp = this.manager.findStatusIDByValue(this.flightOFP.getFlightActualStatus().getFlightActualStatusValue());
				temp.setFlightActualStatus(statusTemp);	
			}
			setDates();
			setLandings(temp);
			if (temp != null)
			{
				temp.setValues(this.flightOFP, manager);
				
			}
			else {this.flightOFP.setCreateDate(new Date());temp = this.flightOFP;}
			
			temp.setAircraft(this.manager.getAircraftByReg(this.aircraftRef));
			temp.setGCatagory(this.manager.getGCategoryByValue(this.gCategory));
			this.flightOFP = manager.saveFlightOFP(temp);
			this.status=this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
			this.typeId = this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
			notificationMessage = "Record Saved.";
			
		}
		catch (Exception ex)
		{
			approval=0;
			pass=false;
			errorMessage = "Unable to save the Flight Plan: <br />Possible Reason: " + ex.getMessage().toString();
		}
		approval=0;
		return "redirect";
	}
	/*---------------------------------------------------------------------*/
	public String approval()
	/*---------------------------------------------------------------------*/
	{
		FlightActualStatus prevStatus = null;
		
		if ((typeId != 0) && (this.approval == 1))
		{
			
			FlightOFP temp = this.manager.findFlightOFPById(id);
			prevStatus = temp.getFlightActualStatus();
			FlightActualStatus statusTemp = this.manager.findStatusValueByID(typeId);
			temp.setFlightActualStatus(statusTemp);
			this.flightOFP = manager.saveFlightOFP(temp);
			if (typeId == 7)
			{
				try{
				updateComponentHrs();
				}
				catch(Exception e)
				{
					temp.setFlightActualStatus(prevStatus);
					this.flightOFP = manager.saveFlightOFP(temp);
					this.errorMessage = "Unable to release hours. Possible Reason: " + e.getMessage().toString();
					
				}
			}
			
						
		}
		if (typeId == 4)
		{
			tab = "flight OFP Approval";
		}
		else
		{
			tab = "flight OFP Release";
		}
		return "redirect";
	}
	/*---------------------------------------------------------------------*/
	private  void updateComponentHrs() throws Exception
	/*---------------------------------------------------------------------*/
	{
		Double totalHrs = getTotalFlightHrs();
		List<Component> components = manager.getAllClassComponents(this.flightOFP.getAircraft().getRef().replaceAll("-", ""));
		
		for (Component comp : components)
		{
			if (comp.getHoursRun() == null )
			{
				comp.setHoursRun(new Double(0.0));
			}
			Double hoursRun = comp.getHoursRun() + totalHrs;
			comp.setHoursRun(hoursRun);
			manager.saveComponent(comp);
		}
		
	}
	/*---------------------------------------------------------------------*/
	private Double getTotalFlightHrs() throws Exception
	/*---------------------------------------------------------------------*/
	{
		Double totalHrs = 0.0;
		
		for (FlightPlan fp:this.flightOFP.getLandings())
		{
		    totalHrs += fp.getFlightTime();
		}
		return totalHrs;
	}
	/*---------------------------------------------------------------------*/
	public String edit()
	/*---------------------------------------------------------------------*/
	{
		//Set the breadcrumbs for the current activity
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("<a href='aircraft.action'>Aircraft</a>"),
				new Breadcrumb("<a href='aircraft!flightOFP.action'>OFP</a>"),
				new Breadcrumb("Edit OFP")};
		
		prepare();
		prepareData();
		
		return "edit";
	}
	/*---------------------------------------------------------------------*/
	public String setNavPlan()
	{
		
		//set the bread crumbs
		this.breadcrumbs = new Breadcrumb[]{
				new Breadcrumb("OFP"),
				new Breadcrumb("Navigation Plan")};
		this.current = "Navigation Planning";
		
		return INPUT;
	}
	
	private void prepareTabs() {

		Tab aircraftGeneralTab = new Tab("General", "FlightOFP.action", tab.equals("aircraft"));
		Tab aircraftNavPlanTab = new Tab("Navigation Planning", "#", tab.equals("aircraft type"));
		Tab aircraftFuelPlanTab = new Tab("Fuel Plan", "aircraft!flightActuals.action", tab.equals("flight actuals"));
		Tab aircraftPerformanceTab = new Tab("Performance", "aircraft!FlightOFPs.action", tab.equals("flight plans"));
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
			this.flightOFP= new FlightOFP();
	
		}
		else
		{
			try
			{
				this.flightOFP = this.manager.findFlightOFPById(id);
				this.gCategory = this.flightOFP.getGCatagory().getValue();
				this.aircraftRef = this.flightOFP.getAircraft().getRef();
				getLandings();
				if (this.flightOFP.getFlightActualStatus() == null)
				{
					this.flightOFP.setFlightActualStatus(manager.findStatusValueByID(1));
					this.flightOFP = manager.saveFlightOFP(flightOFP);
				}
				this.status=this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
				this.typeId=this.flightOFP.getFlightActualStatus().getFlightActualStatusID();
				
				
				
				if (this.flightOFP== null)
				{
					this.flightOFP = new FlightOFP();
	
				}
			}
			catch(Exception ex)
			{
				this.flightOFP= new FlightOFP();
				
			}
		}
		
	}
	

}
