package com.itao.starlite.ui.actions;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;

import com.google.inject.Inject;
import com.itao.jmesa.dsl.entities.Table;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.manager.StarliteCoreManager;
import com.opensymphony.xwork2.ActionSupport;

public class ScriptAction extends ActionSupport implements UserAware {
	/**
	 * GENERATED
	 */
	private static final long serialVersionUID = -6504719981541958135L;
	
	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	public String scriptName;
	public String html;
	public String view = "HTML";
	public String current = "reports";
	public Map<String, Object> pageContext;
	
	@Inject
	private StarliteCoreManager manager;
	@Inject
	public DocumentManager docManager;
	
	private User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		pageContext = new HashMap<String, Object>();
		pageContext.put("extraParams", processParams(ServletActionContext.getRequest()));
		
		pageContext.put("RESULT", SUCCESS);
		pageContext.put("request", ServletActionContext.getRequest());
		pageContext.put("response", ServletActionContext.getResponse());
		
		Table.Type t = Table.Type.valueOf(view);
		if (t == null)
			t = Table.Type.HTML;
		pageContext.put("VIEW", t);
		
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("groovy");
        String webpath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/groovyReports/");
        if (webpath.charAt(webpath.length()-1) != '/') {
        	webpath = webpath + "/";
        }
        FileInputStream fis = new FileInputStream(webpath+scriptName);
        Reader reader = new InputStreamReader(fis);
        scriptEngine.eval(reader);
        Invocable inv = (Invocable)scriptEngine;
        
        html = (String) inv.invokeFunction("generate", manager, pageContext);
        
        if (html == null)
        	return null;
        
		return pageContext.get("RESULT").toString();
	}

	private Map<String, Object> processParams(HttpServletRequest httpServletRequest) {
		Map<String, Object> extraParams = new HashMap<String, Object>();
		String month = (String)httpServletRequest.getParameter("month");
		String year = (String)httpServletRequest.getParameter("year");
		
		if (month != null && year != null) {
			extraParams.put("month", month);
			extraParams.put("year", year);
			Integer monthNumber = NumberUtils.createInteger(month.toString());
			Integer yearNumber = NumberUtils.createInteger(year.toString());
			
			if (monthNumber != null && yearNumber != null) {
				Integer nextMonth = monthNumber+1;
				Integer previousMonth = monthNumber-1;
				if (nextMonth == 13)
					nextMonth = 1;
				if (previousMonth == 0)
					previousMonth = 12;
				Integer nextYear = yearNumber;
				Integer previousYear = yearNumber;
				if (nextMonth == 1)
					nextYear++;
				if (previousMonth == 12)
					previousYear--;
				extraParams.put("nextMonth", nextMonth);
				extraParams.put("nextYear", nextYear);
				extraParams.put("previousMonth", previousMonth);
				extraParams.put("previousYear", previousYear);
			}
		}
		return extraParams;
	}
}
