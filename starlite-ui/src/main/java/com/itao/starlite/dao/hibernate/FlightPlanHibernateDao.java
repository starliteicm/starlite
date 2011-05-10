package com.itao.starlite.dao.hibernate;

import java.util.List;
import java.util.Map;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.FlightPlanDao;
import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobTicket;
/**
 * <p>Get Aircraft from database.</p>
 * @author [Creator] i-tao
 * @author [Modifier] Celeste Groenewald
 *
 */
public class FlightPlanHibernateDao extends GenericHibernateDao<FlightPlan, Integer> implements FlightPlanDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<FlightPlan> findFlightPlan(String customer, String invoiceNo,
			String flightNo, String flightDate, String takeOffTime) 
			{
		//List<Integer> fligtPlan = (List<Integer>)getCurrentSession().createQuery("select id from CrewMember where code = ?").setParameter(0, username).list();
		
		return (List<FlightPlan>) getCurrentSession().createQuery("from FlightPlan where customer = '"+ customer +"' and invoiceNo = '"+ invoiceNo +"' and flightNumber = '"+ flightNo +"' and flightDateField = '"+ flightDate +"' and takeoffField = '"+ takeOffTime+"'").list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FlightPlan> findAllFlightPlans() {
		// TODO Auto-generated method stub
		return (List<FlightPlan>) getCurrentSession().createQuery("from FlightPlan").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public FlightPlan findFlightPlanById(Integer id) {
		
		List<FlightPlan> tempList = getCurrentSession().createQuery("from FlightPlan where flightPlanID = ?").setParameter(0,id).list();
		FlightPlan temp = null;
		if (tempList.isEmpty() == false)
		{
			temp = tempList.get(0);
		}
		return temp; 
	}



}
