package com.itao.starlite.dao.hibernate;

import com.itao.starlite.dao.ExchangeDao;
import com.itao.starlite.model.ExchangeRate;

import com.itao.persistence.GenericHibernateDao;

public class ExchangeHibernateDao extends GenericHibernateDao<ExchangeRate, Integer> implements ExchangeDao {

	public ExchangeRate findExchangeByCodes(String fromCode, String toCode) {
		return (ExchangeRate) getCurrentSession().createQuery("from ExchangeRate e where e.currencyCodeFrom = ? and e.currencyCodeTo = ? ")
			.setString(0, fromCode)
			.setString(1, toCode)
			.uniqueResult();
	}

}
