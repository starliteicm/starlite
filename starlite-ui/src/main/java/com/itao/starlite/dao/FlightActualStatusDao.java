package com.itao.starlite.dao;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.FlightActualStatusHibernateDao;
import com.itao.starlite.model.FlightActualStatus;

/**
 * <p>Injects (persistance) the classes for Hibernate.</p>
 * @author [Creator] Celeste Groenewald
 * 
 *
 */
@ImplementedBy(FlightActualStatusHibernateDao.class)
public interface FlightActualStatusDao extends GenericDao<FlightActualStatus, Integer>
{
	public FlightActualStatus findStatusValueByID(int id);
	public FlightActualStatus findStatusIDByValue(String value);
}