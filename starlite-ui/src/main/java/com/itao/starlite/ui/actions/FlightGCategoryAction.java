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
import com.itao.starlite.model.FlightGCatagory;
import com.itao.starlite.model.FlightOFP;
import com.itao.starlite.model.JobTask;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="aircraft!flightOFP.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="FlightOFP.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
@Permissions("ManagerView")
public class FlightGCategoryAction extends ActionSupport implements UserAware {
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

	
	public boolean edited = false;
	public String gVal;
	public FlightGCatagory gCategory;
	
	@Inject
	private StarliteCoreManager manager;	
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public FlightGCategoryAction() {
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
				new Breadcrumb("<a href='aircraft!flightOFP.action'>OFP</a>"),
				new Breadcrumb("New G-Category")};
		tab = "flight OFP";
		
		prepare();
		prepareTabs();
		prepareData();
		
		
		return SUCCESS;
	}
	
	/*---------------------------------------------------------------------*/
	public void prepareData()
	/*---------------------------------------------------------------------*/
	{
 	}
	/*---------------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public String save() throws Exception
	/*---------------------------------------------------------------------*/
	{
	    boolean pass = false;
	    prepare();
		try
		{
			FlightGCatagory flightGCat = manager.getGCategoryByValue(this.gVal);
			
			if (flightGCat == null)
			{
				this.gCategory.setValue(gVal);
				this.gCategory = manager.saveGCategory(gCategory);
				this.notificationMessage = "G-Category saved.";
				pass = true;
			}
			else
			{
				pass=false;
				errorMessage = gVal + " already exists.";
    		}
		}
		catch (Exception ex)
		{
			pass=false;
			errorMessage = "Unable to save the G-Category: <br />Possible Reason: " + ex.getMessage().toString();
		}
		
		//If it's not a duplicate and if there were no errors, then continue.
		
		return execute();
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
			this.gCategory = manager.saveGCategory(gCategory);
		}
		catch (Exception ex)
		{
			pass=false;
			errorMessage = "Unable to save the G-Category: <br />Possible Reason: " + ex.getMessage().toString();
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
				new Breadcrumb("<a href='aircraft!flightOFP.action'>OFP</a>"),
				new Breadcrumb("Edit G-Category")};
		
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
			this.gCategory= new FlightGCatagory();
	
		}
		else
		{
			this.gCategory = this.manager.getGCategoryByID(id);
			this.gVal = gCategory.getValue();
		}
		
	}
	

}
