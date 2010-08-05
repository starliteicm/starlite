package com.itao.starlite.ui.actions;


import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.jfree.util.Log;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.limit.ExportType;
import org.jmesa.limit.Limit;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;

import au.com.bytecode.opencsv.CSVReader;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.model.Store;
import com.itao.starlite.model.Component.ComponentHistory;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
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
	
	public String tab = "active";
	public Tab[] tableTabs;
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
	
	//Locations
	public Integer loc;
	public Integer locationId;
	public Integer addLocation;
	public String location;
	public String bin;
	public Integer quantity;
	public Integer locCurrent;
	
	//Upload of Components
	public File document;
	public String documentContentType;
	public String documentFileName;
	
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		prepareTabs();
		components = manager.getComponents();
		stores = manager.getStores();
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
	
	public String upload() throws Exception{
		int uploaded = 0 ;
		try{
			if(document != null){
				CSVReader reader = new CSVReader(new FileReader(document));
				List<String[]> lines = reader.readAll();
				for(String[] line : lines){

					try{
						String _class = line[0];
						String _part = line[1];
						String _serial = line[2];
						String _qty = line[3];
						String _desc = line[4];
						String _status = line[5];
						String _batch = line[6]; //NOT USED
						String _exp = line[7]; //NOT USED
						String _manu = line[8];
						String _loc = line[9]; //NOT USED Store is selected from drop down.
						String _bin = line[10];

						//DEFAULT CLASS TO E TYPE
						if(!("A").equals(_class)){_class="E";}
						
						if(!("").equals(_part)){
							//FIRST CHECK IF COMPONENT ALREADY EXISTS WITH PART + SERIAL
							//IF YES... update the fields of this component and add qty to location
							//IF NO... add the new component then add the qty to location
							component = new Component();
							component.setType(_class);
							component.setNumber(_part);
							component.setSerial(_serial);
							component.setName(_desc);
							component.setDescription(_desc);
							component.setState(_status);
							component.setManufacturer(_manu);
							//component.setExpiryDate(expiryDate)
							manager.saveComponent(component);
							
							if(new Integer(_qty) > 0){
								component.updateLocation(null , location, _bin, new Integer(_qty), new Integer(0));
								manager.saveComponent(component);
							}
							uploaded ++;
						}
						
					}
					catch(Exception e){
						//oh well... this line wasnt formatted correctly.
					}

				}
				notificationMessage = documentFileName+" Uploaded - "+lines.size()+" Lines Read - "+uploaded + " Components Uploaded";
			}
		}
		catch(Exception e){
			Log.error(e);
			e.printStackTrace();
			errorMessage = "File/Format Error - Unable to Upload";
		}
		return execute();
	}
	
	public String deactive() throws Exception {
		tab = "deactive";
		prepareTabs();
		components = manager.getComponentsDeactivated();
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
					Date install =  tf.parse(installTime);
					Calendar cal = Calendar.getInstance();
					cal.setTime(install);
					cal.add(Calendar.SECOND, 1); 
					//ADD a Second to stop java.sql error for 00:00:00 as this is not a valid time.
					component.setInstallTime(cal.getTime());
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
		
		Column fCol = table.getRow().getColumn("fromVal");
		fCol.setTitle("From");
		
		Column tCol = table.getRow().getColumn("toVal");
		tCol.setTitle("To");
		
		tableFacade.setView(new PlainTableView());
		histTableHtml = tableFacade.render();
	}
	
	private void prepareTabs() {

		Tab activeTab = new Tab("Active", "component.action", tab.equals("active"));
		Tab deactiveTab = new Tab("Inactive", "component!deactive.action", tab.equals("deactive"));
		
		if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {activeTab, deactiveTab};
		
	}
	
	public TableFacade createTable(){
		
		
		
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
