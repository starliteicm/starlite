package com.itao.starlite.ui.actions;


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
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.model.Store;
import com.itao.starlite.model.Component.ComponentHistory;
import com.itao.starlite.model.Component.ComponentLocation;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.jmesa.NavTableView;
import com.itao.starlite.ui.jmesa.PlainTableView;
import com.itao.starlite.ui.jmesa.SearchTableView;
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
	
	public String tableHtml;
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
	public String location;
	public String bin;
	public Integer quantity;
	public String note;
	public String batch;
	public Double purchaseValue;
	public String purchaseCurrency;
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		components = manager.getComponents();
		
		TableFacade tableFacade = createComponentTable();
		tableHtml = tableFacade.render();
		stores = manager.getStores();
		prepare();
		System.out.println(components);
		return SUCCESS;
	}
	
	public String create(){
		System.out.println("Updating Component("+id+") Location - "+type);
		
		prepare();
		
		
        if("Purchase".equals(type)){
        	component.updateLocation(user.getUsername(),type,batch,location,bin,quantity,null,note);
        	try {
        		if(purchaseValue != null){
        		Date now = Calendar.getInstance().getTime();
        		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
				component.updateValuation(null, df.format(now),tf.format(now),user.getUsername(),null,null,purchaseValue,purchaseCurrency,null,null);
        		}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if("Repair".equals(type)){
        	component.updateLocation(user.getUsername(),type,batch,location,bin,quantity,locCurrent,note);
        }
        else if("Move".equals(type)){
        	component.updateLocation(user.getUsername(),type,null,location,bin,quantity,locCurrent,note);
		}
		else if("Reserve".equals(type)){
			component.updateLocation(user.getUsername(),type,null,null,null,quantity,locCurrent,note);		
		}
		else if("Sell".equals(type)){
			component.updateLocation(user.getUsername(),type,null,null,null,quantity,locCurrent,note);
			try {
        		if(purchaseValue != null){
        		Date now = Calendar.getInstance().getTime();
        		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
				component.updateValuation(null, df.format(now),tf.format(now),user.getUsername(),purchaseValue,purchaseCurrency,null,null,null,null);
        		}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("Scrap".equals(type)){
			component.updateLocation(user.getUsername(),type,null,null,null,quantity,locCurrent,note);
			try {
        		if(purchaseValue != null){
        		Date now = Calendar.getInstance().getTime();
        		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
				component.updateValuation(null, df.format(now),tf.format(now),user.getUsername(),purchaseValue,purchaseCurrency,null,null,null,null);
        		}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("Consume".equals(type)){
			component.updateLocation(user.getUsername(),type,null,null,null,quantity,locCurrent,note);
			try {
        		if(purchaseValue != null){
        		Date now = Calendar.getInstance().getTime();
        		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
				component.updateValuation(null, df.format(now),tf.format(now),user.getUsername(),purchaseValue,purchaseCurrency,null,null,null,null);
        		}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        manager.saveComponent(component);
		
		notificationMessage = "Transation Created";
		return "redirect";
	}
	
	
	public TableFacade createComponentTable(){
		TableFacade tableFacade = TableFacadeFactory.createTableFacade("componentTable", ServletActionContext.getRequest());		
		tableFacade.setColumnProperties("type","name", "number", "serial","state","action");
		tableFacade.setItems(components);
		tableFacade.setMaxRows(15);
		Table table = tableFacade.getTable();
		table.getRow().setUniqueProperty("id");
		
		HtmlColumn type = (HtmlColumn) table.getRow().getColumn("type");
		type.getFilterRenderer().setFilterEditor(new DroplistFilterEditor());
		
		HtmlColumn name = (HtmlColumn) table.getRow().getColumn("name");
		name.setTitle("Description");
		
		HtmlColumn num = (HtmlColumn) table.getRow().getColumn("number");
		num.setTitle("Part No.");
		
		HtmlColumn state = (HtmlColumn) table.getRow().getColumn("state");
		state.setTitle("Status");
		state.getFilterRenderer().setFilterEditor(new DroplistFilterEditor());
		
		HtmlColumn refCol = (HtmlColumn) table.getRow().getColumn("action");
		refCol.setFilterable(false);
		refCol.getCellRenderer().setCellEditor(new CellEditor() {

		public Object getValue(Object item, String property, int rowCount) {
				Object id = new BasicCellEditor().getValue(item, "id", rowCount);
				HtmlBuilder html = new HtmlBuilder();
				html.div().style("width:350px;").close();
				html.input().type("hidden").name("id").value(""+id).close();
				html.select().name("type").id("type_"+id).style("margin-top:4px;padding:4px;").close();
				html.option().close().append("Purchase").optionEnd();
				html.option().close().append("Repair").optionEnd();
				html.option().close().append("Move").optionEnd();
				html.option().close().append("Reserve").optionEnd();
				html.option().close().append("Sell").optionEnd();
				html.option().close().append("Scrap").optionEnd();
				html.option().close().append("Consume").optionEnd();
				html.selectEnd();
				html.button().type("button").onclick("location='transaction.action?id="+id+"&type='+$('#type_"+id+"').val()+'#createTransButton';").styleClass("smooth").style("float:right;").close();
				html.img().src("images/icons/accept.png").close();
				html.append("Select Transaction Type");
				html.buttonEnd();
				html.divEnd();
				return html.toString();
			}	
		});
		
		tableFacade.setView(new SearchTableView());
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
