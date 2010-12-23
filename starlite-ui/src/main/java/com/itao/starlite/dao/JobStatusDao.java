package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.JobStatusHibernateDao;
import com.itao.starlite.model.JobStatus;


/**
 * <p>Interface implemented by JobStatusHibernateDao. Used in StarliteCoreManager. </p>
 * @author Celeste Groenewald
 *
 */
@ImplementedBy(JobStatusHibernateDao.class)
public interface JobStatusDao extends GenericDao<JobStatus, Integer>{

	public JobStatus findJobStatusValue(String valuename);
	
}