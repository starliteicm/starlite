package com.itao.starlite.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Transaction;
import org.joda.time.DateMidnight;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.CrewDayDao;
import com.itao.starlite.model.CrewDay;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CrewMember;

public class CrewDayHibernateDao extends GenericHibernateDao<CrewDay, Integer> implements CrewDayDao {

	@SuppressWarnings("unchecked")
	public List<CrewDay> getCrewDayByCrewMemberByMonth(CrewMember c, Integer month, Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from CrewDay c where c.crewMember.id = ? AND c.date >= ? AND c.date < ? order by c.date asc")
			.setInteger(0, c.getId())
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<CrewDay> getCrewDayByCrewMemberByMonth(Integer cId, Integer year, Integer month) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from CrewDay c where c.crewMember.id = ? AND c.date >= ? AND c.date < ? order by c.date asc")
			.setInteger(0, cId)
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<CrewDay> getCrewDayByAircraftByMonth(Aircraft a, Integer month, Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from crewday c where c.aircraft.id = ? AND c.date >= ? AND c.date < ? order by c.date asc")
			.setInteger(0, a.getId())
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}

	@SuppressWarnings("unchecked")
	public List<CrewDay> getCrewDayByAircraftByMonth(Integer aId, Integer month, Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from crewday c where c.aircraft.id = ? AND c.date >= ? AND c.date < ? order by c.date asc")
			.setInteger(0, aId)
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}


	@SuppressWarnings("unchecked")
	public List<CrewDay> getCrewDayByCharterByMonth(Charter c, Integer month,
			Integer year) {
		DateMidnight dm = new DateMidnight(year, month, 1);
		Date startDate = dm.toDate();
		Date endDate = dm.plusMonths(1).toDate();
		
		return getCurrentSession().createQuery("from crewday c where c.charter.id = ? AND c.date >= ? AND c.date < ? order by c.date asc")
			.setInteger(0, c.getId())
			.setDate(1, startDate)
			.setDate(2, endDate)
			.list();
	}
	
	public CrewDay getCrewDay(Date date, CrewMember crewMember){
		return (CrewDay) getCurrentSession().createQuery("from CrewDay c where c.crewMember.id = ? AND c.date = ? order by c.date asc LIMIT 1")
			.setInteger(0, crewMember.getId())
			.setDate(1, date)
			.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<CrewDay> getCrewDayByCharterBetween(Integer id, Date dateFrom,Date dateTo){
		return getCurrentSession().createQuery("from CrewDay c where c.charter.id = ? AND c.date >= ? AND c.date < ? order by c.date asc")
		.setInteger(0, id)
		.setDate(1, dateFrom)
		.setDate(2, dateTo)
		.list();
	}

}
