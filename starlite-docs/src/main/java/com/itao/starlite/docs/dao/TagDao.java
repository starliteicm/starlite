package com.itao.starlite.docs.dao;

import java.util.List;
import java.util.Set;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.docs.dao.hibernate.TagHibernateDao;
import com.itao.starlite.docs.model.Tag;

@ImplementedBy(TagHibernateDao.class)
public interface TagDao extends GenericDao<Tag, Integer>{
	public Set<Tag> findTagsCreateIfRequired(String... tags);

	public List<Object[]> findTagsWithCount();
}
