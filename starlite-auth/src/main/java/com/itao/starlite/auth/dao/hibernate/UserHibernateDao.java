package com.itao.starlite.auth.dao.hibernate;

import org.hibernate.Transaction;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.dao.UserDao;

public class UserHibernateDao extends GenericHibernateDao<User, String> implements UserDao {
	@Override
	public User findById(String id) {
		Transaction t = createTransactionIfRequired();
		User u = (User) getCurrentSession().createQuery("from User u where UPPER(u.username) = ?")
			.setString(0, id.toUpperCase())
			.uniqueResult();
		if (t != null)
			t.commit();
		return u;
	}
}
