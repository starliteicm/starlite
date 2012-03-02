package com.itao.starlite.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.FlightOFPDao;
import com.itao.starlite.model.FlightOFP;

/**
 * <p>Get OFP from database.</p>
 * @author [Creator] i-tao
 * @author [Modifier] Celeste Groenewald
 *
 */
public class FlightOFPHibernateDao extends GenericHibernateDao<FlightOFP, Integer> implements FlightOFPDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<FlightOFP> findFlightOFP(String contract, String invoiceNo,	Date flightDate) 
	{
		List<FlightOFP> ofp = new ArrayList<FlightOFP>();
		ofp = getCurrentSession().createQuery("from FlightOFP where contract = ? and invoiceNo = ? and OFPDate = ?")
		.setParameter(0, contract)
		.setParameter(1, invoiceNo)
		.setParameter(2, flightDate).list();
		return ofp;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FlightOFP> findAllFlightOFP() {
		List<FlightOFP> ofp = new ArrayList<FlightOFP>();
		ofp = (List<FlightOFP>) getCurrentSession().createQuery("from FlightOFP").list();
		return ofp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FlightOFP findFlightOFPById(Integer id) {
		
		List<FlightOFP> tempList = getCurrentSession().createQuery("from FlightOFP where id = ?").setParameter(0,id).list();
		FlightOFP temp = new FlightOFP();
		if (tempList.isEmpty() == false)
		{
			temp = tempList.get(0);
		}
		return temp; 
	}

	



}
