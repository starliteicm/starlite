package com.itao.starlite.ui.actions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
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
import com.itao.starlite.model.Component.ComponentHistory;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@Results({
	@Result(name="redirect", type=ServletRedirectResult.class, value="component!edit.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="component.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
})
public class ComponentAction extends ActionSupport implements UserAware, Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	public String tableHtml;
	public String valTableHtml;
	public String histTableHtml;
	
	public String notificationMessage;
	public String errorMessage;
	
	public Integer id;
	
	public List<Store> stores;
	public List<Component> components;
	public List<ExchangeRate> rates;
	
	public Component component;
	public String current="Components";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Components")};
	
	//Valuations
	public Integer val;
	public Integer valuationId;
	public String valDate;
	public String valTime;
	public Double marketVal;
	public String marketCurrency;
	public Double purchaseVal;
	public String purchaseCurrency;
	public Double replacementVal;
	public String replacementCurrency;
	public String installTime;
	
	//Config
	public Integer config;
	public Integer nvg;
	public Integer flir;
	public Integer night;
	public Integer floa;
	public Integer indi;
	public Integer tcas;
	public Integer hoist;
	public Integer cargo;
	public Integer bambi;
	public Integer vip;
	public Integer troop;
	public Integer ferry;
	public Integer fdr;
	public Integer air;
	public Integer mmel;
	
	//Locations
	public Integer loc;
	public Integer locationId;
	public Integer addLocation;
	public String location;
	public String bin;
	public Integer quantity;
	public Integer locCurrent;
	
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		components = manager.getComponents();
		TableFacade tableFacade = createTable();
		
		Limit limit = tableFacade.getLimit();
		if (limit.isExported()) {
		    tableFacade.render();
		    return null;
		} 
     	tableFacade.setView(new NavTableView());
		tableHtml = tableFacade.render();
		
		return SUCCESS;
	}
	
	public String edit(){
		prepare();
		if(component != null){
			rates=manager.getExchangeRates();
			stores = manager.getStores();
			createLocationTable();
			createValuationTable();
			createHistoryTable();
			return "edit";
		}
	    return "redirect-list";
	}

	public String save(){
		if(component != null){
			
			if(config != null){
				//Save Config
				component.setNvg(nvg);
				component.setFlir(flir);
				component.setNight(night);
				component.setFloa(floa);
				component.setIndi(indi);
				component.setTcas(tcas);
				component.setHoist(hoist);
				component.setCargo(cargo);
				component.setBambi(bambi);
				component.setVip(vip);
				component.setTroop(troop);
				component.setFerry(ferry);
				component.setFdr(fdr);
				component.setAir(air);
				component.setMmel(mmel);
			}
			else if( loc != null){
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
			else if( val != null ){
				//Save Valuation
			    try {
					component.updateValuation(valuationId, valDate,valTime,user.getUsername(),marketVal,marketCurrency,purchaseVal,purchaseCurrency,replacementVal,replacementCurrency);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(installTime != null){
				try{
					SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
					component.setInstallTime(tf.parse(installTime));
				}
				catch(Exception e){
					
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
		valTableHtml = tableFacade.render();
	}
	
	 private List<ComponentHistory> sortDesc(List<ComponentHistory> history) {
	        Collections.sort(history, new Comparator<ComponentHistory>() {
				public int compare(ComponentHistory o1, ComponentHistory o2) {
					return o2.getId().compareTo(o1.getId());
				}

			});
	        return history;
	    }
	
	public void createLocationTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("locationTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("location","bin","quantity","batch","status","edit");
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
		tableHtml = tableFacade.render();
	}
	
	public void createHistoryTable(){
		List<ComponentHistory> history = sortDesc(component.getHistory());
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("historyTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("id","date","time","type","field","location","fromVal","toVal","user","description");
		tableFacade.setItems(history);
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		Column refCol = table.getRow().getColumn("field");
		refCol.setTitle("Action");
		
		tableFacade.setView(new PlainTableView());
		histTableHtml = tableFacade.render();
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
		
		Column currentCol = table.getRow().getColumn("currentHours");
		currentCol.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return ((Component) item).getCurrentHoursStr();
					}
		});
		
		Column remainingCol = table.getRow().getColumn("remainingHours");
		remainingCol.getCellRenderer().setCellEditor(new CellEditor() {
					public Object getValue(Object item, String property, int rowCount) {
						return ((Component) item).getRemainingHoursStr();
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
