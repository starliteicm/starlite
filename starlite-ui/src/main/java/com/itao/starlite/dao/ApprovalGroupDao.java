package com.itao.starlite.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.ApprovalGroupHibernateDao;
import com.itao.starlite.model.ApprovalGroup;

@ImplementedBy(ApprovalGroupHibernateDao.class)
public interface ApprovalGroupDao extends GenericDao<ApprovalGroup, Integer>{
	
}
