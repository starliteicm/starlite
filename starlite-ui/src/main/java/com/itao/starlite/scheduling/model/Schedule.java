package com.itao.starlite.scheduling.model;

import java.util.ArrayList;

public class Schedule {
	private ArrayList<Assignment> assignments;
	private ArrayList<Assignable> assignables;
	private ArrayList<Allocation> allocations;
	
	public ArrayList<Allocation> getAllocations() {
		return allocations;
	}
	
	public void setAllocations(ArrayList<Allocation> allocations) {
		this.allocations = allocations;
	}

	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(ArrayList<Assignment> assignments) {
		this.assignments = assignments;
	}

	public ArrayList<Assignable> getAssignables() {
		return assignables;
	}

	public void setAssignables(ArrayList<Assignable> assignables) {
		this.assignables = assignables;
	}
	
	
}
