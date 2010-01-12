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
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.util.JasperReportUtil;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Results({
    @Result(name="crewList", value="crew.action", type=ServletRedirectResult.class),
    @Result(name="crewMember", value="crewMember!addFlightActuals.action?id=${cmId}&tab=flight&actualsId=${acId}", type=ServletRedirectResult.class),
    @Result(name="payAdvice",    type=JasperReportsResult.class,   value="/jasperreports/Pay.jasper", params={"dataSource","crewMembers"})
})
@Permissions("ManagerView")
public class CrewAction extends ActionSupport implements UserAware, ServletContextAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4164134961742440395L;
	public List<CrewMember> crew;
	public String tab = "Personal";

	public String tableHtml = "";

	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Crew")};
	public String current="crew";

	public User user;

	public String dateFrom;
	public String dateTo;
	public String date;
	public Integer acId;
	public String cmId;
	
	@Inject
	private StarliteCoreManager manager;
	@Inject
	private AuthManager authManager;
    @Inject
    private MailService mailService;
    
    private ServletContext servletContext;
	
	
	//testing for jasperreports
	private String[] ids;
	public void setIds(String[] ids){
	this.ids = ids;
	}
	
	
	@SuppressWarnings("unchecked")
	public List crewMembers;
	
	public List getCrewMembers(){
		return crewMembers;
	}
	
	public String payPDF(){
	    //check dates supplied
		//ids = new String[]{"32","19","10","57","20"};
		//dateFrom = "";
		//dateTo   = "";
		//date     = "";
		return payPDF(ids, dateFrom, dateTo, date);
	}
	
	@SuppressWarnings("unchecked")
	public String emailPayPDF(){
		//crew!emailPayPDF.action?ids=#cmId=#&acId=#&date=#
		
		Log.info("emailPayPDF ids= "+ids);
		
		String[] ids    = this.ids;
		String dateFrom = this.dateFrom;
		String dateTo   = this.dateTo;
		String date     = this.date;

		payPDF(ids, dateFrom, dateTo, date);

		String firstname = "";
		String lastname  = "";
		String preferredname= "";
		String fromDate  = "";
		String toDate    = "";
		String email     = "";

		if(crewMembers.size() > 0){
			CrewMember cm = (CrewMember) crewMembers.get(0);
			firstname = cm.getPersonal().getFirstName();
			//preferredname = cm.getPersonal().getPreferredName();
			lastname  = cm.getPersonal().getLastName();
			email     = cm.getPersonal().getEmail();
			fromDate  = cm.startDate;
			toDate    = cm.endDate;


			Log.info("Sending Payslip to :"+email+" ids= "+ids);
			//testing
			//email= "jelliott@i-tao.com";

			String filename = "Pay Slip "+"("+firstname+" "+lastname+") "+fromDate.replaceAll("/", "")+" - "+toDate.replaceAll("/","")+".pdf";
			String path = servletContext.getRealPath("/jasperreports/Pay.jasper");
			JasperReportUtil.toFile( path, crewMembers, new HashMap(), filename );
			File file = new File(JasperReportUtil.JASPER_DIR + filename);
			Map model = new HashMap<String,String>();
			try{
				model.put("firstname", firstname);
				model.put("lastname",  lastname);
				model.put("fromDate",  fromDate);
				model.put("toDate",    toDate);
				mailService.sendAttachedMessage(""+firstname+" "+lastname+" Pay Slip "+fromDate+" - "+toDate, email, firstname+" "+lastname , file, "mailconfirm-html.ftl" ,model);
			}
			catch(Exception e){
				LOG.error("Error sending mail",e);
				e.printStackTrace();
			}
		}
        return "crewMember";
	}
	
	@SuppressWarnings("unchecked")
	public String payPDF(String[] ids, String dateFrom, String dateTo, String date){
		//build a list of all selected crew member ids
		//test email
		
		
		String idList = "";
		crewMembers = new ArrayList();
		for(String iditem : ids){
			if("".equals(idList)){
				idList = iditem;
			}
			else{
				idList += ","+iditem;
			}
		}
		crew = manager.getCrewMembersByCodes(idList);
		for(CrewMember cm : crew){
			crewMembers.addAll(cm.getPaymentsClones(dateFrom, dateTo, date));
		}
		Log.info("Payments:"+crewMembers);
		//test = crew;

		
		return "payAdvice";
		
	}
	
    
    private void sortCrewByName() {
        Collections.sort(crew, new Comparator<CrewMember>() {

			public int compare(CrewMember o1, CrewMember o2) {
				String n1 = o1.getPersonal().getLastName();
				String n2 = o2.getPersonal().getLastName();
				if (n1 == null)
					n1 = "";
				if (n2 == null)
					n2 = "";
				String[] n1Parts = n1.split(" ");
				String[] n2Parts = n2.split(" ");

				String name1 = "";
				String name2 = "";
				for (String s: n1Parts) {
					if (s.length() > name1.length())
						name1 = s;
				}
				for (String s: n2Parts) {
					if (s.length() > name2.length())
						name2 = s;
				}
				return name1.compareTo(name2);
			}

		});
    }

	@Override
	public String execute() throws Exception {
		crew = manager.getPermCrew();
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
		return SUCCESS;
	}
	
	public String freelance() throws Exception{
		crew = manager.getAdHocCrew();
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
		return "freelance";
	}

	public String firstName=null;
	public String lastName=null;
	public String title=null;
	public String generatedPassword;
	public boolean prepareAdd = true;
	public String errorMessage = null;
	public CrewMember createdCrewMember;

	@Permissions("UserAdmin")
	public String addCrewMember() {
		if (!prepareAdd) {
			if (firstName == null || lastName == null || firstName.trim().equals("") || lastName.trim().equals("")) {
				errorMessage = "First Name and Last Name must be entered";
				return ERROR;
			}
			try {
				Object[] result = manager.createCrewMember(title, firstName, lastName, user);
				createdCrewMember = (CrewMember) result[0];
				generatedPassword = (String) result[1];
				return "added";
			} catch (CannotCreateCrewMemberException e) {
				errorMessage = "Error: Unable to create Crew Member.";
				return ERROR;
			}
		}
		return "add";
	}


	private void addColumnsToRow(HtmlRow row, HtmlComponentFactory factory) {
		HtmlColumn column = factory.createColumn("code");
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
		}
	}

	public String username;
	public String newPassword1;
	public String newPassword2;
	public boolean doChange = false;
	public String notificationMessage;
	@Permissions("UserAdmin")
	public String changePassword() throws Exception {
		if (username == null) {
			errorMessage = "No User selected";
			return execute();
		}
		if (doChange) {
			if (newPassword1 == null || newPassword2 == null || newPassword1.trim().equals("") || newPassword2.trim().equals("") || !newPassword1.equals(newPassword2)) {
				errorMessage = "Passwords must be the same and not empty";
				return "password";
			}
			authManager.changePassword(username, newPassword1);
			return "crewList";
		} else {
			return "password";
		}
	}

	@Permissions("UserAdmin")
	public String resetPassword() throws Exception {
		if (username == null) {
			errorMessage = "No User Selected";
			return execute();
		}
		String newPass = authManager.generateRandomPassword();
		authManager.changePassword(username, newPass);
		CrewMember cm = manager.getCrewMemberByCode(username);
		notificationMessage = cm.getPersonal().getFirstName()+" " + cm.getPersonal().getLastName()+"'s password changed to: " + newPass;
		return execute();
	}

	public void setUser(User arg0) {
		this.user = arg0;
	}

    public String period = "200803";
    public List<HashMap> report;
    public String hoursReport() {
        breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Crew", "crew.action"),
            new Breadcrumb("Reports", "crew!reports.action"),
			new Breadcrumb("Payments Report - March 2008")
		);

        report = new ArrayList<HashMap>();
        crew = manager.getAllCrew();
        sortCrewByName();
        for (CrewMember cm : crew) {
            HashMap<String, Object> reportRow = new HashMap<String, Object>();
            reportRow.put("lastName", cm.getPersonal().getLastName());
            reportRow.put("firstName", cm.getPersonal().getFirstName());

            CrewMember.FlightAndDutyActuals actuals = cm.getFlightAndDutyActualsForMonth(period);
            reportRow.put("total", actuals.getTotal());
            reportRow.put("paid", actuals.getPaidAmount());

            report.add(reportRow);
        }
        return "hoursReport";
    }


    public String reports() {
        breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Crew", "crew.action"),
			new Breadcrumb("Reports")
		);
        return "reports";
    }

    public String expiryReport() {
        breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Crew", "crew.action"),
            new Breadcrumb("Reports", "crew!reports.action"),
			new Breadcrumb("Expiry Report")
		);
        crew = manager.getAllCrew();
        sortCrewByName();

        TableFacade tableFacade = new TableFacadeImpl("expiry", ServletActionContext.getRequest());
        tableFacade.setItems(crew);
        tableFacade.setColumnProperties("personal.lastName",
        		"personal.firstName",
        		"personal.passportExpiryDate",
        		"role.expiryDate",
        		"role.r1.expiryDate",
        		"role.r2.expiryDate",
        		"role.crm.expiryDate",
        		"role.dg.expiryDate",
        		"role.ifr.expiryDate");
        tableFacade.setExportTypes(ServletActionContext.getResponse(), CSV, EXCEL);

        Limit limit = tableFacade.getLimit();
        if (limit.isExported()) {
            tableFacade.render();
            return null;
        }

        return "expiryReport";
    }

    public String licenceReport() {
        breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Crew", "crew.action"),
            new Breadcrumb("Reports", "crew!reports.action"),
			new Breadcrumb("Licences Report")
		);
        crew = manager.getAllCrew();
        sortCrewByName();
        return "licenceReport";
    }

    public String certificateReport() {
        breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Crew", "crew.action"),
            new Breadcrumb("Reports", "crew!reports.action"),
			new Breadcrumb("Certificates Report")
		);
        crew = manager.getAllCrew();
        sortCrewByName();
        return "certificateReport";
    }

    private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    public String script;
    public String html;
    public String reportTitle = "none";
    public String scriptReport() {

        crew = manager.getAllCrew();
        sortCrewByName();
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
            		                           crew,
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
		
		if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {aircraftTab, aircraftTypeTab};
		
	}


}
