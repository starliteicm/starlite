package com.itao.starlite.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.FlightGCatagoryDao;
import com.itao.starlite.model.FlightGCatagory;

/**
 * <p>Finds all the JobTasks in the system.</p>
 * @author Celeste Groenewald
 *
 */
public class FlightGCategoryHibernateDao extends GenericHibernateDao<FlightGCatagory, Integer> implements FlightGCatagoryDao {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<FlightGCatagory> findAllGCategories() 
	{
		List<FlightGCatagory> list = new ArrayList<FlightGCatagory>();
		list = (List<FlightGCatagory>) getCurrentSession().createQuery("from FlightGCatagory").list();
		
		return list;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public FlightGCatagory findGCategoryByValue(String categoryValue) {
		List<FlightGCatagory> tempList  = (List<FlightGCatagory>) getCurrentSession().createQuery("from FlightGCatagory where value = ?").setParameter(0,categoryValue).list();
		FlightGCatagory firstCategory = null;
		if (tempList.isEmpty() == false)
		{
			firstCategory = (FlightGCatagory)tempList.get(0);
		}
		return (firstCategory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FlightGCatagory findGCategoryByID(Integer id) {
		List<FlightGCatagory> tempList  = (List<FlightGCatagory>) getCurrentSession().createQuery("from FlightGCatagory where id = ?").setParameter(0,id).list();
		FlightGCatagory firstCategory = null;
		if (tempList.isEmpty() == false)
		{
			firstCategory = (FlightGCatagory)tempList.get(0);
		}
		return (firstCategory);
	}

	

}
