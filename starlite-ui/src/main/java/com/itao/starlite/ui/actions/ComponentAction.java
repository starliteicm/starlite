package com.itao.starlite.ui.actions;


import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.editor.DroplistFilterEditor;

import au.com.bytecode.opencsv.CSVReader;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.model.Store;
import com.itao.starlite.model.Component.ComponentHistory;
import com.itao.starlite.model.Component.ComponentLocation;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SearchTableView;
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
	public String componentTable;
	public String valTableHtml;
	public String histTableHtml;
	
	public String notificationMessage;
	public String errorMessage;
	
	public Integer id;
	public boolean newComponent = false;
	
	public List<Store> stores;
	public List<Component> components;
	public List<ExchangeRate> rates;
	
	public Component component;
	public String current;
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
	public String batch;
	public String store;
	public Integer quantity;
	public Integer locCurrent;
	
	
	//Upload of Components
	public File document;
	public String documentContentType;
	public String documentFileName;
	
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception 
	{
				
		prepareTabs();
		prepare();
		components = manager.getComponents();
		stores = manager.getStores();
		TableFacade tableFacade = createTable();
		tab = "active";
		current = "active";
		
		Limit limit = tableFacade.getLimit();
		if (limit.isExported()) {
		    tableFacade.render();
		    return null;
		} 
     	//tableFacade.setView(new NavTableView());
		tableFacade.setView(new SearchTableView());
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
						
						//Ignore the header line
						if (_class.compareToIgnoreCase("Class") != 0)
						{

						//DEFAULT CLASS TO E TYPE
						if(("A").equals(_class.toUpperCase())){_class="Class A";}
						else{_class="Class E";}
						
						
						if(!("").equals(_part)){
							//FIRST CHECK IF COMPONENT ALREADY EXISTS WITH PART + SERIAL
							component = manager.getComponent(_class,_part,_serial);
							
							if(component == null){
								component = new Component();
								component.setActive(1);
							}
							//IF YES... update the fields of this component and add qty to location
							//IF NO... add the new component then add the qty to location
							component.setType(_class);
							component.setNumber(_part);
							component.setSerial(_serial);
							component.setName(_desc);
							component.setDescription(_desc);
							component.setState(_status);
							component.setManufacturer(_manu);
						
							//component.setExpiryDate(expiryDate) //Removed as Requested
							manager.saveComponent(component);
							if(new Integer(_qty) >= 0){
								component.updateLocation(null , location, _bin, new Integer(_qty), new Integer(0),_batch);
								manager.saveComponent(component);
							}
							uploaded ++;
						}
						}// if (_class = "Class")
						
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
		prepareTabs();
		components = manager.getComponentsDeactivated();
		stores = manager.getStores();
		TableFacade tableFacade = createTable();
		tab = "deactive";
		current = "deactive";
		
		Limit limit = tableFacade.getLimit();
		if (limit.isExported()) {
		    tableFacade.render();
		    return null;
		} 
     	//tableFacade.setView(new NavTableView());
		tableFacade.setView(new SearchTableView());
		tableHtml = tableFacade.render();
		//componentTable = tableFacade.render();
		
		return SUCCESS;
	}
	
	public String edit()
	{
		//if id == null, then adding a new component, so should hide the batchNo Field.
		if (id==null)
		{
			this.newComponent = true;
			tab="componentAdd";
	    }
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

	public String save()
	{
		if(component != null)
		{
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
						//component.updateLocation(locationId,location,bin,quantity,locCurrent);
						component.updateLocation(locationId,location,bin,quantity,locCurrent,batch);
						
						
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
	
	public void createValuationTable()
	{
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
		tableFacade.setColumnProperties("location","bin","quantity","batch","status");
		tableFacade.setItems(component.getLocations());
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
	/*	Column refCol = table.getRow().getColumn("edit");
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

		public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				Object loc = new BasicCellEditor().getValue(item, "location", rowCount);
				Object bin = new BasicCellEditor().getValue(item, "bin", rowCount);
				Object qty = new BasicCellEditor().getValue(item, "quantity", rowCount);
				Object batch = new BasicCellEditor().getValue(item, "batch", rowCount);
				Object value = new BasicCellEditor().getValue(item, property, rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.a().onclick("editLoc('"+id+"','"+loc+"','"+bin+"','"+batch+"','"+qty+"');return false;").href().quote().append("#").quote().close();
				//html.append("Edit");
				html.aEnd();
				return html.toString();
			}	
		});
		*/
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
						Double val = ((Component) item).getTimeBetweenOverhaul();
						DecimalFormat twoDForm = new DecimalFormat("#.#");
						return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(val))+"</div>";
						//return "<div style='text-align:right'>"+((Component) item).getTimeBetweenOverhaul()+"</div>";
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
						Double val = ((Component) item).getHoursRun();
						DecimalFormat twoDForm = new DecimalFormat("#.#");
						return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(val))+"</div>";
						
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
						Double val = ((Component) item).getHoursOnInstall();
						DecimalFormat twoDForm = new DecimalFormat("#.#");
						return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(val))+"</div>";
						//return "<div style='text-align:right'>"+((Component) item).getHoursOnInstall()+"</div>";
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
						Double val = ((Component) item).getLifeExpiresHours();
						DecimalFormat twoDForm = new DecimalFormat("#.#");
						return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(val))+"</div>";
						//return "<div style='text-align:right'>"+((Component) item).getLifeExpiresHours()+"</div>";
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
						Double val = Double.valueOf(((Component) item).getCurrentHoursStr());
						DecimalFormat twoDForm = new DecimalFormat("#.#");
						return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(val))+"</div>";
						//return "<div style='text-align:right'>"+((Component) item).getCurrentHoursStr()+"</div>";
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
					public Object getValue(Object item, String property, int rowCount) 
					{
						Double val = Double.valueOf(((Component) item).getRemainingHoursStr());
						DecimalFormat twoDForm = new DecimalFormat("#.#");
						return  "<div style='text-align:right'>"+Double.valueOf(twoDForm.format(val))+"</div>";
						//return "<div style='text-align:right'>"+((Component) item).getRemainingHoursStr()+"</div>";
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
	
    public String uploadComponents()
    {
    	prepare();
    	prepareTabs();
    	components = manager.getComponents();
		stores = manager.getStores();
    	current = "componentUpload";
    	tab="componentUpload";
    	return "componentUpload";
    }
	private void prepareTabs() {

		Tab activeTab = new Tab("Active", "component.action", tab.equals("active"));
		Tab deactiveTab = new Tab("Inactive", "component!deactive.action", tab.equals("deactive"));
		Tab addComponentTab = new Tab("Add Component", "component!edit.action", tab.equals("componentAdd"));
		Tab uploadComponentTab = new Tab("Upload Components", "component!uploadComponents.action", tab.equals("componentUpload"));
		
		if (user.hasPermission("ManagerView"))
		{
			tableTabs = new Tab[] {activeTab, deactiveTab,addComponentTab,uploadComponentTab};
		}
		else{tableTabs = new Tab[] {activeTab, deactiveTab};}
		
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
