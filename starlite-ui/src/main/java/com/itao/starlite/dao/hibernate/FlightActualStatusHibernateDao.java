package com.itao.starlite.dao.hibernate;

import java.util.List;
import java.util.Map;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.FlightActualStatusDao;
import com.itao.starlite.model.FlightActualStatus;

/**
 * <p>Get Aircraft from database.</p>
 * @author [Creator] Celeste Groenewald
 *
 */
public class FlightActualStatusHibernateDao extends GenericHibernateDao<FlightActualStatus, Integer> implements FlightActualStatusDao {

	
	@SuppressWarnings("unchecked")
	@Override
	public FlightActualStatus findStatusValueByID(int id) 
	{
		FlightActualStatus tempActual = null;
		
		List<FlightActualStatus> tempList = (List<FlightActualStatus>)getCurrentSession().createQuery("from FlightActualStatus where flightActualStatusID = ?").setParameter(0, id).list();
			
		if (tempList.isEmpty()==false)
		{
			tempActual = (FlightActualStatus)tempList.get(0);
		}
		return tempActual;
	}



}