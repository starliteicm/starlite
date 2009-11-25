package com.itao.starlite.auth.dao.hibernate;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.auth.Permission;
import com.itao.starlite.auth.dao.PermissionDao;

public class PermissionHibernateDao extends GenericHibernateDao<Permission, Integer> implements PermissionDao {

}
