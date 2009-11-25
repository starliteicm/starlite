package com.itao.starlite.auth.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.dao.hibernate.UserHibernateDao;

@ImplementedBy(UserHibernateDao.class)
public interface UserDao extends GenericDao<User, String>{
	
}
