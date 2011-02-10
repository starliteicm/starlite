package com.itao.starlite.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.JobTicketDao;
import com.itao.starlite.model.JobTicket;


/**
 * <p>Hibernate operations for the JobTicket.</p>
 * <ol>
 * <li>Finds all tickets for a specific user that's not closed. (Used for table matrix in hanger.ftl)</li>
 * <li>Finds all tickets for a specific user that's not open. (Used for the table displaying ticket history)</li>
 * <li>Finds a specific ticket for a specific user.</li>
 * </ol>
 * @author Celeste Groenewald
 *
 */
public class JobTicketHibernateDao extends GenericHibernateDao<JobTicket, Integer> implements JobTicketDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<JobTicket> findAllTicketsPerUser(String username)
	{
		List<JobTicket> list = new ArrayList<JobTicket>();
		List<Integer> persons = (List<Integer>)getCurrentSession().createQuery("select id from CrewMember where code = ?").setParameter(0, username).list();
		if (persons.isEmpty() == false)
		{
		int personid = persons.get(0);
		
		list = (List<JobTicket>) getCurrentSession().createQuery("from JobTicket where assignedto_id = ? and jobticketstatus_jobstatus_id in (1,2,3)").setParameter(0, personid).list();
		}
		return (list);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<JobTicket> findAllNonOpenTicketsPerUser(String username) 
	{
		List<JobTicket> list = new ArrayList<JobTicket>();
		
		List<Integer> persons = (List<Integer>)getCurrentSession().createQuery("select id from CrewMember where code = ?").setParameter(0, username).list();
		
		if (persons.isEmpty() == false)
		{
		int personid = persons.get(0);
		list = (List<JobTicket>) getCurrentSession().createQuery("from JobTicket where assignedto_id = ? and jobticketstatus_jobstatus_id in (2,3,4)").setParameter(0, personid).list(); 
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<JobTicket> findAllWIPTicketsByUser(String username) {
        List<JobTicket> list = new ArrayList<JobTicket>();
		
		List<Integer> persons = (List<Integer>)getCurrentSession().createQuery("select id from CrewMember where code = ?").setParameter(0, username).list();
		
		if (persons.isEmpty() == false)
		{
		int personid = persons.get(0);
		list = (List<JobTicket>) getCurrentSession().createQuery("from JobTicket where assignedto_id = ? and jobticketstatus_jobstatus_id = 2").setParameter(0, personid).list(); 
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobTicket> findAllSUSPENDEDTicketsByUser(String username) {
        List<JobTicket> list = new ArrayList<JobTicket>();
		
		List<Integer> persons = (List<Integer>)getCurrentSession().createQuery("select id from CrewMember where code = ?").setParameter(0, username).list();
		
		if (persons.isEmpty() == false)
		{
		int personid = persons.get(0);
		list = (List<JobTicket>) getCurrentSession().createQuery("from JobTicket where assignedto_id = ? and jobticketstatus_jobstatus_id = 3").setParameter(0, personid).list(); 
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<JobTicket> findAllCLOSEDTicketsByUser(String username) {
        List<JobTicket> list = new ArrayList<JobTicket>();
		
		List<Integer> persons = (List<Integer>)getCurrentSession().createQuery("select id from CrewMember where code = ?").setParameter(0, username).list();
		
		if (persons.isEmpty() == false)
		{
		int personid = persons.get(0);
		list = (List<JobTicket>) getCurrentSession().createQuery("from JobTicket where assignedto_id = ? and jobticketstatus_jobstatus_id = 4").setParameter(0, personid).list(); 
		}
		return list;
	}
	
	@Override
	public void insertNewJobTicket(JobTicket jobTicket) 
	{
		//getCurrentSession().saveOrUpdate(arg0, arg1);
		
	}

	
	@SuppressWarnings("unchecked")
	public JobTicket findJobTicketByID(Integer id) 
	{
		
		List<JobTicket> tempList = getCurrentSession().createQuery("from JobTicket where jobTicketID = ? ").setParameter(0, id).list();
		JobTicket temp = (JobTicket)tempList.get(0);
		return temp;	
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean userHasWIPTickets(String username) 
	{
		boolean WIPTickets = false;
		List<JobTicket> list = new ArrayList<JobTicket>();
		List<Integer> persons = (List<Integer>)getCurrentSession().createQuery("select id from CrewMember where code = ?").setParameter(0, username).list();
		
		//check if user exists
		if (persons.isEmpty() == false)
		{
			int personid = persons.get(0);
			list = (List<JobTicket>) getCurrentSession().createQuery("from JobTicket where assignedto_id = ? and jobticketstatus_jobstatus_id = 2").setParameter(0, personid).list();
			if (list.isEmpty() == false)
			{
				WIPTickets = true;
			}
		}
		return (WIPTickets);
	}
	
	

	

	
	

}
