package com.itao.starlite.ui.actions;


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
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.editor.DroplistFilterEditor;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.Store;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SearchTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="store!edit.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="store.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
})
public class StoreAction extends ActionSupport implements UserAware, Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;

	public String params;
	public String tableHtml;
	public String componentTable;
	public String notificationMessage;
	public String errorMessage;

	public Integer id;
	public List<Store> stores;
	public List<Component> components;
	public Store store;
	public String current="Stores";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Stores")};

	public String tab = "active";
	public Tab[] tableTabs;

	@Inject
	private StarliteCoreManager manager;

	@Override
	public String execute() throws Exception {
		stores = manager.getStores();
		prepareTabs();
		createTable();
		return SUCCESS;
	}

	public String deactive() throws Exception {
		tab = "deactive";
		prepareTabs();
		stores = manager.getStoresDeactivated();
		createTable();
		return SUCCESS;
	}

	private void prepareTabs() {

		Tab activeTab = new Tab("Active", "store.action", tab.equals("active"));
		Tab deactiveTab = new Tab("Inactive", "store!deactive.action", tab.equals("deactive"));

		if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {activeTab, deactiveTab};

	}



	public String edit(){
		prepare();
		if(store != null){

			components = manager.getComponents(store.getLocation());

			TableFacade tableFacade = createComponentTable();

			Limit limit = tableFacade.getLimit();
			if (limit.isExported()) {
				tableFacade.render();
				return null;
			} 
			tableFacade.setView(new SearchTableView());
			componentTable = tableFacade.render();
			params = "id="+store.getId();
			return "edit";
		}
		return "redirect-list";
	}

	public String save(){
		if(store != null){
			manager.saveStore(store);
			notificationMessage = "Store saved";
			errorMessage = "";
		}
		return "redirect";
	}


	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}

	public void createTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("storeTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("location","code", "seccode", "type", "description");

		tableFacade.setItems(stores);
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");

		Column secCol = table.getRow().getColumn("seccode");
		secCol.setTitle("Seconday Identifier");

		Column codeCol = table.getRow().getColumn("code");
		codeCol.setTitle("Primary Identifier");

		Column refCol = table.getRow().getColumn("location");
		refCol.setTitle("Code");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append("store!edit.action?id="+id).quote().close();
				html.append(value);
				html.aEnd();
				return html.toString();
			}

		});
		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
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
		
		Column name = table.getRow().getColumn("name");
		name.setTitle("Desc");
		
		
		
		
		if (!limit.isExported()) {
			HtmlColumn type = (HtmlColumn) table.getRow().getColumn("type");
			type.getFilterRenderer().setFilterEditor(new DroplistFilterEditor());
			type.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						if((""+((Component) item).getType()).indexOf("Class") >= 0){
							return (""+((Component) item).getType()).substring(5);
						}
						return ((Component) item).getType();
					}
			});
		}
		
		
		if (!limit.isExported()) {
			HtmlColumn tbo = (HtmlColumn) table.getRow().getColumn("timeBetweenOverhaul");
			tbo.setFilterable(false);
			tbo.setTitle("TBO");
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
			Column tbo = table.getRow().getColumn("timeBetweenOverhaul");
			tbo.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getTimeBetweenOverhaul() ;
				}
			});
		}
		
		
		if (!limit.isExported()) {
			HtmlColumn hoursRunCol = (HtmlColumn) table.getRow().getColumn("hoursRun");
			hoursRunCol.setFilterable(false);
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
			Column hoursRunCol = table.getRow().getColumn("hoursRun");
			hoursRunCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getHoursRun() ;
				}
			});
		}
		
		
		if (!limit.isExported()) {
			HtmlColumn hoursInstallCol = (HtmlColumn) table.getRow().getColumn("hoursOnInstall");
			hoursInstallCol.setFilterable(false);
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
			Column hoursInstallCol = table.getRow().getColumn("hoursOnInstall");
			hoursInstallCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getHoursOnInstall() ;
				}
			});
		}
		
		
		if (!limit.isExported()) {
			HtmlColumn expiresCol = (HtmlColumn) table.getRow().getColumn("lifeExpiresHours");
			expiresCol.setFilterable(false);
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
			Column expiresCol = table.getRow().getColumn("lifeExpiresHours");
			expiresCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) ((Component) item).getLifeExpiresHours() ;
				}
			});
		}
		
		
		
		if (!limit.isExported()) {
			HtmlColumn currentCol = (HtmlColumn) table.getRow().getColumn("currentHours");
			currentCol.setFilterable(false);
			currentCol.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return "<div style='text-align:right'>"+((Component) item).getCurrentHoursStr()+"</div>";
					}
			});
		}
		else{	
			Column currentCol = table.getRow().getColumn("currentHours");
			currentCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) new Double( ((Component) item).getCurrentHoursStr() );
				}
			});
		}
		
		
		if (!limit.isExported()) {
			HtmlColumn remainingCol = (HtmlColumn) table.getRow().getColumn("remainingHours");
			remainingCol.setFilterable(false);
			remainingCol.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return "<div style='text-align:right'>"+((Component) item).getRemainingHoursStr()+"</div>";
					}
			});
		}
		else{		
			Column remainingCol = table.getRow().getColumn("remainingHours");
			remainingCol.getCellRenderer().setCellEditor(new CellEditor() {
				public Object getValue(Object item, String property, int rowCount) {			
					return (Number) new Double( ((Component) item).getRemainingHoursStr() );
				}
			});
		}
		
		if (!limit.isExported()) {
		HtmlColumn totalDays = (HtmlColumn) table.getRow().getColumn("totalDays");
		totalDays.setFilterable(false);
		HtmlColumn remainingDays = (HtmlColumn) table.getRow().getColumn("remainingDays");
		remainingDays.setFilterable(false);
		}	
		
		
		
		if (!limit.isExported()) {
			HtmlColumn percentCol = (HtmlColumn) table.getRow().getColumn("remainingPercent");
			percentCol.setTitle("Remaining %");
			percentCol.setFilterable(false);
		percentCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				
				try{
					if(value != null){
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




	public void prepare(){
		if (id == null) {
			store = new Store();
		}
		else {
			store = manager.getStore(id);
		}
	}


}
