package com.itao.starlite.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;

import com.itao.starlite.dao.CharterDao;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.MonthlyInvoicedHours;

import com.itao.persistence.GenericHibernateDao;

public class CharterHibernateDao extends GenericHibernateDao<Charter, Integer> implements CharterDao {

	public List<Charter> findAllBetween(DateMidnight startDate,
			DateMidnight endDate) {
		return null;
	}

	public List<Charter> findAllPresentAndFuture(int offset, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Charter> getUnscheduledCharters() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Charter> findAllOrdererdByStartDate() {
		List<Charter> charters = getCurrentSession().createQuery("from Charter c order by c.startDate asc").list();
		return charters;
	}

	public Charter findByCode(String code) {
		return (Charter) getCurrentSession().createQuery("from Charter c where c.code=?")
			.setString(0, code)
			.uniqueResult();
	}

	public Double getInvoicedHours(Charter c, Date date) {
		MonthlyInvoicedHours i = (MonthlyInvoicedHours) getCurrentSession().createQuery("from MonthlyInvoicedHours i where i.charter = :charter and i.date = :date")
			.setEntity("charter", c)
			.setDate("date", date)
			.uniqueResult();
		
		if (i == null)
			return 0.0;
		return i.getHours();
	}

	public void setInvoicedHours(Charter c, Date date, Double hours) {
		MonthlyInvoicedHours i = (MonthlyInvoicedHours) getCurrentSession().createQuery("from MonthlyInvoicedHours i where i.charter = :charter and i.date = :date")
			.setEntity("charter", c)
			.setDate("date", date)
			.uniqueResult();
		if (i == null) {
			i = new MonthlyInvoicedHours();
			i.setDate(date);
			i.setCharter(c);
		}
		i.setHours(hours);
		getCurrentSession().save(i);
	}
}
