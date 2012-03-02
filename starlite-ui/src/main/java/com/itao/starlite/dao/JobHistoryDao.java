package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.JobHistoryHibernateDao;
import com.itao.starlite.model.JobHistory;


/**
 * <p>Interface implemented by JobHistoryHibernateDao. Used in StarliteCoreManager. </p>
 * @author Celeste Groenewald
 *
 */
@ImplementedBy(JobHistoryHibernateDao.class)
public interface JobHistoryDao extends GenericDao<JobHistory, Integer>{

	public List<JobHistory> findAllHistoryTicketsPerUser(String username);
	
	public List<JobHistory> findAllNonEditHistroryTicketsByParentID(Integer parentTicketNo);
	
	
	public List<JobHistory> findAllEDITHrsTicketsByParentID(Integer parentTicketNo);
	
}
