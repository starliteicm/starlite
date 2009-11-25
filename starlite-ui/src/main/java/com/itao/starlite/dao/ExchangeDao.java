package com.itao.starlite.dao;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.itao.persistence.GenericDao;
import com.itao.starlite.dao.hibernate.ExchangeHibernateDao;
import com.itao.starlite.model.ExchangeRate;

@ImplementedBy(ExchangeHibernateDao.class)
public interface ExchangeDao extends GenericDao<ExchangeRate, Integer>{
	
	public ExchangeRate findExchangeByCodes(String fromCode,String toCode);

	
}
