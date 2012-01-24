package com.itao.starlite.dao;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.FlightPlanHibernateDao;

import com.itao.starlite.model.FlightPlan;
import com.itao.starlite.model.JobHistory;
/**
 * <p>Injects (persistance) the classes for Hibernate.</p>
 * @author [Creator] Celeste Groenewald
 * 
 *
 */
@ImplementedBy(FlightPlanHibernateDao.class)
public interface FlightPlanDao extends GenericDao<FlightPlan, Integer>
{
	public List<FlightPlan> findFlightPlan(String customer, String invoiceNo, String flightNo, String flightDate, String takeOffTime);
	public List<FlightPlan> findAllFlightPlans();
	public FlightPlan findFlightPlanById(Integer id);
	
}
