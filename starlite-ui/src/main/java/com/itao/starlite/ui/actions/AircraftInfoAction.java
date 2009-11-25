package com.itao.starlite.ui.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
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
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.docs.manager.BookmarkManager;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.docs.model.Tag;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.CombinedActuals;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@Permissions("ManagerView")
public class AircraftInfoAction extends ActionSupport implements Preparable, UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6227836989478734568L;
	@Inject
	private StarliteCoreManager manager;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs;
	
	public String id;
	public Aircraft aircraft;
	public Tab[] tableTabs;
	public String tab = "information";
	
	@Inject
	private DocumentManager docManager;
	
	@Inject
	private BookmarkManager bookmarkManager;
	
	@Override
	public String execute() throws Exception {
		breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Aircraft", "aircraft.action"),
			new Breadcrumb(aircraft.getRef())
		);
		prepareTabs();
		if (tab.equals("information"))
			return SUCCESS;

		return tab;
	}
	public void prepare() throws Exception {
		if (id == null) {
			aircraft = new Aircraft();
			aircraftTypes = manager.getAircraftTypes();
		} else {
			aircraft = manager.getAircraftByReg(id);
			aircraftTypes = manager.getAircraftTypes();
		}
	}
	
	public String save() throws Exception {
		manager.saveAircraft(aircraft);
		tab = "information";
		return execute();
	}
	
	public String tableHtml;
	public List<AircraftType> aircraftTypes;
	public String hours() throws Exception {
		DateMidnight dm = new DateMidnight();
		Integer endMonth = dm.getMonthOfYear();
		Integer endYear = dm.getYear();
		
		Integer startYear = endYear-1;
		Integer startMonth = endMonth+1;
		if (startMonth > 12) {
			startMonth = 1;
			startYear = endYear;
		}
		
		Aircraft a = manager.getAircraftByReg(id);
		
		
		List<CombinedActuals> totals = manager.getTotalActualsByAircraft(a.getId(), startMonth, startYear, endMonth, endYear);
		
		totals.add(new CombinedActuals("Totals", totals));
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("crewTable", ServletActionContext.getRequest());

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
				html.a().href().quote().append("monthlyHours.action?"+params).quote().close();
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
			new Breadcrumb("Aircraft", "aircraft.action"),
			new Breadcrumb(a.getRef() + " Hours")
		);
		tab = "hours";
		prepareTabs();
		return "hours";
	}
	
	public List<Document> docs;
	public String errorMessage;
	public String tagArray;
	private User user;
	
	public String documents() throws Exception {
		if (id == null) {
			return ERROR;
		}
		tab = "documents";
		docs = new LinkedList<Document>();
		Folder f = docManager.getFolderByPath("/crew/"+id, user);
		if (f != null) {
			if (f.canRead(user))
				docs.addAll(f.getDocs());
			else {
				errorMessage = "Insufficient Privilages";
			}
		}
		Collections.sort(docs, new Comparator<Document>() {

			public int compare(Document o1, Document o2) {
				return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}
			
		});
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Aircraft", "aircraft.action"),
				new Breadcrumb(aircraft.getRef())
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
		return "docs";
	}
	
	private void prepareTabs() {
		String idStr = "";
		if (id != null) {
			idStr=id;
		}
		Tab information = new Tab("Information", "aircraftInfo.action?id="+idStr, tab.equals("information"));
		Tab hours = new Tab("Hours", "aircraftInfo!hours.action?id="+idStr, tab.equals("hours"));
		Tab docs = new Tab("Documents", "aircraftInfo!documents.action?id="+idStr, tab.equals("documents"));

		tableTabs = new Tab[] {information, hours, docs};
	}
	public void setUser(User arg0) {
		this.user = arg0;
	}
}
