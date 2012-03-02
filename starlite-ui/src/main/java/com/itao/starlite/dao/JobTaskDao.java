package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.JobTaskHibernateDao;
import com.itao.starlite.model.JobTask;


/**
 * <p>Interface implemented by JobTaskHibernateDao. Used in StarliteCoreManager. </p>
 * @author Celeste Groenewald
 *
 */
@ImplementedBy(JobTaskHibernateDao.class)
public interface JobTaskDao extends GenericDao<JobTask, Integer>{

	public List<JobTask> findAllTasks();
	public JobTask findJobTaskByValue(String jobTaskValue);
}
