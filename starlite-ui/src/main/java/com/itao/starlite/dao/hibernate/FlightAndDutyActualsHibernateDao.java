package com.itao.starlite.dao.hibernate;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.FlightAndDutyActualsDao;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals;

public class FlightAndDutyActualsHibernateDao extends GenericHibernateDao<FlightAndDutyActuals, Integer> implements FlightAndDutyActualsDao {
	@Override
	public void makeTransient(FlightAndDutyActuals entity) {
		CrewMember c = (CrewMember) getCurrentSession().createQuery("from CrewMember c where ? member of c.flightAndDutyActuals")
			.setEntity(0, entity)
			.uniqueResult();
		if (c != null)
			c.getFlightAndDutyActuals().remove(entity);
	}
}
