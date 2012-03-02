package com.itao.starlite.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.FlightOFPHibernateDao;

import com.itao.starlite.model.FlightOFP;

/**
 * <p>Injects (persistance) the classes for Hibernate.</p>
 * @author [Creator] Celeste Groenewald
 * 
 *
 */
@ImplementedBy(FlightOFPHibernateDao.class)
public interface FlightOFPDao extends GenericDao<FlightOFP, Integer>
{
	public List<FlightOFP> findFlightOFP(String contract, String invoiceNo, Date flightDate);
	public List<FlightOFP> findAllFlightOFP();
	public FlightOFP findFlightOFPById(Integer id);
	
}
