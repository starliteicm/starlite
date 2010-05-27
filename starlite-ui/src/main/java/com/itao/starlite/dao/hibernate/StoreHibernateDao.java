package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.StoreDao;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.Store;

public class StoreHibernateDao extends GenericHibernateDao<Store, Integer> implements StoreDao {

	public Store findByCode(String location){
		if(location.length() == 5){
			String loc1 = location.substring(0,2);
			String loc2 = location.substring(2,5);
			return (Store) getCurrentSession().createQuery("from Store s where s.code = ? and s.seccode = ? ")
			.setString(0, loc1)
			.setString(1, loc2)
			.uniqueResult();
			}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Store> findActive() {
		List<Store> stores = (List<Store>) getCurrentSession().createQuery("select s from Store s where s.active =1").list();
		return stores;
	}

	@SuppressWarnings("unchecked")
	public List<Store> findDeactive() {
		List<Store> stores = (List<Store>) getCurrentSession().createQuery("select s from Store s where s.active =0").list();
		return stores;
	}

}
