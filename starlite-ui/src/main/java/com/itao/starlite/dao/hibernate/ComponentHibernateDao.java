package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.ComponentDao;
import com.itao.starlite.model.Component;

public class ComponentHibernateDao extends GenericHibernateDao<Component, Integer> implements ComponentDao {

	@SuppressWarnings("unchecked")
	public List<Component> findByLocation(String location){
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c "+
				"LEFT JOIN c.locations cl where cl.location = ? order by c.name,c.number")
		.setString(0, location).list();
		
		for(Component c : components){
			c.location = location;
		}
		
		return components;
	}

	@SuppressWarnings("unchecked")
	public List<Component> findActive() {
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c where c.active =1 order by c.name,c.number").list();
		return components;
	}

	@SuppressWarnings("unchecked")
	public List<Component> findDeactive() {
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c where c.active =0 order by c.name,c.number").list();
		return components;
	}

	public Component getComponent(String _class, String _part, String _serial) {
		// TODO Auto-generated method stub
		Component component = (Component) getCurrentSession().createQuery("select c from Component c "+
		" where c.type = ? and c.number=? and c.serial=? order by c.name,c.number")
		.setString(0, _class)
		.setString(1, _part)
		.setString(2, _serial).uniqueResult();
		 return component;
	}

}
