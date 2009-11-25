package com.itao.starlite.ui.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.util.Log;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.service.MailService;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class ExchangeAction extends ActionSupport implements UserAware {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2250524649483938062L;
	public String current="Exchange";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Exchange")};
	
	public double amount;
	public ExchangeRate rand;

	@Inject
	private StarliteCoreManager manager;
	
	public User user;

	public String errorMessage;
	public String notificationMessage;
	
	
	@Override
	public String execute() throws Exception {
		rand = manager.getExchangeRateByCode("ZAR", "USD");
		if(rand == null){rand = new ExchangeRate("ZAR","USD");rand.setAmount(0.0);}
		return SUCCESS;
	}
	
	public String setRates() throws Exception{
		if(amount > 0.0){
			rand = manager.getExchangeRateByCode("ZAR", "USD");
			
			if(rand == null){rand = new ExchangeRate("ZAR","USD");}
			
			
			rand.setAmount(amount);
			rand.setLastUpdated(new Date());
			rand.setUpdatedBy(user.getUsername());
			manager.saveExchange(rand);
			notificationMessage = "Rate Set Sucessfully";
		}
		else {
			errorMessage = "Please Set the Exchange Rate as a Positive Figure";
		}

		return execute();
	}
	
	public void setUser(User arg0) {
		this.user = arg0;
	}
	
}
