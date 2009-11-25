package com.itao.starlite.docs.dao.hibernate;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.docs.dao.BookmarkTypeDao;
import com.itao.starlite.docs.model.BookmarkType;

public class BookmarkTypeHibernateDao extends GenericHibernateDao<BookmarkType, Integer> implements BookmarkTypeDao {

	public BookmarkType findByName(String name) {
		return (BookmarkType) getCurrentSession().createQuery("from BookmarkType b where b.name = ?")
			.setString(0, name)
			.uniqueResult();
	}

}
