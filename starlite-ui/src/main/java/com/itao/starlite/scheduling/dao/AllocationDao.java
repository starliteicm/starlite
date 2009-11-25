package com.itao.starlite.scheduling.dao;

import java.util.Date;
import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.scheduling.dao.hibernate.AllocationHibernateDao;
import com.itao.starlite.scheduling.model.Allocation;
import com.itao.starlite.scheduling.model.Schedule;

@ImplementedBy(AllocationHibernateDao.class)
public interface AllocationDao extends GenericDao<Allocation, Integer> {
	public List<Allocation> findAllAllocations(Date startDate, Date endDate);
	public List<Allocation> findAllByAssignment(String assignmentType, Integer assignmentId);
	
	public void deleteAll();
	
	public void updateSchedule(Schedule s);
	public boolean checkCollisionsForAssignable(String assignableType,
			Integer assignableId, Date from, Date to);
	public List<Allocation> findAllByAssignable(String assignableType,
			Integer assignableId);
}
