package com.itao.starlite.docs.dao.hibernate;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.docs.dao.FolderDao;
import com.itao.starlite.docs.model.Folder;

public class FolderHibernateDao extends GenericHibernateDao<Folder, Integer> implements FolderDao {

	public Folder getFolderByPath(String path, User user) throws InsufficientPrivilagesException {
//		return (Folder)getCurrentSession().createQuery("from Folder f where f.path = ?")
//			.setString(0, path)
//			.uniqueResult();
		if (path.startsWith("/"))
			path = path.substring(1);
		Folder root = findById(1);
		String[] parts = path.split("/");
		Folder current = root;
		for (int i=0; i<parts.length; i++) {
			Folder next = null;
			if (!current.canExecute(user))
				throw new InsufficientPrivilagesException();
			for (Folder subFolder: current.getSubFolders()) {
				if (subFolder.getPath().equals(parts[i])) {
					next = subFolder;
					break;
				}
			}
			if (next == null)
				break;
			current = next;
		}
		return current;
	}
}
