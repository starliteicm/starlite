package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.CrewHibernateDao;
import com.itao.starlite.model.CrewMember;

@ImplementedBy(CrewHibernateDao.class)
public interface CrewDao extends GenericDao<CrewMember, Integer>{
	public CrewMember findCrewMemberByCode(String code);

	public CrewMember createCrewMember(String title, String firstName, String lastName);

	public CrewMember findByName(String nameOrReg);

	public List<CrewMember> findCrewMembersByCodes(String codes);
}
