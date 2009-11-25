/*package com.itao.starlite.scheduling.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.tools.jmx.ManagedBindingMBean;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftList;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CharterList;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.scheduling.dao.AllocationDao;
import com.itao.starlite.scheduling.model.Allocation;
import com.itao.starlite.scheduling.model.Assignable;
import com.itao.starlite.scheduling.model.Assignment;
import com.itao.starlite.scheduling.model.Schedule;
import com.wideplay.warp.persist.Transactional;

public class SchedulingManager {
	@Inject
	private AllocationDao allocationDao;
	
	@Inject
	private StarliteCoreManager coreManager;
	
	@Transactional
	public Schedule getCompleteSchedule() {
		ArrayList<Allocation> allocations = new ArrayList<Allocation>(allocationDao.findAll());
	
		CharterList charters = coreManager.getAllCharters();
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		for (Charter c: charters.charterList) {
			Assignment a = new Assignment();
			a.type = "Charter";
			a.id = c.getId();
			a.from = c.getStartDate().toDate();
			a.to = c.getEndDate().toDate();
			a.label = c.getCode();
			assignments.add(a);
		}
		
		AircraftList aircraft = coreManager.getAllAircraft();
		ArrayList<Assignable> assignables = new ArrayList<Assignable>();
		for (Aircraft ai: aircraft.aircraftList) {
			Assignable a = new Assignable();
			a.type = "Aircraft";
			a.id = ai.getId();
			a.label = ai.getRef();
			assignables.add(a);
		}
		
		Schedule s = new Schedule();
		s.setAllocations(allocations);
		s.setAssignments(assignments);
		s.setAssignables(assignables);
		return s;
	}
	
	@Transactional
	public Schedule getScheduleForCharter(Integer charterId) {
		ArrayList<Allocation> allocations = new ArrayList<Allocation>(allocationDao.findAllByAssignment("Charter", charterId));
		
		ArrayList<Assignable> assignables = new ArrayList<Assignable>();
		AircraftList aircraft = coreManager.getAllAircraft();
		List<CrewMember> crew = coreManager.getAllCrew();
		
		Set<Assignable> assignedAircraft = new HashSet<Assignable>();
		Set<Assignable> assignedCrew = new HashSet<Assignable>();
		
		for (Allocation a: allocations) {
			if (a.getAssignableType().equals("Aircraft")) {
				for (Aircraft ai: aircraft.aircraftList) {
					if (ai.getId().equals(a.getAssignableId())) {
						Assignable ass = new Assignable();
						ass.id = ai.getId();
						ass.type = "Aircraft";
						ass.label = ai.getRef();
						assignedAircraft.add(ass);
					}
				}
			}
			if (a.getAssignableType().equals("CrewMember")) {
				for (CrewMember cm: crew) {
					if (cm.getId().equals(a.getAssignableId())) {
						Assignable ass = new Assignable();
						ass.id = cm.getId();
						ass.type = "CrewMember";
						ass.label = cm.getPersonal().getFirstName() + " " + cm.getPersonal().getLastName();
						assignedCrew.add(ass);
					}
				}
			}
		}
		
		assignables.addAll(assignedAircraft);
		assignables.addAll(assignedCrew);
		
		Charter c = coreManager.getCharter(charterId);
		Assignment a = new Assignment();
		a.type = "Charter";
		a.id = c.getId();
		a.from = c.getStartDate().toDate();
		a.to = c.getEndDate().toDate();
		a.label = c.getCode();
		
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		assignments.add(a);
		
		Schedule s = new Schedule();
		s.setAllocations(allocations);
		s.setAssignables(assignables);
		s.setAssignments(assignments);
		return s;
	}
	
	@Transactional
	public Schedule getScheduleForCrewMember(Integer crewId) {
		ArrayList<Allocation> allocations = new ArrayList<Allocation>(allocationDao.findAllByAssignable("CrewMember", crewId));
		
		ArrayList<Assignable> assignables = new ArrayList<Assignable>();
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		
		List<Charter> charters = coreManager.getAllCharters().charterList;
		
		Set<Assignment> assignedCharters = new HashSet<Assignment>();
		
		for (Allocation a: allocations) {
			if (a.getAssignmentType().equals("Charter")) {
				for (Charter c: charters) {
					if (c.getId().equals(a.getAssignmentId())) {
						Assignment ass = new Assignment();
						ass.id = c.getId();
						ass.type = "Charter";
						ass.label = c.getCode();
						assignedCharters.add(ass);
					}
				}
			}
		}
		
		assignments.addAll(assignedCharters);
		
		CrewMember c = coreManager.getCrewMember(crewId);
		Assignable a = new Assignable();
		a.id = c.getId();
		a.type = "CrewMember";
		a.label = c.getPersonal().getFirstName() + " - " + c.getPersonal().getLastName();
		
//		Charter c = coreManager.getCharter(charterId);
//		Assignment a = new Assignment();
//		a.type = "Charter";
//		a.id = c.getId();
//		a.from = c.getStartDate().toDate();
//		a.to = c.getEndDate().toDate();
//		a.label = c.getCode();
		
		
		assignables.add(a);
		
		Schedule s = new Schedule();
		s.setAllocations(allocations);
		s.setAssignables(assignables);
		s.setAssignments(assignments);
		return s;
	}
	
	@Transactional
	public void saveCompleteSchedule(Schedule schedule) {
		allocationDao.updateSchedule(schedule);
	}
	
	@Transactional
	public void saveAllocation(Allocation a) {
		//Check that the allocation doesn't overlap a previous one by the same assignable
		boolean causesCollisions = allocationDao.checkCollisionsForAssignable(a.getAssignableType(), a.getAssignableId(), a.getFrom(), a.getTo());
		if (causesCollisions)
			throw new RuntimeException("Allocation causes collisions.");
		allocationDao.makePersistent(a);
	}
	
	@Transactional
	public Allocation getAllocation(Integer id) {
		return allocationDao.findById(id);
	}
	
	@Transactional
	public void deleteAllocation(Integer id) {
		if (id == null)
			return;
		
		Allocation a = allocationDao.findById(id);
		if (a == null)
			return;
		allocationDao.makeTransient(a);
	}
}
*/