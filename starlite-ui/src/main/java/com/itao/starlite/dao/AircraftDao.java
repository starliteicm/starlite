package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.AircraftHibernateDao;
import com.itao.starlite.model.Aircraft;

@ImplementedBy(AircraftHibernateDao.class)
public interface AircraftDao extends GenericDao<Aircraft, Integer>{
	public Aircraft findByReg(String reg);

	public List<Aircraft> findByType(String typeName);
}
