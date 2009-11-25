package com.itao.starlite.docs.dao.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.docs.dao.TagDao;
import com.itao.starlite.docs.model.Tag;

public class TagHibernateDao extends GenericHibernateDao<Tag, Integer> implements TagDao {

	@SuppressWarnings("unchecked")
	public Set<Tag> findTagsCreateIfRequired(String... tags) {
		Set<Tag> tagSet = new HashSet<Tag>();
		Set<String> expectedTags = new HashSet<String>();
		StringBuilder buf = new StringBuilder();
		buf.append("from Tag t where ");
		boolean first = true;
		for (String tag: tags) {
			if (tag != null && !tag.equals("")) {
				if (first) {
					first = false;
				} else {
					buf.append(" or ");
				}
				buf.append("t.tag = '");
				buf.append(tag);
				buf.append('\'');
				expectedTags.add(tag);
			}
		}
		buf.append(')');
		
		if (first)
			return tagSet;
		
		tagSet.addAll(getCurrentSession().createQuery(buf.toString()).list());
		
		for (Tag t: tagSet) {
			expectedTags.remove(t.getTag());
		}
		
		for (String t: expectedTags) {
			Tag newTag = new Tag(t);
			makePersistent(newTag);
			tagSet.add(newTag);
		}
		
		return tagSet;
	}

	public List<Object[]> findTagsWithCount() {
		return getCurrentSession().createQuery(
			"select t.tag, count(t) from Bookmark b join b.tags t group by t having count(t) > 0 order by t.tag"	
		).list();
	}

}
