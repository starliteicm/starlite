package com.itao.starlite.dao;


import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.CrewDayDao;
import com.itao.starlite.dao.hibernate.CrewDayHibernateDao;
import com.itao.starlite.model.CrewDay;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CrewMember;

@ImplementedBy(CrewDayHibernateDao.class)
public interface CrewDayDao extends GenericDao<CrewDay, Integer> {

	public List<CrewDay> getCrewDayByCrewMemberByMonth(CrewMember c, Integer year, Integer month);
	public List<CrewDay> getCrewDayByCrewMemberByMonth(Integer cId, Integer year, Integer month);
	
	public List<CrewDay> getCrewDayByAircraftByMonth(Aircraft a, Integer year, Integer month) ;
	public List<CrewDay> getCrewDayByAircraftByMonth(Integer aId, Integer year, Integer month);
	
	public List<CrewDay> getCrewDayByCharterByMonth(Charter c, Integer year, Integer month) ;
	public CrewDay getCrewDay(Date date, CrewMember crewMember);
	public List<CrewDay> getCrewDayByCharterBetween(Integer id, Date dateFrom,Date dateTo);
	public List<CrewDay> getCrewDayByCrewMemberBetween(Integer id,Date dateFrom, Date dateTo);
	public TreeMap getSumCrewDays(Date dateFrom, Date dateTo);


}
