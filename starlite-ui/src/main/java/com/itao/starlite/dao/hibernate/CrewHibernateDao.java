package com.itao.starlite.dao.hibernate;

import java.util.List;

import com.itao.starlite.dao.CrewDao;
import com.itao.starlite.model.CrewMember;

import com.itao.persistence.GenericHibernateDao;

public class CrewHibernateDao extends GenericHibernateDao<CrewMember, Integer> implements CrewDao {
	public CrewMember findCrewMemberByCode(String code) {
		return (CrewMember) getCurrentSession().createQuery("from CrewMember c where c.code = ?")
			.setString(0, code)
			.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public CrewMember createCrewMember(String title, String firstName, String lastName) {
		String[] nameParts = lastName.split(" ");
		
		String mainPart = "";
		for (String s: nameParts) {
			if (s.length() > mainPart.length())
				mainPart = s;
		}
		
		int codeLength = Math.min(3, mainPart.length());
		String codePrefix = mainPart.substring(0, codeLength);
		
		int numExisting = getCurrentSession().createQuery("from CrewMember c where c.code like '" + codePrefix + "__'").list().size();
		int thisCodeNum = numExisting + 1;
		if (thisCodeNum < 10) {
			codePrefix += "0";
		}
		codePrefix += thisCodeNum;
		
		Integer numID = 0;
		List<Integer> numList = getCurrentSession().createQuery("Select MAX(id) from CrewMember").list();
		Integer temp = numList.get(0);
		numID = ++temp;
		
		
		CrewMember cm = new CrewMember();
		cm.getPersonal().setTitle(title);
		cm.getPersonal().setFirstName(firstName);
		cm.getPersonal().setLastName(lastName);
		cm.getRole().setEmployment("Permanent CRI");
		cm.setCode(codePrefix);
		//cm.setID(numID);
		
		return makePersistent(cm);
	}

	public CrewMember findByName(String nameOrReg) {
		return (CrewMember) getCurrentSession().createQuery("from CrewMember cm where concat(concat(cm.personal.firstName, ' '), cm.personal.lastName) like ?")
			.setString(0, nameOrReg)
			.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<CrewMember> findCrewMembersByCodes(String codes){
		return (List<CrewMember>) getCurrentSession().createQuery("from CrewMember cm where id in ("+codes+")").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<CrewMember> findAllCrewReadOnly() {
		// TODO Auto-generated method stub
		return (List<CrewMember>) getCurrentSession().createQuery("from CrewMember cm").setReadOnly(true).list();
	}
}
