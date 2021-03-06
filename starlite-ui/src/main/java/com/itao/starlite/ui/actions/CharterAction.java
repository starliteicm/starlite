package com.itao.starlite.ui.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.editor.NumberCellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlTable;
import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.docs.manager.BookmarkManager;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.docs.model.Tag;
import com.itao.starlite.exceptions.CausesCollisionsException;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CombinedActuals;
import com.itao.starlite.model.CrewDay;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.types.CharterStatus;
import com.itao.starlite.scheduling.manager.SchedulingManager;
import com.itao.starlite.scheduling.model.Allocation;
import com.itao.starlite.scheduling.model.Assignable;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
@ParentPackage("prepare")
public class CharterAction extends ActionSupport implements Preparable, UserAware {
	public Charter charter;
	public Integer id;

	public String current="charters";
	public boolean notAuthorised = false;

	public Breadcrumb[] breadcrumbs;

	public Tab[] tableTabs;

	public String tab = "administrative";

	@Inject
	private StarliteCoreManager manager;
	@Inject
	private DocumentManager docManager;
	@Inject
	private BookmarkManager bookmarkManager;

	public User user;
	public List<CrewMember> allCrew;
	public List<Aircraft> allAircraft;
	@SuppressWarnings("unchecked")
	public TreeMap<String,Map> crewDayAircraft;
	
	public Date dateFrom;
	public Date dateTo;
	
	public String activity;
	public String tail;
	public List<String> members;
	
	public SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat mysqlFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String execute() throws Exception {
		if (!user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		//crewMember = manager.getCrewMember(id);
		breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Contract", "charters.action"),
			new Breadcrumb(charter.getCode())
		);
		prepareTabs();
		if (tab.equals("administrative"))
			return SUCCESS;
		else return tab;
	}

	public List<Document> docs;
	public String tagArray;
	public Folder folder;
	public String docs() throws Exception {
		if (id == null) {
			return ERROR;
		}
		tab = "documents";
		docs = new LinkedList<Document>();
		folder = docManager.getFolderByPath("/charters/"+charter.getCode(), user);
		if (folder != null)
			docs.addAll(folder.getDocs());
		Collections.sort(docs, new Comparator<Document>() {

			public int compare(Document o1, Document o2) {
				return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}

		});
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Contract", "charters.action"),
				new Breadcrumb(charter.getCode())
			);
		prepareTabs();

		List<Tag> tags = bookmarkManager.findAllTags();

		StringBuilder buf = new StringBuilder();
		buf.append('[');
		boolean first = true;
		for (Tag t: tags) {
			if (first) {
				first = false;
			} else {
				buf.append(',');
			}
			buf.append('\'');
			buf.append(t.getTag());
			buf.append('\'');
		}
		buf.append(']');
		tagArray = buf.toString();

		tab = "docs";
		prepareTabs();

		return "docs";
	}

	public String create() {
		//crewMember = new CrewMember();
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Contract", "charters.action"),
				new Breadcrumb("New Charter")
			);
		Tab administrativeTab = new Tab("Administrative", "#", true);
		tableTabs = new Tab[] {administrativeTab};
		return SUCCESS;
	}
	
	public String saveCrew() throws Exception{
		prepare();
		Aircraft aircraft =  null;
		
		if(tail != null){
			if(tail != ""){
				aircraft = manager.getAircraft(new Integer(tail));
			}
		}

		try {

			for(String member : members){

				CrewMember crewMember = manager.getCrewMemberByCode(member);

				Date from = dateFrom;
				Date to   = dateTo;
				Calendar cal = Calendar.getInstance();
				cal.setTime(from);
				to.setHours(23);
			    to.setMinutes(59);
			    to.setSeconds(59);


				if(from.before(to)){
					while(!to.after(cal.getTime())){
						String date = mysqlFormat.format(cal.getTime());
						CrewDay cd = null;
						cd = manager.getCrewDay(cal.getTime(),crewMember);
						if(cd == null){
							cd = new CrewDay(null,date,activity,null,null,null,null,aircraft,charter,crewMember,null,null,null,null);
						}
						else {
							cd.setActivity(activity);
							cd.setAircraft(aircraft);
							cd.setCharter(charter);
						}
						manager.saveCrewDay(cd);
						cal.add(Calendar.DAY_OF_MONTH,1);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect-contract";
	}

	@SuppressWarnings("unchecked")
	public String contract() throws Exception{
		tab = "contract";
		prepareTabs();
		
		prepare();
		
		List<CrewMember> crew = manager.getAllCrew();
		
		TreeMap<String,CrewMember> ordered = new TreeMap<String,CrewMember>();
		for(CrewMember cm : crew){
			if(cm.getCode() != null){
			   ordered.put(cm.getCode(), cm);
			}
		}
		allCrew = new ArrayList<CrewMember>(ordered.values());
		allAircraft= manager.getAllAircraft().aircraftList;
		
		//getCrewDays
		if((dateFrom == null)||("".equals(dateFrom))||(dateTo == null)||("".equals(dateTo))){
			setDefaultDates();
		}
		
		List<CrewDay> crewDays = manager.getCrewDayByCharterBetween(charter.getId(), dateFrom, dateTo); 
		
		crewDayAircraft =  new TreeMap<String,Map>();
		
		for(CrewDay cd : crewDays){
			Aircraft a = cd.getAircraft();
			CrewMember c = cd.getCrewMember();
			
			if((a != null)&&(c != null)){
			
			if(!crewDayAircraft.containsKey(""+a.getRef())){
				//add aircraft
				HashMap aircraft = new HashMap();
				aircraft.put("aircraft", a);
				aircraft.put("crewMap", new TreeMap());
				crewDayAircraft.put(""+a.getRef(), aircraft);
			}
			
			Map crewMap =  (Map) crewDayAircraft.get(""+a.getRef()).get("crewMap");
			if(!crewMap.containsKey(""+c.getCode())){
				//crew member already added
				Map crewMember = new HashMap();
				crewMember.put("crewMember", c);
				crewMember.put("crewDayMap", new HashMap());
				crewMap.put(""+c.getCode(), crewMember);
			}

			
			Map crewDayMap = (Map) ((Map) crewMap.get(""+c.getCode())).get("crewDayMap");
			Calendar cal = Calendar.getInstance();
			cal.setTime(cd.getDate());
			cal.add(Calendar.DAY_OF_MONTH, -1);
			if(crewDayMap.containsKey(df.format(cal.getTime()))){
				Map crewDay = (Map) crewDayMap.get(df.format(cal.getTime()));
				crewDay.put("end",cd.getDate());
				crewDayMap.remove(df.format(cal.getTime()));
				crewDayMap.put(df.format(cd.getDate()),crewDay);
			}
			else{
				Map crewDay = (Map) new HashMap();
				crewDay.put("start",cd.getDate());
				crewDay.put("end",cd.getDate());
				crewDayMap.put(df.format(cd.getDate()),crewDay);
			}
			
			}	
			
		}
		
		System.out.println(crewDayAircraft);
		
		return "contract";
	}
	
	public void setDefaultDates(){
	
		//starlite months 21-20
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,20);
		dateTo = cal.getTime();
		
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH,21);
		dateFrom = cal.getTime();
		
		
	
	}

	public String tableHtml;
	public String hours() {
		DateMidnight dm = new DateMidnight();
		Integer endMonth = dm.getMonthOfYear();
		Integer endYear = dm.getYear();

		Integer startYear = endYear-1;
		Integer startMonth = endMonth+1;
		if (startMonth > 12) {
			startMonth = 1;
			startYear = endYear;
		}

		List<CombinedActuals> totals = manager.getTotalActualsByCharter(id, startMonth, startYear, endMonth, endYear);

		totals.add(new CombinedActuals("Totals", totals));
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("charterMonthlySummary", ServletActionContext.getRequest());

		tableFacade.setColumnProperties("label", "capt", "aframe", "landings", "pax");
		tableFacade.setItems(totals);

		Table table = tableFacade.getTable();
		Column refCol = table.getRow().getColumn("label");
		refCol.setTitle("Month");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object params = new BasicCellEditor().getValue(item, "params", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				if (params == null)
					return value;
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("charterMonthlyHours.action?"+params).quote().close();
				html.append(value);
				html.aEnd();
				return html.toString();
			}

		});
		Column captCol = table.getRow().getColumn("capt");
		captCol.getCellRenderer().setCellEditor(new NumberCellEditor("###,##0.0"));

		Column aframeCol = table.getRow().getColumn("aframe");
		aframeCol.getCellRenderer().setCellEditor(new NumberCellEditor("###,##0.0"));
		((HtmlTable)table).getTableRenderer().setWidth("600px");
		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
		breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Contract", "charters.action"),
			new Breadcrumb(" Hours")
		);
		tab = "hours";
		prepareTabs();
		return "hours";
	}

	public String save() throws Exception {
        LOG.info(charter.getInsurance().getItems().get(2).isRequired());
        LOG.info(charter.getInsurance().getItems().get(2).getLimit().getAmount());
        //Quick fix to save the charter status
        Object[] charterStatus = (Object[])ActionContext.getContext().getParameters().get("charter.administrative.status");
        if (charterStatus != null)
        	charter.getAdministrative().setStatus(CharterStatus.valueOf(charterStatus[0].toString()));
		manager.saveCharter(charter);
		return execute();
	}

	public void prepare() throws Exception {
		if (id == null) {
			charter = new Charter();
		} else {
			charter = manager.getCharter(id);
			charter.getResources();
			charter.getAdministrative();
			charter.getInsurance();
			charter.getCost();
			charter.getPricing();
		}
	}

	public String assignableArray;
	public String assignments() throws Exception {
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Contract", "charters.action"),
				new Breadcrumb(charter.getCode())
		);
		tab = "assignments";
		prepareTabs();

		List<CrewMember> crew = manager.getAllCrew();
		List<Aircraft> aircraft = manager.getAllAircraft().aircraftList;

		StringBuilder buf = new StringBuilder();
		buf.append('[');
		boolean first = true;
		for (Aircraft a: aircraft) {
			if (first) {
				first = false;
			} else {
				buf.append(',');
			}
			buf.append('\'');
			buf.append(a.getRef());
			buf.append('\'');
		}
		for (CrewMember cm: crew) {
			if (first) {
				first = false;
			} else {
				buf.append(',');
			}
			buf.append('\'');
			buf.append(cm.getPersonal().getFirstName()+" "+cm.getPersonal().getLastName());
			buf.append('\'');
		}
		buf.append(']');
		assignableArray = buf.toString();

		return "assignments";
	}

	private void prepareTabs() {
		String idStr = "";
		if (id != null) {
			idStr=""+id;
		}
		List<Tab> tabList = new ArrayList<Tab>();
		
	    
		if(user.hasRead("contractAdmin")){
		Tab administrative = new Tab("Administrative", "charter.action?id="+idStr, tab.equals("administrative"));
		tabList.add(administrative);
		}
		if(user.hasRead("contractResources")){
		Tab resources = new Tab("Resources", "charter.action?tab=resources&id="+idStr, tab.equals("resources"));
		tabList.add(resources);
		}
		if(user.hasRead("contractPrice")){
		Tab pricing = new Tab("Pricing", "charter.action?tab=pricing&id="+idStr, tab.equals("pricing"));
		tabList.add(pricing);
		}
		if(user.hasRead("contractIns")){
		Tab insurance = new Tab("Insurance", "charter.action?tab=insurance&id="+idStr, tab.equals("insurance"));
		tabList.add(insurance);
		}
		if(user.hasRead("contractCost")){
		Tab cost = new Tab("Cost", "charter.action?tab=cost&id="+idStr, tab.equals("cost"));
		tabList.add(cost);
		}
		if(user.hasRead("contractHours")){
		Tab hours = new Tab("Hours", "charter!hours.action?id="+idStr, tab.equals("hours"));
		tabList.add(hours);
		}
		if(user.hasWrite("contractOnContract")){
		Tab contract = new Tab("On Contract", "charter!contract.action?id="+idStr, tab.equals("contract"));
		tabList.add(contract);
		}
		if(user.hasRead("contractDoc")){
		Tab docs = new Tab("Documents", "charter!docs.action?id="+idStr, tab.equals("docs"));
		tabList.add(docs);
		}
		if(user.hasRead("contractAssign")){
		Tab assignments = new Tab("Assignments", "charter!assignments.action?id="+idStr, tab.equals("assignments"));
		tabList.add(assignments);
		}
		
		tableTabs = new Tab[tabList.size()];
		int count = 0;
		for(Tab tab : tabList){
			tableTabs[count] = tab;
			count++;
		}		
		
	}

	public void setUser(User arg0) {
		this.user = arg0;
	}

	public String nameOrReg;
	public Date startDate;
	public Date endDate;

	public String errorMessage;

	@Inject
	private SchedulingManager schedulingManager;
	public String addAllocation() throws Exception {
		Assignable ass = manager.getAssignableByNameOrReg(nameOrReg);
		Allocation all = new Allocation();
		all.setAssignableId(ass.id);
		all.setAssignableType(ass.type);
		all.setAssignmentId(charter.getId());
		all.setAssignmentType("Charter");
		all.setFrom(startDate);
		all.setTo(endDate);
		try {
			schedulingManager.saveAllocation(all);
		} catch (CausesCollisionsException e) {
			errorMessage = "Allocation cannot be made due to conflicts.";
		}
		return assignments();
	}
}
