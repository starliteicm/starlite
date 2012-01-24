package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.FlightGCategoryHibernateDao;
import com.itao.starlite.model.FlightGCatagory;


/**
 * <p>Interface implemented by FlightGCategoryHibernateDao. Used in StarliteCoreManager. </p>
 * @author Celeste Groenewald
 *
 */
@ImplementedBy(FlightGCategoryHibernateDao.class)
public interface FlightGCatagoryDao extends GenericDao<FlightGCatagory, Integer>{

	public List<FlightGCatagory> findAllGCategories();
	public FlightGCatagory findGCategoryByValue(String gCategoryValue);
	public FlightGCatagory findGCategoryByID(Integer id);
}
