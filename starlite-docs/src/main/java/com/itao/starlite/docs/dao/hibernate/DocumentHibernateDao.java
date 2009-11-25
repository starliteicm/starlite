package com.itao.starlite.docs.dao.hibernate;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.docs.dao.DocumentDao;
import com.itao.starlite.docs.model.Document;

public class DocumentHibernateDao extends GenericHibernateDao<Document, Integer> implements DocumentDao {

}
