package com.itao.starlite.ui.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;
import org.apache.struts2.views.jasperreports.JasperReportsResult;
import org.jfree.util.Log;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.limit.Limit;
import org.jmesa.view.editor.AbstractCellEditor;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlComponentFactory;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;
import static org.jmesa.limit.ExportType.CSV;
import static org.jmesa.limit.ExportType.EXCEL;


import com.google.inject.Inject;

import com.itao.starlite.ui.service.MailService;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.auth.manager.AuthManager;
import com.itao.starlite.exceptions.CannotCreateCrewMemberException;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Task;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.util.JasperReportUtil;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Results({
    @Result(name="taskname", value="task.action", type=ServletRedirectResult.class),
    @Result(name="crewMember", value="crewMember!addFlightActuals.action?id=${cmId}&tab=flight&actualsId=${acId}", type=ServletRedirectResult.class),
    @Result(name="payAdvice",    type=JasperReportsResult.class,   value="/jasperreports/Pay.jasper", params={"dataSource","crewMembers","documentName","PayAdvice" })
})
@Permissions("ALL")
public class TaskAction extends ActionSupport implements UserAware, ServletContextAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4164134961742440395L;
	public List<Task> task;
	public String tab = "Task Tab";

	public String tableHtml = "";

	public Breadcrumb[] breadcrumbs = {new Breadcrumb("TaskBC")};
	public String current="task";

	public User user;

	@Inject
	private StarliteCoreManager manager;
	@Inject
	private AuthManager authManager;
    @Inject
    private MailService mailService;
    
    private ServletContext servletContext;
    private HttpServletResponse response;
    
    public void setServletResponse(HttpServletResponse response){
        this.response = response;
    }
	
	
	//testing for jasperreports
	private String[] ids;
	public void setIds(String[] ids){
	this.ids = ids;
	}
	
	
	@SuppressWarnings("unchecked")
	public List taskList;
	
	
	public String payPDF(){
	    //check dates supplied
		//ids = new String[]{"32","19","10","57","20"};
		//dateFrom = "";
		//dateTo   = "";
		//date     = "";
		return "PayPDF";//payPDF(ids, dateFrom, dateTo, date);
	}
	
	@SuppressWarnings("unchecked")
	public String emailPayPDF(){
		
        return "crewMember";
	}
	
	@SuppressWarnings("unchecked")
	public String payPDF(String[] ids, String dateFrom, String dateTo, String date)
	{
	
		return "payAdvice";
		
	}
	
    
    private void sortCrewByName() {
        
    }

	@Override
	public String execute() throws Exception {
		/*crew = manager.getPermCrew();
                sortCrewByName();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("crewTable", ServletActionContext.getRequest());
		tableFacade.setItems(crew);
		tableFacade.setMaxRows(300);
		HtmlComponentFactory factory = new HtmlComponentFactory(tableFacade.getWebContext(), tableFacade.getCoreContext());
		HtmlTable table = factory.createTable();
		HtmlRow row = factory.createRow();
		addColumnsToRow(row, factory);
		table.setRow(row);
		tableFacade.setTable(table);
		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
		tab="Permenant Crew";
		prepareTabs();
		*/return SUCCESS;
	}
	
	public String freelance() throws Exception{
	/*	crew = manager.getAdHocCrew();
        sortCrewByName();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("crewTable", ServletActionContext.getRequest());
		tableFacade.setItems(crew);
		tableFacade.setMaxRows(300);
		HtmlComponentFactory factory = new HtmlComponentFactory(tableFacade.getWebContext(), tableFacade.getCoreContext());
		HtmlTable table = factory.createTable();
		HtmlRow row = factory.createRow();
		addColumnsToRow(row, factory);
		table.setRow(row);
		tableFacade.setTable(table);
		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
		tab="Freelance Crew";
		prepareTabs();
		*/return "freelance";
	}

	public String firstName=null;
	public String lastName=null;
	public String title=null;
	public String generatedPassword;
	public boolean prepareAdd = true;
	public String errorMessage = null;
	//public CrewMember createdCrewMember;

	@Permissions("ALL")
	public String addCrewMember() {
		
		return "add";
	}


	private void addColumnsToRow(HtmlRow row, HtmlComponentFactory factory) 
	{
	/*	HtmlColumn column = factory.createColumn("code");
		column.getCellRenderer().setCellEditor(new AbstractCellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object val = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("crewMember.action?id="+val).quote().close();
				if (val == null) val = "";
				html.append(val.toString());
				html.aEnd();
				return html.toString();
			}

		});
		row.addColumn(column);

		column = factory.createColumn("personal.lastName");
		column.setTitle("Last Name");
		row.addColumn(column);

		column = factory.createColumn("personal.firstName");
		column.setTitle("First Name");
		row.addColumn(column);

		column = factory.createColumn("role.position");
		column.setTitle("Role");
		row.addColumn(column);
		
		column = factory.createColumn("personal.mobilePhone");
		column.setTitle("Mobile Phone");
		row.addColumn(column);

		column = factory.createColumn("personal.homePhone");
		column.setTitle("Home Phone");
		row.addColumn(column);

		if (user.hasPermission("UserAdmin")) {
			column = factory.createColumn("Password");
			column.getCellRenderer().setCellEditor(new AbstractCellEditor() {

				public Object getValue(Object item, String property, int rowCount) {
					Object val = new BasicCellEditor().getValue(item, property, rowCount);
					Object username = new BasicCellEditor().getValue(item, "code", rowCount);
					HtmlBuilder html = new HtmlBuilder();
					html.a().href().quote().append("crew!resetPassword.action?username="+username).quote().close();
					html.append("Reset");
					html.aEnd();
					return html.toString();
				}

			});
			row.addColumn(column);
		}*/
	}

	

	public void setUser(User arg0) {
		this.user = arg0;
	}

    public String period = "200803";
    public List<HashMap> report;
    


    public String reports() {
        breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Crew", "crew.action"),
			new Breadcrumb("Reports")
		);
        return "reports";
    }

  

    private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    public String script;
    public String html;
    public String reportTitle = "none";
    public String scriptReport() {

        //crew = manager.getAllCrew();
       // sortCrewByName();
        FileInputStream fis = null;
        try {
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("groovy");
            String webpath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/groovyReports/");
            fis = new FileInputStream(webpath+script);
            Reader reader = new InputStreamReader(fis);
            scriptEngine.eval(reader);
            Invocable inv = (Invocable)scriptEngine;
            reportTitle = (String) inv.invokeFunction("title");
            html = (String) inv.invokeFunction("build",
            		                           task,
            		                           ServletActionContext.getRequest(),
            		                           ServletActionContext.getResponse());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	try {
        		if (fis!=null) fis.close();
        	}
        	catch (Exception ignored) {;}
        }

        if (html==null) {
            return null;
        }

        breadcrumbs = Breadcrumb.toArray(
    		new Breadcrumb("Crew", "crew.action"),
            new Breadcrumb("Reports", "crew!reports.action"),
    		new Breadcrumb(reportTitle)
    	);

        return "scriptReport";
    }


	public void setServletContext(ServletContext _context) {
		// TODO Auto-generated method stub
		servletContext = _context;
		
	}

	public Tab[] tableTabs;
	public void prepareTabs() {
		Tab aircraftTab = new Tab("Permenant Crew", "crew.action", tab.equals("Permenant Crew"));
		Tab aircraftTypeTab = new Tab("Freelance Crew", "crew!freelance.action", tab.equals("Freelance Crew"));
		
		if (user.hasPermission("ALL"))
			tableTabs = new Tab[] {aircraftTab, aircraftTypeTab};
		
	}


}
