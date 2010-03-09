package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.StoreHibernateDao;
import com.itao.starlite.model.Store;

@ImplementedBy(StoreHibernateDao.class)
public interface StoreDao extends GenericDao<Store, Integer>{

	public Store findByCode(String location);

}
