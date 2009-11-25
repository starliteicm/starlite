package com.itao.starlite.ui.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

	@Override
	public String execute() throws Exception {
		if (!user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		//crewMember = manager.getCrewMember(id);
		breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Charters", "charters.action"),
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
				new Breadcrumb("Charters", "charters.action"),
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
				new Breadcrumb("Charters", "charters.action"),
				new Breadcrumb("New Charter")
			);
		Tab administrativeTab = new Tab("Administrative", "#", true);
		tableTabs = new Tab[] {administrativeTab};
		return SUCCESS;
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
			new Breadcrumb("Charters", "charters.action"),
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
				new Breadcrumb("Charters", "charters.action"),
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
		Tab administrative = new Tab("Administrative", "charter.action?id="+idStr, tab.equals("administrative"));
		Tab resources = new Tab("Resources", "charter.action?tab=resources&id="+idStr, tab.equals("resources"));
		Tab pricing = new Tab("Pricing", "charter.action?tab=pricing&id="+idStr, tab.equals("pricing"));
		Tab insurance = new Tab("Insurance", "charter.action?tab=insurance&id="+idStr, tab.equals("insurance"));
		Tab cost = new Tab("Cost", "charter.action?tab=cost&id="+idStr, tab.equals("cost"));
		Tab hours = new Tab("Hours", "charter!hours.action?id="+idStr, tab.equals("hours"));
		Tab docs = new Tab("Documents", "charter!docs.action?id="+idStr, tab.equals("docs"));

		Tab assignments = new Tab("Assignments", "charter!assignments.action?id="+idStr, tab.equals("assignments"));
		tableTabs = new Tab[] {administrative, resources, pricing, insurance, cost, hours, docs, assignments};
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
