package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.JobSubTaskDao;
import com.itao.starlite.model.JobSubTask;

/**
 * <p>Finds all the JobTasks in the system.</p>
 * @author Celeste Groenewald
 *
 */
public class JobSubTaskHibernateDao extends GenericHibernateDao<JobSubTask, Integer> implements JobSubTaskDao {

	@SuppressWarnings("unchecked")
	public List<JobSubTask> findAllSubTasks(){
		return (List<JobSubTask>) getCurrentSession().createQuery("from JobSubTask").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JobSubTask findJobSubTaskByValue(String jobSubTaskCode) {
		List<JobSubTask> tempTasks  = (List<JobSubTask>) getCurrentSession().createQuery("from JobSubTask where jobSubTaskCode = ?").setParameter(0,jobSubTaskCode).list();
		JobSubTask firstTask = new JobSubTask();
		if (tempTasks.isEmpty() == false)
		{
			firstTask = (JobSubTask)tempTasks.get(0);
		}
		return (firstTask);
	}

	

}
