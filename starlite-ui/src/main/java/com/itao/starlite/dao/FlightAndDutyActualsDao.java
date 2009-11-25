package com.itao.starlite.dao;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.FlightAndDutyActualsHibernateDao;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals;

@ImplementedBy(FlightAndDutyActualsHibernateDao.class)
public interface FlightAndDutyActualsDao extends GenericDao<FlightAndDutyActuals, Integer>{

}
