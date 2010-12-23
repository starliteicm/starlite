package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.JobHistoryDao;
import com.itao.starlite.model.JobHistory;


/**
 * <p>Finds all the JobHistory Tickets for the current user.</p>
 * @author Celeste Groenewald
 *
 */
public class JobHistoryHibernateDao extends GenericHibernateDao<JobHistory, Integer> implements JobHistoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<JobHistory> findAllHistoryTicketsPerUser(String username) {
		// TODO Auto-generated method stub
		return (List<JobHistory>) getCurrentSession().createQuery("from JobHistory where assignedto_id = ?").setParameter(0, username).list();
	}

	
	

}
