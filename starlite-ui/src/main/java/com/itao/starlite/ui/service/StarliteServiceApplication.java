package com.itao.starlite.ui.service;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;

import com.google.inject.Injector;
import com.itao.guice.InjectorFactory;
import com.itao.starlite.AircraftService;
import com.itao.starlite.CharterService;
import com.itao.starlite.CrewService;
import com.itao.starlite.scheduling.AllocationService;
import com.itao.starlite.scheduling.ScheduleService;
import com.opensymphony.xwork2.ObjectFactory;

public class StarliteServiceApplication extends Application {
	private ObjectFactory objFactory;
	private CrewService crewService;
	private AircraftService aircraftService;
	private CharterService charterService;
	
	private ScheduleService scheduleService;
	private AllocationService allocationService;
	
	public StarliteServiceApplication(Context ctx) throws Exception {
		super(ctx);
		Injector injector = InjectorFactory.getInjector();
		crewService = (CrewService) injector.getInstance(CrewService.class);
		aircraftService = (AircraftService) injector.getInstance(AircraftService.class);
		charterService = (CharterService) injector.getInstance(CharterService.class);
		scheduleService = (ScheduleService) injector.getInstance(ScheduleService.class);
		allocationService = (AllocationService) injector.getInstance(AllocationService.class);
	}
	
	@Override
	public Restlet createRoot() {
		Router router = new Router(getContext());
		
		router.attach("/crew/{id}", crewService);
		router.attach("/crew", crewService);
		
		router.attach("/aircraft", aircraftService);
		router.attach("/aircraft/{id}", aircraftService);
		
		router.attach("/charter", charterService);
		router.attach("/charter/{id}", charterService);
		
		router.attach("/schedule", scheduleService);
		router.attach("/schedule/charter/{charterId}", scheduleService);
		router.attach("/schedule/crewMember/{crewMemberId}", scheduleService);
		router.attach("/schedule/allocation/{allocationId}", scheduleService);
		
		router.attach("/allocation/{id}", allocationService);

		return router;
	}
}
