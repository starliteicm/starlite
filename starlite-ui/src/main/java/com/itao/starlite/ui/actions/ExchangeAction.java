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
	public ExchangeRate gbp;
	public ExchangeRate aud;
	public ExchangeRate eur;

	@Inject
	private StarliteCoreManager manager;
	
	public User user;

	public String errorMessage;
	public String notificationMessage;
	public String type;
	
	
	@Override
	public String execute() throws Exception {
		
		rand = manager.getExchangeRateByCode("ZAR", "USD");
		if(rand == null){rand = new ExchangeRate("ZAR","USD");rand.setAmount(0.0);}
		
		gbp = manager.getExchangeRateByCode("GBP", "USD");
		if(gbp == null){gbp = new ExchangeRate("GBP","USD");gbp.setAmount(0.0);}
		
		aud = manager.getExchangeRateByCode("AUD", "USD");
		if(aud == null){aud = new ExchangeRate("AUD","USD");aud.setAmount(0.0);}
		
		eur = manager.getExchangeRateByCode("EUR", "USD");
		if(eur == null){eur = new ExchangeRate("EUR","USD");eur.setAmount(0.0);}
		
		return SUCCESS;
	}
	
	public String setRates() throws Exception{
		if(amount > 0.0){	
			if(type.equals("ZAR")){
			  rand = manager.getExchangeRateByCode("ZAR", "USD");
			  if(rand == null){rand = new ExchangeRate("ZAR","USD");}
			  rand.setAmount(amount);
			  rand.setLastUpdated(new Date());
			  rand.setUpdatedBy(user.getUsername());
			  manager.saveExchange(rand);
			  notificationMessage = "Rate Set Sucessfully";
			}
			else if(type.equals("GBP")){
				gbp = manager.getExchangeRateByCode("GBP", "USD");
				if(gbp == null){gbp = new ExchangeRate("GBP","USD");}
				gbp.setAmount(amount);
				gbp.setLastUpdated(new Date());
				gbp.setUpdatedBy(user.getUsername());
				manager.saveExchange(gbp);
				notificationMessage = "Rate Set Sucessfully";
			}
			else if(type.equals("AUD")){
				aud = manager.getExchangeRateByCode("AUD", "USD");
				if(aud == null){aud = new ExchangeRate("AUD","USD");}
				aud.setAmount(amount);
				aud.setLastUpdated(new Date());
				aud.setUpdatedBy(user.getUsername());
				manager.saveExchange(aud);
				notificationMessage = "Rate Set Sucessfully";
				
			}
			else if(type.equals("EUR")){
				eur = manager.getExchangeRateByCode("EUR", "USD");
				if(eur == null){eur = new ExchangeRate("EUR","USD");}
				eur.setAmount(amount);
				eur.setLastUpdated(new Date());
				eur.setUpdatedBy(user.getUsername());
				manager.saveExchange(eur);
				notificationMessage = "Rate Set Sucessfully";
			}
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
