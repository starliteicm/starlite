package com.itao.starlite.ui.actions;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jmesa.core.filter.MatcherKey;
import org.jmesa.core.filter.StringFilterMatcher;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.html.component.HtmlTable;

import com.google.inject.Inject;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.DocumentCellEditor;
import com.itao.starlite.ui.jmesa.ExpirableDateEditor;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class OldCrewAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3660113865892471422L;
	public String tableHtml;
	public String current="crew";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Crew")};
	
	private Tab personalTab = new Tab("Personal", "crew.action", false);
	private Tab roleTab = new Tab("Professional", "crew!role.action", false);
	private Tab flightAndDutyTab = new Tab("F & D", "crew!flightDuty.action", false);
	private Tab documentsTab = new Tab("Documents", "crew!documents.action", false);
	
	public Tab[] tableTabs = new Tab[] {personalTab, roleTab, flightAndDutyTab, documentsTab};
	
	@Inject
	private StarliteCoreManager manager;
	@Override
	public String execute() throws Exception {
		List<CrewMember> crew = manager.getAllCrew();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("crewTable", ServletActionContext.getRequest());
		tableFacade.setEditable(false);
		tableFacade.setColumnProperties("lastName", "firstName", "dateOfBirth", "address", "contactNumber", "email", "nationality", "passportNumber", "passportExpiryDate", "nextOfKin", "nextOfKinContactNumber");
		tableFacade.setItems(crew);
		
		tableFacade.addFilterMatcher(new MatcherKey(Object.class), new StringFilterMatcher());
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
//		HtmlColumn refCol = (HtmlColumn) table.getRow().getColumn("id");
//		refCol.getCellRenderer().setCellEditor(new CellEditor() {
//
//			public Object getValue(Object item, String property, int rowCount) {
//				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
//				HtmlBuilder html = new HtmlBuilder();
//				html.a().href().quote().append("crewMember.action?id="+id).quote().close();
//				html.append(id);
//				html.aEnd();
//				return html.toString();
//			}
//			
//		});
		
		Column passportExpiryDateCol = table.getRow().getColumn("passportExpiryDate");
		passportExpiryDateCol.setTitle("Expiry Date");
		passportExpiryDateCol.getCellRenderer().setCellEditor(new ExpirableDateEditor());
		
		Column nextOfKinContact = table.getRow().getColumn("nextOfKinContactNumber");
		nextOfKinContact.setTitle("Contact Number");
		
		
		((HtmlTable)table).getTableRenderer().setWidth("1000px");
		tableFacade.setView(new PlainTableView().showFilters());
		tableHtml = tableFacade.render();
		
		personalTab.setCurrent(true);
		return SUCCESS;
	}
	
	public String role() {
		List<CrewMember> crew = manager.getAllCrew();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("crewRoleTable", ServletActionContext.getRequest());
		tableFacade.setEditable(false);
		tableFacade.setColumnProperties("lastName", "firstName", "role", "medicalCertClass", "medicalCertExpiry", "licenceType", "r1", "r2", "primaryContractLocation");
		tableFacade.setItems(crew);
		
		tableFacade.addFilterMatcher(new MatcherKey(Object.class), new StringFilterMatcher());
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
//		HtmlColumn refCol = (HtmlColumn) table.getRow().getColumn("id");
//		refCol.getCellRenderer().setCellEditor(new CellEditor() {
//
//			public Object getValue(Object item, String property, int rowCount) {
//				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
//				HtmlBuilder html = new HtmlBuilder();
//				html.a().href().quote().append("crewMember.action?id="+id).quote().close();
//				html.append(id);
//				html.aEnd();
//				return html.toString();
//			}
//			
//		});
		
		Column medCertExpiry = table.getRow().getColumn("medicalCertExpiry");
		medCertExpiry.setTitle("Expiry Date");
		medCertExpiry.getCellRenderer().setCellEditor(new ExpirableDateEditor());
		
		((HtmlTable)table).getTableRenderer().setWidth("1000px");
		tableFacade.setView(new PlainTableView().showFilters());
		tableHtml = tableFacade.render();
		
		roleTab.setCurrent(true);
		return SUCCESS;
	}
	
	public String documents() {
		List<CrewMember> crew = manager.getAllCrew();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("crewDocumentsTable", ServletActionContext.getRequest());
		tableFacade.setEditable(false);
		tableFacade.setColumnProperties("lastName", "firstName", "contract");
		tableFacade.setItems(crew);
		
		tableFacade.addFilterMatcher(new MatcherKey(Object.class), new StringFilterMatcher());
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
//		HtmlColumn refCol = (HtmlColumn) table.getRow().getColumn("id");
//		refCol.getCellRenderer().setCellEditor(new CellEditor() {
//
//			public Object getValue(Object item, String property, int rowCount) {
//				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
//				HtmlBuilder html = new HtmlBuilder();
//				html.a().href().quote().append("crewMember.action?id="+id).quote().close();
//				html.append(id);
//				html.aEnd();
//				return html.toString();
//			}
//			
//		});
		
		Column contractCol = table.getRow().getColumn("contract");
		contractCol.getCellRenderer().setCellEditor(new DocumentCellEditor());
		
		((HtmlTable)table).getTableRenderer().setWidth("1000px");
		tableFacade.setView(new PlainTableView().showFilters());
		tableHtml = tableFacade.render();
		
		documentsTab.setCurrent(true);
		return SUCCESS;
	}
	
	public String flightDuty() {
		List<CrewMember> crew = manager.getAllCrew();
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("crewDocumentsTable", ServletActionContext.getRequest());
		tableFacade.setEditable(false);
		tableFacade.setColumnProperties("lastName", "firstName");
		tableFacade.setItems(crew);
		
		tableFacade.addFilterMatcher(new MatcherKey(Object.class), new StringFilterMatcher());
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
//		HtmlColumn refCol = (HtmlColumn) table.getRow().getColumn("id");
//		refCol.getCellRenderer().setCellEditor(new CellEditor() {
//
//			public Object getValue(Object item, String property, int rowCount) {
//				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
//				HtmlBuilder html = new HtmlBuilder();
//				html.a().href().quote().append("crewMember.action?id="+id).quote().close();
//				html.append(id);
//				html.aEnd();
//				return html.toString();
//			}
//			
//		});
		
//		Column contractCol = table.getRow().getColumn("contract");
//		contractCol.getCellRenderer().setCellEditor(new DocumentCellEditor());
//		
		((HtmlTable)table).getTableRenderer().setWidth("1000px");
		tableFacade.setView(new PlainTableView().showFilters());
		tableHtml = tableFacade.render();
		
		flightAndDutyTab.setCurrent(true);
		return SUCCESS;
	}
}
