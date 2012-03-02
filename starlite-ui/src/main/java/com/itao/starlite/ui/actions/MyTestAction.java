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
import com.itao.starlite.model.MyTest;
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
	@Result(name="redirect", type=ServletRedirectResult.class, value="myTest!edit.action?id=${id}&notificationMessage=${notificationMessage}&errorMessage=${errorMessage}"),
	@Result(name="redirect-list", type=ServletRedirectResult.class, value="myTest.action?notificationMessage=${notificationMessage}&errorMessage=${errorMessage}")
})
public class MyTestAction extends ActionSupport implements UserAware, Preparable {
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
	public List<MyTest> myTestDataList;
	public List<ExchangeRate> rates;
	
	public Component component;
	public MyTest myTest;
	public String current="Components";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("My First Test ")};
	
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
		//this.myTestDataList = manager.getMyTest();
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
		
		return execute();
	}
	
	public String deactive() throws Exception {
		
		
		return SUCCESS;
	}
	
	public String edit(){
		
	    return "redirect-list";
	}

	public String save(){
		
		return "redirect";
	}
	
	public void createValuationTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("valuationTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("date","time","user","marketVal","purchaseVal","replaceVal","edit");
		tableFacade.setItems(component.getValuations());
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		
		tableFacade.setView(new PlainTableView());
		valTableHtml = tableFacade.render();
	}
	
	
	
	public void createLocationTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("locationTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("location","bin","quantity","batch","status","edit");
		tableFacade.setItems(component.getLocations());
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
				
		tableFacade.setView(new PlainTableView());
		histTableHtml = tableFacade.render();
	}
	
	private void prepareTabs() {

		Tab activeTab = new Tab("Active", "myTest.action", tab.equals("active"));
		Tab deactiveTab = new Tab("Inactive", "myTest!deactive.action", tab.equals("deactive"));
		Tab myTestTab = new Tab("MyTab", "myTest.action", tab.equals("myTest"));
		
		if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {activeTab, deactiveTab,myTestTab};
		
	}
	
	public TableFacade createTable(){
		
		
		
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("mytest", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("name");		
				
		tableFacade.setItems(this.myTestDataList);
		tableFacade.setMaxRows(15);
		
		Limit limit = tableFacade.getLimit();
		
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		Column name = table.getRow().getColumn("name");
		name.setTitle("Action");
		
				
		tableFacade.setView(new PlainTableView());
		histTableHtml = tableFacade.render();
		
		
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
			this.myTest = new MyTest();
		}
		else {
			//component = manager.getComponent(id);
		}
	}
	
	
}
