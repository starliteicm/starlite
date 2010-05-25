package com.itao.starlite.ui.actions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.model.Store;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="transaction.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="transaction.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
})
public class TransactionAction extends ActionSupport implements UserAware, Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	public String notificationMessage;
	public String errorMessage;
	
	public Integer id;
	public String type;
	
	public List<Store> stores;
	public List<Component> components;
	
	public Component component;
	public String current="Transactions";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Transactions")};
	
	//Locations
	public Integer locationId;
	public Integer locCurrent;
	public String bin;
	public Integer quantity;
	public String note;
	
	
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		components = manager.getComponents();
		stores = manager.getStores();
		prepare();
		System.out.println(components);
		return SUCCESS;
	}
	
	
	public String create(){
		
		prepare();
		
        if("Purchase".equals(type)){
        	component.updateLocation(type,locationId,bin,quantity,null);
        }
        else if("Repair".equals(type)){
        	component.updateLocation(type,locationId,bin,quantity,locCurrent);
        }
        else if("Move".equals(type)){
        	component.updateLocation(type,locationId,bin,quantity,locCurrent);
		}
		else if("Reserve".equals(type)){
			component.updateLocation(type,null,null,quantity,locCurrent);
			
		}
		else if("Sell".equals(type)){
			component.updateLocation(type,null,null,quantity,locCurrent);
			
		}
		else if("Scrap".equals(type)){
			component.updateLocation(type,null,null,quantity,locCurrent);
		}
		else if("Consume".equals(type)){
			component.updateLocation(type,null,null,quantity,locCurrent);
			
		}
		
		notificationMessage = "Transation Created";
		return "redirect";
	}
	
	public String edit(){
		prepare();
		if(component != null){
			stores = manager.getStores();
			createLocationTable();
			createValuationTable();
			return "edit";
		}
	    return "redirect-list";
	}

	public String save(){
		if(component != null){
			
		
			if( loc != null){
				//Save Location
				if(((location!= null)&&(bin!= null))||(locCurrent == 0)) {
					if((location.length() == 5)){
						
						Store locExists = manager.findStore(location);
						if(locExists == null){
							//Create Store
							Store store = Store.createStore(location);
							manager.saveStore(store);
						}
					}
						
					if((location.length() == 5)||(locCurrent == 0)){
						if(quantity.equals(0)){locCurrent = 0;}
						
						if(addLocation != null){
							locationId = addLocation;
						}
						//Record History of Location Move
						component.updateLocation(locationId,location,bin,quantity,locCurrent);
						
						
					}
				}
			}
			
			manager.saveComponent(component);
			notificationMessage = "Component saved";
			errorMessage = "";
		}
		return "redirect";
	}
	
	public void createValuationTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("valuationTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("date","time","user","marketVal","purchaseVal","replaceVal","edit");
		tableFacade.setItems(component.getValuations());
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		Column mCol = table.getRow().getColumn("marketVal");
		mCol.setTitle("Market");
		mCol.getCellRenderer().setCellEditor(new CellEditor() {
		public Object getValue(Object item, String property, int rowCount) {
				Object mval = new BasicCellEditor().getValue(item, "marketCurrency", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.append(value+" "+mval);
				return html.toString();
			}	
		});
		
		Column rCol = table.getRow().getColumn("replaceVal");
		rCol.setTitle("Replacement");
		rCol.getCellRenderer().setCellEditor(new CellEditor() {
		public Object getValue(Object item, String property, int rowCount) {
				Object mval = new BasicCellEditor().getValue(item, "replaceCurrency", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.append(value+" "+mval);
				return html.toString();
			}	
		});
		
		Column pCol = table.getRow().getColumn("purchaseVal");
		pCol.setTitle("Purchase");
		pCol.getCellRenderer().setCellEditor(new CellEditor() {
		public Object getValue(Object item, String property, int rowCount) {
				Object mval = new BasicCellEditor().getValue(item, "purchaseCurrency", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.append(value+" "+mval);
				return html.toString();
			}	
		});
		
				
				
		
		Column refCol = table.getRow().getColumn("edit");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {
		public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Date date = (Date) new BasicCellEditor().getValue(item, "date", rowCount);
				Date time = (Date) new BasicCellEditor().getValue(item, "time", rowCount);
				Object mval = new BasicCellEditor().getValue(item, "marketVal", rowCount);
				Object mcur = new BasicCellEditor().getValue(item, "marketCurrency", rowCount);
				Object pval = new BasicCellEditor().getValue(item, "purchaseVal", rowCount);
				Object pcur = new BasicCellEditor().getValue(item, "purchaseCurrency", rowCount);
				Object rval = new BasicCellEditor().getValue(item, "replaceVal", rowCount);
				Object rcur = new BasicCellEditor().getValue(item, "replaceCurrency", rowCount);

				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
				
				HtmlBuilder html = new HtmlBuilder();
				html.a().onclick("editVal('"+id+"','"+df.format(date)+"','"+tf.format(time)+"','"+mval+"','"+mcur+"','"+pval+"','"+pcur+"','"+rval+"','"+rcur+"');return false;").href().quote().append("#").quote().close();
				html.append("Edit");
				html.aEnd();
				return html.toString();
			}	
		});
		
		tableFacade.setView(new PlainTableView());
	}
	
	public void createLocationTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("locationTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("location","bin","quantity","edit");
		tableFacade.setItems(component.getLocations());
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		Column refCol = table.getRow().getColumn("edit");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

		public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Object loc = new BasicCellEditor().getValue(item, "location", rowCount);
				Object bin = new BasicCellEditor().getValue(item, "bin", rowCount);
				Object qty = new BasicCellEditor().getValue(item, "quantity", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().onclick("editLoc('"+id+"','"+loc+"','"+bin+"','"+qty+"');return false;").href().quote().append("#").quote().close();
				html.append("Edit");
				html.aEnd();
				return html.toString();
			}	
		});
		
		tableFacade.setView(new PlainTableView());
	}
	
	public TableFacade createTable(){
		
		
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("componentTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("type","name", "number", "serial", "timeBetweenOverhaul","hoursRun","hoursOnInstall","installDate","lifeExpiresHours","currentHours","remainingHours","remainingHoursPercent");		
		tableFacade.setExportTypes(ServletActionContext.getResponse(), ExportType.CSV, ExportType.EXCEL);
		
		tableFacade.setItems(components);
		tableFacade.setMaxRows(15);
		
		Limit limit = tableFacade.getLimit();
		
		
		Table table = tableFacade.getTable();
		table.setCaption("Components");
		table.getRow().setUniqueProperty("id");
		
		if (!limit.isExported()) {
		Column percentCol = table.getRow().getColumn("remainingHoursPercent");
		percentCol.getCellRenderer().setCellEditor(new CellEditor() {

			public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				
				try{
					long valueLong = (Long) value;
					if(valueLong >= 50){
						html.div().style("text-align:center;background-color:#99FF99;font-weight:bold;").styleEnd();
					}
					else if(valueLong >= 20){
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
	
	
	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}
	
	public void prepare(){
		if (id == null) {
			component = new Component();
		}
		else {
			component = manager.getComponent(id);
		}
	}
	
	
}
