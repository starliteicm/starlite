package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.JobTicketHibernateDao;
import com.itao.starlite.model.JobTicket;


/**
 * <p>Interface implemented by JobTicketHibernateDao. Used in StarliteCoreManager. </p>
 * @author Celeste Groenewald
 *
 */
@ImplementedBy(JobTicketHibernateDao.class)
public interface JobTicketDao extends GenericDao<JobTicket, Integer>{

	public List<JobTicket> findAllTicketsPerUser(String username);
	public List<JobTicket> findAllNonOpenTicketsPerUser(String username);
	public List<JobTicket> findAllWIPTicketsByUser(String username);
	public List<JobTicket> findAllSUSPENDEDTicketsByUser(String username);
	public List<JobTicket> findAllCLOSEDTicketsByUser(String username);
	public void insertNewJobTicket(JobTicket jobTicket);
	public JobTicket findJobTicketByID(Integer ID);
	public boolean userHasWIPTickets(String username);
	public List<JobTicket> findAllTicketsPerPeriod(String period);
	public List<JobTicket> findAllSuspiciousTicketsPerPeriod(String period);
	public List<JobTicket> findAllNonLoggedOutTickets();
}
