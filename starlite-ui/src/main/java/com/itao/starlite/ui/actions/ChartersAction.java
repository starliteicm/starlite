package com.itao.starlite.ui.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.AbstractCellEditor;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlComponentFactory;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CharterList;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.DocumentCellEditor;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class ChartersAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5796650497911723236L;
	public String tableHtml;
	public String current="charters";
	
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Contracts")};
	
	public User user;
	
	private Tab staticTab = new Tab("Static", "charters.action", false);
	private Tab dynamicTab = new Tab("Dynamic", "charters!dynamic.action", false);
	private Tab documentsTab = new Tab("Documents", "charters!documents.action", false);
	
	//public Tab[] tableTabs = new Tab[] {staticTab, dynamicTab, documentsTab};
	
	@Inject
	private StarliteCoreManager manager;
	@Override
	public String execute() throws Exception {
		List<Charter> charters = manager.getAllCharters().charterList;
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("chartersTable", ServletActionContext.getRequest());

		tableFacade.setColumnProperties("code", "client", "formattedStartDate", "formattedEndDate");
		tableFacade.setItems(charters);
		tableFacade.setMaxRows(300);
		
		Table table = tableFacade.getTable();
		Column code = table.getRow().getColumn("code");
		code.getCellRenderer().setCellEditor(new AbstractCellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Object val = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("charter.action?id="+id).quote().close();
				html.append(val.toString());
				html.aEnd();
				return html.toString();
			}
			
		});
		
		Column from = table.getRow().getColumn("formattedStartDate");
		from.setTitle("From");
		Column to = table.getRow().getColumn("formattedEndDate");
		to.setTitle("To");
		
		((HtmlTable)table).getTableRenderer().setWidth("600px");
		tableFacade.setView(new PlainTableView().showFilters());
		tableHtml = tableFacade.render();
		staticTab.setCurrent(true);
		return SUCCESS;
	}
	
	public String dynamic() throws Exception {
		List<Charter> charters = manager.getAllCharters().charterList;
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("chartersDynamicTable", ServletActionContext.getRequest());

		tableFacade.setColumnProperties("code", "hours");
		tableFacade.setItems(charters);
		
		Table table = tableFacade.getTable();
		
		Column hoursCol = table.getRow().getColumn("hours");
		hoursCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("charterMonthlyTotals.action?id="+id).quote().close();
				html.append("View");
				html.aEnd();
				return html.toString();
			}
			
		});
		
		((HtmlTable)table).getTableRenderer().setWidth("600px");
		tableFacade.setView(new PlainTableView().showFilters());
		tableHtml = tableFacade.render();
		dynamicTab.setCurrent(true);
		return SUCCESS;
	}
	
	public String documents() throws Exception {
		List<Charter> charters = manager.getAllCharters().charterList;
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("chartersDocumentsTable", ServletActionContext.getRequest());

		tableFacade.setColumnProperties("code", "contract");
		tableFacade.setItems(charters);
		
		Table table = tableFacade.getTable();
		
		Column contractCol = table.getRow().getColumn("contract");
		contractCol.getCellRenderer().setCellEditor(new DocumentCellEditor());
		
		((HtmlTable)table).getTableRenderer().setWidth("600px");
		tableFacade.setView(new PlainTableView().showFilters());
		tableHtml = tableFacade.render();
		documentsTab.setCurrent(true);
		return SUCCESS;
	}
	
	public boolean prepareAdd = true;
	public String charterCode;
	public String errorMessage;
	public Charter createdCharter;
	@Permissions("UserAdmin")
	public String addCharter() {
		if (!prepareAdd) {
			if (charterCode == null || charterCode.trim().equals("")) {
				errorMessage = "Contract code must be entered";
				return ERROR;
			}
			try {
				Charter c = new Charter();
				c.setCode(charterCode);
				manager.saveCharter(c);
				createdCharter = c;
				return "added";
			} catch (Throwable t) {
				errorMessage = "An unexpected error occured. Contract could not be added";
				return ERROR;
			}
		}
		return "add";
	}

	public void setUser(User arg0) {
		user = arg0;
	}
	
	public String html;
	public String aircraftHoursReport() {
		List<HashMap<String, Object>>report = new ArrayList<HashMap<String, Object>>();
		CharterList cl = manager.getAllCharters();
		
		List<Aircraft> aircraft = manager.getAllAircraft().aircraftList;
		for (Aircraft a: aircraft) {
			//create a row for each aircraft
			HashMap<String, Object> reportRow = new HashMap<String, Object>();
			//put aircraft reg in aircraft column
			reportRow.put("aircraft", a.getRef());
			//create columns for each charter
			for (Charter c: cl.charterList) {
				reportRow.put(c.getId().toString(), new Double(0));
			}
			
			List<Actuals> actualsList = manager.getActualsByAircraftByMonth(a.getId(), new Integer(2), new Integer(2008));
			for (Actuals actuals: actualsList) {
				Double total = (Double) reportRow.get(actuals.getCharter().getId().toString());
				double total_d = total.doubleValue();
				Double val = actuals.getCapt();
				double val_d = val.doubleValue();
				reportRow.put(actuals.getCharter().getId().toString(), new Double(total_d+val_d));
			}
			report.add(reportRow);
			
		}
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("hours", ServletActionContext.getRequest());
		tableFacade.setItems(report);

		HtmlComponentFactory factory = new HtmlComponentFactory(tableFacade.getWebContext(), tableFacade.getCoreContext());

		HtmlTable table = factory.createTable();

		HtmlRow row = factory.createRow();
		row.addColumn(factory.createColumn("aircraft"));
		for (Charter c: cl.charterList) {
			row.addColumn(factory.createColumn(c.getId().toString()));
		}
		
		row.getColumn("aircraft").setFilterable(false);
		for (Charter c: cl.charterList) {
			row.getColumn(c.getId().toString()).setFilterable(false);
			row.getColumn(c.getId().toString()).setTitle(c.getCode());
		}

		table.setRow(row); // be sure to set the row on the table

		((HtmlTable)table).getTableRenderer().setWidth("600px");
		
		tableFacade.setTable(table);
		tableFacade.setView(new PlainTableView().showFilters());
		
		html = tableFacade.render();
		
		return "aircraftHoursReport";
	}
	
}
