package com.itao.starlite.dao;

import java.util.List;

import org.joda.time.DateMidnight;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.ActualsHibernateDao;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.ApprovalGroup;
import com.itao.starlite.model.Charter;

@ImplementedBy(ActualsHibernateDao.class)
public interface ActualsDao extends GenericDao<Actuals, Integer> {
	public List<Actuals> getActualsByAircraftByMonth(Aircraft a, Integer month, Integer year);
	public List<Actuals> getActualsByAircraftType(AircraftType a);
	public boolean hasActualsForMonth(Aircraft a, Integer month, Integer year);
	public List<DateMidnight> getMonthYearsWithActuals(Aircraft a);
	public List<Actuals> getActualsByCharterByMonth(Charter c, Integer month,
			Integer year);
	public boolean haveActualsBeenApproved(Integer aircraftId, Integer year,
			Integer month);
	public void approveActuals(Integer aircraftId, Integer year, Integer month);
	public ApprovalGroup getActualsApprovalGroup(Integer aircraftId, Integer year,
			Integer month);
}

