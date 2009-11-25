package com.itao.starlite.ui.actions;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts2.ServletActionContext;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.NumberCellEditor;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.worksheet.Worksheet;
import org.jmesa.worksheet.WorksheetCallbackHandler;
import org.jmesa.worksheet.WorksheetColumn;
import org.jmesa.worksheet.WorksheetRow;
import org.jmesa.worksheet.WorksheetUtils;
import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.manager.ApprovalsManager;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.ApprovalGroup;
import com.itao.starlite.model.ApprovalStatus;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CharterList;
import com.itao.starlite.model.CombinedActuals;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.jmesa.DateOrBasicEditor;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SelectWorksheetEditor;
import com.itao.starlite.ui.jmesa.UneditableWorksheetEditor;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.wideplay.warp.persist.Transactional;

@Permissions("ManagerView")
public class MonthlyHoursAction extends ActionSupport implements Preparable, UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8066166580837161304L;
	public Integer month;
	public Integer year;
	public Integer aircraftId;
	
	public Aircraft aircraft;
	
	private String monthName;
	
	public String tableHtml;
	public String current="aircraft";
	
	public Breadcrumb[] breadcrumbs;
	
	public String saveDisabled = "disabled=\"disabled\"";
		
	@Inject
	private StarliteCoreManager manager;
	private boolean changed = false;
	private boolean worksheetHandled;
	
	@Inject
	private ApprovalsManager approvalsManager;
	
	public ApprovalGroup approvalGroup;
	
	public User user;
	
	@Override
	public String execute() throws Exception {
		aircraft = manager.getAircraft(aircraftId);
		CharterList charters = manager.getAllCharters();
		
		String[] charterCodes = new String[charters.charterList.size()];

		for (int i=0; i< charters.charterList.size(); i++) {
			Charter c = charters.charterList.get(i);
			charterCodes[i] = c.getCode();
		}
		
		Arrays.sort(charterCodes);
		
		List<Actuals> actuals = manager.getActualsByAircraftByMonth(aircraftId, month, year);
		
		List<Object> items = new LinkedList<Object>();
		
		int last = 0;
		DateMidnight dm = new DateMidnight(year, month, 1);
		int daysInMonth = dm.plusMonths(1).minusDays(1).getDayOfMonth();
		DateMidnight current = dm;
		for (Actuals a: actuals) {
			int dayOfMonth = new DateMidnight(a.getDate()).getDayOfMonth();
			for (int i=last+1; i<dayOfMonth; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();
				current = new DateMidnight(current.getYear(), current.getMonthOfYear(), i);
				m.put("date", current.toDate());
				m.put("id", -1*i);
				items.add(m);
			}
			items.add(a);
			last = dayOfMonth;
		}
		
		for (int i=last+1; i<=daysInMonth; i++) {			
			HashMap<String, Object> m = new HashMap<String, Object>();
			current = new DateMidnight(current.getYear(), current.getMonthOfYear(), i);
			m.put("date", current.toDate());
			m.put("id", -1*i);
			items.add(m);
		}
		
		CombinedActuals ca = new CombinedActuals("Totals", actuals);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("date", "Totals");
		hm.put("capt", ca.getCapt());
		hm.put("aframe", ca.getAframe());
		hm.put("landings", ca.getLandings());
		hm.put("pax", ca.getPax());
		items.add(hm);
		
		monthName = dm.toString("MMMM");
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("monthlyHours"+aircraft.getRef()+monthName+year+"Table", ServletActionContext.getRequest());

		tableFacade.setEditable(approvalGroup == null || approvalGroup.getApprovalStatus().equals(ApprovalStatus.OPEN_FOR_EDITING));
		
		tableFacade.setColumnProperties("date", "charter.code", "invoiceNumber", "capt", "aframe", "landings", "pax");
		tableFacade.setItems(items);
		tableFacade.setMaxRows(items.size());
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		Column refCol = table.getRow().getColumn("date");
		
		((HtmlColumn)refCol).getCellRenderer().setWorksheetEditor(new UneditableWorksheetEditor());
		((HtmlColumn)refCol).getCellRenderer().getWorksheetEditor().setCellEditor(new DateOrBasicEditor());
		((HtmlColumn)refCol).getCellRenderer().setCellEditor(new DateOrBasicEditor());
		
		Column invoiceCol = table.getRow().getColumn("invoiceNumber");
		invoiceCol.setTitle("Invoice");
		
		HtmlColumn clientCol = (HtmlColumn) table.getRow().getColumn("charter.code");
		clientCol.setTitle("Charter");
		clientCol.getCellRenderer().setWorksheetEditor(new SelectWorksheetEditor(charterCodes));
		clientCol.setWidth("140px");
		
		Column captCol = table.getRow().getColumn("capt");
		captCol.getCellRenderer().setCellEditor(new NumberCellEditor("###,##0.0"));
		
		Column aframeCol = table.getRow().getColumn("aframe");
		aframeCol.getCellRenderer().setCellEditor(new NumberCellEditor("###,##0.0"));
		((HtmlTable)table).getTableRenderer().setWidth("600px");
		
		//tableFacade.setToolbar(null);
		PlainTableView ptv = new PlainTableView();
		//ptv.setColsToTotal("capt", "aframe", "landings", "pax");
		tableFacade.setView(ptv);
		if (!worksheetHandled) {
			changed = handleWorksheet(tableFacade);
			worksheetHandled = true;
			if (changed)
				return execute();
		}
		
		tableHtml = tableFacade.render();
		
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Aircraft", "aircraft.action"),
//				new Breadcrumb(aircraft.getRef(), "aircraftInfo.action?id="+aircraftId),
				new Breadcrumb(aircraft.getRef() + " Hours", "aircraftInfo!hours.action?id="+aircraft.getRef()),
				new Breadcrumb(monthName+", " +year)
			);
		return SUCCESS;
	}

	public String getMonthName() {
		return monthName;
	}
	
	public String getAircraftCode() {
		return aircraft.getRef();
	}
	
	public String getPreviousLink() {
		int newMonth = month;
		int newYear = year;
		
		if (newMonth > 1)
			newMonth--;
		else {
			newMonth = 12;
			newYear--;
		}
		return "monthlyHours.action?aircraftId="+aircraftId+"&year="+newYear+"&month="+newMonth;
	}
	
	public String getNextLink() {
		int newMonth = month;
		int newYear = year;
		
		if (newMonth < 12)
			newMonth++;
		else {
			newMonth = 1;
			newYear++;
		}
		return "monthlyHours.action?aircraftId="+aircraftId+"&year="+newYear+"&month="+newMonth;
	}
	
	/**
	 * This method is marked as Transactional because quite a lot of logic which must complete in one unit occurs here.
	 * This is not good practice as there is lots of business logic in the Presentation layer. But it is a simple work-around for now
	 * Eventually this could be refactored so that all the saving happens in the manager layer.
	 * 
	 * This will rollback on any unchecked exception, such as the RuntimeException thrown by {@link StarliteCoreManager.saveActuals} 
	 * if the actuals for that month have been approved.
	 * @param tableFacade
	 * @return
	 */
	@Transactional
	public boolean handleWorksheet(TableFacade tableFacade) {
		try {
			Worksheet worksheet = tableFacade.getWorksheet();
			
			if (worksheet == null)
				return false;
			
			if (worksheet.hasChanges()) {
				saveDisabled = "";
			}
			
			if (!worksheet.isSaving() || !worksheet.hasChanges()) {
			    return false;
			}
			
			if (approvalGroup == null)
				approvalGroup = new ApprovalGroup();
			
			List<String> uniquePropertyValues = WorksheetUtils.getUniquePropertyValues(worksheet);
			final Map<String, Actuals> aircrafts = manager.getActualsByIds(uniquePropertyValues);
			
			worksheet.processRows(new WorksheetCallbackHandler() {
	
				public void process(WorksheetRow worksheetRow) {
					Collection<WorksheetColumn> columns = worksheetRow.getColumns();
					Actuals a = null;
					if (worksheetRow.getUniqueProperty().getValue().startsWith("-")) {
						a = new Actuals();
						Integer day = Integer.parseInt(worksheetRow.getUniqueProperty().getValue())*-1;
						DateMidnight dm = new DateMidnight(year, month, day);
						a.setDate(dm.toDate());
						Aircraft aircraft = manager.getAircraft(aircraftId);
						a.setAircraft(aircraft);
					} else {
						a = aircrafts.get(worksheetRow.getUniqueProperty().getValue());
					}
					if (a == null) {
						return;
					}
					for (WorksheetColumn worksheetColumn: columns) {
						boolean change = false;
						String changedValue = worksheetColumn.getChangedValue();
						String property = worksheetColumn.getProperty();
						
						Object val = changedValue;
						if (property.equals("capt") || property.equals("aframe")) {
							change = true;
							try {
								val = Double.parseDouble(changedValue);
							} catch (NumberFormatException e){
								e.printStackTrace();
								val = null;
							}
						} else if (property.equals("landings") || property.equals("pax")) {
							change = true;
							try {
								val = new Double(Math.floor(Double.parseDouble(changedValue))).intValue();
							} catch (Exception e) {
								e.printStackTrace();
								val = null;
							}
						} else if (property.equals("charter.code")) {
							change = true;
							val = manager.getCharterByCode(changedValue);
							property = "charter";
						} else if (property.equals("invoiceNumber")){
							change = true;
							val = changedValue;
							if (val.equals(""))
								val = null;
						}
						
						try {
							if (change)
								PropertyUtils.setProperty(a, property, val);
						} catch (Exception e) {}
					}
					if (a.getCharter() == null && a.getCapt() == null && a.getAframe() == null && a.getLandings() == null & a.getPax() == null) {
						manager.deleteActuals(a);
					} else {
						a.setApprovalGroup(approvalGroup);
						manager.saveActuals(a);
					}
					setSaveDisabled("disabled=\"disabled\"");
				}
			});
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
	
	private void setSaveDisabled(String ds) {
		this.saveDisabled = ds;
	}
	
	public String notificationMessage;
	
	@Permissions("Approve")
	public String review() throws Exception {
		String approvalKey = approvalsManager.review(approvalGroup.getId());
		ServletActionContext.getRequest().getSession().setAttribute("approvalKey-"+approvalGroup.getId(), approvalKey);
		prepare();
		notificationMessage = "Actuals locked for 2 minutes";
		return execute();
	}
	
	@Permissions("Approve")
	public String approve() throws Exception {
		String approvalKey = (String) ServletActionContext.getRequest().getSession().getAttribute("approvalKey-"+approvalGroup.getId());
		approvalsManager.approve(approvalGroup.getId(), approvalKey);
		prepare();
		notificationMessage = "Actuals have been approved";
		return execute();
	}
	
	@Permissions("Approve")
	public String open() throws Exception {
		String approvalKey = (String) ServletActionContext.getRequest().getSession().getAttribute("approvalKey-"+approvalGroup.getId());
		approvalsManager.open(approvalGroup.getId(), approvalKey);
		prepare();
		notificationMessage = "Actuals have been opened for editing";
		return execute();
	}

	public void prepare() throws Exception {
		approvalGroup = manager.getActualsApprovalGroup(aircraftId, year, month);
	}

	public void setUser(User user) {
		this.user = user;
	}
}
