package com.itao.starlite.dao.hibernate;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.ComponentDao;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.Component.ComponentLocation;

public class ComponentHibernateDao extends GenericHibernateDao<Component, Integer> implements ComponentDao {

	@SuppressWarnings("unchecked")
	public List<Component> findByLocation(String location)
	{
		//Used by Stores Tab
		//Only get back the C and E components
		//List<Component> storesCompList = new ArrayList();
		
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select distinct c from Component c "+
				"LEFT JOIN c.locations cl where cl.location in (?,?) and c.type=? order by c.name,c.number")
		.setString(0, location).setString(1,"Class C").setParameter(2, "Class E").list();
		
		for(Component c : components)
		{
			c.location = location;
		}
		
		
		
		return components;
	}

	@SuppressWarnings("unchecked")
	public List<Component> findTransactionComponents() {
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c where c.active =1 order by c.name,c.number").list();
		return components;
	}

	@SuppressWarnings("unchecked")
	public List<Component> findActive() {
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c where c.active =1 and c.type = ? order by c.name,c.number").setParameter(0,"Class A").list();
		return components;
	}

	@SuppressWarnings("unchecked")
	public List<Component> findDeactive() {
		List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c where c.active =0 and c.type = ? order by c.name,c.number").setParameter(0,"Class A").list();
		return components;
	}

	@SuppressWarnings("unchecked")
	public Component getComponent(String _class, String _part, String _serial) {
		// TODO Auto-generated method stub
		Component component = (Component) getCurrentSession().createQuery("select c from Component c "+
		" where c.type = ? and c.number=? and c.serial=?  order by c.name,c.number")
		.setString(0, _class)
		.setString(1, _part)
		.setString(2, _serial).uniqueResult();
		
		
		//if it found something, then test for Bin Number, if not found then return null.
/*		if (component != null)
		{
			try{
			   Integer id = component.getId();
			   		   	   
			   List<ComponentLocation> c = component.getLocations();
			   
			   //mysql> select locations_id from component_component$componentlocation where component_id = 324;
			   //Get the location's id of this component
			   //Integer locID = (Integer) getCurrentSession().createQuery("select locations_id from Component_Component$ComponentLocation where component_id = ?").setString(0,String.valueOf(id)).uniqueResult();
			   
			   List<Component> components = (List<Component>) getCurrentSession().createQuery("select c from Component c "+
				"LEFT JOIN c.locations cl where cl.location = ? order by c.name,c.number").setString(0, location).list();
			   
			   //mysql> select bin from component$componentlocation where id = 355;
			   //Get the bin number for this component's location
			   
			   String binNo = (String)getCurrentSession().createQuery("select bin from Component$Componentlocation where id = ?").setString(0,String.valueOf(locID)).uniqueResult();
			   
			   //Check if the current Bin Number is the same as the object that we read in from the csv file.
			   //If no, then it's a new component and an empty object should be returned, otherwise, return the existing object.
			   
			   if (binNo != null)
			   {
				   if (binNo.compareToIgnoreCase(_binNo) != 0)
				   {
					   component = null;
				   }
			   }
			}
			catch (HibernateException e)
			{
				int i=0;
			}
			catch(Exception ex)
			{
				//unable to convert numbers to Strings, return what you currently have.
				int r = 0;
			}
		}
	*/	
		 return component;
	}

}
