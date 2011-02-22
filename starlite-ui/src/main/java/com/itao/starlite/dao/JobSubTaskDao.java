package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.JobSubTaskHibernateDao;
import com.itao.starlite.model.JobSubTask;


/**
 * <p>Interface implemented by JobTaskHibernateDao. Used in StarliteCoreManager. </p>
 * @author Celeste Groenewald
 *
 */
@ImplementedBy(JobSubTaskHibernateDao.class)
public interface JobSubTaskDao extends GenericDao<JobSubTask, Integer>{

	public List<JobSubTask> findAllSubTasks();
	public JobSubTask findJobSubTaskByValue(String jobTaskValue);
}
