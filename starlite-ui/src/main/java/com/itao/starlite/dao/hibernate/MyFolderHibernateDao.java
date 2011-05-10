package com.itao.starlite.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.auth.User;
import com.itao.starlite.dao.MyFolderDao;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.docs.model.Tag;
import com.itao.starlite.model.FlightOFP;


public class MyFolderHibernateDao extends GenericHibernateDao<Folder, Integer> implements MyFolderDao  {

	@SuppressWarnings("unchecked")
	@Override
	public List<Bookmark> findFilesPerTag(String tag, String person) {

       List<Bookmark> docs = new ArrayList<Bookmark>();
       
		List<Tag> tags = new ArrayList<Tag>();
		tags = getCurrentSession().createQuery("from Tag where tag = ?").setParameter(0, tag).list();
		
		//Unable to find the given tag that we're looking for
		if (tags.isEmpty() == false)
		{
			Integer tagId = tags.get(0).getId();
			List<Bookmark> bookmarks = new ArrayList<Bookmark>();
			String userDir = "%"+person+"%";
			bookmarks= getCurrentSession().createQuery("from Bookmark where bookmarkedid like ?").setParameter(0,userDir).list();

			if (bookmarks.isEmpty() == false)
			{
				for (int i=0; i< bookmarks.size(); i++)
				{
					Set<Tag> tempTags =  bookmarks.get(i).getTags();
	
					for (Tag e : tempTags)
						{
							if (e.getId() == tagId)
							{
								docs.add(bookmarks.get(i));
							}
									
						}
				}
			}
			return (docs);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteDocByName(String name) 
	{
		getCurrentSession().createSQLQuery("delete from Bookmark where bookmarkid = ?").setParameter(0, name);
		
	}

	@Override
	public void makeTransient(Tag e) {
		// TODO Auto-generated method stub
		Integer tagId = e.getId();
		getCurrentSession().delete(e);
		//createSQLQuery("delete from Bookmark where id = ?").setParameter(0, tagId);
	}

}
