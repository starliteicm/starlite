package com.itao.starlite.auth.dao.hibernate;

import org.hibernate.Transaction;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.auth.Permission;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.dao.PermissionDao;

public class PermissionHibernateDao extends GenericHibernateDao<Permission, Integer> implements PermissionDao {

	public Permission findByName(String perm){
		Transaction t = createTransactionIfRequired();
		Permission p = (Permission) getCurrentSession().createQuery("from Permission p where p.name = ?")
			.setString(0, perm.toUpperCase())
			.uniqueResult();
		if (t != null)
			t.commit();
		return p;
	}
	
}
