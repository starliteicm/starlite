package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.ComponentDao;
import com.itao.starlite.model.Component;

public class ComponentHibernateDao extends GenericHibernateDao<Component, Integer> implements ComponentDao {

	@SuppressWarnings("unchecked")
	public List<Component> findByLocation(String location){
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c "+
				"LEFT JOIN c.locations cl where cl.location = ?")
		.setString(0, location).list();
		return components;
	}

	@SuppressWarnings("unchecked")
	public List<Component> findActive() {
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c where c.active =1").list();
		return components;
	}

	@SuppressWarnings("unchecked")
	public List<Component> findDeactive() {
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c where c.active =0").list();
		return components;
	}

}
