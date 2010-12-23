package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.JobStatusDao;
import com.itao.starlite.model.JobStatus;

/**
 * <p>Finds a specific JobStatus.</p>
 * @author Celeste Groenewald
 *
 */
public class JobStatusHibernateDao extends GenericHibernateDao<JobStatus, Integer> implements JobStatusDao {

	
	@SuppressWarnings("unchecked")
	public JobStatus findJobStatusValue(String valuename) {
		List<JobStatus> js = (List<JobStatus>)getCurrentSession().createQuery("from JobStatus where jobstatus_value = ?").setParameter(0,valuename).list();
		return (js.get(0));
	}

	

}
