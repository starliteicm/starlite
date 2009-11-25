package com.itao.starlite.docs.dao.hibernate;

import java.util.LinkedList;
import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.docs.dao.BookmarkDao;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.BookmarkType;
import com.itao.starlite.docs.model.Tag;

public class BookmarkHibernateDao extends GenericHibernateDao<Bookmark, Integer> implements BookmarkDao {
	@SuppressWarnings("unchecked")
	public List<Bookmark> findBookmarksWithTag(Tag tag) {
		return getCurrentSession().createQuery("from Bookmark b where ? in b.tags")
			.setParameter(0, tag)
			.list();
	}

	@SuppressWarnings("unchecked")
	public List<Bookmark> findBookmarksOfTypeWithTag(BookmarkType type, Tag tag) {
		return getCurrentSession().createQuery("from Bookmark b where b.type = ? and ? in b.tags")
			.setParameter(0, type)
			.setParameter(1, tag)
			.list();
	}

	@SuppressWarnings("unchecked")
	public List<Bookmark> findBookmarksWithTags(String... tags) {
		StringBuilder buf = new StringBuilder();
		buf.append('(');
		boolean first = true;
		for (String t: tags) {
			if (!first) {
				buf.append(" or ");
			} else {
				first = false;
			}
			buf.append("t.tag = '");
			buf.append(t);
			buf.append('\'');
		}		
		buf.append(')');
		
		String tagsString = buf.toString();
		
		return getCurrentSession().createQuery("select distinct b from Bookmark b, Tag t where t member of b.tags and ("+tagsString+")")
			.list();
	}

	@SuppressWarnings("unchecked")
	public List<Bookmark> findBookmarksByQuery(String query) {
		return getCurrentSession().createQuery(parseQueryString(query)).list();
	}
	
	private String parseQueryString(String query) {
		StringBuilder buf = new StringBuilder("select distinct b from Bookmark b, Tag t where t member of b.tags");
		String[] tokens = query.split(" ");
		
		List<String> tagTokens = new LinkedList<String>();
		List<String> typeTokens = new LinkedList<String>();
		List<String> termTokens = new LinkedList<String>();
		
		for (String token: tokens) {
			if (token.startsWith("tag:")) {
				tagTokens.add(token.substring(4).toLowerCase());
			} else if (token.startsWith("type:")) {
				typeTokens.add(token.substring(5).toLowerCase());
			} else {
				termTokens.add(token);
			}
		}
		
		if (!tagTokens.isEmpty()) {
			buf.append(" AND (");
			boolean first = true;
			for (String t: tagTokens) {
				if (first) {
					first = false;
				} else {
					buf.append(" or ");
				}
				buf.append("LOWER(t.tag) = '" + t + "'");
			}
			buf.append(")");
		}
		
		if (!typeTokens.isEmpty()) {
			buf.append(" AND (");
			boolean first = true;
			for (String t: typeTokens) {
				if (first) {
					first = false;
				} else {
					buf.append(" or ");
				}
				buf.append("LOWER(b.bookmarkType.name) = '" + t + "'");
			}
			buf.append(")");
		}
		
		if (!termTokens.isEmpty()) {
			buf.append(" AND (");
			boolean first = true;
			for (String t: termTokens) {
				if (first) {
					first = false;
				} else {
					buf.append(" or ");
				}
				buf.append("b.name like '%" + t + "%'");
			}
			buf.append(")");
		}
		return buf.toString();
	}
}
