package com.itao.starlite.ui.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import com.itao.starlite.auth.manager.AuthManager;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.service.MailService;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class UserAction extends ActionSupport implements UserAware {
	
	
	public String current="User";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("User")};
	
	@Inject
	private StarliteCoreManager manager;
	
	@Inject 
	private AuthManager authManager;
	
	public String id;
	public User user;
	
	@SuppressWarnings("unchecked")
	public List users;
	public String[] permissions;

	public String errorMessage;
	public String notificationMessage;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		List<User> userList = authManager.getUsers();
		TreeMap<String,HashMap<String,Object>> userMap = new TreeMap<String,HashMap<String,Object>>();
		for(User user : userList){
			String username = user.getUsername();
			if(!userMap.containsKey(username)){
				HashMap u = new HashMap();
				u.put("user", user);
				CrewMember cm = manager.getCrewMemberByCode(username);
				if(cm != null){
					u.put("crew", cm);
				}
				userMap.put((""+username).toUpperCase(), u);
			}
		}
		
		users = new ArrayList();
		for( HashMap user : userMap.values()){
			users.add(user);
		}
		
		return SUCCESS;
	}
	
	public String update() throws Exception{
		user = authManager.getUser(id);
		if(user != null){
			user.clearPermissions();
			for(String perm : permissions){
				user.addPermission(perm);
			}
			authManager.saveUser(user);
		}
		return execute();
	}
	
	public void setUser(User arg0) {
		this.user = arg0;
	}
	
}
