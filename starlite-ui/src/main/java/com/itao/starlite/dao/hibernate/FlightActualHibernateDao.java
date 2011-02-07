package com.itao.starlite.dao.hibernate;

import java.util.List;
import java.util.Map;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.FlightActualsDao;
import com.itao.starlite.model.FlightActuals;
import com.itao.starlite.model.FlightLog;
import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobHistory;

/**
 * <p>Get Aircraft from database.</p>
 * @author [Creator] Celeste Groenewald
 *
 */
public class FlightActualHibernateDao extends GenericHibernateDao<FlightActuals, Integer> implements FlightActualsDao {

	@SuppressWarnings("unchecked")
	@Override
	public FlightActuals findFlightActualFlightPlan(String aircraft, String aircraftType, String flightDate) 
	{

		boolean found = false;
		FlightActuals match = null;
		
        List<FlightActuals> actuals = getCurrentSession().createQuery("from FlightActuals").list();
        
        if (actuals.isEmpty() == false)
        {
        	for (int i=0; i < actuals.size(); i++)
        	{
        		FlightActuals temp = actuals.get(i);
        		FlightLog tempLog = temp.getFlightLog();
        	    //check if a FlightLog already exists for this FlightActual
        		if (tempLog != null)
        		{
        			//it exists, check if it's a match for the current flightPlan
        			if ( (tempLog.getRegistrationField().compareToIgnoreCase(aircraft) == 0) &&
        				 (tempLog.getFlight_TypeField().compareToIgnoreCase(aircraftType)==0)&&
        				 (tempLog.getFlightDateField().compareToIgnoreCase(flightDate)==0)
        			   )
        			{
        				found = true;
        				match = temp;
        				break;
        			}
        			
        		}
        		
        	}
        	
        }
        
        return match;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FlightActuals> findAllFlightActuals() {
		
		return (List<FlightActuals>)getCurrentSession().createQuery("from FlightActuals where flightActualStatus_flightActualStatusID != 7 and flightPlan_flightPlanID is not null and flightLog_flightIDField is not null").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public FlightActuals findFlightActualsByID(Integer id) {
		
		FlightActuals tempActual = null;
		List<FlightActuals> tempList = (List<FlightActuals>)getCurrentSession().createQuery("from FlightActuals where flightActualsID = ?").setParameter(0, id).list();
		
		if (tempList.isEmpty()==false)
		{
			tempActual = (FlightActuals)tempList.get(0);
		}
		return tempActual;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FlightActuals findFlightActualsByPlanID(Integer id) {
		
		FlightActuals tempActual = null;
		List<FlightActuals> tempList = (List<FlightActuals>)getCurrentSession().createQuery("from FlightActuals where flightplan_flightPlanID = ?").setParameter(0, id).list();
		
		if (tempList.isEmpty()==false)
		{
			tempActual = (FlightActuals)tempList.get(0);
		}
		return tempActual;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<FlightActuals> findMismatchedFlightActuals() {
		return (List<FlightActuals>)getCurrentSession().createQuery("from FlightActuals where flightPlan_flightPlanID IS NULL OR flightLog_flightIDField IS NULL").list();
	}



}