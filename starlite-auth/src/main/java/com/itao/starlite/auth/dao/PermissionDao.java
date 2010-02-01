package com.itao.starlite.auth.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.auth.Permission;
import com.itao.starlite.auth.dao.hibernate.PermissionHibernateDao;

@ImplementedBy(PermissionHibernateDao.class)
public interface PermissionDao extends GenericDao<Permission, Integer> {

	public Permission findByName(String perm);

}
