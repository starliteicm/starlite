package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.ComponentHibernateDao;
import com.itao.starlite.model.Component;

@ImplementedBy(ComponentHibernateDao.class)
public interface ComponentDao extends GenericDao<Component, Integer>{

	public List<Component> findByLocation(String location);

}
