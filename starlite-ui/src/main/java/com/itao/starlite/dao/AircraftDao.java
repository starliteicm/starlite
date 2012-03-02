package com.itao.starlite.dao;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.AircraftHibernateDao;
import com.itao.starlite.model.Aircraft;
/**
 * <p>Injects (persistance) the classes for Hibernate.</p>
 * @author [Creator] i-tao
 * @author [Modifier] Celeste Groenewald
 *
 */
@ImplementedBy(AircraftHibernateDao.class)
public interface AircraftDao extends GenericDao<Aircraft, Integer>{
	public Aircraft findByReg(String reg);

	public List<Aircraft> findByType(String typeName);

	//gets the names of the aircrafts for hanger manager
	public List<Aircraft> getAllAircraftRefs();
}
