package com.itao.starlite.dao;

import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.CharterHibernateDao;
import com.itao.starlite.model.Charter;

@ImplementedBy(CharterHibernateDao.class)
public interface CharterDao extends GenericDao<Charter, Integer>{
	public List<Charter> getUnscheduledCharters();
	
	public List<Charter> findAllOrdererdByStartDate();
	
	public List<Charter> findAllBetween(DateMidnight startDate,
			DateMidnight endDate);

	public List<Charter> findAllPresentAndFuture(int offset, int limit);
	
	public Charter findByCode(String code);

	public Double getInvoicedHours(Charter c, Date date);

	public void setInvoicedHours(Charter c, Date date, Double hours);
}
