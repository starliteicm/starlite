package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.auth.User;
import com.itao.starlite.dao.hibernate.MyFolderHibernateDao;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.docs.model.Tag;


@ImplementedBy(MyFolderHibernateDao.class)
public interface MyFolderDao extends GenericDao<Folder, Integer>{

	public List<Bookmark> findFilesPerTag(String tag, String person);
	public void deleteDocByName(String name);
	public void makeTransient(Tag e);

}
