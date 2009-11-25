package com.itao.starlite.docs.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.docs.dao.hibernate.BookmarkTypeHibernateDao;
import com.itao.starlite.docs.model.BookmarkType;

@ImplementedBy(BookmarkTypeHibernateDao.class)
public interface BookmarkTypeDao extends GenericDao<BookmarkType, Integer> {
	public BookmarkType findByName(String name);
}
