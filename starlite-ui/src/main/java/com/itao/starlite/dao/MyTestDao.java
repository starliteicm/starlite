package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.MyTestHibernateDao;
import com.itao.starlite.model.MyTest;

@ImplementedBy(MyTestHibernateDao.class)
public interface MyTestDao extends GenericDao<MyTest, Integer>{

	public List<MyTest> findMyTestNames();

	public List<MyTest> findMyTestDescriptions();

}
