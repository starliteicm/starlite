package com.itao.starlite.ui.actions;

import java.util.List;

import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.starlite.dao.CrewDao;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.ApprovalGroup;
import com.itao.starlite.model.CrewMember;
import com.opensymphony.xwork2.ActionSupport;
import com.wideplay.warp.persist.Transactional;

public class SetupApprovalGroupsAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5568606534038571099L;

	@Inject
	private CrewDao crewMemberDao;
	
	@Inject
	private StarliteCoreManager manager;
	
	@Transactional
	public String execute() {
		List<CrewMember> crew = crewMemberDao.findAll();
		for (CrewMember cm: crew) {
			cm.getApprovalGroup();
		}
		
		DateMidnight dm = new DateMidnight(2006,1,1);
		List<Aircraft> aircraft = manager.getAllAircraft().aircraftList;
		while (dm.getYear() != 2009) {
			for (Aircraft a: aircraft) {
				List<Actuals> actuals = manager.getActualsByAircraftByMonth(a.getId(), dm.getMonthOfYear(), dm.getYear());
				if (!actuals.isEmpty()) {
					ApprovalGroup ag = null;
					ag = actuals.get(0).getApprovalGroup();
					if (ag == null)
						ag = new ApprovalGroup();
					for (Actuals ac: actuals) {
						ac.setApprovalGroup(ag);
					}
				}
			}
			dm = dm.plusMonths(1);
		}
		return SUCCESS;
	}
}
