package com.itao.starlite.ui.actions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.NumberCellEditor;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlTable;
import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CombinedActuals;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.jmesa.DateOrBasicEditor;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@Permissions("ManagerView")
public class CharterMonthlyHoursAction extends ActionSupport implements Preparable, UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7101175187830238977L;
	public Integer month;
	public Integer year;
	public Integer charterId;
	
	public Charter charter;
	
	private String monthName;
	
	public String tableHtml;
	public String current="charters";
	public Double hoursInvoiced;
	
	public Breadcrumb[] breadcrumbs;
	
	public User user;
		
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		List<Actuals> actuals = manager.getActualsByCharterByMonth(charterId, month, year);
		hoursInvoiced = manager.getMonthlyInvoicedHours(charter, year, month);
		
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
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("monthlyHours"+monthName+year+"Table", ServletActionContext.getRequest());
		
		tableFacade.setColumnProperties("date", "aircraft.ref", "invoiceNumber", "capt", "aframe", "landings", "pax");
		tableFacade.setItems(items);
		tableFacade.setMaxRows(items.size());
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		Column refCol = table.getRow().getColumn("date");
		refCol.getCellRenderer().setCellEditor(new DateOrBasicEditor());
		
		Column invoiceCol = table.getRow().getColumn("invoiceNumber");
		invoiceCol.setTitle("Invoice");
		
		HtmlColumn clientCol = (HtmlColumn) table.getRow().getColumn("aircraft.ref");
		clientCol.setTitle("Aircraft");
		
		Column captCol = table.getRow().getColumn("capt");
		captCol.getCellRenderer().setCellEditor(new NumberCellEditor("###,##0.0"));
		
		Column aframeCol = table.getRow().getColumn("aframe");
		aframeCol.getCellRenderer().setCellEditor(new NumberCellEditor("###,##0.0"));
		((HtmlTable)table).getTableRenderer().setWidth("600px");
		
		//tableFacade.setToolbar(null);
		PlainTableView ptv = new PlainTableView();
		//ptv.setColsToTotal("capt", "aframe", "landings", "pax");
		tableFacade.setView(ptv);
		
		tableHtml = tableFacade.render();
		
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Charters", "charters.action"),
				new Breadcrumb(charter.getCode(), "charter!hours.action?id="+charterId),
				new Breadcrumb(monthName+", " +year)
			);
		return SUCCESS;
	}
	
	@Permissions("InvoiceHours")
	public String saveHoursInvoiced() throws Exception {
		manager.setMonthlyInvoicedHours(charter, year, month, hoursInvoiced);
		return execute();
	}

	public String getMonthName() {
		return monthName;
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
		return "charterMonthlyHours.action?charterId="+charterId+"&year="+newYear+"&month="+newMonth;
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
		return "charterMonthlyHours.action?charterId="+charterId+"&year="+newYear+"&month="+newMonth;
	}

	public void prepare() throws Exception {
		if (charterId != null)
			charter = manager.getCharter(charterId);
	}

	public void setUser(User user) {
		this.user = user;
	}
}
