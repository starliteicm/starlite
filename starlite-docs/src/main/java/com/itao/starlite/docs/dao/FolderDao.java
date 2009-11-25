package com.itao.starlite.docs.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.docs.dao.hibernate.FolderHibernateDao;
import com.itao.starlite.docs.model.Folder;

@ImplementedBy(FolderHibernateDao.class)
public interface FolderDao extends GenericDao<Folder, Integer>{
	public Folder getFolderByPath(String path, User user) throws InsufficientPrivilagesException;
}
