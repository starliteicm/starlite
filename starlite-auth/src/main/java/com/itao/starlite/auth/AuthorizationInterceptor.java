package com.itao.starlite.auth;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.itao.starlite.auth.annotations.Authorization;
import com.itao.starlite.auth.annotations.Permissions;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthorizationInterceptor extends AbstractInterceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1683279898414626199L;
	public static final Log LOG = LogFactory.getLog(AuthorizationInterceptor.class);
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<?,?> session = invocation.getInvocationContext().getSession();

		User u = (User)session.get("userObj");
		
		Object action = invocation.getAction();
		Class<?> actionClass = action.getClass();
		
		ActionProxy proxy = invocation.getProxy();
		String methodName = proxy.getMethod();
		
		Method method = actionClass.getMethod(methodName);
		
		String shortRequestRep = actionClass.getSimpleName();
		if (!methodName.equals("execute"))
			shortRequestRep += "!"+methodName;
		
		boolean anonymousAllowed = true;
		boolean hasPermission = true;
		Authorization auth = actionClass.getPackage().getAnnotation(Authorization.class);
		Permissions permissions = actionClass.getPackage().getAnnotation(Permissions.class);
		
		if (auth != null) {
			anonymousAllowed = auth.allowAnonymous();
		}
		
		if (permissions != null) {
			if (auth == null)
				anonymousAllowed = false;
			hasPermission = PermissionTester.hasPermission(permissions.value(), u);
		}

		//If has package level permission, check class level permission
		if (hasPermission) {
			auth = actionClass.getAnnotation(Authorization.class);
			permissions = actionClass.getAnnotation(Permissions.class);
			
			if (auth != null) {
				anonymousAllowed = auth.allowAnonymous();
			}
			
			if (permissions != null) {
				if (auth == null)
					anonymousAllowed = false;
				hasPermission = hasPermission && PermissionTester.hasPermission(permissions.value(), u);
			}
			
			//Finally check method level permissions
			if (hasPermission) {
				auth = method.getAnnotation(Authorization.class);
				permissions = method.getAnnotation(Permissions.class);
				
				if (auth != null) {
					anonymousAllowed = auth.allowAnonymous();
				}
				
				if (permissions != null) {
					if (auth == null)
						anonymousAllowed = false;
					hasPermission = hasPermission && PermissionTester.hasPermission(permissions.value(), u);
				}
			}
		}
	
		if (!anonymousAllowed && u == null) {
//			ServletActionContext.getResponse().setStatus(401);
//			ServletActionContext.getResponse().getWriter().write("<body><h1>Unauthenticated</h1></body>");
//			return null;
			ServletActionContext.getRequest().getRequestDispatcher("/login.html")
				.forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
			LOG.info("Anonymous User attempted to access a non-anonymous page: " + shortRequestRep +". Denied");
			return null;
		}
		
		String username = "anonymous";
		if (u != null)
			username = u.getUsername();
		
		//If they don't have permission set the HTTP Status to 403
		//And return a simple error message
		if (!hasPermission) {
			ServletActionContext.getRequest().getRequestDispatcher("/unauthorised.html")
				.forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
			
			LOG.info(username + " is not authorized to view page: " + shortRequestRep + ". Denied");
			return null;
		}
		
		//If the action is UserAware, inject the user
		if (action instanceof UserAware) {
			UserAware userAware = (UserAware) action;
			userAware.setUser(u);
		}
		//Carry on down the interceptor chain
		LOG.info(username + " hit page: " + shortRequestRep);
		return invocation.invoke();
	}
}
