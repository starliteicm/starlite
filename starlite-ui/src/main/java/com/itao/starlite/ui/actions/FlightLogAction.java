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
	@Result(name="redirect", type=ServletRedirectResult.class, value="flightPlan!save.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="flightPlan.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
	
})
@Permissions("ManagerView")
public class FlightLogAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3344297237658297955L;
	public String tableHtml;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Flight Log")};
	public String errorMessage = "";
	public String notificationMessage;
	
	public Tab[] tableTabs;
	public String tab = "flight plans";
	private Tab staticTab = new Tab("Static", "aircraft.action", false);
	public User user;
	public Integer id = null;

	public FlightLog flightLog = new FlightLog();

	
	@Inject
	private StarliteCoreManager manager;	
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public FlightLogAction() {
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
				new Breadcrumb("<a href='aircraft!flightMismatches.action'>Flight Mismatches</a>"),
				new Breadcrumb("Flight Log")};
				
		
		
		prepareTabs();
		prepare();
		
		
		return SUCCESS;
	}
	
	/*---------------------------------------------------------------------*/
		private void prepareTabs() {

		//nothing to do here
	}

	@Override

	public void setUser(User arg0) {
		user = arg0;
	}
	
	public void prepare()
	{
		if (this.id == null)
		{
			this.flightLog = new FlightLog();
	
		}
		else
		{
			try
			{
				this.flightLog = this.manager.findFlightLogById(id);
				
				if (this.flightLog == null)
				{
					this.flightLog = new FlightLog();
				}
			}
			catch(Exception ex)
			{
				this.flightLog = new FlightLog();
			}
		}
		
	}
	

}
