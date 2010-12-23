package com.itao.starlite.dao.hibernate;

import java.util.List;
import java.util.Map;

import com.itao.persistence.GenericHibernateDao;
import com.itao.starlite.dao.AircraftDao;
import com.itao.starlite.model.Aircraft;
/**
 * <p>Get Aircraft from database.</p>
 * @author [Creator] i-tao
 * @author [Modifier] Celeste Groenewald
 *
 */
public class AircraftHibernateDao extends GenericHibernateDao<Aircraft, Integer> implements AircraftDao {

	public Aircraft findByReg(String reg) {
		return (Aircraft) getCurrentSession().createQuery("from Aircraft a where a.ref = ?")
			.setString(0, reg)
			.uniqueResult();
	}
	
	public List<Aircraft> findByType(String typeName){
		return (List<Aircraft>) getCurrentSession().createQuery("from Aircraft a where a.type = ?")
		.setString(0, typeName);
	}
	
	//get the names for aircrafts
	@SuppressWarnings("unchecked")
	public List<Aircraft> getAllAircraftRefs() {
		return (List<Aircraft>) getCurrentSession().createQuery("from Aircraft").list();
	}

	

}
