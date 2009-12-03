package com.itao.starlite.dao;


import java.util.List;
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

	public List<CrewDay> getCrewDayByCrewMemberByMonth(CrewMember c, Integer month, Integer year);
	public List<CrewDay> getCrewDayByCrewMemberByMonth(Integer cId, Integer month, Integer year);
	
	public List<CrewDay> getCrewDayByAircraftByMonth(Aircraft a, Integer month, Integer year) ;
	public List<CrewDay> getCrewDayByAircraftByMonth(Integer aId, Integer month, Integer year);
	
	public List<CrewDay> getCrewDayByCharterByMonth(Charter c, Integer month, Integer year) ;


}
