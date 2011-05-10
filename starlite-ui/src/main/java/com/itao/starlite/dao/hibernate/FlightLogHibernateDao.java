package com.itao.starlite.dao.hibernate;

import java.util.List;
import java.util.Map;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.FlightLogDao;
import com.itao.starlite.model.FlightLog;

/**
 * <p>Get Aircraft from database.</p>
 * @author [Creator] Celeste Groenewald
 *
 */
public class FlightLogHibernateDao extends GenericHibernateDao<FlightLog, Integer> implements FlightLogDao {

	@SuppressWarnings("unchecked")
	@Override
	public FlightLog findFlightLogById(Integer id) {
		
		List<FlightLog> tempList = getCurrentSession().createQuery("from FlightLog where flightIDField = ?").setParameter(0,id).list();
		FlightLog temp = null;
		if (tempList.isEmpty() == false)
		{
			temp = tempList.get(0);
		}
		return temp; 
	}



}
