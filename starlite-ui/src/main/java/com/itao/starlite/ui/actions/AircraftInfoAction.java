package com.itao.starlite.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.limit.ExportType;
import org.jmesa.limit.Limit;
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
import com.itao.starlite.model.Component;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@Permissions("ManagerView")
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="aircraftInfo.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
})
public class AircraftInfoAction extends ActionSupport implements Preparable, UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6227836989478734568L;
	@Inject
	private StarliteCoreManager manager;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs;
	public String notificationMessage;
	public User user;
	
	public String id;
	public Aircraft aircraft;
	public Tab[] tableTabs;
	public String tab = "information";

	public List<Component> components;
	
	
	
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
		notificationMessage ="Aircraft Saved";
		return "redirect";
	}
	
	
	public String components() throws Exception{
		tab = "components";
		tableHtml = "";
		prepare();
		prepareTabs();
		components = manager.getComponents(aircraft.getRef().replaceAll("-", ""));
		TableFacade tableFacade = createComponentTable();

		Limit limit = tableFacade.getLimit();
		if (limit.isExported()) {
			tableFacade.render();
			return null;
		} 
		tableFacade.setView(new NavTableView());
		tableHtml = tableFacade.render();
		return "components";
	}
	
	public String config() throws Exception{
		tab = "config";
		prepareTabs();
		return "config";
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
		
		
		List<Tab> tabList = new ArrayList<Tab>();
		if(user.hasRead("aircraftInfo")){
		  Tab information = new Tab("Information", "aircraftInfo.action?id="+idStr, tab.equals("information"));
		  tabList.add(information);
		}
		if(user.hasRead("aircraftHours")){
		  Tab hours = new Tab("Hours", "aircraftInfo!hours.action?id="+idStr, tab.equals("hours"));
		  tabList.add(hours);
		}
		if(user.hasRead("aircraftHours")){
			  Tab hours = new Tab("Config", "aircraftInfo!config.action?id="+idStr, tab.equals("config"));
			  tabList.add(hours);
		}
		if(user.hasRead("aircraftHours")){
			  Tab hours = new Tab("Components", "aircraftInfo!components.action?id="+idStr, tab.equals("components"));
			  tabList.add(hours);
		}
		if(user.hasRead("aircraftDoc")){
		  Tab docs = new Tab("Documents", "aircraftInfo!documents.action?id="+idStr, tab.equals("documents"));
		  tabList.add(docs);
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
	
	public TableFacade createComponentTable(){    			

		TableFacade tableFacade = TableFacadeFactory.createTableFacade("componentTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("type","name", "number", "serial", "qty", "timeBetweenOverhaul","hoursRun","hoursOnInstall","installDate","lifeExpiresHours","currentHours","remainingHours","expiryDate","totalDays","remainingDays","remainingPercent");		
		tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);

		tableFacade.setItems(components);
		tableFacade.setMaxRows(15);

		Limit limit = tableFacade.getLimit();


		Table table = tableFacade.getTable();
		table.setCaption("Components");
		table.getRow().setUniqueProperty("id");

		Column type = table.getRow().getColumn("type");
		if (!limit.isExported()) {
			type.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {
					if((""+((Component) item).getType()).indexOf("Class") >= 0){
						return (""+((Component) item).getType()).substring(5);
					}
					return ((Component) item).getType();
				}
			});
		}

		Column tbo = table.getRow().getColumn("timeBetweenOverhaul");
		tbo.setTitle("TBO");
		if (!limit.isExported()) {
			tbo.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {
					if(((Component) item).getTimeBetweenOverhaul() == null){
						return "";
					}
					return "<div style='text-align:right'>"+((Component) item).getTimeBetweenOverhaul()+"</div>";
				}
			});
		}
		else{			
			tbo.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getTimeBetweenOverhaul() ;
				}
			});
		}

		Column hoursRunCol = table.getRow().getColumn("hoursRun");
		if (!limit.isExported()) {
			hoursRunCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {
					if(((Component) item).getHoursRun() == null){
						return "";
					}
					return "<div style='text-align:right'>"+((Component) item).getHoursRun()+"</div>";
				}
			});
		}
		else{			
			hoursRunCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getHoursRun() ;
				}
			});
		}

		Column hoursInstallCol = table.getRow().getColumn("hoursOnInstall");
		if (!limit.isExported()) {
			hoursInstallCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {
					if(((Component) item).getHoursOnInstall() == null){
						return "";
					}
					return "<div style='text-align:right'>"+((Component) item).getHoursOnInstall()+"</div>";
				}
			});
		}
		else{			
			hoursInstallCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getHoursOnInstall() ;
				}
			});
		}

		Column expiresCol = table.getRow().getColumn("lifeExpiresHours");
		if (!limit.isExported()) {
			expiresCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {
					if(((Component) item).getLifeExpiresHours() == null){
						return "";
					}
					return "<div style='text-align:right'>"+((Component) item).getLifeExpiresHours()+"</div>";
				}
			});
		}
		else{			
			expiresCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getLifeExpiresHours() ;
				}
			});
		}


		Column currentCol = table.getRow().getColumn("currentHours");
		if (!limit.isExported()) {
			currentCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {
					return "<div style='text-align:right'>"+((Component) item).getCurrentHoursStr()+"</div>";
				}
			});
		}
		else{			
			currentCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) new Double( ((Component) item).getCurrentHoursStr() );
				}
			});
		}


		Column remainingCol = table.getRow().getColumn("remainingHours");
		if (!limit.isExported()) {
			remainingCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {
					return "<div style='text-align:right'>"+((Component) item).getRemainingHoursStr()+"</div>";
				}
			});
		}
		else{			
			remainingCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) new Double( ((Component) item).getRemainingHoursStr() );
				}
			});
		}




		Column percentCol = table.getRow().getColumn("remainingPercent");
		percentCol.setTitle("Remaining %");
		if (!limit.isExported()) {
			percentCol.getCellRenderer().setCellEditor(new CellEditor() {

				public Object getValue(Object item, String property, int rowCount) {
					Object id = new BasicCellEditor().getValue(item, "id", rowCount);
					Object value = new BasicCellEditor().getValue(item, property, rowCount);
					HtmlBuilder html = new HtmlBuilder();

					try{
						long valueLong = (Long) value;
						if(valueLong >= 25){
							html.div().style("text-align:center;background-color:#99FF99;font-weight:bold;").styleEnd();
						}
						else if(valueLong >= 10){
							html.div().style("text-align:center;background-color:#FFFF99;font-weight:bold;").styleEnd();
						}
						else{
							html.div().style("text-align:center;background-color:#FF9999;font-weight:bold;").styleEnd();	
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}

					if(value != null){
						html.append(value+" %");
						html.divEnd();
					}

					return html.toString();
				}

			});
		}

		Column serialCol = table.getRow().getColumn("serial");
		serialCol.setTitle("Serial No.");

		Column refCol = table.getRow().getColumn("number");
		refCol.setTitle("Part No.");
		if (!limit.isExported()) {
			refCol.getCellRenderer().setCellEditor(new CellEditor() {

				public Object getValue(Object item, String property, int rowCount) {
					Object id = new BasicCellEditor().getValue(item, "id", rowCount);
					Object value = new BasicCellEditor().getValue(item, property, rowCount);
					if(value == null){value="(blank)";}
					if("".equals(value)){value="(blank)";}
					System.out.println(value);
					HtmlBuilder html = new HtmlBuilder();
					html.a().href().quote().append("component!edit.action?id="+id).quote().close();
					html.append(value);
					html.aEnd();
					return html.toString();
				}

			});
		}

		return tableFacade;


	}

	
	
}
