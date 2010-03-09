package com.itao.starlite.ui.actions;


import java.text.ParseException;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
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
import com.itao.starlite.ui.Breadcrumb;
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
	public String locationTableHtml;
	
	public String notificationMessage;
	public String errorMessage;
	
	public Integer id;
	
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
	
	//Locations
	public Integer loc;
	public Integer locationId;
	public String location;
	public String bin;
	public Integer quantity;
	public Integer locCurrent;
	
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		components = manager.getComponents();
		createTable();
		return SUCCESS;
	}
	
	public String edit(){
		prepare();
		if(component != null){
			rates=manager.getExchangeRates();
			createLocationTable();
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
			}
			else if( loc != null){
				//Save Location
				if((location!= null)&&(bin!= null)){
					if((location.length() == 5)){
						if(quantity.equals(0)){locCurrent = 0;}
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
			
			manager.saveComponent(component);
			notificationMessage = "Component saved";
			errorMessage = "";
		}
		return "redirect";
	}
	
	
	public void createLocationTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("locationTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("location","bin","quantity");
		tableFacade.setItems(component.getLocations());
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
	}
	
	public void createTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("componentTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("type","name", "number", "serial", "timeBetweenOverhaul","hoursRun","hoursOnInstall","installDate","lifeExpiresHours","currentHours","remainingHours","remainingHoursPercent");
		
		

		tableFacade.setItems(components);
		tableFacade.setMaxRows(100);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
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
     	tableFacade.setView(new PlainTableView());
		tableHtml = tableFacade.render();
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
