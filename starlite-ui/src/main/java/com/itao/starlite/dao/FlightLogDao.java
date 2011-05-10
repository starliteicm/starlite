package com.itao.starlite.dao;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.FlightLogHibernateDao;

import com.itao.starlite.model.FlightLog;


/**
 * <p>Injects (persistance) the classes for Hibernate.</p>
 * @author [Creator] Celeste Groenewald
 * 
 *
 */
@ImplementedBy(FlightLogHibernateDao.class)
public interface FlightLogDao extends GenericDao<FlightLog, Integer>
{
	public FlightLog findFlightLogById(Integer id);
	
}
