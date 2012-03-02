package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.JobTaskDao;
import com.itao.starlite.model.JobTask;

/**
 * <p>Finds all the JobTasks in the system.</p>
 * @author Celeste Groenewald
 *
 */
public class JobTaskHibernateDao extends GenericHibernateDao<JobTask, Integer> implements JobTaskDao {

	@SuppressWarnings("unchecked")
	public List<JobTask> findAllTasks(){
		return (List<JobTask>) getCurrentSession().createQuery("from JobTask").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JobTask findJobTaskByValue(String jobTaskValue) {
		List<JobTask> tempTasks  = (List<JobTask>) getCurrentSession().createQuery("from JobTask where jobTaskValue = ?").setParameter(0,jobTaskValue).list();
		JobTask firstTask = null;
		if (tempTasks.isEmpty() == false)
		{
			firstTask = (JobTask)tempTasks.get(0);
		}
		return (firstTask);
	}

	

}
