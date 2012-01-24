package com.itao.starlite.dao.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.JobTicketDao;
import com.itao.starlite.model.JobHistory;
import com.itao.starlite.model.JobTicket;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals;


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
	@SuppressWarnings("unchecked")
	public List<JobTicket> findAllTicketsPerPeriod(String period) {
       
		period = period+"01";
		
		String year = period.substring(0,4);
		String month = period.substring(4,6);
		
		if ((month.substring(0,1).compareToIgnoreCase("0") == 0))
				{
			       Integer tempMonth = Integer.parseInt(month.substring(1));
			       tempMonth++;
			       month = "0"+tempMonth;
				}
		else
		{
			Integer tempMonth = Integer.parseInt(month.substring(0));
		    tempMonth++;
		    month = String.valueOf(tempMonth);
		}
		List<JobTicket> list = new ArrayList<JobTicket>();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date from;
        Date to;
		try {
			String toDateStr = year+month+"01";
			from = df.parse(period);
            to = df.parse(toDateStr);
	        	       
	             
	    	list = getCurrentSession().createQuery("from JobTicket where createDate >= ? and createDate < ?").setParameter(0, from).setParameter(1,to).list();
    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<JobTicket> findAllNonLoggedOutTickets() {
		
		SimpleDateFormat mydf = new SimpleDateFormat("yyyyMMdd");
		String todayCal = mydf.format(Calendar.getInstance().getTime());
		Date today;
		List<JobTicket> ticketArray = new ArrayList<JobTicket>();
		try {
			today = mydf.parse(todayCal);

			List<JobTicket> allWIPTickets = new ArrayList<JobTicket>();
			allWIPTickets = (List<JobTicket>) getCurrentSession().createQuery("from JobTicket where endtime IS NULL and jobticketstatus_jobstatus_id = 2").list();
			
			if (allWIPTickets.isEmpty() == false)
			{
				for (int i=0; i<allWIPTickets.size(); i++)
				{
					JobTicket tempTicket = (JobTicket)allWIPTickets.get(i);
					List<JobHistory> historyList = tempTicket.getJobTicketHistory();
					if (historyList.isEmpty() == false)
					{
						//get latest record.
						JobHistory history = historyList.get(historyList.size()-1);
						
					    if (history.getJobTimeStamp().before(today))
							{
								ticketArray.add(tempTicket);
							}
						
					}
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ticketArray;
	}
	
	@SuppressWarnings("unchecked")
	public List<JobTicket> findAllSuspiciousTicketsPerPeriod(String period) {
       
		period = period+"01";
		
		String year = period.substring(0,4);
		String month = period.substring(4,6);
		
		if ((month.substring(0,1).compareToIgnoreCase("0") == 0))
				{
			       Integer tempMonth = Integer.parseInt(month.substring(1));
			       tempMonth++;
			       month = "0"+tempMonth;
				}
		else
		{
			Integer tempMonth = Integer.parseInt(month.substring(0));
		    tempMonth++;
		    month = String.valueOf(tempMonth);
		}
		List<JobTicket> ticketArray = new ArrayList<JobTicket>();
		List<JobTicket> list = new ArrayList<JobTicket>();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date from;
        Date to;
		try {
			String toDateStr = year+month+"01";
			from = df.parse(period);
            to = df.parse(toDateStr);
	        	       
	             
	    	list = getCurrentSession().createQuery("from JobTicket where createDate >= ? and createDate < ?").setParameter(0, from).setParameter(1,to).list();
	    	
	    	if (list.isEmpty() == false)
	    	{
	    		for (JobTicket tempTicket: list)
	    		{
	    			List<JobHistory> historyList =  tempTicket.getJobTicketHistory();
	    			
	    			for (JobHistory tempHist : historyList)
	    			{
	    				if (tempHist.getTotalTaskHours() > 8.50)
	    				{
	    					ticketArray.add(tempTicket);
	    					break;
	    				}
	    			}
	    		}
	    	}
    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticketArray;
	}

	

	
	

}
