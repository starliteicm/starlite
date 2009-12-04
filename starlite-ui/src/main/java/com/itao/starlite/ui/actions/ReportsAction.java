package com.itao.starlite.ui.actions;

import java.util.List;

import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.model.CrewMember;
import com.opensymphony.xwork2.ActionSupport;

import com.itao.starlite.manager.StarliteCoreManager;


@SuppressWarnings("serial")
public class ReportsAction extends ActionSupport implements UserAware {
	public int month, year;

	public boolean notAuthorised = false;

	public String current="reports";

	private User user;
    
	public List<CrewMember> crewMembers;
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {

		if (!user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		DateMidnight dm = new DateMidnight();
		month = dm.getMonthOfYear();
		year = dm.getYear();
		crewMembers = manager.getAllCrew();
		/*for(CrewMember c : crewMembers){
			LOG.info(c.getPersonal().getFirstName());
		}*/
		return SUCCESS;
	}

	public void setUser(User arg0) {
		this.user = arg0;
	}

}
