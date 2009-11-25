package com.itao.starlite.docs.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.docs.dao.hibernate.DocumentHibernateDao;
import com.itao.starlite.docs.model.Document;

@ImplementedBy(DocumentHibernateDao.class)
public interface DocumentDao extends GenericDao<Document, Integer>{
	
}
