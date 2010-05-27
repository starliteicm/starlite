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

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.Store;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
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
	
	public String tableHtml;
	public String notificationMessage;
	public String errorMessage;
	
	public Integer id;
	public List<Store> stores;
	public List<Component> components;
	public Store store;
	public String current="Stores";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Stores")};
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		stores = manager.getStores();
		createTable();
		return SUCCESS;
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
	     	tableFacade.setView(new NavTableView());
			tableHtml = tableFacade.render();
			
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
		tableFacade.setColumnProperties("type","name", "number", "serial", "timeBetweenOverhaul","hoursRun","hoursOnInstall","installDate","lifeExpiresHours","currentHours","remainingHours","remainingHoursPercent");		
		tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
		tableFacade.setItems(components);
		tableFacade.setMaxRows(15);
		
		Limit limit = tableFacade.getLimit();
		
		
		Table table = tableFacade.getTable();
		table.setCaption("Components");
		table.getRow().setUniqueProperty("id");
		
		
		Column currentCol = table.getRow().getColumn("currentHours");
		currentCol.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return (Number) new Double(((Component) item).getCurrentHoursStr());
					}
		});
		
		Column remainingCol = table.getRow().getColumn("remainingHours");
		remainingCol.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return (Number) new Double( ((Component) item).getRemainingHoursStr() );
					}
		});
		
		if (!limit.isExported()) {
		Column percentCol = table.getRow().getColumn("remainingHoursPercent");
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
		
		if (!limit.isExported()) {
		Column refCol = table.getRow().getColumn("number");
		refCol.setTitle("Part Number");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
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
