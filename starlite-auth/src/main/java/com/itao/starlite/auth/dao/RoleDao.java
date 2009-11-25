package com.itao.starlite.auth.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.auth.Role;
import com.itao.starlite.auth.dao.hibernate.RoleHibernateDao;

@ImplementedBy(RoleHibernateDao.class)
public interface RoleDao extends GenericDao<Role, Integer>{
	public List<Role> selectRolesByNames(String... names);
}
