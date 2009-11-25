package com.itao.starlite.dao.hibernate;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Transaction;
import org.joda.time.DateMidnight;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.ActualsDao;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.ApprovalGroup;
import com.itao.starlite.model.Charter;

public class ActualsHibernateDao extends GenericHibernateDao<Actuals, Integer> implements ActualsDao {

	@SuppressWarnings("unchecked")
	public List<Actuals> getActualsByAircraftByMonth(Aircraft a, Integer month, Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from Actuals a where a.aircraft.ref = ? AND a.date >= ? AND a.date < ? order by a.date asc")
			.setString(0, a.getRef())
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Actuals> getActualsByAircraftType(AircraftType a){
		return getCurrentSession().createQuery("from Actuals a LEFT JOIN Aircraft ai ON a.aircraft.id = ai.id LEFT JOIN AircraftType at ON ai.type = at.name where at.id = ? ")
		.setLong(0, a.getId())
		.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Actuals> getActualsByAircraftByMonth(Integer aId, Integer month, Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from Actuals a where a.aircraft.id = ? AND a.date >= ? AND a.date < ? order by a.date asc")
			.setInteger(0, aId)
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}

	public boolean hasActualsForMonth(Aircraft a, Integer month, Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return !getCurrentSession().createQuery("from Actuals a where a.aircraft.ref = ? AND a.date >= ? AND a.date < ?")
			.setString(0, a.getRef())	
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<DateMidnight> getMonthYearsWithActuals(Aircraft a) {
		List<Actuals> actuals = getCurrentSession().createQuery("from Actuals a where a.aircraft.ref = ? order by a.date asc")
			.setString(0, a.getRef()).list();
		
		List<DateMidnight> ds = new LinkedList<DateMidnight>();
		for (Actuals ac: actuals) {
			DateMidnight d = new DateMidnight(ac.getDate());
			DateMidnight d2 = new DateMidnight(d.getYear(), d.getMonthOfYear(), 1);
			if (ds.isEmpty())
				ds.add(d2);
			else {
				DateMidnight last = ds.get(ds.size()-1);
				if (!last.equals(d2)) {
					ds.add(d2);
				}
			}
		}
		return ds;
	}

	@SuppressWarnings("unchecked")
	public List<Actuals> getActualsByCharterByMonth(Charter c, Integer month,
			Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from Actuals a where a.charter.id = ? AND a.date >= ? AND a.date < ? order by a.date asc")
			.setInteger(0, c.getId())
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}

	public boolean haveActualsBeenApproved(Integer aircraftId, Integer year,
			Integer month) {
		DateMidnight dm = new DateMidnight(year, month, 1);	
		return !getCurrentSession().createQuery("from Actuals a where a.approved = true and a.aircraft.id = ? and a.date = ?")
			.setInteger(0, aircraftId)
			.setDate(1, dm.toDate())
			.list().isEmpty();
	}

	public void approveActuals(Integer aircraftId, Integer year, Integer month) {
		Transaction t = createTransactionIfRequired();
		DateMidnight dm = new DateMidnight(year, month, 1);
		getCurrentSession().createQuery("update Actuals a set a.approved = true where a.aircraft.id = ? and a.date = ?")
			.setInteger(0, aircraftId)
			.setDate(1, dm.toDate())
			.executeUpdate();
		if (t != null)
			t.commit();
	}

	public ApprovalGroup getActualsApprovalGroup(Integer aircraftId, Integer year,
			Integer month) {
		List<Actuals> actuals = getActualsByAircraftByMonth(aircraftId, month, year);
		if (actuals.isEmpty())
			return null;
		return actuals.get(0).getApprovalGroup();
	}
}
