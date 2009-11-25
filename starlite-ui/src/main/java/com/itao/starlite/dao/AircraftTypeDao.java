package com.itao.starlite.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.AircraftTypeHibernateDao;
import com.itao.starlite.model.AircraftType;

@ImplementedBy(AircraftTypeHibernateDao.class)
public interface AircraftTypeDao extends GenericDao<AircraftType, Integer>{
}
