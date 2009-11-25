package com.itao.starlite.docs.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.docs.dao.hibernate.BookmarkHibernateDao;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.BookmarkType;
import com.itao.starlite.docs.model.Tag;

@ImplementedBy(BookmarkHibernateDao.class)
public interface BookmarkDao extends GenericDao<Bookmark, Integer>{
	public List<Bookmark> findBookmarksWithTag(Tag tag);
	public List<Bookmark> findBookmarksOfTypeWithTag(BookmarkType type, Tag tag);
	
	public List<Bookmark> findBookmarksWithTags(String... tags);
	
	public List<Bookmark> findBookmarksByQuery(String query);
}
