package com.itao.starlite.ui.guice;

import com.google.inject.AbstractModule;
import com.itao.persistence.WarpPersistGuiceModuleConfig;
import com.itao.starlite.dao.ActualsDao;
import com.itao.starlite.dao.AircraftDao;
import com.itao.starlite.dao.ApprovalGroupDao;
import com.itao.starlite.dao.CharterDao;
import com.itao.starlite.dao.CrewDao;
import com.itao.starlite.dao.ExchangeDao;
import com.itao.starlite.dao.FlightAndDutyActualsDao;
import com.itao.starlite.dao.hibernate.ActualsHibernateDao;
import com.itao.starlite.dao.hibernate.AircraftHibernateDao;
import com.itao.starlite.dao.hibernate.ApprovalGroupHibernateDao;
import com.itao.starlite.dao.hibernate.CharterHibernateDao;
import com.itao.starlite.dao.hibernate.CrewHibernateDao;
import com.itao.starlite.dao.hibernate.ExchangeHibernateDao;
import com.itao.starlite.dao.hibernate.FlightAndDutyActualsHibernateDao;
import com.itao.starlite.docs.dao.FolderDao;
import com.itao.starlite.docs.dao.hibernate.FolderHibernateDao;
import com.itao.starlite.scheduling.dao.AllocationDao;
import com.itao.starlite.scheduling.dao.hibernate.AllocationHibernateDao;
import com.wideplay.warp.persist.UnitOfWork;


public class StarliteGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new WarpPersistGuiceModuleConfig(UnitOfWork.REQUEST));
		
		bind(ActualsDao.class).to(ActualsHibernateDao.class);
		bind(AircraftDao.class).to(AircraftHibernateDao.class);
		bind(ApprovalGroupDao.class).to(ApprovalGroupHibernateDao.class);
		bind(CharterDao.class).to(CharterHibernateDao.class);
		bind(CrewDao.class).to(CrewHibernateDao.class);
		bind(ExchangeDao.class).to(ExchangeHibernateDao.class);
		bind(FlightAndDutyActualsDao.class).to(FlightAndDutyActualsHibernateDao.class);
		bind(AllocationDao.class).to(AllocationHibernateDao.class);
		bind(FolderDao.class).to(FolderHibernateDao.class);
	}
	
}
