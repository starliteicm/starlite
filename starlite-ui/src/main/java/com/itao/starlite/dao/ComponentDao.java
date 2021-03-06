package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.ComponentHibernateDao;
import com.itao.starlite.model.Component;

@ImplementedBy(ComponentHibernateDao.class)
public interface ComponentDao extends GenericDao<Component, Integer>{

	public List<Component> findByLocation(String location);

	public List<Component> findAllClassesByLocation(String location);
	
	public List<Component> findByLocationClassA(String location);

	public List<Component> findTransactionComponents();
	
	public List<Component> findActive();

	public List<Component> findDeactive();

	public Component getComponent(String _class, String _part, String _serial);

}
