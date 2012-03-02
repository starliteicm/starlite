package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.MyTestDao;
import com.itao.starlite.model.MyTest;

public class MyTestHibernateDao extends GenericHibernateDao<MyTest, Integer> implements MyTestDao {

	@SuppressWarnings("unchecked")
	public List<MyTest> findMyTestNames(){
		List<MyTest> myTestList = (List<MyTest>) getCurrentSession().createQuery("select name from MyTest").list();
		return myTestList;
	}

	@SuppressWarnings("unchecked")
	public List<MyTest> findMyTestDescriptions() {
		List<MyTest> myTestList = (List<MyTest>) getCurrentSession().createQuery("select description from MyTest").list();
		return myTestList;
	}

}
