package com.itao.starlite.scheduling.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.scheduling.dao.AllocationDao;
import com.itao.starlite.scheduling.model.Allocation;
import com.itao.starlite.scheduling.model.Schedule;

public class AllocationHibernateDao extends GenericHibernateDao<Allocation, Integer> implements AllocationDao{

	public void deleteAll() {
		Session s = getCurrentSession();
		s.createQuery("delete from Allocation").executeUpdate();
		flush();
	}

	public List<Allocation> findAllAllocations(Date startDate, Date endDate) {
		throw new UnsupportedOperationException();
	}
	
	public void updateSchedule(Schedule s) {
		for (Allocation a: s.getAllocations()) {
			makePersistent(a);
		}
		
		List<Allocation> allAllocations = findAll();
		for (Allocation a: allAllocations) {
			if (!s.getAllocations().contains(a))
				makeTransient(a);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Allocation> findAllByAssignment(String assignmentType,
			Integer assignmentId) {
		return getCurrentSession().createQuery("from Allocation a where a.assignmentType = ? and a.assignmentId = ?")
			.setString(0, assignmentType)
			.setInteger(1, assignmentId)
			.list();
	}

	/**
	 * @return true if there are collisions
	 */
	public boolean checkCollisionsForAssignable(String assignableType,
			Integer assignableId, Date from, Date to) {
		return !getCurrentSession().createQuery("from Allocation a where a.assignableType = ? and a.assignableId = ? and a.to >= ? and a.from <= ?")
			.setString(0, assignableType)
			.setInteger(1, assignableId)
			.setDate(2, from)
			.setDate(3, to)
			.list().isEmpty();
	}

	public List<Allocation> findAllByAssignable(String assignableType,
			Integer assignableId) {
		return getCurrentSession().createQuery("from Allocation a where a.assignableType = ? and a.assignableId = ?")
		.setString(0, assignableType)
		.setInteger(1, assignableId)
		.list();
	}
}
