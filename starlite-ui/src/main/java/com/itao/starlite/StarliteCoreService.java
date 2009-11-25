package com.itao.starlite;

import org.restlet.Router;

import com.itao.concierge.pub.annotations.ConciergeService;

@ConciergeService(name="starlite-core", version="1.0", implementationName="starlite-core-impl", implementationVersion="1.0.0")
public class StarliteCoreService extends Router {
	public StarliteCoreService() {
		CharterService c = new CharterService();
		AircraftService a = new AircraftService();
		CrewService crewS = new CrewService();
		attach("/charters", c);
		attach("/charters/presentAndFuture/{offset}/{limit}", c);
		attach("/charters/{year}-{month}", c);
		
		attach("/charters/{id}", c);
		
		
		attach("/aircraft", a);
		attach("/aircraft/{id}", a);
		
		attach("/crew", crewS);
		attach("/crew/{id}", crewS);
	}
}
