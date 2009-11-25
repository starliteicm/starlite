package com.itao.starlite.ui.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts2.ServletActionContext;
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
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class AircraftAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3344297237658297955L;
	public String tableHtml;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Aircraft")};
	
	public Tab[] tableTabs;

	public String tab = "aircraft";
	
	private Tab staticTab = new Tab("Static", "aircraft.action", false);
	
	public User user;
	
	//public Tab[] tableTabs = new Tab[] {staticTab, dynamicTab, documentsTab};
	
	public String saveDisabled = "disabled=\"disabled\"";
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public AircraftAction() {
		super();
	}
	
	private static Date parseDate(String str) throws ParseException {
		return df.parse(str);
	}
	
	@Inject
	private StarliteCoreManager manager;
	@Override
	public String execute() throws Exception {
		
		prepareTabs();
		
		List<Aircraft> aircraft = manager.getAllAircraft().aircraftList;
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("aircraftTable", ServletActionContext.getRequest());
		
		//tableFacade.setEditable(true);
		
		tableFacade.setColumnProperties("ref", "ownership", "type", "model", "licence", "aircraftMaintenanceOrganisation", "livery");
		
		tableFacade.setItems(aircraft);
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		
		Column refCol = table.getRow().getColumn("ref");
		refCol.setTitle("Reg");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "ref", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("aircraftInfo.action?id="+id).quote().close();
				html.append(value);
				html.aEnd();
				return html.toString();
			}
			
		});
		
//		Column corCol = table.getRow().getColumn("certificateOfRegistration");
//		corCol.setTitle("CoR");
//		
//		Column aocCol = table.getRow().getColumn("aircraftOperatingCertificate");
//		aocCol.setTitle("AoC");
//		
//		Column coaCol = table.getRow().getColumn("certificateOfAirworthiness");
//		coaCol.setTitle("CoA");
		
		Column amoCol = table.getRow().getColumn("aircraftMaintenanceOrganisation");
		amoCol.setTitle("AMO");
		
//		Column coaExpiry = table.getRow().getColumn("coaExpiryDate");
//		coaExpiry.setTitle("Expiry Date");
//		coaExpiry.getCellRenderer().setCellEditor(new DateOrBasicEditor());
		
		//((HtmlTable)table).getTableRenderer().setWidth("600px");
		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
		staticTab.setCurrent(true);
		handleWorksheet(tableFacade);
		return SUCCESS;
	}
	
	private void handleWorksheet(TableFacade tableFacade) {
		Worksheet worksheet = tableFacade.getWorksheet();
		
		if (worksheet == null)
			return;
		
		if (worksheet.hasChanges()) {
			saveDisabled = "";
		}
		
		if (!worksheet.isSaving() || !worksheet.hasChanges()) {
		    return;
		}
		
		List<String> uniquePropertyValues = WorksheetUtils.getUniquePropertyValues(worksheet);
		final Map<String, Aircraft> aircrafts = manager.getAircraftsById(uniquePropertyValues);
		
		worksheet.processRows(new WorksheetCallbackHandler() {

			public void process(WorksheetRow worksheetRow) {
				Collection<WorksheetColumn> columns = worksheetRow.getColumns();
				Aircraft a = aircrafts.get(worksheetRow.getUniqueProperty().getValue());
				for (WorksheetColumn worksheetColumn: columns) {
					Object changedValue = worksheetColumn.getChangedValue();
					String property = worksheetColumn.getProperty();
					
					boolean error = false;
					if (property.equals("coaExpiryDate")) {
						try {
							changedValue = AircraftAction.parseDate((String)changedValue);
						} catch (ParseException e) {
							error = true;
						}
					}
					try {
						if (!error)
							PropertyUtils.setProperty(a, property, changedValue);
					} catch (Exception e) {}
				}
				manager.saveAircraft(a);
			}
			
		});
	}
	
	public String types(){
		//XXX working on
		current="aircraft type";
		tab="aircraft type";
		prepareTabs();
		List<AircraftType> aircraftTypes = manager.getAircraftTypes();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("aircraftTable", ServletActionContext.getRequest());
		tableFacade.setColumnProperties("name","description","id");
		tableFacade.setItems(aircraftTypes);
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		Column refCol1 = table.getRow().getColumn("name");
		refCol1.setTitle("Aircraft Type");
		
		Column refCol2 = table.getRow().getColumn("description");
		refCol2.setTitle("Type Description");
		
		Column refCol = table.getRow().getColumn("id");
		refCol.setTitle("Edit");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("aircraft!addType.action?typeId="+value).quote().close();
				html.append("Edit");
				html.aEnd();
				return html.toString();
			}
			
		});

		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
		return "types";
	}

	public AircraftType aircraftType;
	public Integer typeId;
	public String typeName;
	public String typeDescription;
	public String addType(){
		if (!prepareAdd) {
			if (typeName == null || typeName.trim().equals("")) {
				errorMessage = "Aircraft Type Name must be entered";
				return ERROR;
			}
			try {
				AircraftType a = new AircraftType();
				a.setId(typeId);
				a.setName(typeName);
				a.setDescription(typeDescription);
				manager.saveAircraftType(a);
				return types();
			} catch (Throwable t) {
				errorMessage = "An unexpected error occured. Aircraft Type could not be added";
				return ERROR;
			}
		}
		else {
			if(typeId != null){
				aircraftType = manager.getAircraftType(typeId);
			}
		}
		return "addType";
	}
	
	public String removeType(){
		return "redirect-types";
	}
	
	public boolean prepareAdd = true;
	public String aircraftReg;
	public String errorMessage;
	public Aircraft createdAircraft;
	@Permissions("UserAdmin")
	public String addAircraft() {
		if (!prepareAdd) {
			if (aircraftReg == null || aircraftReg.trim().equals("")) {
				errorMessage = "Aircraft Registration must be entered";
				return ERROR;
			}
			try {
				Aircraft a = new Aircraft();
				a.setRef(aircraftReg);
				manager.saveAircraft(a);
				createdAircraft = a;
				return "added";
			} catch (Throwable t) {
				errorMessage = "An unexpected error occured. Aircraft could not be added";
				return ERROR;
			}
		}
		return "add";
	}

	public void setUser(User arg0) {
		user = arg0;
	}
	
	private void prepareTabs() {

		Tab aircraftTab = new Tab("Aircraft", "aircraft.action", tab.equals("aircraft"));
		Tab aircraftTypeTab = new Tab("Aircraft Type", "aircraft!types.action", tab.equals("aircraft type"));
		
		if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {aircraftTab, aircraftTypeTab};
		
	}
}
