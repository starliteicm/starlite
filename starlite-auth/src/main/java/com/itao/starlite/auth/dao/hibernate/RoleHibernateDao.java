package com.itao.starlite.auth.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.auth.Role;
import com.itao.starlite.auth.dao.RoleDao;

public class RoleHibernateDao extends GenericHibernateDao<Role, Integer> implements RoleDao {

	@SuppressWarnings("unchecked")
	public List<Role> selectRolesByNames(String... names) {
		StringBuilder buf = new StringBuilder();
		if (names == null || names.length == 0)
			return new ArrayList<Role>(0);
		buf.append("from Role r where ");
		boolean first = true;
		for (String s: names) {
			if (first)
				first = false;
			else
				buf.append(" or ");
			// r.name='NAME'
			buf.append("r.name='").append(s).append('\'');
		}
		return getCurrentSession().createQuery(buf.toString()).list();
	}
	
}
