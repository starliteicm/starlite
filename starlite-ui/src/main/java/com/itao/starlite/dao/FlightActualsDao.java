package com.itao.starlite.dao;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.FlightActualHibernateDao;
import com.itao.starlite.model.FlightActuals;
import com.itao.starlite.model.FlightPlan;
;
/**
 * <p>Injects (persistance) the classes for Hibernate.</p>
 * @author [Creator] Celeste Groenewald
 * 
 *
 */
@ImplementedBy(FlightActualHibernateDao.class)
public interface FlightActualsDao extends GenericDao<FlightActuals, Integer>
{
	public FlightActuals findFlightActualFlightPlan(String aircraft, String aircraftType, String flightDate);
	
	public List<FlightActuals> findAllFlightActuals();
	
	public FlightActuals findFlightActualsByID(Integer id);
	public FlightActuals findFlightActualsByPlanID(Integer id);
	
	public List<FlightActuals> findMismatchedFlightActuals();
	
}
